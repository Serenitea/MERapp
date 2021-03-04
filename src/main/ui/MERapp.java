package ui;

import model.Pet;
import model.PetList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

// Pet Calorie Calculator application
// user interface methods
public class MERapp {
    private static final String JSON_STORE = "./data/profiles.json";
    private static final Scanner scanner = new Scanner(System.in);
    private PetList petList;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    public MERapp() {
        petList = new PetList();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runApp();
    }

    //EFFECTS: directs user to intro menu if no profile currently loaded or the main menu of options
    private void runApp() {
        boolean keepGoing = true;

        while (keepGoing) {
            if (petList.getNumPets() == 0) {
                keepGoing = introMenu();
            } else {
                displayMainPetMenu();
                String command = scanner.next();
                if (command.equalsIgnoreCase("q")) {
                    keepGoing = false;
                } else {
                    processMainMenuCommand(command);
                }
            }
        }
        System.out.println("\nGoodbye!");
    }

    /*
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
        return true; //todo
    }

    /*
    EFFECTS: display intro menu options: load saved profile, create new profile, or quit
     */
    private void displayIntroMenu() {
        printMenuHeader();
        for (String s : Arrays.asList(
                "\nl -> Load profile",
                "n -> Create a new profile",
                "q -> quit",
                "\n---------------------------")) {
            System.out.println(s);
        }
    }

    /*
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

    //todo json
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
                //view all pets name
                viewPet();
                break;
            case "r":
                removePetChoose();
                break;
            case "s":
                savePetList();
                break;
            case "p":
                clearPetList();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    //todo
    /*
    MODIFIES: this
    EFFECTS: remove all data from fields of petList
     */
    private void clearPetList() {
        petList = new PetList();
    }

    //todo json method
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
    //todo
    private void confirmOwnerName() {
        if (petList.getOwnerName() == null) {
            System.out.println("This profile currently has no owner name.");
            newOwnerName();
        } else {
            String ownerName = petList.getOwnerName();
            System.out.printf("Please confirm that the current profile name is correct (Y/N): %s",
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
    EFFECTS: prints Application menu header
     */
    private void printMenuHeader() {
        System.out.println("\n------------------------------------");
        System.out.println("Pet Weight Management App Main Menu");
        System.out.println("------------------------------------");
    }


    /*
    EFFECTS: prints options for main menu if a profile is currently loaded
     */
    private void displayMainPetMenu() {
        printMenuHeader();
        System.out.println("\nPlease input the key corresponding to an option.");
        System.out.println("\nCurrent Pets:");
        printPetNames(", ");
        for (String s : Arrays.asList(
                "\n\nOptions:",
                "\nm -> Manage Current Pets",
                "n -> Add a New Pet",
                "r -> Remove a Pet",
                "s -> Save Session",
                "p -> exit current profile",
                "q -> quit")) {
            System.out.println(s);
        }
    }

    /*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: prints all pets and allows user to select a pet to edit or (b) to main menu
    */
    private void viewPet() {
        System.out.println("CURRENT PETS:");
        printAllPetsAllFields();

        System.out.println("Select a pet to edit by entering the corresponding number, or b to return to main menu");
        printPetSelect();
        selectOrBack(); //todo fix
    }

    /*
    REQUIRES: inputted int < number of pets in newPetList
    EFFECTS: processes user input for the viewPet menu -
             either an integer corresponding to a pet to edit or (b)ack to main menu
     */
    private void selectOrBack() {
        String selection = scanner.next();
        try {
            int petIndex = Integer.parseInt(selection);
            editPet(petIndex);
        } catch (NumberFormatException e) {
            if ("b".equals(selection)) {
                System.out.println("Back to main menu.");
            } else {
                System.out.println("Selection not valid...");
            }
            System.out.println("Back to main menu.");
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
            displayPetMenu();
            String command = scanner.next();
            if ((command.equals("b")) && (petList.getNumPets() == 0)) {
                stayHere = false;
            } else {
                processPetMenuCommand(command, selectedPet);
            }
        }
    }

    /*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: displays a pet's option menu
     */
    private void displayPetMenu() {
        System.out.println("e -> edit pet name");
        System.out.println("w -> edit pet weight");
        System.out.println("d -> add or change diet information");
        System.out.println("r -> remove this pet");
        System.out.println("b -> back to Main Menu");
    }

    /*
    REQUIRES: at least 1 Pet in newPetList
    EFFECTS: processes user input for a pet's option menu
     */
    private void processPetMenuCommand(String command, Pet selectedPet) {
        switch (command) {
            case "d":
                editDiet(selectedPet);
                break;
            case "e":
                editName(selectedPet);
                break;
            case "w":
                editWeight(selectedPet);
                break;
            case "r":
                removeSelectedPet(selectedPet);
                break;
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
