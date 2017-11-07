package minitwitter.application;

import minitwitter.gui.AdminControlPanel;
import javax.swing.SwingUtilities;

public class Driver {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminControlPanel.getInstance();
        });
    }

}
