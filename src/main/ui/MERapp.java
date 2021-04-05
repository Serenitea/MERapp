package ui;

import model.Pet;
import model.PetList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.GridBagConstraints.BOTH;

// Pet Calorie Calculator application
//John Zukowski - the Definitive Guide to Java Swing (2005, Apress)
//Kishori Sharan - Beginning Java 8 APIs, Extensions and Libraries (2014, Apress)
//https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html#gridbagConstraints
//resource used: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SplitPaneDemoProject/src/components/SplitPaneDemo.java
// user interface methods
public class MERapp extends JFrame implements Runnable {
    public static final int FRAMEWIDTH = 450;
    public static final int FRAMEHEIGHT = 600;
    public static final int MINWIDTH = 450;
    public static final int MINHEIGHT = 400;
    private static final String JSON_STORE = "./data/profiles.json";
    private static final List<String> PET_MENU = Arrays.asList(
            "e -> edit pet name",
            "w -> edit pet weight",
            "d -> add or change diet information",
            "r -> remove this pet",
            "b -> back to Main Menu");
    private static final List<String> WELCOME_MENU = Arrays.asList(
            "\nl -> Load profile",
            "n -> Create a new profile",
            "q -> quit",
            "\n---------------------------");
    private static final List<String> MAIN_MENU = Stream.of("\n\nOptions:",
            "\nm -> Manage Current Pets",
            "n -> Add a New Pet",
            "r -> Remove a Pet",
            "s -> Save Session",
            "b -> exit current profile",
            "q -> quit").collect(Collectors.toList());
    private static final List<String> MAIN_MENU_HEADER = Arrays.asList(
            "\n------------------------------------",
            "Pet Weight Management App Main Menu",
            "\n------------------------------------");
    private static final List<String> PET_MENU_HEADER = Arrays.asList(
            "\n------------------------------------",
            "Choose a Pet to Manage - Menu",
            "\n------------------------------------");
    private static final Scanner scanner = new Scanner(System.in);
    private static List<String> MANAGE_PET_MENU_HEADER = Arrays.asList(
            "\n------------------------------------",
            "Manage a Pet - Menu",
            "\n------------------------------------");
    JFrame frame;
    Tabs.IntroMenuPanel introPanel;
    ActionListener actionListener;
    ActionListener mainMenuListener;
    ListSelectionListener listSelectionListener;
    MainMenuTab mainMenuTab;
    JSplitPane managePetSplitPanes;
    int index;
    JTabbedPane tabbedPane;
    ActionListener introListener;
    GridBagLayout gridBagLayout;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private PetList petList;
    private ArrayList<Pet> petArrayList;
    private JList<String> petJList; //for listSelectionListener
    int selectedIndex;

    private JList<String> toNamesJList(ArrayList<Pet> petArrayList) {
        ListModel<String> petNameListModel = castNameToListModel(petArrayList);
        JList<String> petNameJList = new JList<String>(petNameListModel);
        return petNameJList;
    }

    /*
    EFFECTS: constructs the Pet MER application and initializes json I/O
     */
    public MERapp() {
        super("Pet Weight Management App");

        run();
//        EventQueue.invokeLater(this::run);
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        initializeFields();
        initializeFrame();
//        runApp(); //will be deleted later
    }

    /*
EFFECTS: prints Application menu header
 */
    private void printMenu(List<String> menu) {
        for (String s : menu) {
            System.out.println(s);
        }
    }

    //todo doc
    private void initializeFrame() {
        frame = new JFrame("Pet Weight Management App GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initIntroListener();
        introPanel = new Tabs.IntroMenuPanel(introListener);
        frame.getContentPane().add(introPanel);
        frame.setMinimumSize(new Dimension(MINWIDTH, MINHEIGHT));
        frame.setVisible(true);
    }

    //todo doc
    private void initIntroListener() {
        introListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "New Profile":
                    createNewProfile();
                    break;
                case "Load saved profile":
                    loadProfile();
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };
    }

    private void initMainListener() {
        mainMenuListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "Add New Pet":
                    addNewPetEvent();
                    break;
                case "Remove a Pet":
                    removePetEvent();
                    break;
                case "Save Session":
                    saveSessionEvent();
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };

        listSelectionListener = e -> {
            JList<String> curPetJList = (JList) e.getSource();
            selectedIndex = curPetJList.getSelectedIndex();
            updateRightPane(selectedIndex);

        };

    }

    //TODO save
    private void saveSessionEvent() {
        System.out.println("save session");
    }
    //TODO remove pet
    private void removePetEvent() {
        System.out.println("remove pet");
    }
    //TODO add new pet
    private void addNewPetEvent() {
        System.out.println("add pet");
    }


    public void loadProfile() {
        System.out.println("load profile");
        loadSavedPetList();
        frame.remove(introPanel);
        initMainListener();
        tabbedPane = initializeTabs();
        frame.getContentPane().add(tabbedPane);
//        initGridBag();


        gridBagLayout = new GridBagLayout();

        frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        frame.setVisible(true);
    }

    /*REQUIRES: no currently loaded profile (empty petList)
    MODIFIES: this
    EFFECTS: loads previously saved profiles from local json storage path*/

    private void loadSavedPetList() {
        try {
            petList = jsonReader.read();
            petArrayList = petList.getPetArray();
            System.out.println("Loaded " + petList.getOwnerName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private JTabbedPane initializeTabs() {
        initializeSplitPane(toNamesJList(petArrayList));
        this.mainMenuTab = new MainMenuTab(petArrayList,
                mainMenuListener,
                listSelectionListener, managePetSplitPanes);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Main Dashboard", mainMenuTab);
//        tabs.addTab("New Pet", addPetTab(actionListener));
//        tabs.addTab("Edit a Pet", editPetTab(actionListener));
        return tabs;
    }

    private void initializeSplitPane(JList<String> toNamesJList) {
        petJList = toNamesJList;
        petJList.addListSelectionListener(listSelectionListener);
        managePetSplitPanes = new JSplitPane();
        JPanel displayPane = new JPanel();
        JScrollPane leftPane = new JScrollPane(toNamesJList);
        managePetSplitPanes.setLeftComponent(leftPane);
        if (petArrayList.size() > 0) {
            displayPane.add(new JLabel("No pet selected"));
            managePetSplitPanes.setRightComponent(displayPane);
        } else {
            displayPane.add(new JLabel("No pets in profile"));
            managePetSplitPanes.setRightComponent(displayPane);
        }
    }

    private void updateRightPane(int index) {
        JPanel displayPane = new JPanel();
        displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
        Pet selectedPet = petArrayList.get(index);
        String petName = selectedPet.getPetName();
        double weight = selectedPet.getWeight();
        double dietCalPerKg = selectedPet.getDietCalPerKg();
        displayPane.add(new JLabel("Selected Pet: " + petName));
        displayPane.add(new JLabel("Weight (kg): " + weight));
        displayPane.add(new JLabel("Current diet calories (KCal/kg): " + dietCalPerKg));
        managePetSplitPanes.setRightComponent(displayPane);
    }

    private static ListModel<String> castNameToListModel(ArrayList<Pet> petArrayList) {
        DefaultListModel<String> petListModel = new DefaultListModel<>();
        for (Pet pet : petArrayList) {
            petListModel.addElement(pet.getPetName());
        }

        return petListModel;
    }

    //TODO prob
    public void goToMainMenu() {
    }

    //TODO create new profile
    public void createNewProfile() {
        System.out.println("create new profile");
    }

    //todo doc
    private void initializeFields() {
        petList = new PetList();
        petArrayList = petList.getPetArray();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    //todo doc
    //4 rows 3 columns
    public static class MainMenuTab extends JPanel {
        JSplitPane managePetsPanel;
        Boolean emptyPetList;
        ActionListener actionListener;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc;
        JButton newPetButton = new JButton("Add New Pet");
        JButton editPetButton = new JButton("Edit a Pet");
        JButton savePetButton = new JButton("Save Session");
        ListModel<String> petListModel;
//        JPanel petDisplayPanel = new JPanel();
        //todo doc
        public MainMenuTab(ArrayList<Pet> petArrayList, ActionListener actionListener,
                           ListSelectionListener listSelectionListener, JSplitPane splitPane1) {
            managePetsPanel = splitPane1;
            emptyPetList = (petArrayList.size() <= 0);
            this.setLayout(gridBagLayout);
            gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
//            this.petListModel = castToListModel(petArrayList);
            addActionListeners();
            this.petListModel = castNameToListModel(petArrayList);
            this.addWithConstraints(mainMenuHeader(), 3, 0, 0);
//            this.managePetsPanel = new ManagePetsPanel(petListModel, listSelectionListener);
            this.addWithConstraints(managePetsPanel, 3, 0, 1);
            this.addWithConstraints(newPetButton, 1, 0, 2);
            this.addWithConstraints(editPetButton, 1, 1, 2);
            this.addWithConstraints(savePetButton, 1, 2, 2);
            this.addWithConstraints(Tabs.closeButton(actionListener), 3, 0, 3);
        }
        //todo doc
        private static JLabel mainMenuHeader() {
            JLabel label = new JLabel("Pet Weight Management App Dashboard", SwingConstants.CENTER);
            return label;
        }
        //todo doc
        private void addWithConstraints(JComponent component, int gbcWidth, int gbcX, int gbcY) {
            gbc.gridwidth = gbcWidth;
            gbc.gridx = gbcX;
            gbc.gridy = gbcY;
            this.add(component, gbc);
        }
        //EFFECTS: add actionListeners for the following buttons
        private void addActionListeners() {
            newPetButton.addActionListener(actionListener);
            editPetButton.addActionListener(actionListener);
            savePetButton.addActionListener(actionListener);
        }

    }

}
