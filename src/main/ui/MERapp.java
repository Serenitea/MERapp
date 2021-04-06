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
import java.util.Arrays;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;

// Pet Calorie Calculator application
//
//
//
// user interface methods
public class MERapp extends JFrame implements Runnable {
    public static final int FRAMEWIDTH = 450;
    public static final int FRAMEHEIGHT = 600;
    public static final int MINWIDTH = 450;
    public static final int MINHEIGHT = 400;
    private static final String JSON_STORE = "./data/profiles.json";

    private static final String DEFAULTIMGURL = "./data/dog.png";
    private static final Dimension PORTRAITPICSIZE = new Dimension(75, 75);
    private static List<String> MANAGE_PET_MENU_HEADER = Arrays.asList(
            "\n------------------------------------",
            "Manage a Pet - Menu",
            "\n------------------------------------");
    JFrame frame;
    Tabs.IntroMenuPanel introPanel;
    ActionListener mainMenuListener;
    ListSelectionListener listSelectionListener;
    MainMenuTab mainMenuTab;
    JSplitPane managePetSplitPanes;
    JPanel editPetTab;
    int index;
    JTabbedPane tabbedPane;
    ActionListener introListener;
    GridBagLayout gridBagLayout;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private PetList petList;
    private ArrayList<Pet> petArrayList;
    private JList<String> petJList; //for listSelectionListener
    private NumberFormat decimalFormat;
    private JLabel weightLabel;
    private JFormattedTextField weightField;
    private JPanel labelPane;
    private JPanel fieldPane;
    private JPanel editPane;
    private JTextField nameField;
    private JFormattedTextField dietCalField;
    private JLabel nameLabel;
    private JLabel dietCalLabel;
    private Pet currentPet;
    private JScrollPane leftPane;

    private JList<String> toNamesJList(ArrayList<Pet> petArrayList) {
        ListModel<String> petNameListModel = castNameToListModel(petArrayList);
        JList<String> petNameJList = new JList<String>(petNameListModel);
        return petNameJList;
    }

    /*
    EFFECTS: constructs the Pet MER application and initializes json I/O
     */
    //tododoc
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

    //tododoc
    private void initializeFrame() {
        frame = new JFrame("Pet Weight Management App GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initIntroListener();
        introPanel = new Tabs.IntroMenuPanel(introListener);
        frame.getContentPane().add(introPanel);
        frame.setMinimumSize(new Dimension(MINWIDTH, MINHEIGHT));
        frame.setVisible(true);
    }

    //tododoc
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

    //tododoc
    private void initMainListener() {
        mainMenuListener = e -> {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "Add New Pet":
                    addNewPetEvent();
                    break;
                case "Edit a Pet":
                    if (editPetTab == null) {
                        //tabbedPane.indexOfTabComponent(editPetTab) == -1
                        editPetEvent();
                        break;
                    } else {
                        JOptionPane.showMessageDialog(frame, "You can only edit one pet at a time");
                        break;
                    }
                case "Save Session":
                    updatePetList();
                    saveSessionEvent();
                    break;
                default:
                    System.out.println(editPetTab);
                    JOptionPane.showMessageDialog(frame, "Unknown event");
                    break;
            }
        };

        //tododoc
        listSelectionListener = e -> {
            JList<String> curPetJList = (JList) e.getSource();
            index = curPetJList.getSelectedIndex();
            updateRightPane();
        };

    }
    //tododoc
    private void saveSessionEvent() {
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
    //fields - name/string, weight/double, diet/double
    //catch incorrect entries
    //done button
    //save button
    //close edit window
    private void editPetEvent() {
        String name = petArrayList.get(index).getPetName();
        System.out.println("edit pet" + index);
        editPetTab = new JPanel();
        Pet editPet = petArrayList.get(index);


        //editPetTab labels
        weightLabel = new JLabel("Weight (kg)");
        nameLabel = new JLabel("Pet Name");
        dietCalLabel = new JLabel("Diet Calories (kCal/kg)");
        ActionListener editPetListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonName = e.getActionCommand();
                switch (buttonName) {
                    case "Okay":
                        updatePetList();
                        closeWindowEvent();
                        break;
                    case "Cancel":
                        closeWindowEvent();
                        break;
                    default:
                        JOptionPane.showMessageDialog(frame, "Unknown event");
                }
            }
        };


        //editPetTab buttons
        JButton savePetButton = new JButton("Okay");
        JButton backButton = new JButton("Cancel");
        savePetButton.addActionListener(editPetListener);
        backButton.addActionListener(editPetListener);

        //editPetTab fields
        decimalFormat = new DecimalFormat();
        weightField = new JFormattedTextField(decimalFormat);
        weightField.setValue(editPet.getWeight());
        nameField = new JTextField();
        nameField.setText(editPet.getPetName());
        dietCalField = new JFormattedTextField(decimalFormat);
        dietCalField.setValue(editPet.getDietCalPerKg());

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

        //add panels to tab, labels on left, text fields on right
//        editPetTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        editPetTab.setLayout();
        editPetTab.add(editPane, BorderLayout.NORTH);
        editPetTab.add(buttonPane, BorderLayout.SOUTH);

        tabbedPane.addTab("Edit " + name, editPetTab);
        tabbedPane.setSelectedComponent(editPetTab);
    }
    //tododoc
    private void updatePetList() {
        System.out.println("update pet list");
        String newName = nameField.getText();
        double weightInput = ((Number)weightField.getValue()).doubleValue();
        double dietCalKg = ((Number)dietCalField.getValue()).doubleValue();

        currentPet = petArrayList.get(index);
        currentPet.setNewName(newName);
        currentPet.setNewDiet(dietCalKg);
        currentPet.setWeight(weightInput);
        //update PetList, petArrayList, petJList
        //todo later dialog popup if attempted input doesn't match formatter
    }

    //tododoc
    private void closeWindowEvent() {
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
        petJList = toNamesJList(petArrayList);
        petJList.addListSelectionListener(listSelectionListener);
        leftPane = new JScrollPane(petJList);
        managePetSplitPanes.setLeftComponent(leftPane);
    }

    //TODO#3 remove pet
    //TODO#2 add new pet
    //TODO HEREHERE
    //tododoc
    private void addNewPetEvent() {
        System.out.println("add pet");
        JPanel newPetInputPane = new JPanel();
        newPetInputPane.setLayout(new GridLayout(4, 2));
    }


    //tododoc
    //todo later add owner name
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

    //tododoc
    private void loadSavedPetList() {
        try {
            petList = jsonReader.read();
            petArrayList = petList.getPetArray();
            System.out.println("Loaded " + petList.getOwnerName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //tododoc
    private JTabbedPane initializeTabs() {
        initializeSplitPane(toNamesJList(petArrayList));
        this.mainMenuTab = new MainMenuTab(petArrayList,
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
        ImageIcon imgFromURL = new ImageIcon(DEFAULTIMGURL);
        Image img = imgFromURL.getImage();
        Image scaledImg = img.getScaledInstance(PORTRAITPICSIZE.width, PORTRAITPICSIZE.height, Image.SCALE_FAST);
        ImageIcon imgIcon = new ImageIcon(scaledImg);
        return imgIcon;
    }

    //tododoc
    //todo latermaybe use SizeDisplayer to resize img
    private void updateRightPane() {
        JPanel displayPane = new JPanel();
        displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
        Pet selectedPet = petArrayList.get(index);
        String petName = selectedPet.getPetName();
        double weight = selectedPet.getWeight();
        double dietCalPerKg = selectedPet.getDietCalPerKg();
        if (selectedPet.getPortraitPic() == "") {
            ImageIcon imgIcon = generateDefaultPicLabel();
            JLabel portraitPic = new JLabel(imgIcon, SwingConstants.CENTER);
            displayPane.add(portraitPic);
        } //no else bc currently no functionality to add custom pic
        displayPane.add(new JLabel("Selected Pet: " + petName));
        displayPane.add(new JLabel("Weight (kg): " + weight));
        displayPane.add(new JLabel("Current diet calories (KCal/kg): " + dietCalPerKg));
        managePetSplitPanes.setRightComponent(displayPane);
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

    //TODO#4 create new profile
    //todo later able to have multiple profiles (can add or remove)
    public void createNewProfile() {
        System.out.println("create new profile");
    }

    //tododoc
    private void initializeFields() {
        petList = new PetList();
        petArrayList = petList.getPetArray();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    //tododoc
    //todo later change "close app" button to "close profile"
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
        //tododoc
        public MainMenuTab(ArrayList<Pet> petArrayList, ActionListener actionListener,
                           ListSelectionListener listSelectionListener, JSplitPane splitPane1) {
            managePetsPanel = splitPane1;
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
            this.addWithConstraints(Tabs.closeButton(actionListener), 3, 0, 3);
        }
        //EFFECTS: returns a JLabel of the main menu's header (the app title)
        private static JLabel mainMenuHeader() {
            JLabel label = new JLabel("Pet Weight Management App Dashboard", SwingConstants.CENTER);
            return label;
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
