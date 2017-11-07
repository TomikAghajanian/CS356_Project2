package minitwitter.model;

import minitwitter.gui.UserView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public class User implements IObserver, IUserComponent, IUserVisitable {

    private String id;
    private UserView view;
    private Map<String, IObserver> followers;
    private Map<String, IObserver> followings;
    private List<String> newsFeed;

    public User(String id) {
        this.id = id;
        followers = new HashMap<String, IObserver>();
        followings = new HashMap<String, IObserver>();
        newsFeed = new ArrayList<String>();
    }
    
    public void updateFollowersList(User user) {
        if (!followers.containsKey(user.getUserId())) {
            followers.put(user.getUserId(), user);
        }
    }

    public void updateFollowingList(User followedUser) {
        if (followedUser.getUserId().equals(id)) {
            return;
        }

        followings.put(followedUser.getUserId(), followedUser);
        followedUser.updateFollowersList(this);
    }

    public void postTweet(String tweet) {
        newsFeed.add(id + ": " + tweet);
        notifyObservers(id);
    }

    public String getUserId() {
        return id;
    }

    public Map<String, IObserver> getFollowers() {
        return followers;
    }

    public Map<String, IObserver> getFollowedUsers() {
        return followings;
    }

    public List<String> getNewsFeed() {
        return newsFeed;
    }

    // Displays the view for this user
    public void getUserView(UserGroup rootGroup) {
        if (view == null) {
            view = new UserView(this, rootGroup);
        } else {
            view.setVisible(true);
        }
    }
    
    public void notifyObservers(String userId) {
        for (Map.Entry<String, IObserver> observer : followers.entrySet()) {
            User user = (User) observer.getValue();
            user.update(userId, newsFeed.get(newsFeed.size() - 1));
        }
    }

    @Override
    public void update(String userId, String tweet) {
        if (followings.containsKey(userId) || userId.equals(id)) {
            newsFeed.add(tweet);
            view.updateNewsFeedListView();
        }
    }

    @Override
    public DefaultMutableTreeNode getUserTreeNode() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(id);
        node.setAllowsChildren(false); // Only groups should have children. Users are leafs.
        return node;
    }

    @Override
    public void accept(IUserVisitor visitor) {
        visitor.getUserInfo(this);
    }
}
