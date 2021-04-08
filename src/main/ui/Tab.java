package ui;

import model.Pet;
import model.PetList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

import static java.awt.GridBagConstraints.BOTH;
import static java.lang.System.out;

public class Tab {
    /*
        REQUIRES: index refers to a valid Pet object in PetArrayList
        MODIFIES: this.EditPetTab
        EFFECTS: creates EditPetTab's form.
         */
    static class EditPetTab extends JPanel {
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
        public EditPetTab(int index, Pet pet, ActionListener editPetListener) {
            this.currentPet = pet;
            this.index = index;
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
    public static class MainTab extends JPanel {
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
            this.petListModel = MERapp.toListModel(petArrayList);
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
            JButton closeButton = Content.closeAppButton(actionListener);
            JButton exitProfileButton = Content.exitProfileButton(actionListener);

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
            out.println("profile name: " + petList.getOwnerName()); //print
            out.println(petList); //print
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
        public Pet getNewPet() {
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

    //REQUIRES: actionListener input must have appropriate action commands for the JButtons
    //          newProfileButton and cancelButton
    //EFFECTS: creates the UI that allows users to fill and submit a form to create a new profile.
    public static class IntroMenuPane extends JPanel {
        ActionListener actionListener;
        JButton startCreateNewProfileButton = new JButton("New Profile");
        JButton loadSavedProfileButton = new JButton("Load saved profile");

        //tododoc
        public IntroMenuPane(ActionListener actionListener) {
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            this.actionListener = actionListener;
            gbc.ipady = 40;
            gbc.gridwidth = 2;
            gbc.gridy = 0;
            this.add(Content.header(), gbc);
            /*closeButton.addActionListener(e -> System.exit(0));*/
            startCreateNewProfileButton.addActionListener(actionListener);
            loadSavedProfileButton.addActionListener(actionListener);
            gbc.ipady = 0;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(startCreateNewProfileButton, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(loadSavedProfileButton, gbc);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(Content.closeAppButton(actionListener), gbc);
        }

    }

    //REQUIRES: actionListener input must have appropriate action commands for the JButtons
    //          submitNewProfileButton and cancelButton
    //EFFECTS: creates the UI that allows users to fill and submit a form to create a new profile.
    public static class NewProfilePanel extends JPanel {
        private JTextField userNameField = new JTextField();
        private final JLabel userNameLabel;
        ActionListener actionListener;
        JButton submitNewProfileButton = new JButton("Submit");
        JButton cancelNewProfileButton = new JButton("Cancel");

        //initializes the JPanel display
        public NewProfilePanel(ActionListener actionListener) {

            JPanel textPane = new JPanel();
            JPanel inputPane = new JPanel();
            JPanel buttonPane = new JPanel();

            //TODO PHASE4 add exception to load new profile if there's no saved profile
            textPane.add(new JLabel("Create New Profile"));
            userNameLabel = new JLabel("User Name");
            inputPane.setLayout(new GridLayout(1, 2));
            inputPane.add(userNameLabel);
            inputPane.add(userNameField);

            this.actionListener = actionListener;
            submitNewProfileButton.addActionListener(actionListener);
            cancelNewProfileButton.addActionListener(actionListener);
            buttonPane.add(submitNewProfileButton);
            buttonPane.add(cancelNewProfileButton);

            JPanel entirePane = new JPanel();
            entirePane.setLayout(new BorderLayout());
            entirePane.add(textPane, BorderLayout.NORTH);
            entirePane.add(inputPane, BorderLayout.CENTER);
            entirePane.add(buttonPane, BorderLayout.SOUTH);
            this.add(entirePane);
        }

        //EFFECTS: returns a String of the user's input in the JTextField to be set as profile name.
        // returns empty String if the input is unable to be processed by the default text formatter.
        public String getProfileNameInput() {
            return (userNameField.getText());
        }
    }
}
