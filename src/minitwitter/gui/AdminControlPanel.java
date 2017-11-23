package minitwitter.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import minitwitter.model.User;
import minitwitter.model.UserGroup;
import minitwitter.model.UserVisitor;

@SuppressWarnings("serial")
public class AdminControlPanel extends JFrame {

    private static AdminControlPanel instance = null;
    private JTree tree;
    private UserGroup rootGroup;
    private String selectedGroup;
    private String selectedUser;

    private AdminControlPanel() {
        this.setTitle("Mini Twitter");
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        rootGroup = new UserGroup("Root");
        selectedGroup = "Root";

        // Initializers
        init_View();

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void init_View() {
        JPanel panel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();

        panel.setLayout(new GridBagLayout());

        c.insets = new Insets(5, 5, 5, 5);

        DefaultTreeModel model = new DefaultTreeModel(rootGroup.getUserTreeNode(), true);
        tree = new JTree(model); // Add root node to tree
        tree.setPreferredSize(new Dimension(250, 350));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionRow(0); // Select root node
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener((TreeSelectionEvent) -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            if (!node.getAllowsChildren()) {
                selectedUser = (String) node.getUserObject();
                node = (DefaultMutableTreeNode) node.getParent();
            } else {
                selectedUser = null;
            }

            selectedGroup = (String) node.getUserObject();
        });

        JScrollPane scroll = new JScrollPane(tree); // Add tree to a scroll pane
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 8;
        c.fill = GridBagConstraints.VERTICAL;
        panel.add(scroll, c);
        JTextField userId = new JTextField("Tomik");
        userId.setPreferredSize(new Dimension(175, 26));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        panel.add(userId, c);

        JButton btn_UserId = new JButton("Add User");
        btn_UserId.addActionListener((ActionEvent) -> {
            updateTree(new User(userId.getText()), (UserGroup) rootGroup.getUser(selectedGroup, rootGroup));
        });

        c.gridx = 2;
        c.gridy = 0;
        panel.add(btn_UserId, c);
        JTextField groupId = new JTextField("CS356");
        groupId.setPreferredSize(new Dimension(175, 26));
        c.gridx = 1;
        c.gridy = 1;
        panel.add(groupId, c);

        JButton btn_Validate = new JButton("Validate User/GroupID");
        btn_Validate.addActionListener((ActionEvent) -> {
            JOptionPane.showMessageDialog(this, "Valid user/GroupIDs: "
                    + getVisitor().validate(rootGroup));
        });
        c.gridx = 2;
        c.gridy = 3;
        panel.add(btn_Validate, c);

        JButton btn_LastUpdateTime = new JButton("Last Update Time User");
        btn_LastUpdateTime.addActionListener((ActionEvent) -> {
            JOptionPane.showMessageDialog(this, "Last Update Time User: "
                    + getVisitor().lastUpdateTimeUser(rootGroup));
        });
        c.gridx = 1;
        c.gridy = 3;
        panel.add(btn_LastUpdateTime, c);

        JButton btn_GroupId = new JButton("Add Group");
        btn_GroupId.addActionListener((ActionEvent) -> {
            updateTree(new UserGroup(groupId.getText()), (UserGroup) rootGroup.getUser(selectedGroup, rootGroup));
        });
        c.gridx = 2;
        c.gridy = 1;
        panel.add(btn_GroupId, c);
        JButton btn_UserView = new JButton("Open User View");
        btn_UserView.addActionListener((ActionEvent) -> {
            if (selectedUser != null) {
                User user = rootGroup.getUser(selectedUser, rootGroup);
                user.getUserView(rootGroup);
            }
        });
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(btn_UserView, c);
        JButton btn_ShowUserTotal = new JButton("Show User Total");
        btn_ShowUserTotal.addActionListener((ActionEvent) -> {
            JOptionPane.showMessageDialog(this, "Number of Users: " + getVisitor().getNumberOfUsers());
        });
        c.insets = new Insets(200, 5, 5, 5);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        panel.add(btn_ShowUserTotal, c);
        JButton btn_ShowGroupTotal = new JButton("Show Group Total");
        btn_ShowGroupTotal.addActionListener((ActionEvent) -> {
            JOptionPane.showMessageDialog(this, "Number of Groups: " + getVisitor().getNumberOfGroups());
        });
        c.gridx = 2;
        c.gridy = 3;
        panel.add(btn_ShowGroupTotal, c);
        JButton btn_ShowMsgTotal = new JButton("Show Message Total");
        btn_ShowMsgTotal.addActionListener((ActionEvent) -> {
            JOptionPane.showMessageDialog(this, "Number of Messages: " + getVisitor().getNumberOfMessages());
        });
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 1;
        c.gridy = 4;
        c.gridheight = 1;
        panel.add(btn_ShowMsgTotal, c);
        JButton btn_ShowPositivePercentage = new JButton("Show Positive Percentage");
        btn_ShowPositivePercentage.addActionListener((ActionEvent) -> {
            JOptionPane.showMessageDialog(this, "Percentage of Positive Messages: " + getVisitor().getPositivePercentage() + "%");
        });
        c.gridx = 2;
        c.gridy = 4;
        panel.add(btn_ShowPositivePercentage, c);

        add(panel);
    }

    private void updateTree(User user, UserGroup group) {
        if (group != null) {
            group.addUser(user, rootGroup);

            // This creates a new model and checks if a node allows children. If children
            // are allowed then it uses the folder icon
            DefaultTreeModel model = new DefaultTreeModel(rootGroup.getUserTreeNode(), true);
            tree.setModel(model);
        }
    }

    public UserVisitor getVisitor() {
        UserVisitor visitor = new UserVisitor();

        rootGroup.accept(visitor);

        return visitor;
    }

    public static AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }
}
