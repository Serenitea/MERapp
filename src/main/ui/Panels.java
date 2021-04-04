package ui;

import javax.swing.*;

public abstract class Panels extends JPanel {

    public Panels() {

    }

    public static class IntroMenuPanel extends Panels {
        public IntroMenuPanel() {
            this.add(new JLabel("Intro Menu here"));
        }
    }

    public static class MainMenuPanel extends Panels {
        public MainMenuPanel() {
            this.add(new JLabel("Main Menu here"));
        }
    }

}
