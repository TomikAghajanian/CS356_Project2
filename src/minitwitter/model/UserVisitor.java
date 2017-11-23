package minitwitter.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserVisitor implements IUserVisitor {

    private int[] userInfo;
    private int numberOfGroups;

    public UserVisitor() {
        userInfo = new int[3];
        numberOfGroups = 1;
    }

    @Override
    public int getUserInfo(User user) {
        return user.getNewsFeed().size();
    }

    @Override
    public void getGroupInfo(UserGroup group) {
        for (Map.Entry<String, User> user : group.getUserMap().entrySet()) {
            if (user.getValue() instanceof UserGroup) {
                numberOfGroups++;
                this.getGroupInfo((UserGroup) user.getValue());
            } else {
                setUserInfo(user);
            }
        }
    }

    private void setUserInfo(Map.Entry<String, User> user) {
        userInfo[0]++;

        for (String message : user.getValue().getNewsFeed()) {
            String msg = message.toLowerCase();

            if (msg.contains(user.getValue().getUserId().toLowerCase())) {
                userInfo[1]++;

                //Determines if the message has a positive word
                if ((msg.contains("good") || msg.contains("excellent") || msg.contains("great"))) {
                    userInfo[2]++;
                }
            }
        }
    }

    public String validate(UserGroup userGroup) {
        Map<String, User> users = userGroup.getAllUsers();
        System.out.println("from Validate all users: " + userGroup.getAllUsers());
        System.out.println("from Validate last update: " + userGroup.getAllUsersTimes());
        List keys = new ArrayList(users.keySet());
        System.out.println(users.keySet());
        for (int i = 0; i < keys.size(); i++ ){
            if (((String)keys.get(i)).contains(" ")) {
                return "Invalid";
            }
        }
        return "Valid";
    }
    
    public String lastUpdateTimeUser(UserGroup group) {
        List keys = new ArrayList(group.getAllUsersTimes().keySet());        
        Collections.sort(keys);
               
        return group.getAllUsersTimes().get(keys.get(keys.size()-1));
    }

    public int getNumberOfUsers() {
        return userInfo[0];
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public int getNumberOfMessages() {
        return userInfo[1];
    }

    public double getPositivePercentage() {
        if (userInfo[2] == 0) {
            return 0;
        }
        return Math.round((((userInfo[2] * 1.0) / userInfo[1]) * 100) * 100) / 100.0;
    }

}
