package ui;

import model.Pet;
import model.PetList;

import java.util.Arrays;
import java.util.Scanner;

// Pet Calorie Calculator application
// user interface methods
public class MERApp {
    private static final Scanner scanner = new Scanner(System.in);
    private final PetList newPetList = new PetList();

    public MERApp() {
        runApp();
    }

    //MODIFIES: this
    //EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;

        while (keepGoing) {
            displayMenu();
            String command = scanner.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    /*
    MODIFIES: this
     EFFECTS: processes user command from main menu of options
     */
    private void processCommand(String command) {
        switch (command) {
            case "n":
                // make new pet
                addNewPet();
                break;

            case "v":
                //view all pets name
                viewPet();
                break;

            case "r":
                removePet();
                break;

            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    private void printMenuHeader() {
        System.out.println("\n------------------------------------");
        System.out.println("Pet Weight Management App Main Menu");
        System.out.println("------------------------------------");
    }

    /*
    EFFECTS: displays action menu to user, excludes view/edit/remove options if newPetList is empty
    */
    private void displayMenu() {
        if (newPetList.getNumPets() == 0) {
            printMenuHeader();
            for (String s : Arrays.asList(
                    "\nNo Current Pets Found. ",
                    "\nn -> Add a New Pet",
                    "p -> quit",
                    "\n---------------------------")) {
                System.out.println(s);
            }
        } else {
            printMenuHeader();
            System.out.println("\nPlease input the key corresponding to an option.");
            System.out.println("\nCurrent Pets:");
            printPetNames();
            for (String s : Arrays.asList(
                    "\n\nOptions:",
                    "\nv -> Current Pets",
                    "n -> Add a New Pet",
                    "r -> Remove a Pet",
                    "q -> quit")) {
                System.out.println(s);
            }
        }

    }

    /*REQUIRES: at least 1 Pet in newPetList
    EFFECTS: prints all pets */
    private void viewPet() {
        System.out.println("CURRENT PETS:");
        printAllPets();

        System.out.println("Select a pet by entering the corresponding number, or b to return to main menu");
        printPetSelect();
        selectOrBack();
    }

    private void selectOrBack() {
        if (scanner.hasNextInt()) {
            //todo add exception to check that scanner.nextInt() < newPetList.getNumPets()
            //int petIndex = scanner.nextInt();
            editPet(scanner.nextInt());
        } else if (scanner.hasNext() && scanner.next().equalsIgnoreCase("b")) {
            System.out.println("Back to main menu.");
        }
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes 1 field of a Pet object to new user input*/
    private void editPet(int petIndex) {
        Pet selectedPet = newPetList.getPetArray().get(petIndex);
        boolean stayHere = true;

        while (stayHere) {
            printPetFields(selectedPet);
            displayPetMenu();
            String command = scanner.next();
            if (command.equals("b")) {
                stayHere = false;
            } else {
                switch (command) {
                    /*case "d":
                        editDiet(selectedPet);
                        break;*/
                    case "n":
                        editName(selectedPet);
                        break;
                    case "w":
                        editWeight(selectedPet);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + command);
                }
            }
        }

    }

    //todo editDiet()

    private void displayPetMenu() {
        System.out.println("n -> edit pet name");
        System.out.println("w -> edit pet weight");
        System.out.println("d -> add or change diet information");
        System.out.println("b -> back to Main Menu");
    }

    /*
    REQUIRES: newPetList Array created
    MODIFIES: this
    EFFECTS: adds new Pet object to newPetList Array via user inputs for fields
    */
    private void addNewPet() {
        boolean keepgoing = true;

        while (keepgoing) {
            System.out.println("ADD A NEW PET");
            System.out.println("Enter new pet name:");
            String petName = scanner.next();
            petName = petName.toLowerCase();

            if (newPetList.duplicateName(petName)) {
                System.out.println("There is already a pet with that name! Try a new pet name:");
            } else {
                Pet newPet = createNewPet(petName);
                System.out.println("\nNew pet added: " + newPet.getPetName());
                newPetList.add(newPet);
                printPetFields(newPet);
                keepgoing = false;
            }
        }
    }

    /*
    REQUIRES: unique pet name from existing Pets in newPetList
    EFFECTS: creates new Pet based on name argument and field values from user input
     */
    private Pet createNewPet(String petName) {
        System.out.println("Enter new pet's weight (kg):");
        double petWtKg = scanner.nextDouble();

        return new Pet(petName, petWtKg);
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: removes Pet object to newPetList Array*/
    private void removePet() {
        for (String s : Arrays.asList("REMOVE PET", "Select pet to remove (enter corresponding number):")) {
            System.out.println(s);
        }
        printPetSelect();
        Pet petToRemove = selectPet();
        String removedPetName = petToRemove.getPetName();
        newPetList.remove(petToRemove);
        System.out.printf("%s successfully removed.%n", removedPetName);
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    EFFECTS: prints a list of pet names and returns a user-selected Pet from newPetList*/
    private Pet selectPet() {
        //todo: add option to immediately go back to main menu without editing
        if (scanner.hasNextInt()) {
            int petIndex = scanner.nextInt();
            return newPetList.getPetArray().get(petIndex);
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

    /*
    EFFECTS: prints the list of Pet names in newPetList and their array index.
     */
    private void printPetSelect() {
        for (int i = 0; newPetList.getNumPets() > i; i++) {
            Pet currentPet = newPetList.getPetArray().get(i);
            System.out.printf("%s - %d%n", currentPet.getPetName(), i);
        }
    }

    /*
    EFFECTS: prints the list of Pet names in newPetList.
     */
    private void printPetNames() {
        for (int i = 0; newPetList.getNumPets() > i; i++) {
            Pet currentPet = newPetList.getPetArray().get(i);
            System.out.printf(currentPet.getPetName());
        }
    }

    /*
    EFFECTS: Prints all fields for a Pet.
    */
    public void printPetFields(Pet petToPrint) {
        System.out.println("\nName: " + petToPrint.getPetName());
        System.out.println("Weight: " + petToPrint.getWeight() + " kg\n");
    }

    /*
    EFFECTS: prints all pets with all of their fields to the user console.
    */
    public void printAllPets() {
        for (int i = 0; i < newPetList.getNumPets(); i++) {
            Pet printPet = newPetList.getPetArray().get(i);
            printPetFields(printPet);
        }
    }


}
