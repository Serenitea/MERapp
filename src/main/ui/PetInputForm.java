package ui;

import model.Pet;

import javax.swing.*;

//interface for UI classes that requires user input for pet traits
//AddPetTab, EditPetTab
public interface PetInputForm {

    //creates the UI component containing the editable form for Pet traits,
    // composed of user input text fields and text labels
    JPanel setInputFormPane();

    //checks if nameField returns a valid name String
    default boolean validNameInput() {
        return false;
    }

    //returns true if the input is a valid positive number
    //value of 0 is allowed if the user doesn't know pet weight.
    default boolean validWeightInput() {
        return false;
    }

    //checks if dietCalField returns a positive double type number
    default boolean validDietCalInput() {
        return false;
    }


    //creates and returns a Pet object constructed from user inputted parameters
    Pet getInputtedPet();

}
