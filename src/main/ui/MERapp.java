package ui;

import model.MERcalc;
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
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.exit;
import static java.lang.System.out;

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
//todo later ?unsaved changes
//todo later able to have multiple profiles (can add or remove)
//todo later add option to edit owner name

public class MERapp extends JFrame implements Runnable {
    public static final int FRAME_WIDTH = 400;
    public static final int FRAME_HEIGHT = 600;
    public static final Dimension MIN_FRAME_SIZE = new Dimension(450, 300);
    private static final Dimension PORTRAIT_PIC_SIZE = new Dimension(75, 75);
    private static Dimension RIGHT_PANE_SIZE = new Dimension(300, 300);
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
    private Tab.AddNewPetTab addPetTab;
    private JScrollPane leftPane;
    private JPanel rightPane;
    private Tab.NewProfilePanel createNewProfilePane;
    private JSplitPane mainTabSplitPane;
    private JTabbedPane tabbedPaneUI;
    private Tab.EditPetTab editPetTab;



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
        Tab.IntroMenuPane introPane = new Tab.IntroMenuPane();
        introPane.setActionListener(introPaneUIListener);
        introPaneUI.add(introPane);
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
        Tab.IntroMenuPane introPane = new Tab.IntroMenuPane();
        introPane.setActionListener(introPaneUIListener);
        setJPanel(introPaneUI, introPane);
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
                exit(0);
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
                exit(0);
                break;
            case JOptionPane.NO_OPTION: //exit without saving
                exit(0);
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

    /*
    REQUIRES: these fields instantiated: petList, petArrayList mainTabListener, leftPaneSelectListener, mainTabSplitPane
    MODIFIES: this.mainTab
    EFFECTS: initializes mainTab and returns a JTabbedPane with mainTab as its only tab.
     */
    private JTabbedPane initMainTab() {
        initSplitPane(petArrayList);
        Tab.MainTab mainTab = new Tab.MainTab(petList, mainTabSplitPane);
        mainTab.setActionListener(mainTabListener);
        mainTab.getPane();
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
        mainTabSplitPane.setMinimumSize(MIN_FRAME_SIZE);
        rightPane = new JPanel();
        rightPane.setPreferredSize(RIGHT_PANE_SIZE);
        leftPane = new JScrollPane(petJList);
        mainTabSplitPane.setLeftComponent(leftPane);
        mainTabSplitPane.setRightComponent(rightPane);
        mainTabSplitPane.setDividerLocation(0.4);
        emptyRightPane();
    }

    /*
    REQUIRES:
    MODIFIES: this.petList, this.createNewProfilePane, introPaneUI, frame
    EFFECTS:
     */
    public void createNewProfileTabEvent() {
        out.println("Create new profile"); //print
//        petList = new PetList();
        createNewProfilePane = new Tab.NewProfilePanel();
        createNewProfilePane.setActionListener(introPaneUIListener);
        setJPanel(introPaneUI, createNewProfilePane);
        frame.setContentPane(introPaneUI);
        frame.setVisible(true);
    }

    //
    //==============================================================
    //add pet
    //

    /*
    REQUIRES: petList, addPetTab, currentPet instantiated
    MODIFIES: this
    EFFECTS: when user submits form to create new pet, calls function to process input and checks whether
             pet is successfully added to petList. If successful, close AddPetTab and direct user to mainTab
     */
    private void createNewPetEvent() {
        int count = petList.getPetArray().size();
        submitAddNewPetTab();
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
            out.println("add pet"); //print
            addPetTab = new Tab.AddNewPetTab();
            addPetTab.setActionListener(addPetTabListener());
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
        out.println("close window"); //print
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
        out.println("adding new pet to pet list"); //print
        currentPet = addPetTab.getInputtedPet();
        out.println(currentPet); //print
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
    REQUIRES: petList instantiated
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
            out.println("index = " + index); //print
            try {
                editSelectedPet();
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(frame, "Please select a pet to edit.");
                out.println(e.getMessage()); //print
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
    MODIFIES: this
    EFFECTS: gets a Pet to edit from petArrayList, instantiates a editPetTab pre-filled with the Pet's traits.
     */
    private void editSelectedPet() {
        currentPet = petArrayList.get(index);
        ActionListener editPetListener = editPetTabListener();
        editPetTab = new Tab.EditPetTab(index, currentPet);
        editPetTab.setActionListener(editPetListener);
        tabbedPaneUI.addTab("Edit " + currentPet.getPetName(), editPetTab);
        tabbedPaneUI.setSelectedComponent(editPetTab);
    }

    /*
    REQUIRES: frame, tabbedPaneUI, mainTab and EditPetTab instantiated
    MODIFIES: this
    EFFECTS: closes the EditPetTab and returns user to main dashboard tab.
     */
    private void closeEditTabEvent() {
        out.println("close window"); //print
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
        out.println("save session"); //print
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
            out.println("Unable to write to file: " + JSON_STORE_URL); //print
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
        currentPet = editPetTab.getInputtedPet();
        index = editPetTab.getIndex();
        petList.getPetArray().set(index, currentPet);
        out.println("update pet list"); //print
    }

    /*
    REQUIRES: leftPane and rightPane instantiated
    MODIFIES:this.leftPane, this.rightPane
    EFFECTS: updates petArrayList with current petList and calls functions to update each side of the JSplitPane
     */
    private void updateMainDash() {
        out.println("updating dash"); //print
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
        mainTabSplitPane.setDividerLocation(0.3);
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

        //default portrait pic
        ImageIcon imgIcon = setDefaultPortraitImgIcon();
        JLabel portraitPic = new JLabel(imgIcon, SwingConstants.CENTER);
        displayPane.add(portraitPic);
        displayPane.add(new JLabel("Selected Pet: " + currentPet.getPetName()));
        displayPane.add(new JLabel("Weight (kg): " + currentPet.getWeight()));
        displayPane.add(new JLabel("Current diet calories: " + currentPet.getDietCalPerKg() + " KCal/kg"));
        DecimalFormat df = new DecimalFormat("0.0");
        Double mer = MERcalc.calcMER(currentPet.getWeight());

        String merString = df.format(mer).toString();
        displayPane.add(new JLabel("Daily recommended feeding amount: "));
        displayPane.add(new JLabel(merString + " g"));
        mainTabSplitPane.setRightComponent(displayPane);
        mainTabSplitPane.setDividerLocation(0.3);
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

}
