package ui;

import javax.swing.*;
import java.awt.event.ActionListener;

// describes and initializes JTabbedPanes for The UI's main JFrame window
//minimum size dimensions not currently in use
public class ContentComponents extends JComponent {
//    public static final int WIDTH = 450;
//    public static final int HEIGHT = 600;

    public ContentComponents() {
//        super.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    //EFFECT: creates a JButton that will close the app when pressed.
    public static JButton closeAppButton(ActionListener actionListener) {
        JButton button = new JButton("Close App");
        button.addActionListener(actionListener);
        return button;
    }


    //REQUIRES: the GUI currently is displaying MainTab, and no other tabs are open.
    //EFFECT: creates a JButton that will prompt mainTabListener to "close" the current profile session.
    public static JButton exitProfileButton(ActionListener actionListener) {
        JButton button = new JButton("Exit Profile");
        button.addActionListener(actionListener);
        return button;
    }



    //EFFECTS: creates a JLabel to serve as a header for the intro UI
    public static JLabel header() {
        //todo format the header
        return new JLabel("Pet Weight Management App", SwingConstants.CENTER);
    }

}
