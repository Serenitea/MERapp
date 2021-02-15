package ui;

import model.Pet;
import model.PetList;
//import model.PetList;

import java.util.ArrayList;
import java.util.Scanner;

// Pet Calorie Calculator application
// user interface methods
public class MerApp {
    private static Scanner scanner = new Scanner(System.in);
    private PetList newPetList = new PetList();

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

    // MODIFIES: this
    // EFFECTS: processes user command from main menu of options
    private void processCommand(String command) {
        switch (command) {
            case "n":
                // make new pet
                System.out.println("Enter new pet name:");
                String petName = scanner.next();
                System.out.println("Enter new pet's weight (kg):");
                double petWtKg = scanner.nextDouble();
                Pet newPet = new Pet(petName, petWtKg);
                System.out.println(newPet.getPetName());
                System.out.println(newPet.getWeight());

                // add new pet to listofpets
                newPetList.add(newPet);
                newPetList.printPets();

                break;


            case "e":
                //print list of names
                newPetList.printPets();
                System.out.println("Enter the number corresponding to the order your pet is in: (starting with 0)");

                //enter name of pet to be selected
                int indexPet = scanner.nextInt();
                System.out.println("Selected Pet: ");
                Pet petToEdit = newPetList.getPetArray().get(indexPet);
                System.out.println(petToEdit.getPetName());


                //menu -> enter name of var to be changed
                System.out.println("Enter (name) to edit name and (w) to edit weight:");
                String editVar = scanner.next();
                switch (editVar) {
                    case "name":
                        // edit name
                        System.out.println("Enter new pet name:");
                        String newPetName = scanner.next();
                        petToEdit.setNewName(newPetName);
                        break;

                    case "w":
                        // edit weight
                        System.out.println("Enter new pet weight:");
                        double newWeight = scanner.nextDouble();
                        petToEdit.setWeight(newWeight);
                        break;
                }

                // print changed pet
                System.out.println("Edited Pet Information:");
                System.out.println(petToEdit.getPetName());
                System.out.println(petToEdit.getWeight());

                break;

            case "v":
                //view all pets name
                newPetList.printPets();
                break;
                /*
                Exception case (if none of the options are selected)
                 */
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: displays action menu to user
    private void displayMenu() {
        System.out.println("\ne -> Edit Current Pets");
        System.out.println("\nv -> Current Pets");
        System.out.println("\nn -> Add a New Pet");
        System.out.println("\nq -> quit");
    }



}
