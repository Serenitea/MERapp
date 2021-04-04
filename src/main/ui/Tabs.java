package ui;

import javax.swing.*;
import java.awt.*;

// initializes JFrames and GUI for GUI
public abstract class Tabs extends JTabbedPane {
    JTabbedPane tabbedPane = new JTabbedPane();



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

    public static class InitializeTabs extends Tabs {
        public InitializeTabs() {
            JPanel introMenuPanel = new Panels.IntroMenuPanel();
            super.tabbedPane.add(introMenuPanel);
        }
    }



    static class IntroPane extends Tabs {

    }

    class MainPane extends Tabs {

    }

    class AddPetPane {

    }

    class ManagePetsPane {

    }

    class EditPetPane {

    }

}
