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

    private static void updatePetDisplay(int selectedIndex,
                                         ArrayList<Pet> petArrayList) {
        JPanel newPetDisplay = new JPanel();
        Pet displayedPet = petArrayList.get(selectedIndex);
        String petName = displayedPet.getPetName();
        JLabel petNameJLabel = new JLabel(String.format("Selected Pet: %s", petName));
        newPetDisplay.add(petNameJLabel);

        System.out.println("update pet display - test");
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
            /*if (petJList.getLastVisibleIndex() != -1) {

//            updatePetDisplay(selectedIndex, petArrayList);
                System.out.println(selectedIndex);
                updateSplitPane(petJList, selectedIndex);
            } else {
                System.out.println("no pets in profile");
            }*/

        };

    }


    private void saveSessionEvent() {
        System.out.println("save session");
    }

    private void removePetEvent() {
        System.out.println("remove pet");
    }

    private void addNewPetEvent() {
        System.out.println("add pet");
    }

    //todo docs
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
        if (toNamesJList.getLastVisibleIndex() != -1) {
            displayPane.add(new JLabel("No pet selected"));
            managePetSplitPanes.setRightComponent(displayPane);
        } else {
            displayPane.add(new JLabel("No pets in profile"));
            managePetSplitPanes.setRightComponent(displayPane);
        }
    }

    private void updateRightPane(int index) {
        JPanel displayPane = new JPanel();
        displayPane.add(new JLabel("pet display here" + index));
        managePetSplitPanes.setRightComponent(displayPane);
    }

    private static ListModel<String> castNameToListModel(ArrayList<Pet> petArrayList) {
        DefaultListModel<String> petListModel = new DefaultListModel<>();
        //todo abstract method maybe
        for (Pet pet : petArrayList) {
            petListModel.addElement(pet.getPetName());
        }

        return petListModel;
    }


    public void goToMainMenu() {
    }

    public void createNewProfile() {
        System.out.println("create new profile");
    }


    /*private void initializeListener() {
        changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                sourceTabbedPane = (JTabbedPane)changeEvent.getSource();
                index = sourceTabbedPane.getSelectedIndex();
                System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
            }
        };
    }*/

    private void initializeFields() {
        petList = new PetList();
        petArrayList = petList.getPetArray();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    //todo docs
    //4 rows 3 columns

/*
    //EFFECTS: directs user to intro menu if no profile currently loaded, the main menu of options, or exit
    private void runApp() {
        boolean keepGoing = true;

        while (keepGoing) {
            if (petList.getNumPets() == 0) {
                keepGoing = introMenu();
            } else {
                displayMainMenu();
                String command = scanner.next();
                if (command.equalsIgnoreCase("q")) {
                    promptConfirmSave();
                    keepGoing = false;
                } else {
                    processMainMenuCommand(command);
                }
            }
        }
        System.out.println("\nGoodbye!");
    }

    *//*
    MODIFIES: ./data/profiles.json
    EFFECTS:
     *//*
    private void promptConfirmSave() {

    }

    *//*
    REQUIRES: no currently loaded profile (empty petList)
    EFFECTS: displays and processes input for intro menu
     *//*
    private Boolean introMenu() {
        displayIntroMenu();
        String command = scanner.next();
        if (command.equalsIgnoreCase("q")) {
            return false;
        } else {
            processIntroMenuCommand(command);
        }
        return true;
    }

    *//*
    REQUIRES: no currently loaded profile (empty petList)
    EFFECTS: display intro menu options: load saved profile, create new profile, or quit
     *//*
    private void displayIntroMenu() {
        printMenu(MAIN_MENU_HEADER);
        printMenu(WELCOME_MENU);
    }

    *//*
    REQUIRES: no currently loaded profile (empty petList)
    EFFECTS: processes commands of intro menu given a valid non-exit command
     *//*
    private void processIntroMenuCommand(String command) {
        switch (command) {
            case "n":
                //make new pet with new profile
                addNewPet();
                break;
            case "l":
                //load saved profile
                loadSavedPetList();
        }
    }

    *//*
    MODIFIES: this
     EFFECTS: processes user command from main menu of options
     *//*
    private void processMainMenuCommand(String command) {
        switch (command) {
            case "n":
                // make new pet
                addNewPet();
                break;
            case "m":
                //view all pets
                managePets();
                break;
            case "r":
                removePetChoose();
                break;
            case "s":
                savePetList();
                break;
            case "b":
                clearPetList();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    *//*
    MODIFIES: this
    EFFECTS: remove all data from fields of petList
     *//*
    private void clearPetList() {
        petList = new PetList();
    }

    *//*
    REQUIRES: at least 1 pet in petList
    MODIFIES: ./data.profiles.json
    EFFECTS: confirms user name then saves petList to save path
     *//*
    private void savePetList() {
        confirmOwnerName();
        try {
            jsonWriter.open();
            jsonWriter.write(petList);
            jsonWriter.close();
            System.out.println("Saved " + petList.getOwnerName() + "'s profile to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    *//*
        REQUIRES: at least 1 pet in petList
        MODIFIES: this
        EFFECTS: prompts user to input owner name if name is null, or confirm current user name with option to edit
         *//*
    private void confirmOwnerName() {
        if (petList.getOwnerName().equals("")) {
            System.out.println("This profile currently has no owner name.");
            newOwnerName();
        } else {
            String ownerName = petList.getOwnerName();
            System.out.printf("Please confirm that the current profile name is correct (Y/N): %s \n",
                    ownerName);
            boolean confirmNameBool = processConfirmName();
            if (!confirmNameBool) {
                newOwnerName();
            }
        }
    }

    *//*
    REQUIRES: assumes there is at least 1 Pet in petList.
    MODIFIES: this
    EFFECTS: sets owner name field of petList to inputted String.
     *//*
    private void newOwnerName() {
        System.out.println("Please enter your new name:");
        String inputName = scanner.next();
        petList.setOwnerName(inputName);
    }

    *//*
    REQUIRES: valid String variable in the owner name field of petList.
    EFFECTS: processes user input for confirming the current owner name
     *//*
    private boolean processConfirmName() {
        String confirmNameStr = scanner.next();
        confirmNameStr = confirmNameStr.toLowerCase();
        switch (confirmNameStr) {
            case "y":
                return true;
            case "n":
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + confirmNameStr);
        }
    }

    *//*
    EFFECTS: prints options for main menu if a profile is currently loaded
     *//*
    private void displayMainMenu() {
        printMenu(MAIN_MENU_HEADER);
        System.out.println("\nPlease input the key corresponding to an option.");
        System.out.println("\nCurrent Pets:");
        printPetNames(", ");
        printMenu(MAIN_MENU);
    }

    *//*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: prints all pets and allows user to select a pet to edit or (b) to main menu
    *//*
    private void managePets() {
        printMenu(PET_MENU_HEADER);
        System.out.println("Select a pet to edit by entering the corresponding number, or b to return to main menu");
        System.out.println("CURRENT PETS:");
        printPetSelect();
        System.out.println("b - back to main menu");
        selectAPetOrBack();
    }

    *//*
    REQUIRES: inputted int < number of pets in newPetList
    EFFECTS: processes user input for the viewPet menu -
             either an integer corresponding to a pet to edit or (b)ack to main menu
     *//*
    private void selectAPetOrBack() {
        boolean invalidCommand = true;
        while (invalidCommand) {
            String selection = scanner.next();
            try {
                int petIndex = Integer.parseInt(selection);
                editPet(petIndex);
                invalidCommand = false;
            } catch (NumberFormatException e) {
                if ("b".equals(selection)) {
                    System.out.println("Back to main menu.");
                    invalidCommand = false;
                } else {
                    System.out.println("Selection not valid...");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Selection not valid...");
            }
        }
    }

    *//*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes 1 field of a Pet object to new user input*//*
    private void editPet(int petIndex) {
        Pet selectedPet = petList.getPetArray().get(petIndex);
        boolean stayHere = true;

        while (stayHere) {
            printPetFields(selectedPet);
            printMenu(MANAGE_PET_MENU_HEADER);
            printMenu(PET_MENU);
            //displayPetMenu();
            String command = scanner.next();
            if ((command.equals("b")) && (petList.getNumPets() == 0)) {
                stayHere = false;
            } else {
                stayHere = processPetMenuCommand(command, selectedPet);
            }
        }
    }

    *//*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: displays a pet's option menu
     *//*
    private void displayPetMenu() {
        for (String s : PET_MENU) {
            System.out.println(s);
        }
    }

    *//*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: processes user input for a pet's option menu
     *//*
    private boolean processPetMenuCommand(String command, Pet selectedPet) {
        switch (command) {
            case "d":
                editDiet(selectedPet);
                return true;
            case "e":
                editName(selectedPet);
                return true;
            case "w":
                editWeight(selectedPet);
                return true;
            case "r":
                removeSelectedPet(selectedPet);
                return false;
            case "b":
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    *//*
    REQUIRES: newPetList Array created
    MODIFIES: this
    EFFECTS: adds new Pet object to newPetList Array via user inputs for fields
    *//*
    private void addNewPet() {
        boolean keepgoingnaming = true;

        while (keepgoingnaming) {
            System.out.println("ADD A NEW PET");
            System.out.println("Enter new pet name:");
            String petName = scanner.next();

            if (petList.duplicateName(petName)) {
                System.out.println("There is already a pet with that name! Try a new pet name:");
            } else {
                Pet newPet = instantiateNewPet(petName);
                System.out.println("\nNew pet added: " + newPet.getPetName());
                petList.add(newPet);
                printPetFields(newPet);
                keepgoingnaming = false;
            }
        }
    }

    *//*
    REQUIRES: unique pet name from existing Pets in newPetList
    EFFECTS: creates new Pet based on name argument and field values from user input
     *//*
    private Pet instantiateNewPet(String petName) {
        System.out.println("Enter new pet's weight (kg):");
        double petWtKg = scanner.nextDouble();
        return new Pet(petName, petWtKg);
    }

    *//*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: removes Pet object to newPetList Array*//*
    //todo add removal confirmation
    private void removePetChoose() {
        String removedPetName;
        for (String s : Arrays.asList("REMOVE PET", "Select pet to remove (enter corresponding number):")) {
            System.out.println(s);
        }
        printPetSelect();
        Pet petToRemove = selectPet();
        assert petToRemove != null;
        removedPetName = petToRemove.getPetName();
        petList.remove(petToRemove);
        System.out.printf("%s successfully removed.%n", removedPetName);
    }

    *//*
    REQUIRES: newPetList has at least 1 Pet object
    MODIFIES: this
    EFFECTS: removes a selected Pet from newPetList
     *//*
    private void removeSelectedPet(Pet selectedPet) {
        String removedPetName = selectedPet.getPetName();
        petList.remove(selectedPet);
        //todo printing not working
        System.out.printf("%s successfully removed.%n", removedPetName);
    }

    *//*
    REQUIRES: newPetList Array has >= 1 Pet
    EFFECTS: prints a list of pet names and returns a user-selected Pet from newPetList, or b -> prev menu
    *//*
    private Pet selectPet() {
        if (scanner.hasNextInt()) {
            int petIndex = scanner.nextInt();
            return petList.getPetArray().get(petIndex);
        } else if (scanner.hasNext() && scanner.next().equalsIgnoreCase("b")) {
            runApp();
        }
        return null;
    }

    *//*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes the weight field of the Pet object to new user input*//*
    private void editWeight(Pet petToEdit) {
        // edit weight
        System.out.println("Enter new pet weight:");
        double newWeight = scanner.nextDouble();
        petToEdit.setWeight(newWeight);
    }

    *//*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes the name field of the Pet object to new user input*//*
    private void editName(Pet petToEdit) {
        // edit name
        System.out.println("Enter new pet name:");
        String newPetName = scanner.next();
        petToEdit.setNewName(newPetName);
    }

    //todo abstract the edit fns
    //todo editDiet() docstring
    //todo expand diet var to 3 types (can cup kg)
    private void editDiet(Pet petToEdit) {
        System.out.println("Enter caloric information for current diet (kCal/kg):");
        double newPetDiet = scanner.nextDouble();
        petToEdit.setNewDiet(newPetDiet);
    }

    *//*
    EFFECTS: prints the list of Pet names in newPetList and their array index.
     *//*
    private void printPetSelect() {
        for (int i = 0; petList.getNumPets() > i; i++) {
            Pet currentPet = petList.getPetArray().get(i);
            System.out.printf("%s - %d%n", currentPet.getPetName(), i);
        }
    }

    *//*
    REQUIRES: assumes there is at least 1 Pet in petList
    EFFECTS: prints the list of Pet names in newPetList.
     *//*
    private void printPetNames(String delimiter) {
        //only one pet
        if (petList.getNumPets() == 1) {
            Pet currentPet = petList.getPetArray().get(0);
            System.out.println(currentPet.getPetName());
        } else { //more than 1 pet, print pet+delim, last pet is not followed by delim
            for (int i = 0; (petList.getNumPets() - 1) > i; i++) {
                Pet currentPet = petList.getPetArray().get(i);
                System.out.print(currentPet.getPetName() + delimiter);
            }
            int lastIndex = petList.getNumPets() - 1;
            Pet lastPet = petList.getPetArray().get(lastIndex);
            System.out.print(lastPet.getPetName());
        }
    }

    *//*
    EFFECTS: Prints all fields for a Pet.
    *//*
    public void printPetFields(Pet petToPrint) {
        System.out.println("\nName: " + petToPrint.getPetName());
        System.out.println("Weight: " + petToPrint.getWeight() + " kg\n");
        System.out.println("Diet caloric content: " + petToPrint.getDietCalPerKg() + " kCal/kg\n");
    }

    *//*
    EFFECTS: prints all pets with all of their fields to the user console.
    *//*
    public void printAllPetsAllFields() {
        for (int i = 0; i < petList.getNumPets(); i++) {
            Pet printPet = petList.getPetArray().get(i);
            printPetFields(printPet);
        }
    }*/


    public static class MainMenuTab extends JPanel {
        JSplitPane managePetsPanel;
        Boolean emptyPetList;
        ActionListener actionListener;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc;
        JButton newPetButton = new JButton("Add New Pet");
        JButton editPetButton = new JButton("Remove a Pet");
        JButton savePetButton = new JButton("Save Session");
        ListModel<String> petListModel;
//        JPanel petDisplayPanel = new JPanel();

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

        private static JLabel mainMenuHeader() {
            JLabel label = new JLabel("Pet Weight Management App Dashboard", SwingConstants.CENTER);
            return label;
        }

        public JPanel getManagePetsRightPane() {
            return (JPanel) this.managePetsPanel.getRightComponent();
        }

        private void addWithConstraints(JComponent component, int gbcWidth, int gbcX, int gbcY) {
            gbc.gridwidth = gbcWidth;
            gbc.gridx = gbcX;
            gbc.gridy = gbcY;
            this.add(component, gbc);
        }

        private void addActionListeners() {
            newPetButton.addActionListener(actionListener);
            editPetButton.addActionListener(actionListener);
            savePetButton.addActionListener(actionListener);
        }

        /*ListModel<Class<?>> model = myList.getModel();
                for(int i = 0; i < model.getSize(); i++) {
                System.out.println(model.getElementAt(i));*/

        //Listens to the Jlist
//            public int valueChanged(ListSelectionEvent e) {
//                petJList = (JList)e.getSource();
//                return petJList.getSelectedIndex();
//            }
//
//            //Renders view of selected Pet
//            protected void updateLabel(int index) {
//                Pet pet = pe.get(index);
//                String name = pets.get(index).getPetName();
//                System.out.println(name);
//            }
//
//            public JScrollPane petScrollSelect(JList<Pet> pets) {
//                JScrollPane scrollPane = new JScrollPane(petList);
//                return scrollPane;
//            }
    }
//
//    //todo docs
//    public static class ManagePetsPanel extends JSplitPane {
//        JList<String> petNameJList;
//        JScrollPane leftPane;
//        JPanel rightPane;
//
//        /*ListSelectionListener testlistSelectionListener = new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                JList<Pet> petJList = (JList) e.getSource();
//                int selectedIndex = petJList.getSelectedIndex();
//                System.out.println("test" + selectedIndex);
//                JScrollPane newDisplayPane = new JScrollPane();
//                newDisplayPane.add(new JLabel(String.format("%d", selectedIndex)));
//                setLeftPane(newDisplayPane);
//                setLeftPane(leftPane);
//
//            }
//        };*/
//
//        public ManagePetsPanel(ListModel<String> petNameListModel) {
//            this.petNameJList = new JList<>(petNameListModel);
//            this.petNameJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//            this.setOneTouchExpandable(true);
//            //init both views
//            this.leftPane = new JScrollPane(petNameJList);
//            this.setLeftComponent(leftPane);
//            initRightPane();
//            this.setRightComponent(rightPane);
//        }
//
//        //todo docs
//        private void initRightPane() {
//            JPanel displayPane = new JPanel();
//            if (petNameJList.getLastVisibleIndex() != -1) {
//                displayPane.add(new JLabel("No pet selected"));
//                this.rightPane = displayPane;
//            } else {
//                displayPane.add(new JLabel("No pets in profile"));
//                this.rightPane = displayPane;
//            }
//        }
//
//        public void setPetNameJList(JList<String> petNameJList) {
//            this.petNameJList = petNameJList;
//        }
//
//        public void setLeftPane(JScrollPane leftPane) {
//            leftPane = leftPane;
//            this.setLeftPane(leftPane);
//        }
//
//        public void setRightPane(JPanel rightPane) {
//            rightPane = rightPane;
//            this.setRightPane(rightPane);
//        }
//
//        public JList<String> getPetNameJList() {
//            return petNameJList;
//        }
//
//        public JScrollPane getLeftPane() {
//            return leftPane;
//        }
//
//        public JPanel getRightPane() {
//            return rightPane;
//        }
//    }
}
