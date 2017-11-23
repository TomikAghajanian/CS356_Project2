package minitwitter.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import minitwitter.model.IObserver;
import minitwitter.model.User;
import minitwitter.model.UserGroup;

public class UserView extends JFrame {

    private User user;
    private UserGroup rootGroup;
    private JList<String> followingList;
    private JList<String> newsFeedList;
    private SimpleDateFormat sdf;
    private Date resultdate;
    private Date lastUpdate;
    private JLabel lastUpdateLabel;

    public UserView(User user, UserGroup rootGroup) {
        this.user = user;
        this.rootGroup = rootGroup;

        resultdate = new Date(user.getCreationTime());
        lastUpdate = new Date(user.getLastUpdatedTime());

        this.setTitle(user.getUserId() + " - MiniTwitter");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());

        init_View();
        updateNewsFeedListView();
        updateFollowingListView();

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void init_View() {
        JPanel panel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(new GridBagLayout());

        c.insets = new Insets(5, 5, 5, 5);

        JTextField userId = new JTextField("");
        userId.setPreferredSize(new Dimension(175, 26));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(userId, c);

        JLabel creationTimeLabel = new JLabel("Created on: " + this.resultdate.toString());
        userId.setPreferredSize(new Dimension(175, 26));
        c.gridx = 0;
        c.gridy = 4;
        panel.add(creationTimeLabel, c);

        lastUpdateLabel = new JLabel();
        lastUpdateLabel.setPreferredSize(new Dimension(300, 26));
        c.gridx = 0;
        c.gridy = 5;
        panel.add(lastUpdateLabel, c);

        JButton btn_followUser = new JButton("Follow User");
        btn_followUser.setPreferredSize(new Dimension(175, 26));
        btn_followUser.addActionListener((ActionEvent) -> {
            User followedUser = rootGroup.getUser(userId.getText(), rootGroup);

            if (followedUser != null) {
                user.updateFollowingList(followedUser);
                updateFollowingListView();
            }
        });
        c.gridx = 1;
        c.gridy = 0;
        panel.add(btn_followUser, c);

        followingList = new JList<String>();
        JScrollPane scroll = new JScrollPane(followingList);

        followingList.setPreferredSize(new Dimension(300, 250));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;

        panel.add(scroll, c);
        JTextField message = new JTextField("Enter Message");
        message.setPreferredSize(new Dimension(175, 26));
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(message, c);

        JButton btn_postMessage = new JButton("Post Message");
        btn_postMessage.setPreferredSize(new Dimension(175, 26));
        btn_postMessage.addActionListener((ActionEvent) -> {
            user.postTweet(message.getText());
            updateNewsFeedListView();
        });
        c.gridx = 1;
        c.gridy = 2;
        panel.add(btn_postMessage, c);

        String[] news = user.getNewsFeed().toArray(new String[user.getNewsFeed().size()]);
        newsFeedList = new JList<String>(news);
        JScrollPane scroll_msgs = new JScrollPane(newsFeedList);

        newsFeedList.setPreferredSize(new Dimension(300, 250));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;

        panel.add(scroll_msgs, c);

        add(panel);
    }

    public void updateFollowingListView() {
        DefaultListModel<String> model = new DefaultListModel<String>();

        for (Map.Entry<String, IObserver> followed : user.getFollowedUsers().entrySet()) {
            model.addElement(followed.getKey());
        }
        followingList.setModel(model);
    }

    public void updateNewsFeedListView() {
        this.user.setLastUpdatedTime();
        this.lastUpdateLabel.setText("Last Update: " +new Date(this.user.getLastUpdatedTime()).toString());
        DefaultListModel<String> model = new DefaultListModel<String>();
        model.addElement("last update: " + new Date(System.currentTimeMillis()).toString());
        for (String tweet : user.getNewsFeed()) {
            model.addElement(tweet);
        }
        newsFeedList.setModel(model);
    }
}
