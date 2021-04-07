package ui;

import model.Pet;
import model.PetList;
import org.json.JSONException;
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

import static java.awt.GridBagConstraints.BOTH;

// Pet Calorie Calculator application
//
//
//
// user interface methods
//tododoc
//todo later can't save if both profile name and pet list are empty

//TODO save not working??? when saving from null
//todo later warning if weight is over 80?kg
//TODO fix IndexOutOfBoundException when deleting pet
//tododoc
//todo later add exception detail of trying to edit a pet that's already being edited
//todo later formatting polish
//todo later move edit pet button to pet display
//todo if blank profile name should say "no profile name set"
//TODO if clicking edit a pet when there's no pets -> dialog "no pets"
//TODO found bug when there's no saved profile and then clicking "load saved profile"
public class MERapp extends JFrame implements Runnable {
    public static final int FRAME_WIDTH = 450;
    public static final int FRAME_HEIGHT = 400;
    public static final int MIN_WIDTH = 450;
    public static final int MIN_HEIGHT = 400;
    private static final Dimension PORTRAIT_PIC_SIZE = new Dimension(75, 75);

    //    private static final String JSON_STORE_URL = "./data/profiles.json";
    private static final String JSON_STORE_URL = "./data/test.json";
    private static final String DEFAULT_PET_PORTRAIT_URL = "./data/dog.png";
    private static final String FRAME_ICON_URL = "./data/paw.png";

    private JList<String> petJList; //for listSelectionListener
    private PetList petList;
    private ArrayList<Pet> petArrayList;
    private Pet currentPet;
    int index;

    private JFrame frame;
    private JPanel introPaneUI = new JPanel(); //used for 2 displays
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private ActionListener introPaneUIListener;
    private ActionListener mainTabListener;
    private ListSelectionListener leftPaneSelectListener;

    private GridBagLayout gridBagLayout;
    private JPanel addPetTab;
    private JScrollPane leftPane;
    private JPanel createNewPetInputPane;
    private JButton deletePetButton;
    private Tabs.NewProfilePanel createNewProfilePane;

    private MainMenuTab mainTab;
    private JSplitPane mainTabSplitPane;
    private JTabbedPane tabbedPaneUI;

    private JPanel editPetTab;
    private NumberFormat decimalFormat = new DecimalFormat();
    private JPanel editPane;
    private JLabel weightLabel;
    private JFormattedTextField weightField;
    private JTextField nameField;
    private JFormattedTextField dietCalField;
    private JLabel nameLabel;
    private JLabel dietCalLabel;




    /*
    EFFECTS: constructs the Pet MER application and initializes json I/O
     */
    //tododoc
    public MERapp() {
        super("Pet Weight Management App");

        run();
//        EventQueue.invokeLater(this::run);
    }

    //tododoc
    //EFFECTS: converts a Pet ArrayList to a ListModel of pet names
    private static ListModel<String> castNameToListModel(ArrayList<Pet> petArrayList) {
        DefaultListModel<String> petListModel = new DefaultListModel<>();
        for (Pet pet : petArrayList) {
            petListModel.addElement(pet.getPetName());
        }
        return petListModel;
    }

    private JList<String> toNamesJList(ArrayList<Pet> petArrayList) {
        ListModel<String> petNameListModel = castNameToListModel(petArrayList);
        JList<String> petNameJList = new JList<String>(petNameListModel);
        return petNameJList;
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
    }

    //==============================================================
    //initialize app
    //tododoc
    private void initializeFields() {
        petList = new PetList();
        petArrayList = new ArrayList<>();
        jsonReader = new JsonReader(JSON_STORE_URL);
        jsonWriter = new JsonWriter(JSON_STORE_URL);
    }

    //tododoc
    private void initializeFrame() {
        frame = new JFrame("Pet Weight Management App GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon frameIcon = new ImageIcon(FRAME_ICON_URL);
        frame.setIconImage(frameIcon.getImage());
        initIntroListener();
        introPaneUI.add(new Tabs.IntroMenuPanel(introPaneUIListener));
        frame.getContentPane().add(introPaneUI);
        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        frame.setVisible(true);
    }

    //==============================================================
    //Listeners
    //TODO prompt to save current Pet to json file when exiting profile
    // or close app from dash
    //TODO  Warns user if they attempt to create a pet with a duplicate name (not case-sensitive)
    //tododoc
    private void initIntroListener() {
        introPaneUIListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "New Profile": //can also use if (source == objName) for cases
                    setCreateNewProfileUI(); //HERE
                    break;
                case "Load saved profile":
                    loadSavedProfile();
                    break;
                case "Submit":
                    System.out.println("submitting new profile info");
                    loadNewProfile();
                    break;
                case "Cancel":
                    setJPanel(introPaneUI, new Tabs.IntroMenuPanel(introPaneUIListener));
                    frame.setContentPane(introPaneUI);
                    frame.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };
    }

    private void initMainListener() {
        mainTabListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "Add New Pet":
                    addNewPetEvent();
                    break;
                case "Edit a Pet": //TODO empty petlist case
                    editPetEvent();
                case "Save Session": //todo later - modify empty petlist case - should still save?
                    saveSessionEvent();
                    break;
                case "Exit Profile": //todo later implement more secure methods
                    exitProfileEvent();
                    break;
                default:
                    System.out.println(editPetTab);
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };
    }

    private void initlistSelectionListener() {
        //tododoc
        leftPaneSelectListener = e -> {
            JList<String> curPetJList = (JList) e.getSource();
            index = curPetJList.getSelectedIndex();
            updateRightPane();
        };
    }

    private ActionListener editPetTabListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = e.getActionCommand();
                switch (buttonName) {
                    case "Okay":
                        updatePetList();
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

    //==============================================================
//beyond the initial intro panel - main menu
    //going to main menu
    private void launchMainMenu() {
        initMainListener();
        initlistSelectionListener();
        tabbedPaneUI = initializeTabs();
//        frame.getContentPane().add(tabbedPane);
        frame.setContentPane(tabbedPaneUI);
        gridBagLayout = new GridBagLayout();

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
    }

    private void loadNewProfile() {
        String profileName = createNewProfilePane.getProfileNameInput();
        System.out.println(petList);
        petList.setOwnerName(profileName);
        launchMainMenu();
    }

    //fields - name/string, weight/double, diet/double
    //catch incorrect entries
    //updates the left panel of the splitPane with current data
    /*REQUIRES: no currently loaded profile (empty petList)
    MODIFIES: this
    EFFECTS: loads previously saved profiles from local json storage path*/
    //todo later add option to edit owner name
    public void loadSavedProfile() {
        try {
            petList = jsonReader.read();
            petArrayList = petList.getPetArray();
            System.out.println("Loaded " + petList.getOwnerName() + " from " + JSON_STORE_URL);
            launchMainMenu();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_URL);
        } catch (JSONException e) {
            System.out.println("No saved profile found at " + JSON_STORE_URL);
            JOptionPane.showMessageDialog(frame, "No saved profile found at" + JSON_STORE_URL);
        }
    }

    //tododoc
    private JTabbedPane initializeTabs() {
        initSplitPane(toNamesJList(petArrayList));
        this.mainTab = new MainMenuTab(petList,
                mainTabListener,
                leftPaneSelectListener, mainTabSplitPane);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Main Dashboard", mainTab);
        return tabs;
    }

    //tododoc
    private void initSplitPane(JList<String> toNamesJList) {
        petJList = toNamesJList;
        petJList.addListSelectionListener(leftPaneSelectListener);
        mainTabSplitPane = new JSplitPane();
        JPanel displayPane = new JPanel();
        leftPane = new JScrollPane(petJList);
        mainTabSplitPane.setLeftComponent(leftPane);
        if (petArrayList.size() > 0) {
            displayPane.add(new JLabel("No pet selected"));
            mainTabSplitPane.setRightComponent(displayPane);
        } else {
            displayPane.add(new JLabel("No pets in profile"));
            mainTabSplitPane.setRightComponent(displayPane);
        }
    }


    //tododoc
    //todo later change "close app" button to "close profile"
    //4 rows 3 columns
    public static class MainMenuTab extends JPanel {
        private static PetList petList;
        ArrayList<Pet> petArrayList;
        JSplitPane managePetsPanel;
        Boolean emptyPetList;
        ActionListener actionListener;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc;
        JButton newPetButton = new JButton("Add New Pet");
        JButton editPetButton = new JButton("Edit a Pet");
        JButton savePetButton = new JButton("Save Session");
        ListModel<String> petListModel;

        //tododoc
        public MainMenuTab(PetList petList, ActionListener actionListener,
                           ListSelectionListener listSelectionListener, JSplitPane splitPane1) {
            managePetsPanel = splitPane1;
            this.petList = petList;
            petArrayList = petList.getPetArray();
            emptyPetList = (petArrayList.size() <= 0);
            this.setLayout(gridBagLayout);
            gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
            addActionListeners();
            this.petListModel = castNameToListModel(petArrayList);
            this.addWithConstraints(mainMenuHeader(), 3, 0, 0);
            this.addWithConstraints(managePetsPanel, 3, 0, 1);
            this.addWithConstraints(newPetButton, 1, 0, 2);
            this.addWithConstraints(editPetButton, 1, 1, 2);
            this.addWithConstraints(savePetButton, 1, 2, 2);
            this.addWithConstraints(buttonPane(actionListener), 3, 0, 3);
        }

        private static JPanel buttonPane(ActionListener actionListener) {
            JPanel pane = new JPanel(new BorderLayout());
            JButton closeButton = Tabs.closeButton(actionListener);
            JButton exitProfileButton = Tabs.exitProfileButton(actionListener);

            pane.add(closeButton, BorderLayout.WEST);
            pane.add(exitProfileButton, BorderLayout.EAST);
            return pane;
        }

        //EFFECTS: returns a JLabel of the main menu's header (the app title)
        private static JPanel mainMenuHeader() {
            JLabel userLabel;
            String profileName = petList.getOwnerName();
            if (profileName == "") {
                userLabel = new JLabel("No Profile Name", SwingConstants.CENTER);
            } else {
                userLabel = new JLabel("Profile Name: " + profileName, SwingConstants.CENTER);
            }
            System.out.println("profile name: " + petList.getOwnerName());
            System.out.println(petList);
            JLabel label = new JLabel("Pet Weight Management App Dashboard", SwingConstants.CENTER);
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.add(label, BorderLayout.NORTH);
            headerPanel.add(userLabel, BorderLayout.SOUTH);
            return headerPanel;
        }

        //tododoc
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

    //
    //==============================================================
    //add profile
    //
    //todo later able to have multiple profiles (can add or remove)
    public void setCreateNewProfileUI() { //HERE#2
        System.out.println("Create new profile");
        petList = new PetList();
        createNewProfilePane = new Tabs.NewProfilePanel(introPaneUIListener);
        setJPanel(introPaneUI, createNewProfilePane);
        frame.setContentPane(introPaneUI);
        frame.setVisible(true);

    }

    private ActionListener addPetTabListener() {
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

    private void createNewPetEvent() {
        int count = petList.getPetArray().size();
        addNewToPetList(); //todo later warning for creating a pet with a duplicate name
        if (petList.getPetArray().size() == count + 1) {
            System.out.println("sucessfully created pet");
            closeAddPetTabEvent(); //includes updating main dash
        } else {
            System.out.println("creating a pet was unsuccessful");
        }
    }

    private void closeAddPetTabEvent() {
        System.out.println("close window");
        tabbedPaneUI.remove(addPetTab);
        addPetTab = null;
        updateMainDash();
        frame.setVisible(true);
    }

    //
    //==============================================================
    //add pet
    //
    private void addNewPetEvent() {
        System.out.println("add pet");
        addPetTab = new JPanel();
        ActionListener addPetListener = addPetTabListener();

        setCreateNewPetInputPane();

        JButton createPetButton = new JButton("Submit");
        JButton backButton = new JButton("Cancel");
        createPetButton.addActionListener(addPetListener);
        backButton.addActionListener(addPetListener);
        JPanel buttonPane = new JPanel(new GridLayout(1, 2));
        buttonPane.add(createPetButton);
        buttonPane.add(backButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addPetTab.add(new JLabel("Please enter your pet's information below", SwingConstants.CENTER));
        addPetTab.add(createNewPetInputPane, BorderLayout.CENTER);
        addPetTab.add(buttonPane, BorderLayout.SOUTH);

        tabbedPaneUI.addTab("Create New Pet", addPetTab);
        tabbedPaneUI.setSelectedComponent(addPetTab);
    }

    private void setCreateNewPetInputPane() {
        weightLabel = new JLabel("Weight (kg) * (required)");
        weightField = new JFormattedTextField(decimalFormat);
        nameLabel = new JLabel("Pet Name * (required)");
        nameField = new JTextField();
        dietCalLabel = new JLabel("Diet Calories (kCal/kg)");
        dietCalField = new JFormattedTextField(decimalFormat);

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

    private void addNewToPetList() {
        System.out.println("adding new pet to pet list");
        //get name and weight for instantiating new pet
        try {
            String nameInput = nameField.getText();
            double weightInput = ((Number) weightField.getValue()).doubleValue();
            Pet newPet = new Pet(nameInput, weightInput);
            currentPet = newPet;

            try {
                double dietCalInput = ((Number) dietCalField.getValue()).doubleValue();
                currentPet.setNewDiet(dietCalInput);
            } catch (NullPointerException e) {
                System.out.println("no diet was inputted");
            } finally {
                petList.add(currentPet);
                System.out.println("added new Pet");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(frame, "Entries don't meet requirements.");
        }
    }

    //
    //==============================================================
    //edit pet
    //
    private void editPetEvent() {
        try {
            if (editPetTab == null) {
                //other option: tabbedPane.indexOfTabComponent(editPetTab) == -1
                editSelectedPet();
            } else {
                JOptionPane.showMessageDialog(frame, "You can only edit one pet at a time");
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "No pets in profile currently. "
                    + "Select \"Add New Pet\" to add a pet to this profile.");
        }

    }

    /*
    REQUIRES:
    MODIFIES:
    EFFECTS:
     */
    private void editSelectedPet() {
        String name = petArrayList.get(index).getPetName();
        System.out.println("edit pet" + index);
        editPetTab = new JPanel();
        currentPet = petArrayList.get(index);

        setEditPetTabEditPane();
        ActionListener editPetListener = editPetTabListener();
        //editPetTab buttons
        JButton savePetButton = new JButton("Okay");
        JButton backButton = new JButton("Cancel");
        savePetButton.addActionListener(editPetListener);
        backButton.addActionListener(editPetListener);
        //todomaybe, accessibility tool info
//        weightLabel.setLabelFor(weightField);

        //buttons panel, save or back
        JPanel buttonPane = new JPanel(new GridLayout(1, 2));
        buttonPane.add(savePetButton);
        buttonPane.add(backButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //info panel - houses DeletePet button + other info in future
        JPanel infoPane = new JPanel();
        deletePetButton = new JButton("Delete this pet");
        deletePetButton.addActionListener(editPetListener);
        infoPane.add(deletePetButton);

        //add panels to tab, labels on left, text fields on right
//        editPetTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        editPetTab.setLayout();
        editPetTab.add(editPane, BorderLayout.CENTER);
        editPetTab.add(infoPane, BorderLayout.EAST);
        editPetTab.add(buttonPane, BorderLayout.SOUTH);
        tabbedPaneUI.addTab("Edit " + name, editPetTab);
        tabbedPaneUI.setSelectedComponent(editPetTab);
    }

    //editPane labels
    private void setEditPetTabEditPane() {
        weightLabel = new JLabel("Weight (kg)");
        nameLabel = new JLabel("Pet Name");
        dietCalLabel = new JLabel("Diet Calories (kCal/kg)");

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
    }



    private void closeEditTabEvent() {
        System.out.println("close window");
        tabbedPaneUI.remove(editPetTab);
        editPetTab = null;
        System.out.println(petList.getPetArray().get(index).getPetName());
        updateMainDash();
        frame.setVisible(true);
    }

    //
    //
    //==============================================================

    //save session

    //

    private void saveSessionEvent() {
        try {
            updatePetList();
            saveSession();
        } catch (NullPointerException e) {
            System.out.println("no pets in profiles");
        }
    }

    private void saveSession() {
        System.out.println("save session");
        //todo later ask for Owner name when saving
        try {
            jsonWriter.open();
            jsonWriter.write(petList);
            jsonWriter.close();
            System.out.println("Saved " + petList.getOwnerName() + "'s profile to " + JSON_STORE_URL);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_URL);
        }
        System.out.println(petList.getPetArray().get(index).getPetName());
    }

    //
    //==============================================================
    //remove pet / exit profile

    private void deletePetFromPetList() {
        currentPet = petList.getPetArray().get(index);
        petList.remove(currentPet);
    }

    private void exitProfileEvent() {
        frame.setVisible(false);
        frame = null;
        introPaneUI = new JPanel();
//        introPanel.add(new Tabs.IntroMenuPanel(introListener));
//        frame.setContentPane(introPanel);
        System.out.println("exited profile");
        initializeFields();
        initializeFrame();
        System.out.println(tabbedPaneUI);
    }

    private void updatePetList() {
        System.out.println("update pet list");
        String newName = nameField.getText();
        double weightInput = ((Number) weightField.getValue()).doubleValue();
        double dietCalKg = ((Number) dietCalField.getValue()).doubleValue();

        currentPet = petArrayList.get(index);
        currentPet.setNewName(newName);
        currentPet.setNewDiet(dietCalKg);
        currentPet.setWeight(weightInput);
        //update PetList, petArrayList, petJList
    }

    //todo later if no diet calories entered, display that instead of 0


    private void updateMainDash() {
        System.out.println("updating dash");
        updateLeftPane();
        updateRightPane();
    }

    private void updateLeftPane() {
        petArrayList = petList.getPetArray();
        petJList = toNamesJList(petArrayList);
        petJList.addListSelectionListener(leftPaneSelectListener);
        leftPane = new JScrollPane(petJList);
        mainTabSplitPane.setLeftComponent(leftPane);
    }


    //default pic
    //tododoc
    private ImageIcon setDefaultPortraitImgIcon() {
        ImageIcon imgFromURL = new ImageIcon(DEFAULT_PET_PORTRAIT_URL);
        Image img = imgFromURL.getImage();
        Image scaledImg = img.getScaledInstance(PORTRAIT_PIC_SIZE.width, PORTRAIT_PIC_SIZE.height, Image.SCALE_FAST);
        ImageIcon imgIcon = new ImageIcon(scaledImg);
        return imgIcon;
    }

    //tododoc
    //todomaybe use SizeDisplayer to resize img
    private void updateRightPane() {
        JPanel displayPane = new JPanel();
        displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
        try {
            currentPet = petArrayList.get(index);
            if (currentPet.getPortraitPic() == "") {
                ImageIcon imgIcon = setDefaultPortraitImgIcon();
                JLabel portraitPic = new JLabel(imgIcon, SwingConstants.CENTER);
                displayPane.add(portraitPic);
            } //no else bc currently no functionality to add custom pic
            displayPane.add(new JLabel("Selected Pet: " + currentPet.getPetName()));
            displayPane.add(new JLabel("Weight (kg): " + currentPet.getWeight()));
            displayPane.add(new JLabel("Current diet calories (KCal/kg): " + currentPet.getDietCalPerKg()));
            mainTabSplitPane.setRightComponent(displayPane);
        } catch (IndexOutOfBoundsException e) {
            //change the right pane to default
            //todo later abstract
            System.out.println(e.getMessage());
            System.out.println(index);
            displayPane.add(new JLabel("No pet selected"));
            mainTabSplitPane.setRightComponent(displayPane);
        }
    }



    public void setJPanel(JPanel panel, Component component) {
        panel.removeAll();
        panel.add(component);
    }



}
