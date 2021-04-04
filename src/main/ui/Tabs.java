package ui;

import javax.swing.*;
import java.awt.*;

// describes and initializes JTabbedPanes for The UI's main JFrame window
public abstract class Tabs extends JTabbedPane {
    /*
        REQUIRES:
        MODIFIES:
        EFFECTS: builds forms for tab panes
         */
    //todo doc
    public Tabs() {
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        //position tabbed pane

    }

    //todo doc
    public static class InitializeTabs extends Tabs {
        public InitializeTabs() {
//            JPanel introMenuPanel = new Panels.IntroMenuPanel();
//            super.add(introMenuPanel);
//            JPanel introTab = new IntroTab();
            JPanel mainTab = new MainTab();
//            super.add(introTab);
            super.add(mainTab);
        }
    }



    /*static class IntroTab extends JPanel {
        public IntroTab() {
            JPanel introMenuPanel = new Panels.IntroMenuPanel();
            super.add(introMenuPanel);
        }
    }*/

    class MainTab extends JPanel {
        public MainTab() {
            JPanel mainMenuPanel = new Panels.MainMenuPanel();
            super.add(mainMenuPanel);
        }
    }

    class AddPetTab {

    }

    class ManagePetsTab {

    }

    class EditPetTab {

    }

}
