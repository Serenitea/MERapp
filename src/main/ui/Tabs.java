package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.BOTH;

// describes and initializes JTabbedPanes for The UI's main JFrame window
//minimum size dimensions not currently in use
public class Tabs extends JComponent {
//    public static final int WIDTH = 450;
//    public static final int HEIGHT = 600;

    public Tabs() {
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

    //REQUIRES: actionListener input must have appropriate action commands for the JButtons
    //          submitNewProfileButton and cancelButton
    //EFFECTS: creates the UI that allows users to fill and submit a form to create a new profile.
    public static class NewProfilePanel extends JPanel {
        private JTextField userNameField = new JTextField();
        private final JLabel userNameLabel;
        ActionListener actionListener;
        JButton submitNewProfileButton = new JButton("Submit");
        JButton cancelNewProfileButton = new JButton("Cancel");

        //initializes the JPanel display
        public NewProfilePanel(ActionListener actionListener) {

            JPanel textPane = new JPanel();
            JPanel inputPane = new JPanel();
            JPanel buttonPane = new JPanel();

            //TODO PHASE4 add exception to load new profile if there's no saved profile
            textPane.add(new JLabel("Create New Profile"));
            userNameLabel = new JLabel("User Name");
            inputPane.setLayout(new GridLayout(1, 2));
            inputPane.add(userNameLabel);
            inputPane.add(userNameField);

            this.actionListener = actionListener;
            submitNewProfileButton.addActionListener(actionListener);
            cancelNewProfileButton.addActionListener(actionListener);
            buttonPane.add(submitNewProfileButton);
            buttonPane.add(cancelNewProfileButton);

            JPanel entirePane = new JPanel();
            entirePane.setLayout(new BorderLayout());
            entirePane.add(textPane, BorderLayout.NORTH);
            entirePane.add(inputPane, BorderLayout.CENTER);
            entirePane.add(buttonPane, BorderLayout.SOUTH);
            this.add(entirePane);
        }

        //EFFECTS: returns a String of the user's input in the JTextField to be set as profile name.
        // returns empty String if the input is unable to be processed by the default text formatter.
        public String getProfileNameInput() {
            return (userNameField.getText());
        }
    }

    //REQUIRES: actionListener input must have appropriate action commands for the JButtons
    //          newProfileButton and cancelButton
    //EFFECTS: creates the UI that allows users to fill and submit a form to create a new profile.
    public static class IntroMenuPane extends JPanel {
        ActionListener actionListener;
        JButton startCreateNewProfileButton = new JButton("New Profile");
        JButton loadSavedProfileButton = new JButton("Load saved profile");

        //tododoc
        public IntroMenuPane(ActionListener actionListener) {
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
            gbc.ipady = 40;
            gbc.gridwidth = 2;
            gbc.gridy = 0;
            this.add(header(), gbc);
            /*closeButton.addActionListener(e -> System.exit(0));*/
            startCreateNewProfileButton.addActionListener(actionListener);
            loadSavedProfileButton.addActionListener(actionListener);
            gbc.ipady = 0;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(startCreateNewProfileButton, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(loadSavedProfileButton, gbc);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(closeAppButton(actionListener), gbc);
        }

    }

}
