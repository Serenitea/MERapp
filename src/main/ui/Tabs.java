package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.BOTH;

// describes and initializes JTabbedPanes for The UI's main JFrame window
public class Tabs extends JComponent {
    public static final int WIDTH = 450;
    public static final int HEIGHT = 600;

    public Tabs() {
        super.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    //todo docs
    //4 rows 3 columns
    public static class MainMenuPanel extends JPanel {
        ActionListener actionListener;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc;
        JButton newPetButton = new JButton("Add New Pet");
        JButton removePetButton = new JButton("Remove a Pet");
        JButton savePetButton = new JButton("Save Session");
        JList petJList;
        JPanel petDisplayPanel = new JPanel();

        private static JLabel mainMenuHeader() {
            JLabel label = new JLabel("Pet Weight Management App Dashboard", SwingConstants.CENTER);
            return label;
        }

        private void addWithConstraints(JComponent component, int gbcWidth, int gbcX, int gbcY) {
            gbc.gridwidth = gbcWidth;
            gbc.gridx = gbcX;
            gbc.gridy = gbcY;
            this.add(component, gbc);
        }
        public MainMenuPanel(ActionListener actionListener, JList petList) {
            this.setLayout(gridBagLayout);
            gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
            this.petJList = petList;
            addActionListeners();

            this.addWithConstraints(mainMenuHeader(), 3, 0, 0);
            this.addWithConstraints(managePetsTab(actionListener, petJList, petDisplayPanel),
                    3, 0, 1);
            this.addWithConstraints(newPetButton, 1, 0, 2);
            this.addWithConstraints(removePetButton, 1, 1, 2);
            this.addWithConstraints(savePetButton, 1, 2, 2);
            this.addWithConstraints(closeButton(actionListener), 3, 0, 3);

            /*gbc.gridwidth = 3;
            gbc.gridy = 0;
            this.add(mainMenuHeader(), gbc);

            closeButton.addActionListener(e -> System.exit(0));


            gbc.gridwidth = 3;
            gbc.gridy = 1;
            this.add(managePetsTab(actionListener, petJList, petDisplayPanel), gbc);
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(newPetButton, gbc);
            gbc.gridx = 1;
            gbc.gridy = 2;
            this.add(removePetButton, gbc);
            gbc.gridx = 2;
            gbc.gridy = 2;
            this.add(savePetButton, gbc);
            gbc.gridx = 2;
            gbc.gridy = 3;
            this.add(closeButton(actionListener), gbc);*/
        }

        private void addActionListeners() {
            newPetButton.addActionListener(actionListener);
            removePetButton.addActionListener(actionListener);
            savePetButton.addActionListener(actionListener);
        }

        public static JSplitPane managePetsTab(ActionListener actionListener,
                                               JComponent leftPane,
                                               JComponent rightPane) {
            JSplitPane splitPane = new JSplitPane();
            splitPane.setLeftComponent(leftPane);
            splitPane.setRightComponent(rightPane);
            return splitPane;
        }
    }





    /*static class IntroTab extends Tabs {
        public IntroTab(ActionListener actionListener) {
            JPanel introMenuPanel = new Panels.IntroMenuPanel(actionListener);
            super.add(introMenuPanel);
        }
    }*/

    public static JPanel addPetTab(ActionListener actionListener) {
        JPanel tab = new JPanel();
        tab.add(new JLabel("Enter information for new pet:"));
        tab.add(new JLabel("Pet Name: "));
        tab.add(new JFormattedTextField(""));
        return tab;
    }



    public static JPanel editPetTab(ActionListener actionListener) {
        JPanel tab = new JPanel();
        return tab;
    }


    public static JButton closeButton(ActionListener actionListener) {
        JButton button = new JButton("Exit");
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    public static JLabel header() {
        JLabel label = new JLabel("Pet Weight Management App", SwingConstants.CENTER);
        //todo format the header
        return label;
    }




    public static class IntroMenuPanel extends JPanel {
        ActionListener actionListener;
        JButton newProfileButton = new JButton("New Profile");
        JButton mainMenuButton = new JButton("Load saved profile");

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
/*
        public JButton getNewProfileButton() {
            return newProfileButton;
        }

        public JButton getMainMenuButton() {
            return mainMenuButton;
        }*/
    }
}
