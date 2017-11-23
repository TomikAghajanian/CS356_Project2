package minitwitter.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

public class UserGroup extends User implements IUserComponent, IUserVisitable {

    private Map<String, User> users;
    
    public Map<String, User> getAllUsers(){
        return this.users;
    }

    public UserGroup(String id) {
        super(id);
        users = new HashMap<String, User>();
    }
    
    
    public void addUser(User user, UserGroup rootGroup) {
        String id = user.getUserId();
        
        if (!userExists(id, rootGroup)) {
            users.put(id, user);
        } else {
            System.out.println(id + " already exists!");
        }
    }
    
    public Map<String, User> getUserMap() {
        return users;
    }

    public boolean userExists(String id, UserGroup userGroup) {
        // Returns true if current group matches id or if a user in its Map matches the id
        if (userGroup.getUserId().equals(id) || userGroup.getUserMap().containsKey(id)) {
            return true;
        }

        // For each group in userGroup do a recursive search for the id
        for (Map.Entry<String, User> group : userGroup.getUserMap().entrySet()) {
            if (group.getValue() instanceof UserGroup && userExists(id, (UserGroup) group.getValue())) {
                return true;
            }
        }
        return false; // User not found
    }

    public User getUser(String id, UserGroup userGroup) {
        if (userGroup.getUserId().equals(id)) {
            return userGroup;
        } else if (userGroup.getUserMap().containsKey(id)) {
            return userGroup.getUserMap().get(id);
        }

        for (Map.Entry<String, User> group : userGroup.getUserMap().entrySet()) {
            if (group.getValue() instanceof UserGroup && userExists(id, (UserGroup) group.getValue())) {
                return getUser(id, (UserGroup) group.getValue());
            }
        }

        return null; // id not found
    }

    @Override
    public DefaultMutableTreeNode getUserTreeNode() {
        DefaultMutableTreeNode group = new DefaultMutableTreeNode(super.getUserId());

        for (Map.Entry<String, User> user : users.entrySet()) {
            group.add(user.getValue().getUserTreeNode());
        }

        return group;
    }

    @Override
    public void accept(IUserVisitor visitor) {
        visitor.getGroupInfo(this);
    }
}
