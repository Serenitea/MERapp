package test;

import model.Pet;
import model.PetList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import ui.MERapp;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class uiTesting {
    private static final String FAIL_URL = "./data/fail.json";
    private static final String JSON_STORE_URL = "./data/profiles.json"; //normal
    private static final String EMPTY_URL = "./data/test.json"; //empty case
    private ArrayList<Pet> petArrayList;
    private JsonReader jsonReader;
    private final JTextField testTextField = new JTextField();


    @Test
    public void testCaseNameToListModel() throws IOException {
        jsonReader = new JsonReader(JSON_STORE_URL);
        PetList petList = jsonReader.read();
        petArrayList = petList.getPetArray();
        System.out.println(petList.toJson());
        System.out.println(MERapp.toListModel(petArrayList));
    }

//    @Test
//    public void testCaseEmptyJson() throws IOException {
//        PetList petList = new PetList();
//        petArrayList = petList.getPetArray();
//        System.out.println(petList.toJson());
//        System.out.println(MERapp.toListModel(petArrayList));
//
//    }

    @Test
    public void testProfileNameInputTextField() {
        System.out.println(testTextField.getText());
        assertEquals("", testTextField.getText());
    }

/*    @Test
    private void testAddPetTab() {
        MERapp.AddNewPetTab addPetTab;
        addPetTab = new MERapp.AddNewPetTab(MERapp.addPetTabListener());
        System.out.println(addPetTab.getWeightInput());
    }*/

    @AfterEach
    public void reset() {
        petArrayList = null;
    }

}