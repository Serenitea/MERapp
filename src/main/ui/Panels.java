package ui;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class Panels extends JPanel {

    public Panels() {

    }

    public static class IntroMenuPanel extends Panels {
        ActionListener actionListener;
        JButton newProfileButton = new JButton("New Profile");
        JButton mainMenuButton = new JButton("Load saved profile");
        JButton closeButton = new JButton("Exit");

        public IntroMenuPanel(ActionListener actionListener) {
            this.actionListener = actionListener;
            this.add(new JLabel("Intro Text here"));
            closeButton.addActionListener(e -> System.exit(0));
            newProfileButton.addActionListener(actionListener);
            mainMenuButton.addActionListener(actionListener);
            this.add(newProfileButton);
            this.add(mainMenuButton);
            this.add(closeButton);
        }

        public JButton getNewProfileButton() {
            return newProfileButton;
        }

        public JButton getMainMenuButton() {
            return mainMenuButton;
        }
    }

    public static class MainMenuPanel extends Panels {
        public MainMenuPanel() {
            this.add(new JLabel("Main Menu here"));
        }
    }

}
