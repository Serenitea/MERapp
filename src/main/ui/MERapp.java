package ui;

import model.Pet;
import model.PetList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Pet Calorie Calculator application
// user interface methods
public class MERapp extends JFrame {
    JFrame frame;
    ChangeListener changeListener;
    JTabbedPane sourceTabbedPane;
    int index;
    JTabbedPane tabbedPane;
    GridBagConstraints gridBagConstraints;
    private static final int GRIDXVAL = 0;
    private static final int GRIDYVAL = 0;
    public static final int FRAMEWIDTH = 450;
    public static final int FRAMEHEIGHT = 600;

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
    private static List<String> MANAGE_PET_MENU_HEADER = Arrays.asList(
            "\n------------------------------------",
            "Manage a Pet - Menu",
            "\n------------------------------------");
    private static final Scanner scanner = new Scanner(System.in);
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private PetList petList;


    /*
EFFECTS: prints Application menu header
 */
    private void printMenu(List<String> menu) {
        for (String s : menu) {
            System.out.println(s);
        }
    }

    /*
    EFFECTS: constructs the Pet MER application and initializes json I/O
     */
    public MERapp() {
        super("Pet Weight Management App");
        initializeFields();
        initializeFrame();
        runApp();
    }

    //todo doc
    private void initializeFrame() {
        frame = new JFrame("Tabbed Pane Frame Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = GRIDXVAL;
        gridBagConstraints.gridy = GRIDYVAL;

        tabbedPane = new Tabs.InitializeTabs();
        frame.add(tabbedPane);
//        frame.getContentPane().setLayout(new GridBagLayout());


//        initializeListener();
//        tabbedPane.addChangeListener(changeListener);

        frame.setVisible(true);

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
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

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

    /*
    MODIFIES: ./data/profiles.json
    EFFECTS:
     */
    private void promptConfirmSave() {

    }

    /*
    REQUIRES: no currently loaded profile (empty petList)
    EFFECTS: displays and processes input for intro menu
     */
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

    /*
    REQUIRES: no currently loaded profile (empty petList)
    EFFECTS: display intro menu options: load saved profile, create new profile, or quit
     */
    private void displayIntroMenu() {
        printMenu(MAIN_MENU_HEADER);
        printMenu(WELCOME_MENU);
    }

    /*
    REQUIRES: no currently loaded profile (empty petList)
    EFFECTS: processes commands of intro menu given a valid non-exit command
     */
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

    /*
    REQUIRES: no currently loaded profile (empty petList)
    MODIFIES: this
    EFFECTS: loads previously saved profiles from local json storage path
     */
    private void loadSavedPetList() {
        try {
            petList = jsonReader.read();
            System.out.println("Loaded " + petList.getOwnerName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    /*
    MODIFIES: this
     EFFECTS: processes user command from main menu of options
     */
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

    /*
    MODIFIES: this
    EFFECTS: remove all data from fields of petList
     */
    private void clearPetList() {
        petList = new PetList();
    }

    /*
    REQUIRES: at least 1 pet in petList
    MODIFIES: ./data.profiles.json
    EFFECTS: confirms user name then saves petList to save path
     */
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

    /*
        REQUIRES: at least 1 pet in petList
        MODIFIES: this
        EFFECTS: prompts user to input owner name if name is null, or confirm current user name with option to edit
         */
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

    /*
    REQUIRES: assumes there is at least 1 Pet in petList.
    MODIFIES: this
    EFFECTS: sets owner name field of petList to inputted String.
     */
    private void newOwnerName() {
        System.out.println("Please enter your new name:");
        String inputName = scanner.next();
        petList.setOwnerName(inputName);
    }

    /*
    REQUIRES: valid String variable in the owner name field of petList.
    EFFECTS: processes user input for confirming the current owner name
     */
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

    /*
    EFFECTS: prints options for main menu if a profile is currently loaded
     */
    private void displayMainMenu() {
        printMenu(MAIN_MENU_HEADER);
        System.out.println("\nPlease input the key corresponding to an option.");
        System.out.println("\nCurrent Pets:");
        printPetNames(", ");
        printMenu(MAIN_MENU);
    }

    /*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: prints all pets and allows user to select a pet to edit or (b) to main menu
    */
    private void managePets() {
        printMenu(PET_MENU_HEADER);
        System.out.println("Select a pet to edit by entering the corresponding number, or b to return to main menu");
        System.out.println("CURRENT PETS:");
        printPetSelect();
        System.out.println("b - back to main menu");
        selectAPetOrBack();
    }

    /*
    REQUIRES: inputted int < number of pets in newPetList
    EFFECTS: processes user input for the viewPet menu -
             either an integer corresponding to a pet to edit or (b)ack to main menu
     */
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

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes 1 field of a Pet object to new user input*/
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

    /*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: displays a pet's option menu
     */
    private void displayPetMenu() {
        for (String s : PET_MENU) {
            System.out.println(s);
        }
    }

    /*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: processes user input for a pet's option menu
     */
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

    /*
    REQUIRES: newPetList Array created
    MODIFIES: this
    EFFECTS: adds new Pet object to newPetList Array via user inputs for fields
    */
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

    /*
    REQUIRES: unique pet name from existing Pets in newPetList
    EFFECTS: creates new Pet based on name argument and field values from user input
     */
    private Pet instantiateNewPet(String petName) {
        System.out.println("Enter new pet's weight (kg):");
        double petWtKg = scanner.nextDouble();
        return new Pet(petName, petWtKg);
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: removes Pet object to newPetList Array*/
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

    /*
    REQUIRES: newPetList has at least 1 Pet object
    MODIFIES: this
    EFFECTS: removes a selected Pet from newPetList
     */
    private void removeSelectedPet(Pet selectedPet) {
        String removedPetName = selectedPet.getPetName();
        petList.remove(selectedPet);
        //todo printing not working
        System.out.printf("%s successfully removed.%n", removedPetName);
    }

    /*
    REQUIRES: newPetList Array has >= 1 Pet
    EFFECTS: prints a list of pet names and returns a user-selected Pet from newPetList, or b -> prev menu
    */
    private Pet selectPet() {
        if (scanner.hasNextInt()) {
            int petIndex = scanner.nextInt();
            return petList.getPetArray().get(petIndex);
        } else if (scanner.hasNext() && scanner.next().equalsIgnoreCase("b")) {
            runApp();
        }
        return null;
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes the weight field of the Pet object to new user input*/
    private void editWeight(Pet petToEdit) {
        // edit weight
        System.out.println("Enter new pet weight:");
        double newWeight = scanner.nextDouble();
        petToEdit.setWeight(newWeight);
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes the name field of the Pet object to new user input*/
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

    /*
    EFFECTS: prints the list of Pet names in newPetList and their array index.
     */
    private void printPetSelect() {
        for (int i = 0; petList.getNumPets() > i; i++) {
            Pet currentPet = petList.getPetArray().get(i);
            System.out.printf("%s - %d%n", currentPet.getPetName(), i);
        }
    }

    /*
    REQUIRES: assumes there is at least 1 Pet in petList
    EFFECTS: prints the list of Pet names in newPetList.
     */
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

    /*
    EFFECTS: Prints all fields for a Pet.
    */
    public void printPetFields(Pet petToPrint) {
        System.out.println("\nName: " + petToPrint.getPetName());
        System.out.println("Weight: " + petToPrint.getWeight() + " kg\n");
        System.out.println("Diet caloric content: " + petToPrint.getDietCalPerKg() + " kCal/kg\n");
    }

    /*
    EFFECTS: prints all pets with all of their fields to the user console.
    */
    public void printAllPetsAllFields() {
        for (int i = 0; i < petList.getNumPets(); i++) {
            Pet printPet = petList.getPetArray().get(i);
            printPetFields(printPet);
        }
    }

    /*    *//*
    EFFECTS: changes given string to be first letter capitalized followed by lower case letters.
     *//*
    public String capitalizeFirst(String str) {
        str = str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
        return str;
    }*/

}
