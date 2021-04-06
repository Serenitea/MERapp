package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.BOTH;

// describes and initializes JTabbedPanes for The UI's main JFrame window
//tododoc
public class Tabs extends JComponent {
    public static final int WIDTH = 450;
    public static final int HEIGHT = 600;
    //tododoc
    public Tabs() {
        super.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    //tododoc
    public static JPanel addPetTab(ActionListener actionListener) {
        JPanel tab = new JPanel();
        tab.add(new JLabel("Enter information for new pet:"));
        tab.add(new JLabel("Pet Name: "));
        tab.add(new JFormattedTextField(""));
        return tab;
    }

    //tododoc
    public static JButton closeButton(ActionListener actionListener) {
        JButton button = new JButton("Close App");
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    //tododoc
    public static JButton exitProfileButton(ActionListener actionListener) {
        JButton button = new JButton("Exit Profile");
        button.addActionListener(actionListener);
        return button;
    }
    //tododoc
    public static JLabel header() {
        JLabel label = new JLabel("Pet Weight Management App", SwingConstants.CENTER);
        //todo format the header
        return label;
    }
    //tododoc
    public static class NewProfilePanel extends JPanel {
        private JTextField userNameField = new JTextField();
        private final JLabel userNameLabel;
        ActionListener actionListener;
        JButton submitNewProfileButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        //tododoc
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
            cancelButton.addActionListener(actionListener);
            buttonPane.add(submitNewProfileButton);
            buttonPane.add(cancelButton);

            JPanel entirePane = new JPanel();
            entirePane.setLayout(new BorderLayout());
            entirePane.add(textPane, BorderLayout.NORTH);
            entirePane.add(inputPane, BorderLayout.CENTER);
            entirePane.add(buttonPane, BorderLayout.SOUTH);

            //inputPane - needs fields
            //actionListener - cancel, submit button actions

            this.add(entirePane);

        }

        public String getProfileNameInput() {
            return (userNameField.getText());
        }

    }

    //tododoc
    public static class IntroMenuPanel extends JPanel {
        ActionListener actionListener;
        JButton newProfileButton = new JButton("New Profile");
        JButton mainMenuButton = new JButton("Load saved profile");

        //tododoc
        public IntroMenuPanel(ActionListener actionListener) {
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
            gbc.ipady = 40;
            gbc.gridwidth = 2;
            gbc.gridy = 0;
            this.add(header(), gbc);
            /*closeButton.addActionListener(e -> System.exit(0));*/
            newProfileButton.addActionListener(actionListener);
            mainMenuButton.addActionListener(actionListener);
            gbc.ipady = 0;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(newProfileButton, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(mainMenuButton, gbc);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(closeButton(actionListener), gbc);
        }

    }

}
