package ui;

import model.Pet;
import model.PetList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

import static java.awt.GridBagConstraints.BOTH;

// Pet Calorie Calculator application
//
//
//
// user interface methods
//todo later can't save if both profile name and pet list are empty
//todo later if no diet calories entered, display that instead of 0
//todo later warning if weight is over 80?kg
//todo later change button names to "exit app" and "close profile"
//todo later ask for Owner name when saving
//todo later add exception detail of trying to edit a pet that's already being edited
//todo later set fixed size for splitPane
//todo later formatting polish
//todo later move edit pet button to pet display
//todo if blank profile name should say "no profile name set"
//todo later maybe - add duplicate check for editing pet as well?? naah
//TODO  Warns user if they attempt to create a pet with a duplicate name (not case-sensitive)
//todo later ?unsaved changes
//tofix dietCal saved properly

public class MERapp extends JFrame implements Runnable {
    public static final int FRAME_WIDTH = 450;
    public static final int FRAME_HEIGHT = 400;
    public static final Dimension MIN_FRAME_SIZE = new Dimension(450, 400);
    private static final Dimension PORTRAIT_PIC_SIZE = new Dimension(75, 75);

    private static final String JSON_STORE_URL = "./data/profiles.json"; //normal
    //        private static final String JSON_STORE_URL = "./data/test.json"; //empty case for testing
//        private static final String JSON_STORE_URL = "./data/fail.json"; //no such file case for
    private static final String DEFAULT_PET_PORTRAIT_URL = "./data/dog.png";
    private static final String FRAME_ICON_URL = "./data/paw.png";
    int index; // -1 represents no index selected
    private JList<String> petJList; //for listSelectionListener
    private PetList petList;
    private ArrayList<Pet> petArrayList;
    private Pet currentPet;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    //listeners
    private ActionListener introPaneUIListener;
    private ActionListener mainTabListener;
    private ListSelectionListener leftPaneSelectListener;
    //
    private JFrame frame;
    private JPanel introPaneUI = new JPanel(); //used for 2 displays
    private AddNewPetTab addPetTab;
    private JScrollPane leftPane;
    private JPanel rightPane;
    private Tabs.NewProfilePanel createNewProfilePane;
    private JSplitPane mainTabSplitPane;
    private JTabbedPane tabbedPaneUI;
    private EditPetTab editPetTab;


    //==============================================================

    /*
    MODIFIES: app title
    EFFECTS: constructs the Pet Weight Management application and initializes fields, GUI, and json I/O
     */
    //tododoc
    public MERapp() {
        super("Pet Weight Management App");
        run();
//        EventQueue.invokeLater(this::run);
    }


    //==============================================================

    // MODIFIES: this
    // EFFECTS: initializes the JSON reader and writer and attempts to read from the provided file URL.
    // also initializes the PetList and Pet ArrayList fields.

    //EFFECTS: converts a Pet ArrayList to a ListModel of pet names as Strings
    public static ListModel<String> toListModel(ArrayList<Pet> petArrayList) {
        DefaultListModel<String> petListModel = new DefaultListModel<>();
        for (Pet pet : petArrayList) {
            petListModel.addElement(pet.getPetName());
        }
        return petListModel;
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
    //starts running app by calling functions to initialize fields and launch the GUI;
    @Override
    public void run() {
        initFields();
        initFrame();
        initIntroUI();
    }

    /*
    REQUIRES:
    MODIFIES: this
    EFFECTS: initializes
     */
    private void initFields() {
        petList = new PetList();
        petArrayList = new ArrayList<>();
        index = -1;
        currentPet = null;
        jsonReader = new JsonReader(JSON_STORE_URL);
        jsonWriter = new JsonWriter(JSON_STORE_URL);
    }

    //==============================================================
    //Listeners

    /*
    REQUIRES: valid image from static URL
    MODIFIES: this.frame, this.introPaneUI
    EFFECTS: creates the main JFrame window that hosts all app components with the following presets
             Preset characteristics: min size, frame image icon, close behaviour, title
     */
    private void initFrame() {
        frame = new JFrame("Pet Weight Management App GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon frameIcon = new ImageIcon(FRAME_ICON_URL);
        frame.setIconImage(frameIcon.getImage());
        frame.setMinimumSize(MIN_FRAME_SIZE);
    }

    //to be based on num of tabs in tabbedpaneUI

    private void initIntroUI() {
        initIntroListener();
        introPaneUI.add(new Tabs.IntroMenuPane(introPaneUIListener));
        frame.getContentPane().add(introPaneUI);
        frame.setVisible(true);
    }

    /*
    MODIFIES: this.introPaneUIListener
    EFFECTS: when a Button in IntroUI is pressed, executes the associated command
             will display "unknown event" message if the received command doesn't match an existing case
     */
    private void initIntroListener() {
        introPaneUIListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "New Profile": //can also use if (source == objName) for cases
                    createNewProfileTabEvent();
                    break;
                case "Load saved profile":
                    loadSavedProfileEvent();
                    break;
                case "Submit":
                    createNewProfileInputEvent();
                    break;
                case "Cancel":
                    cancelCreateNewProfileEvent();
                    break;
                case "Close App":
                    confirmCloseAppIntroEvent();
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };
    }

    /*
    REQUIRES: frame and introPaneUI initialized
    MODIFIES: this
    EFFECTS: return to intro menu without creating new profile.
     */
    private void cancelCreateNewProfileEvent() {
        setJPanel(introPaneUI, new Tabs.IntroMenuPane(introPaneUIListener));
        frame.setContentPane(introPaneUI);
        frame.setVisible(true);
    }

    /*
    REQUIRES: UI with a closeApp JButton instantiated
    MODIFIES: this
    EFFECTS: Exit confirmation dialog box initiated when user selects "Close App" button from introUI.
     */
    private void confirmCloseAppIntroEvent() {
        int response;
        response = JOptionPane.showConfirmDialog(null,
                "Do you want to close this application?",
                "Confirm Close Application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        switch (response) {
            case JOptionPane.YES_OPTION: //close app
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION: //do nothing
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + response);
        }
    }

    /*
    REQUIRES: UI with a closeApp JButton instantiated
    MODIFIES: this
    EFFECTS: Exit confirmation dialog box initiated when user selects "Close App" button from introUI.
     */
    private void confirmCloseAppMainTabEvent() {
        int response;
        response = JOptionPane.showConfirmDialog(null,
                "Do you want to save this session?",
                "Confirm Close Application",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        switch (response) {
            case JOptionPane.YES_OPTION: //save and exit profile
                saveSessionEvent();
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION: //exit without saving
                System.exit(0);
                break;
            case JOptionPane.CANCEL_OPTION: //don't exit
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + response);
        }
    }

    /*
    REQUIRES: mainTab instantiated
    MODIFIES: this.mainTabListener
    EFFECTS: initializes an ActionListener for mainTab's JButtons
     */
    private void initMainListener() {
        mainTabListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "Add New Pet":
                    addNewPetTabEvent();
                    break;
                case "Edit a Pet":
                    editPetEvent();
                    break;
                case "Save Session":
                    saveSessionEvent();
                    break;
                case "Exit Profile":
                    confirmExitProfileEvent();
                    break;
                case "Close App":
                    confirmCloseAppMainTabEvent();
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };
    }

    /*
    REQUIRES: petJList instantiated with >=1 pet name(s)
    MODIFIES: this.leftPaneSelectListener
    EFFECTS: initializes a ListSelectionListener. responds to single selection of the
             JList of pet names displayed in the leftPane of the mainTab's JSplitPane.
     */
    private void initListSelectionListener() {
        leftPaneSelectListener = e -> {
            JList<String> currentPetJList = (JList) e.getSource();
            index = currentPetJList.getSelectedIndex();
            rightPane = updateRightPane();
        };
    }

    //==============================================================
//beyond the initial intro panel - main menu
    //going to main menu

    /*
    REQUIRES: AddPetTab instantiated
    EFFECTS: in AddPetTab, specifies functions called by different JButtons
     */
    public ActionListener addPetTabListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = e.getActionCommand();
                switch (buttonName) {
                    case "Submit":
                        createNewPetEvent();
                        break;
                    case "Cancel":
                        closeAddPetTabEvent();
                        break;
                    default:
                        JOptionPane.showMessageDialog(frame, "Unknown event");
                }
            }
        };
    }

    /*
    REQUIRES: assigned to appropriate JButtons of editPetTabListener
    EFFECTS: returns a
     */
    private ActionListener editPetTabListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = e.getActionCommand();
                switch (buttonName) {
                    case "Okay":
                        submitChangesEditPet();
                        closeEditTabEvent();
                        break;
                    case "Cancel":
                        closeEditTabEvent();
                        break;
                    case "Delete this pet":
                        deletePetFromPetList();
                        updateMainDash(); //todo check this works
                        closeEditTabEvent();
                        break;
                    default:
                        JOptionPane.showMessageDialog(frame, "Unknown event");
                }
            }
        };
    }

    /*
REQUIRES: frame, petList, petArrayList instantiated
MODIFIES: this
EFFECTS: instantiates the main profile UI and set visible in JFrame.
         initializes associated listeners.
 */
    private void launchMainMenu() {
        initMainListener();
        initListSelectionListener();
        tabbedPaneUI = initMainTab();
//        frame.getContentPane().add(tabbedPane);
        frame.setContentPane(tabbedPaneUI);
//        gridBagLayout = new GridBagLayout();

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
    }

    /*
    REQUIRES: petList instantiated
    MODIFIES: this.petList
    EFFECTS: Receives user input from the "create new profile" form and set as petList's owner name.
             null and strings are accepted as per JTextField's formatter.
     */
    private void createNewProfileInputEvent() {
        String profileName = createNewProfilePane.getProfileNameInput();
        petList.setOwnerName(profileName);
        launchMainMenu();
    }

    //todo later add option to edit owner name
    //catch incorrect entries
    //updates the left panel of the splitPane with current data
    /*REQUIRES: no currently loaded profile (empty petList)
    MODIFIES: this
    EFFECTS: loads previously saved profiles from local json storage path*/
    //       current fields - name/string, weight/double, diet/double
    public void loadSavedProfileEvent() {
        try {
            petList = jsonReader.read();
            petArrayList = petList.getPetArray();

            launchMainMenu();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to read from file location: " + JSON_STORE_URL);
        }
    }

    //
    //==============================================================
    //add profile
    //
    //todo later able to have multiple profiles (can add or remove)

    /*
    REQUIRES: these fields instantiated: petList, petArrayList mainTabListener, leftPaneSelectListener, mainTabSplitPane
    MODIFIES: this.mainTab
    EFFECTS: initializes mainTab and returns a JTabbedPane with mainTab as its only tab.
     */
    private JTabbedPane initMainTab() {
        initSplitPane(petArrayList);
        MainTab mainTab = new MainTab(petList,
                mainTabListener,
                mainTabSplitPane);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Main Dashboard", mainTab);
        return tabs;
    }

    /*
    REQUIRES: petList and petArrayList instantiated
    MODIFIES: this
    EFFECTS: inits the JSplitPane which forms part of the opened profile dashboard UI.
     allows selecting and viewing any pet in the opened profile.
     */
    private void initSplitPane(ArrayList<Pet> arrayList) {
        petJList = toNamesJList(arrayList);
        petJList.addListSelectionListener(leftPaneSelectListener);
        mainTabSplitPane = new JSplitPane();
        rightPane = new JPanel();
        leftPane = new JScrollPane(petJList);
        mainTabSplitPane.setLeftComponent(leftPane);
        mainTabSplitPane.setRightComponent(rightPane);
        emptyRightPane();
    }

    /*
    REQUIRES:
    MODIFIES: this.petList, this.createNewProfilePane, introPaneUI, frame
    EFFECTS:
     */
    public void createNewProfileTabEvent() {
        System.out.println("Create new profile"); //print
//        petList = new PetList();
        createNewProfilePane = new Tabs.NewProfilePanel(introPaneUIListener);
        setJPanel(introPaneUI, createNewProfilePane);
        frame.setContentPane(introPaneUI);
        frame.setVisible(true);

    }

    //
    //==============================================================
    //add pet
    //

    /*
    REQUIRES:
    MODIFIES:
    EFFECTS:
            Resets currentPet to null.
     */
    private void createNewPetEvent() {
        int count = petList.getPetArray().size();
        submitAddNewPetTab(); //TODO warning for creating a pet with a duplicate name
        if (petList.getPetArray().size() == count + 1) {
            JOptionPane.showMessageDialog(frame,
                    "Successfully added "
                            + currentPet.getPetName()
                            + " to profile.");
            closeAddPetTabEvent(); //includes updating main dash
        } else {
            JOptionPane.showMessageDialog(frame, "creating a pet was unsuccessful");
        }
    }

    /*
    REQUIRES: tabbedPaneUI instantiated
    MODIFIES: this
    EFFECTS: creates new AddNewPetTab instance if there's no current instance.
     */
    private void addNewPetTabEvent() {
        if (!(addPetTab == null)) {
            JOptionPane.showMessageDialog(frame, "You can only add one pet at a time");
        } else {
            System.out.println("add pet"); //print
            addPetTab = new AddNewPetTab(addPetTabListener());
            tabbedPaneUI.addTab("Create New Pet", addPetTab);
            tabbedPaneUI.setSelectedComponent(addPetTab);
        }
    }

    /*
    REQUIRES: an instance of addPetTab is in tabbedPaneUI.
    MODIFIES: this, this.tabbedPaneUI
    EFFECTS: Closes the AddPetTab. Resets fields and updates main tab.
     */
    private void closeAddPetTabEvent() {
        System.out.println("close window"); //print
        tabbedPaneUI.remove(addPetTab);
        addPetTab = null;
        currentPet = null;
        index = -1;
        rightPane.removeAll();
        updateMainDash();
        frame.setVisible(true);
    }

    /*
    REQUIRES: addPetTab is instantiated
    MODIFIES: this.petList
    EFFECTS: try creating a Pet from user input. if inputs are invalid, will display failed msg.
              if Pet has been created, call function to check for duplicate names
     */
    private void submitAddNewPetTab() {
        System.out.println("adding new pet to pet list"); //print
        currentPet = addPetTab.createNewPet();
        System.out.println(currentPet); //print
        if (currentPet == null) {
            JOptionPane.showMessageDialog(frame, "Failed to"
                    + " create pet. Please ensure that both the pet name"
                    + " and weight are valid. Weight must be a positive"
                    + " number. Please input 0 if you don't know your"
                    + " pet's weight.");
        } else {
            checkDuplicateName();
        }
    }

    /*
    REQUIRES: currentPet and petList instantiated
    MODIFIES: this.petList
    EFFECTS: checks the newly created pet's name against other
             pet names in this profile for duplicate names (not case sensitive).
             if duplicate is found, Show warning dialog.
     */
    private void checkDuplicateName() throws IllegalStateException {
        if (petList.duplicateName(currentPet.getPetName())) {
            int response = JOptionPane.showConfirmDialog(null,
                    "There's already a pet with that name! Do you still want to proceed and create this pet?",
                    "Duplicate name found",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            switch (response) {
                case JOptionPane.YES_OPTION:
                    petList.add(currentPet);
                    break;
                case JOptionPane.NO_OPTION:
//                    submitAddNewPetTab();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + response);

            }
        } else {
            petList.add(currentPet);
        }
    }

    //
    //==============================================================
    //SCRIPTS
    //

    /*
    REQUIRES:
    MODIFIES:
    EFFECTS: When the EditPet JButton is activated, checks whether all circumstances are met for editing.
    Calls function to edit pet or display appropriate msg for situation.
    Non-valid circumstances: No Pets in PetArrayList, no pet selected in JList,
    OR there's already another editPetTab instance.
     */
    private void editPetEvent() {
        if (petListIsEmpty()) {
            JOptionPane.showMessageDialog(frame, "No pets in profile currently. "
                    + "Select \"Add New Pet\" to create a new pet.");
        } else if (index == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a pet to edit from the list of names.");
        } else if (!(editPetTab == null)) {
            JOptionPane.showMessageDialog(frame, "You can only edit one pet at a time");
        } else {
            //other option: (tabbedPaneUI.indexOfTabComponent(editPetTab) == -1)
            System.out.println("index = " + index); //print
            try {
                editSelectedPet();
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(frame, "Please select a pet to edit.");
                System.out.println(e.getMessage()); //print
            }
        }
    }

    //returns true if petList is empty
    private boolean petListIsEmpty() {
        return (petList.getPetArray().size() <= 0);
    }

    //return true if owner name is empty string
    private boolean noProfileName() {
        String profileName = petList.getOwnerName();
        return (Objects.equals(profileName, ""));
    }

    /*
    REQUIRES: index returns a non-null valid Pet object from petArrayList
    MODIFIES:
    EFFECTS:
     */
    private void editSelectedPet() {
        currentPet = petArrayList.get(index);
        ActionListener editPetListener = editPetTabListener();
        editPetTab = new EditPetTab(currentPet, editPetListener);
        tabbedPaneUI.addTab("Edit " + currentPet.getPetName(), editPetTab);
        tabbedPaneUI.setSelectedComponent(editPetTab);
        System.out.println(petArrayList.get(index).getPetName()); //print
        System.out.println("edit pet" + index); //print
    }

    /*
    REQUIRES: frame, tabbedPaneUI, mainTab and EditPetTab instantiated
    MODIFIES: this
    EFFECTS: closes the EditPetTab and returns user to main dashboard tab.
     */
    private void closeEditTabEvent() {
        System.out.println("close window"); //print
        tabbedPaneUI.remove(editPetTab);
        editPetTab = null;
        updateMainDash();
        frame.setVisible(true);
    }

    /*
    REQUIRES: jsonWriter instantiated
    MODIFIES: data/profiles.json
    EFFECTS: writes petList to URL location as json file. A new file is created if no file exists at that URL.
     */
    private void saveSessionEvent() {
        System.out.println("save session"); //print
        try {
            jsonWriter.open();
            jsonWriter.write(petList);
            jsonWriter.close();
            if (noProfileName()) {
                JOptionPane.showMessageDialog(frame,
                        petList.getOwnerName() + "Saved unnamed profile to " + JSON_STORE_URL);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Saved " + petList.getOwnerName() + "'s profile to " + JSON_STORE_URL);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_URL); //print
        }
    }

    //==============================================================
    //save session
    //

    /*
    REQUIRES: EditPetTab instantiated.
    MODIFIES: petList
    EFFECTS: remove pet found at the current index
     */
    private void deletePetFromPetList() {
        currentPet = petList.getPetArray().get(index);
        String deletedPetName = currentPet.getPetName();
        petList.remove(currentPet);
        JOptionPane.showMessageDialog(frame, String.format("Successfully removed %s from profile.", deletedPetName));
    }

    /*
    REQUIRES: petList is instantiated.
    MODIFIES: data.json
    EFFECTS: Save confirmation pop-up dialog box, launched when user selects "Exit Profile"
     */
    private void confirmExitProfileEvent() {
        int response;
        response = JOptionPane.showConfirmDialog(null,
                "Do you want to save this session?",
                "Confirm Exit Profile",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        switch (response) {
            case JOptionPane.YES_OPTION: //save and exit profile
                saveSessionEvent();
                exitProfileEvent();
                break;
            case JOptionPane.NO_OPTION: //exit without saving
                exitProfileEvent();
                break;
            case JOptionPane.CANCEL_OPTION: //don't exit
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + response);
        }
    }

    /*
    REQUIRES: frame initialized
    MODIFIES: this
    EFFECTS: closes currently opened profile and restarts UI to the intro menu.
     */
    private void exitProfileEvent() {
        frame.setVisible(false);
        tabbedPaneUI = null;
        introPaneUI = new JPanel();
        leftPane = null;
        rightPane = null;
        mainTabSplitPane = null;
        initFields();
        initFrame();
        initIntroUI();
    }

    //
    //==============================================================

    /*
    REQUIRES: editPetTab, petList instantiated
    MODIFIES: this
    EFFECTS: updates the edited pet and opened profile with user input.
     */
    private void submitChangesEditPet() {
        currentPet = editPetTab.getEditedPet();
        index = editPetTab.getIndex();
        petList.getPetArray().set(index, currentPet);
        System.out.println("update pet list"); //print
    }

    /*
    REQUIRES: leftPane and rightPane instantiated
    MODIFIES:this.leftPane, this.rightPane
    EFFECTS: updates petArrayList with current petList and calls functions to update each side of the JSplitPane
     */
    private void updateMainDash() {
        System.out.println("updating dash"); //print
        petArrayList = petList.getPetArray();

        updateLeftPane();
        emptyRightPane();
    }

    /*
    REQUIRES: rightPane instantiated
    MODIFIES: this.rightPane
    EFFECTS: launches/resets rightPane with no pet displays.
             will display msg for user to select a pet or that there are no pets to select.
     */
    private void emptyRightPane() {
        rightPane.removeAll();
        if (petListIsEmpty()) {
            rightPane.add(new JLabel("No pets in profile. Select \"Add New Pet\" to create a pet."));
        } else {
            rightPane.add(new JLabel("No pet selected"));
        }
    }

    /*
    REQUIRES: petArrayList is initialized
              mainTabSplitPane is an initialized JSplitPane.
    MODIFIES: this.petJList, this.petPane, this.mainTabSplitPane
    EFFECTS: updates the list of pet names in the L SplitPane.
     */
    private void updateLeftPane() {
        petJList = toNamesJList(petArrayList);
        petJList.addListSelectionListener(leftPaneSelectListener);
        leftPane = new JScrollPane(petJList);
        mainTabSplitPane.setLeftComponent(leftPane);
    }

    /*
    REQUIRES: index returns a valid Pet object from petArrayList
    MODIFIES: this.mainTabSplitPane, this.currentPet
    EFFECTS: when the corresponding Pet is selected in the L pane,
             displays a default portrait pic and details for that pet.
     */
    private JPanel updateRightPane() {
        JPanel displayPane = new JPanel();
        displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
        currentPet = petArrayList.get(index);

        //portrait pic
        ImageIcon imgIcon = setDefaultPortraitImgIcon();
        JLabel portraitPic = new JLabel(imgIcon, SwingConstants.CENTER);
        displayPane.add(portraitPic);
        displayPane.add(new JLabel("Selected Pet: " + currentPet.getPetName()));
        displayPane.add(new JLabel("Weight (kg): " + currentPet.getWeight()));
        displayPane.add(new JLabel("Current diet calories (KCal/kg): " + currentPet.getDietCalPerKg()));
        mainTabSplitPane.setRightComponent(displayPane);
        return displayPane;
    }

    //todomaybe use SizeDisplayer to resize img
    /*
    REQUIRES: valid picture image icon imported from static URL
    EFFECTS: returned a rescaled image of the specified dimensions of PORTRAIT_PIC_SIZE
     */
    private ImageIcon setDefaultPortraitImgIcon() {
        ImageIcon imgFromURL = new ImageIcon(DEFAULT_PET_PORTRAIT_URL);
        Image img = imgFromURL.getImage();
        Image scaledImg = img.getScaledInstance(PORTRAIT_PIC_SIZE.width, PORTRAIT_PIC_SIZE.height, Image.SCALE_FAST);
        return new ImageIcon(scaledImg);
    }

    //EFFECTS: add a given Component to a given JPanel
    public void setJPanel(JPanel panel, Component component) {
        panel.removeAll();
        panel.add(component);
    }

    //EFFECTS: converts a Pet ArrayList to a JList of pet names as strings
    public JList<String> toNamesJList(ArrayList<Pet> petArrayList) {
        ListModel<String> petNameListModel = toListModel(petArrayList);
        JList<String> petNameJList = new JList<String>(petNameListModel);
        return petNameJList;
    }

    //creates and runs the UI tab for creating adding a new pet
    //user may input and submit pet traits into text fields to create new pet
    //user may also cancel pet creation by the "cancel" JButton
    public static class AddNewPetTab extends JPanel {
        private final NumberFormat decimalFormat = new DecimalFormat();
        private final JLabel instructionLabel = new JLabel("Please enter your pet's information below",
                SwingConstants.CENTER);
        ActionListener addPetListener;
        JButton createPetButton;
        JButton backButton;
        JPanel buttonPane;
        private JPanel createNewPetInputPane;
        //        private NumberFormat decimalFormat = new DecimalFormat("PositivePattern");
        private JLabel weightLabel;
        private JFormattedTextField weightField;
        private JTextField nameField;
        private JFormattedTextField dietCalField;
        private JLabel nameLabel;
        private JLabel dietCalLabel;
        private double weightInput;
        private String nameInput;
        private double dietCalInput;

        //inits the UI form for AddNewPetTab
        public AddNewPetTab(ActionListener addPetTabListener) {
            this.addPetListener = addPetTabListener;
            setCreateNewPetInputPane();
            //init buttons
            createPetButton = new JButton("Submit");
            backButton = new JButton("Cancel");
            createPetButton.addActionListener(addPetListener);
            backButton.addActionListener(addPetListener);
            //init button panel
            buttonPane = new JPanel(new GridLayout(1, 2));
            buttonPane.add(createPetButton);
            buttonPane.add(backButton);
            buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            this.add(instructionLabel);
            this.add(createNewPetInputPane, BorderLayout.CENTER);
            this.add(buttonPane, BorderLayout.SOUTH);
        }

        /*
        MODIFIES: this
        EFFECTS: inits the JPanel displaying user input text fields and their labels
         */
        private void setCreateNewPetInputPane() {
            this.weightLabel = new JLabel("Weight (kg) *");
            this.weightField = new JFormattedTextField(decimalFormat);
            this.nameLabel = new JLabel("Pet Name *");
            this.nameField = new JTextField();
            this.dietCalLabel = new JLabel("Diet Calories (kCal/kg)");
            this.dietCalField = new JFormattedTextField(decimalFormat);

            //todo forbid >1 tab of AddPetTab
            //todo check that name and weight are both filled - provide more details
            createNewPetInputPane = new JPanel();
            createNewPetInputPane.setLayout(new GridLayout(3, 2));
            createNewPetInputPane.add(nameLabel);
            createNewPetInputPane.add(nameField);
            createNewPetInputPane.add(weightLabel);
            createNewPetInputPane.add(weightField);
            createNewPetInputPane.add(dietCalLabel);
            createNewPetInputPane.add(dietCalField);
        }


        /*
        REQUIRES: weightInput instantiated
        EFFECTS: returns true if the input is a valid positive number.
        //value of 0 is allowed if the user doesn't know pet weight.
         */
        private boolean validWeightInput() {
            try {
                this.weightInput = ((Number) weightField.getValue()).doubleValue();
                if (weightInput > 0) {
                    return true;
                }
            } catch (NullPointerException e) {
                return false;
            }
            return false;
        }

        //checks if nameField returns a valid name String
        private boolean validNameInput() {
            try {
                nameInput = nameField.getText();
                return true;
            } catch (NullPointerException e) {
                return false;
            }
        }

        //checks if dietCalField returns a positive double type number
        private boolean validDietCalInput() {
            try {
                dietCalInput = ((Number) dietCalField.getValue()).doubleValue();
                if (weightInput > 0) {
                    return true;
                }
            } catch (NullPointerException e) {
                return false;
            }
            return false;
        }

        /*
        MODIFIES: this
        EFFECTS: returns a Pet object if both name and weight inputs are valid.
         */
        public Pet createNewPet() {
            if (validNameInput() && validWeightInput()) {
                Pet newPet = new Pet(nameInput, weightInput);
                if (validDietCalInput()) {
                    newPet.setNewDiet(dietCalInput);
                }
                return newPet;
            } else {
                return null;
            }
        }

    }


    /*
    REQUIRES: index refers to a valid Pet object in PetArrayList
    MODIFIES: this.EditPetTab
    EFFECTS: creates EditPetTab's form.
     */
    private class EditPetTab extends JPanel {
        int index;
        Pet currentPet;
        JButton savePetButton;
        JButton backButton;
        ActionListener editPetListener;
        JPanel buttonPane;
        JPanel deletePetPane;
        private NumberFormat decimalFormat = new DecimalFormat();
        private JPanel editPane;
        private JLabel weightLabel;
        private JFormattedTextField weightField;
        private JTextField nameField;
        private JFormattedTextField dietCalField;
        private JLabel nameLabel;
        private JLabel dietCalLabel;
        private double weightInput;
        private String nameInput;
        private double dietCalInput;

        //constructor
        public EditPetTab(Pet pet, ActionListener editPetListener) {
            this.currentPet = pet;
            this.index = petArrayList.indexOf(pet);
            this.editPetListener = editPetListener;

            this.buttonPane = setEditPetTabButtonPane();
            this.editPane = setEditPetTabEditPane();
            this.deletePetPane = setDeletePetPane();

            //add panels to tab, labels on left, text fields on right
            this.add(editPane, BorderLayout.CENTER);
            this.add(deletePetPane, BorderLayout.EAST);
            this.add(buttonPane, BorderLayout.SOUTH);
        }

        //REQUIRES: actionListener with associated actions for the removePet JButton
        //MODIFIES: this
        //EFFECTS: Creates DeletePet button panel - houses DeletePet button + other info in future
        private JPanel setDeletePetPane() {
            JPanel infoPane = new JPanel();
            JButton deletePetButton = new JButton("Delete this pet");
            deletePetButton.addActionListener(editPetListener);
            infoPane.add(deletePetButton);
            return infoPane;
        }

        /*
        REQUIRES: actionListener with associated actions for all JButtons
        MODIFIES: this
        EFFECTS: creates JPanel containing buttons for the EditPetTab UI
                 Buttons: savePetButton, backButton
         */
        private JPanel setEditPetTabButtonPane() {
            this.savePetButton = new JButton("Okay");
            this.backButton = new JButton("Cancel");
            this.savePetButton.addActionListener(editPetListener);
            this.backButton.addActionListener(editPetListener);
            //buttons panel, save or back
            JPanel buttonPane = new JPanel(new GridLayout(1, 2));
            buttonPane.add(savePetButton);
            buttonPane.add(backButton);
            buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            return buttonPane;
        }

        /*
        MODIFIES: this
        EFFECTS: Construct JPanel input form with Text label and editable text field
                 for each pet trait.
         */
        private JPanel setEditPetTabEditPane() {
            this.weightLabel = new JLabel("Weight (kg)");
            this.nameLabel = new JLabel("Pet Name");
            this.dietCalLabel = new JLabel("Diet Calories (kCal/kg)");

            //editPane fields
            decimalFormat = new DecimalFormat();
            weightField = new JFormattedTextField(decimalFormat);
            weightField.setValue(currentPet.getWeight());
            nameField = new JTextField();
            nameField.setText(currentPet.getPetName());
            dietCalField = new JFormattedTextField(decimalFormat);
            dietCalField.setValue(currentPet.getDietCalPerKg());

            editPane = new JPanel();
            editPane.setLayout(new GridLayout(3, 2));
            editPane.add(nameLabel);
            editPane.add(nameField);
            editPane.add(weightLabel);
            editPane.add(weightField);
            editPane.add(dietCalLabel);
            editPane.add(dietCalField);
            return editPane;
        }

        //GETTER
        public int getIndex() {
            return index;
        }

        //checks if weightField returns a positive double type number
        private boolean validWeightInput() {
            try {
                this.weightInput = ((Number) weightField.getValue()).doubleValue();
                if (weightInput > 0) {
                    return true;
                }
            } catch (NullPointerException e) {
                return false;
            }
            return false;
        }

        //checks if nameField returns a valid name String
        private boolean validNameInput() {
            try {
                nameInput = nameField.getText();
                return true;
            } catch (NullPointerException e) {
                return false;
            }
        }

        //checks if dietCalField returns a positive double type number
        private boolean validDietCalInput() {
            try {
                dietCalInput = ((Number) dietCalField.getValue()).doubleValue();
                if (weightInput > 0) {
                    return true;
                }
            } catch (NullPointerException e) {
                return false;
            }
            return false;
        }

        /*
        MODIFIES: this
        EFFECTS: returns a Pet object if all inputs are valid.
         */
        public Pet getEditedPet() {
            if (validNameInput() && validWeightInput() && validDietCalInput()) {
                Pet editedPet = new Pet(nameInput, weightInput);
                editedPet.setNewDiet(dietCalInput);
//                if (validDietCalInput()) {
//                    newPet.setNewDiet(dietCalInput);
//                }
                return editedPet;
            } else {
                return null;
            }
        }

    }

    //todo later change "close app" button to "close profile"
    //Class for the main user profile interface.
    //SplitPane: in L pane, User may select any pet in the profile from a list of pets.
    //           the R pane will dynamically update with each selection to display individual pet details
    //           default portrait pic is loaded with each Pet display - currently no method to change pet portraits
    public class MainTab extends JPanel {
        PetList petList;
        ArrayList<Pet> petArrayList;
        JSplitPane splitPanePetsPanel;
        //        Boolean isPetListEmpty;
        ActionListener actionListener;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc;
        JButton newPetButton = new JButton("Add New Pet");
        JButton editPetButton = new JButton("Edit a Pet");
        JButton savePetButton = new JButton("Save Session");
        ListModel<String> petListModel;

        /*
        REQUIRES: petArrayList cannot be null
        MODIFIES:
        EFFECTS:
         */
        //instantiates mainTab with a GridBagLayout of 4 rows and 3 columns
        public MainTab(PetList petList, ActionListener actionListener,
                       JSplitPane splitPane1) {
            this.splitPanePetsPanel = splitPane1;
            this.petList = petList;
            this.petArrayList = petList.getPetArray();
//            isPetListEmpty = (petArrayList.size() <= 0);
            this.setLayout(gridBagLayout);
            gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
            addActionListeners();
            this.petListModel = toListModel(petArrayList);
            this.addWithConstraints(mainTabHeader(), 3, 0, 0);
            this.addWithConstraints(splitPanePetsPanel, 3, 0, 1);
            this.addWithConstraints(newPetButton, 1, 0, 2);
            this.addWithConstraints(editPetButton, 1, 1, 2);
            this.addWithConstraints(savePetButton, 1, 2, 2);
            this.addWithConstraints(buttonPane(actionListener), 3, 0, 3);
        }

        /*
        REQUIRES: ActionListener appropriately associated with the buttons
        EFFECTS: creates JPanel with 2 JButtons: "close app", "exit profile"
         */
        private JPanel buttonPane(ActionListener actionListener) {
            JPanel pane = new JPanel(new BorderLayout());
            JButton closeButton = Tabs.closeAppButton(actionListener);
            JButton exitProfileButton = Tabs.exitProfileButton(actionListener);

            pane.add(closeButton, BorderLayout.WEST);
            pane.add(exitProfileButton, BorderLayout.EAST);
            return pane;
        }

        //EFFECTS: returns a JLabel of the main menu's header (the app title)
        private JPanel mainTabHeader() {
            JLabel userLabel;
            String profileName = petList.getOwnerName();
            if (Objects.equals(profileName, "")) {
                userLabel = new JLabel("No Profile Name", SwingConstants.CENTER);
            } else {
                userLabel = new JLabel("Profile Name: " + profileName, SwingConstants.CENTER);
            }
            System.out.println("profile name: " + petList.getOwnerName()); //print
            System.out.println(petList); //print
            JLabel label = new JLabel("Pet Weight Management App Dashboard", SwingConstants.CENTER);
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.add(label, BorderLayout.NORTH);
            headerPanel.add(userLabel, BorderLayout.SOUTH);
            return headerPanel;
        }

        /*
        REQUIRES: GridBagLayout instantiated
        MODIFIES: this
        EFFECTS: adds a Component to mainTab at positions specified by the given GridBagConstraints
         */
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
