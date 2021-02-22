package ui;

import model.Pet;
import model.PetList;

import java.util.Arrays;
import java.util.Scanner;

// Pet Calorie Calculator application
// user interface methods
public class MerApp {
    private static final Scanner scanner = new Scanner(System.in);
    private final PetList newPetList = new PetList();

    public MerApp() {
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
            case "e":
                editPets();
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

    /*
    EFFECTS: displays action menu to user, excludes view/edit/remove options if newPetList is empty
    */
    private void displayMenu() {
        if (newPetList.getNumPets() == 0) {
            for (String s : Arrays.asList("\nPet Weight Management App Main Menu",
                    "\nNo Current Pets Found. ",
                    "\nn -> Add a New Pet",
                    "\np -> quit",
                    "\n---------------------------")) {
                System.out.println(s);
            }
        } else {
            System.out.println("\nPet Weight Management App Main Menu");
            System.out.println("\nv -> View Current Pets");
            System.out.println("\ne -> Edit Current Pets");
            System.out.println("\nn -> Add a New Pet");
            System.out.println("\nr -> Remove a Pet");
            System.out.println("\nq -> quit");
        }

    }

    /*REQUIRES: at least 1 Pet in newPetList
    EFFECTS: prints attributes of all pets or "no pets found" message if newPetList is empty*/
    private void viewPet() {
        if (newPetList.getNumPets() == 0) {
            System.out.println("EDIT PETS");
            System.out.println("No Current Pets Found."
                    + "\n---------------------------");
        } else {
            System.out.println("CURRENT PETS:");
            newPetList.printAllPets();
        }
    }

    /*REQUIRES: newPetList Array created
    MODIFIES: this
    EFFECTS: change newPetList if it's not empty*/
    private void editPets() {
        //todo if don't want to add pet anymore, press b to return to main menu
        if (newPetList.getNumPets() == 0) {
            System.out.println("No Current Pets Found."
                    + "\n---------------------------");
        } else {
            for (String s : Arrays.asList("EDIT PETS", "Select pet to edit (enter corresponding number):")) {
                System.out.println(s);
            }
            editPet();
        }
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
                newPet.printPetAttributes();
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
        Pet petToRemove = selectPet();
        String removedPetName = petToRemove.getPetName();
        newPetList.remove(petToRemove);
        System.out.printf("%s successfully removed.%n", removedPetName);
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    EFFECTS: prints a list of pet names and returns a user-selected Pet from newPetList*/
    private Pet selectPet() {
        for (int i = 0; newPetList.getNumPets() > i; i++) {
            Pet currentPet = newPetList.getPetArray().get(i);
            System.out.printf("%s - %d%n", currentPet.getPetName(), i);
        }
        //todo: add option to immediately go back to main menu without editing
        int petIndex = scanner.nextInt();
        return newPetList.getPetArray().get(petIndex);
    }

    /*REQUIRES: newPetList Array has >= 1 Pet
    MODIFIES: this
    EFFECTS: changes 1 field of a Pet object to new user input*/
    private void editPet() {
        Pet petToEdit = selectPet();
        System.out.printf("Selected pet: %s %n",
                petToEdit.getPetName());

        for (String s : Arrays.asList("Enter the variable you would like to edit:", "\n name (n)", "\n weight (w)")) {
            System.out.println(s);
        }
        
        String varToEdit = scanner.next();
        switch (varToEdit) {
            case "n":
                editName(petToEdit);
                break;
            case "w":
                editWeight(petToEdit);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + varToEdit);
        }
        petToEdit.printPetAttributes();
        System.out.printf("Changes to %s saved.%n", petToEdit.getPetName());
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




}
