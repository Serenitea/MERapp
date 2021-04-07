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
//TODO found bug when there's no saved profile and then clicking "load saved profile"
public class MERapp extends JFrame implements Runnable {
    public static final int FRAME_WIDTH = 450;
    public static final int FRAME_HEIGHT = 400;
    public static final int MIN_WIDTH = 450;
    public static final int MIN_HEIGHT = 400;
    //    private static final String JSON_STORE = "./data/profiles.json";
    private static final String JSON_STORE = "./data/test.json";
    private static final String DEFAULT_PET_PORTRAIT_URL = "./data/dog.png";
    private static final String FRAME_ICON_URL = "./data/paw.png";
    private static final Dimension PORTRAIT_PIC_SIZE = new Dimension(75, 75);

    JFrame frame;
    JPanel introPanel = new JPanel();
    ActionListener mainMenuListener;
    ListSelectionListener listSelectionListener;
    MainMenuTab mainMenuTab;
    JSplitPane managePetSplitPanes;
    JPanel editPetTab;
    int index;
    JTabbedPane tabbedPane;
    ActionListener introListener;
    GridBagLayout gridBagLayout;
    private JPanel addPetTab;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private PetList petList;
    private ArrayList<Pet> petArrayList;
    private JList<String> petJList; //for listSelectionListener
    private NumberFormat decimalFormat = new DecimalFormat();
    private JLabel weightLabel;
    private JFormattedTextField weightField;
    private JPanel editPane;
    private JTextField nameField;
    private JFormattedTextField dietCalField;
    private JLabel nameLabel;
    private JLabel dietCalLabel;
    private Pet currentPet;
    private JScrollPane leftPane;
    private JPanel newPetInputPane;
    private JButton deletePet;
    private Tabs.NewProfilePanel newProfilePane;


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
//        runApp(); //will be deleted later
    }

    //tododoc
    private void initializeFields() {
        petList = new PetList();
        petArrayList = new ArrayList<>();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    //tododoc
    private void initializeFrame() {
        frame = new JFrame("Pet Weight Management App GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon frameIcon = new ImageIcon(FRAME_ICON_URL);
        frame.setIconImage(frameIcon.getImage());
        initIntroListener();
        introPanel.add(new Tabs.IntroMenuPanel(introListener));
        frame.getContentPane().add(introPanel);
        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        frame.setVisible(true);
    }

    //TODO prompt to save current Pet to json file when exiting profile
    // or close app from dash
    //TODO  Warns user if they attempt to create a pet with a duplicate name (not case-sensitive)
    //tododoc
    private void initIntroListener() {
        introListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "New Profile": //can also use if (source == objName)
                    createNewProfile();
                    break;
                case "Load saved profile":
                    loadSavedProfile();
                    break;
                case "Submit":
                    System.out.println("submitting new profile info");
                    loadNewProfile();
                    break;
                case "Cancel":
                    setJPanel(introPanel, new Tabs.IntroMenuPanel(introListener));
                    frame.setContentPane(introPanel);
                    frame.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };
    }

    private void loadNewProfile() {
        String profileName = newProfilePane.getProfileNameInput();
        System.out.println(petList);
        petList.setOwnerName(profileName);
        launchMainMenu();
    }

    private void launchMainMenu() {
        initMainListener();
        initlistSelectionListener();
        tabbedPane = initializeTabs();
//        frame.getContentPane().add(tabbedPane);
        frame.setContentPane(tabbedPane);
        gridBagLayout = new GridBagLayout();

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
    }

    //tododoc
    private void initMainListener() {
        mainMenuListener = e -> {
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

    private void saveSessionEvent() {
        try {
            updatePetList();
            saveSession();
        } catch (NullPointerException e) {
            System.out.println("no pets in profiles");
        }
    }

    //todo later warning if weight is over 80?kg
    //TODO fix IndexOutOfBoundException when deleting pet
    //tododoc
    //todo later add exception detail of trying to edit a pet that's already being edited
    private void editPetEvent() { //TODO was here
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
    //todo later can't save if both profile name and pet list are empty
    //TODO save not working??? when saving from null
    private void exitProfileEvent() {
        frame.setVisible(false);
        frame = null;
        introPanel = new JPanel();
//        introPanel.add(new Tabs.IntroMenuPanel(introListener));
//        frame.setContentPane(introPanel);
        System.out.println("exited profile");
        initializeFields();
        initializeFrame();
        System.out.println(tabbedPane);
    }

    private void initlistSelectionListener() {
        //tododoc
        listSelectionListener = e -> {
            JList<String> curPetJList = (JList) e.getSource();
            index = curPetJList.getSelectedIndex();
            updateRightPane();
        };
    }


    //tododoc
    private void saveSession() {
        System.out.println("save session");
        //todo later ask for Owner name when saving
        try {
            jsonWriter.open();
            jsonWriter.write(petList);
            jsonWriter.close();
            System.out.println("Saved " + petList.getOwnerName() + "'s profile to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
        System.out.println(petList.getPetArray().get(index).getPetName());
    }

    //todo later formatting polish
    //todo later move edit pet button to pet display
    //todo if blank profile name should say "no profile name set"
    //TODO if clicking edit a pet when there's no pets -> dialog "no pets"
    //fields - name/string, weight/double, diet/double
    //catch incorrect entries
    //done button
    //save button
    //close edit window    //TODO SHORTEN
    private void editSelectedPet() {
        try {
            String name = petArrayList.get(index).getPetName();
            System.out.println("edit pet" + index);
            editPetTab = new JPanel();
            currentPet = petArrayList.get(index);

            //editPetTab labels
            weightLabel = new JLabel("Weight (kg)");
            nameLabel = new JLabel("Pet Name");
            dietCalLabel = new JLabel("Diet Calories (kCal/kg)");
            ActionListener editPetListener = editPetListener();


            //editPetTab buttons
            JButton savePetButton = new JButton("Okay");
            JButton backButton = new JButton("Cancel");
            savePetButton.addActionListener(editPetListener);
            backButton.addActionListener(editPetListener);

            //editPetTab fields
            decimalFormat = new DecimalFormat();
            weightField = new JFormattedTextField(decimalFormat);
            weightField.setValue(currentPet.getWeight());
            nameField = new JTextField();
            nameField.setText(currentPet.getPetName());
            dietCalField = new JFormattedTextField(decimalFormat);
            dietCalField.setValue(currentPet.getDietCalPerKg());

            //todo maybe, accessibility tool info
//        weightLabel.setLabelFor(weightField);

            editPane = new JPanel();
            editPane.setLayout(new GridLayout(3, 2));
            editPane.add(nameLabel);
            editPane.add(nameField);
            editPane.add(weightLabel);
            editPane.add(weightField);
            editPane.add(dietCalLabel);
            editPane.add(dietCalField);

            //buttons panel, save or back
            JPanel buttonPane = new JPanel(new GridLayout(1, 2));
            buttonPane.add(savePetButton);
            buttonPane.add(backButton);
            buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel infoPane = new JPanel();
            deletePet = new JButton("Delete this pet");
            deletePet.addActionListener(editPetListener);
            infoPane.add(deletePet);

            //add panels to tab, labels on left, text fields on right
//        editPetTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        editPetTab.setLayout();
            editPetTab.add(editPane, BorderLayout.CENTER);
            editPetTab.add(infoPane, BorderLayout.EAST);
            editPetTab.add(buttonPane, BorderLayout.SOUTH);
            tabbedPane.addTab("Edit " + name, editPetTab);
            tabbedPane.setSelectedComponent(editPetTab);
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(frame, "No pet selected."); //TODO change this so it's not covering  empty case
        }

    }

    //todo later if no diet calories entered, display that instead of 0


    private ActionListener editPetListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = e.getActionCommand();
                switch (buttonName) {
                    case "Okay":
                        updatePetList();
                        closeEditWindowEvent();
                        break;
                    case "Cancel":
                        closeEditWindowEvent();
                        break;
                    case "Delete this pet":
                        removePetFromPetList();
                        updateMainDash(); //todo check this works
                        closeEditWindowEvent();
                        break;
                    default:
                        JOptionPane.showMessageDialog(frame, "Unknown event");
                }
            }
        };
    }

    //tododoc
    private void removePetFromPetList() {
        currentPet = petList.getPetArray().get(index);
        petList.remove(currentPet);
    }

    //tododoc
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
        //todo later dialog popup if attempted input doesn't match formatter
    }

    //tododoc
    private void closeEditWindowEvent() {
        System.out.println("close window");
        tabbedPane.remove(editPetTab);
        editPetTab = null;
        System.out.println(petList.getPetArray().get(index).getPetName());
        updateMainDash();
        frame.setVisible(true);
    }

    //basically just left I think?
    //tododoc
    private void updateMainDash() {
        System.out.println("updating dash");
        updateLeftPane();
        updateRightPane();
    }

    //tododoc
    //updates the left panel of the splitPane with current data
    private void updateLeftPane() {
        petArrayList = petList.getPetArray();
        petJList = toNamesJList(petArrayList);
        petJList.addListSelectionListener(listSelectionListener);
        leftPane = new JScrollPane(petJList);
        managePetSplitPanes.setLeftComponent(leftPane);
    }
    //TODO SHORTEN

    //todo make AddPetTab more robust: allows user to try submitting again
    //tododoc
    private void addNewPetEvent() {
        System.out.println("add pet");
        addPetTab = new JPanel();


        ActionListener addPetListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = e.getActionCommand();
                switch (buttonName) {
                    case "Create New Pet":
                        int count = petList.getPetArray().size();
                        addNewToPetList(); //todo later warning for creating a pet with a duplicate name
                        if (petList.getPetArray().size() == count + 1) {
                            System.out.println("sucessfully created pet");
                            closeAddPetWindowEvent(); //includes updating main dash
                        } else {
                            System.out.println("creating a pet was unsuccessful");
                        }
                        break;
                    case "Cancel":
                        closeAddPetWindowEvent();
                        break;
                    default:
                        JOptionPane.showMessageDialog(frame, "Unknown event");
                }
            }

            private void closeAddPetWindowEvent() {
                System.out.println("close window");
                tabbedPane.remove(addPetTab);
                addPetTab = null;
                updateMainDash();
                frame.setVisible(true);
            }
        };

        weightLabel = new JLabel("Weight (kg) * (required)");
        weightField = new JFormattedTextField(decimalFormat);
        nameLabel = new JLabel("Pet Name * (required)");
        nameField = new JTextField();
        dietCalLabel = new JLabel("Diet Calories (kCal/kg)");
        dietCalField = new JFormattedTextField(decimalFormat);

        //todo forbid >1 tab of AddPetTab
        //todo check that name and weight are both filled - provide more details
        newPetInputPane = new JPanel();
        newPetInputPane.setLayout(new GridLayout(3, 2));
        newPetInputPane.add(nameLabel);
        newPetInputPane.add(nameField);
        newPetInputPane.add(weightLabel);
        newPetInputPane.add(weightField);
        newPetInputPane.add(dietCalLabel);
        newPetInputPane.add(dietCalField);

        JButton addPetButton = new JButton("Create New Pet");
        JButton backButton = new JButton("Cancel");
        addPetButton.addActionListener(addPetListener);
        backButton.addActionListener(addPetListener);
        JPanel buttonPane = new JPanel(new GridLayout(1, 2));
        buttonPane.add(addPetButton);
        buttonPane.add(backButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addPetTab.add(new JLabel("Please enter your pet's information below", SwingConstants.CENTER));
        addPetTab.add(newPetInputPane, BorderLayout.CENTER);
        addPetTab.add(buttonPane, BorderLayout.SOUTH);

        tabbedPane.addTab("Create New Pet", addPetTab);
        tabbedPane.setSelectedComponent(addPetTab);


//        newPetInputPane.setLayout(new GridLayout(4, 2));

    }

    //tododoc
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

    /*REQUIRES: no currently loaded profile (empty petList)
    MODIFIES: this
    EFFECTS: loads previously saved profiles from local json storage path*/
    //todo later add option to edit owner name
    //TODO I was here
    public void loadSavedProfile() {
        try {
            petList = jsonReader.read();
            petArrayList = petList.getPetArray();
            System.out.println("Loaded " + petList.getOwnerName() + " from " + JSON_STORE);
            launchMainMenu();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (JSONException e) {
            System.out.println("No saved profile found at " + JSON_STORE);
            JOptionPane.showMessageDialog(frame, "No saved profile found at" + JSON_STORE);
        }

    }

    //tododoc
    private JTabbedPane initializeTabs() {
        initializeSplitPane(toNamesJList(petArrayList));
        this.mainMenuTab = new MainMenuTab(petList,
                mainMenuListener,
                listSelectionListener, managePetSplitPanes);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Main Dashboard", mainMenuTab);
        return tabs;
    }

    //tododoc
    private void initializeSplitPane(JList<String> toNamesJList) {
        petJList = toNamesJList;
        petJList.addListSelectionListener(listSelectionListener);
        managePetSplitPanes = new JSplitPane();
        JPanel displayPane = new JPanel();
        leftPane = new JScrollPane(petJList);
        managePetSplitPanes.setLeftComponent(leftPane);
        if (petArrayList.size() > 0) {
            displayPane.add(new JLabel("No pet selected"));
            managePetSplitPanes.setRightComponent(displayPane);
        } else {
            displayPane.add(new JLabel("No pets in profile"));
            managePetSplitPanes.setRightComponent(displayPane);
        }
    }

    //default pic
    //tododoc
    private ImageIcon generateDefaultPicLabel() {
        ImageIcon imgFromURL = new ImageIcon(DEFAULT_PET_PORTRAIT_URL);
        Image img = imgFromURL.getImage();
        Image scaledImg = img.getScaledInstance(PORTRAIT_PIC_SIZE.width, PORTRAIT_PIC_SIZE.height, Image.SCALE_FAST);
        ImageIcon imgIcon = new ImageIcon(scaledImg);
        return imgIcon;
    }

    //tododoc
    //todo latermaybe use SizeDisplayer to resize img
    private void updateRightPane() {
        JPanel displayPane = new JPanel();
        displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
        try {
            currentPet = petArrayList.get(index);
            if (currentPet.getPortraitPic() == "") {
                ImageIcon imgIcon = generateDefaultPicLabel();
                JLabel portraitPic = new JLabel(imgIcon, SwingConstants.CENTER);
                displayPane.add(portraitPic);
            } //no else bc currently no functionality to add custom pic
            displayPane.add(new JLabel("Selected Pet: " + currentPet.getPetName()));
            displayPane.add(new JLabel("Weight (kg): " + currentPet.getWeight()));
            displayPane.add(new JLabel("Current diet calories (KCal/kg): " + currentPet.getDietCalPerKg()));
            managePetSplitPanes.setRightComponent(displayPane);
        } catch (IndexOutOfBoundsException e) {
            //change the right pane to default
            //todo later abstract
            System.out.println(e.getMessage());
            System.out.println(index);
            displayPane.add(new JLabel("No pet selected"));
            managePetSplitPanes.setRightComponent(displayPane);
        }
    }

    //todo later able to have multiple profiles (can add or remove)
    public void createNewProfile() {
        System.out.println("Create new profile");
        petList = new PetList();
        newProfilePane = new Tabs.NewProfilePanel(introListener);
        setJPanel(introPanel, newProfilePane);
        frame.setContentPane(introPanel);
        frame.setVisible(true);

    }

    public void setJPanel(JPanel panel, Component component) {
        panel.removeAll();
        panel.add(component);
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

}
