
# Pet Food Calculator
###TODO:

Keywords: set, Pane (areas inside a tab), tab, UI, create, add, new, intro, Listener

Exception handling: 

by event/button:
- [ ] create new profile
    - cases: null/no saved file, there is a saved file - same treatment
    - set petList as a new instance of PetList
    - set createNewProfilePane as new instance of NewProfilePanel
    - set introPanel as createNewProfilePane
    - set frame's contentPane to be introPanel and display the frame
- [ ] load saved profile
    - try setting petList from reading json file
    - 
- [ ] 

locations

- [ ] loadSavedProfile
    - catch (IOException e)
    - catch (JSONException e)
- [ ] addNewToPetList()
    - catch (NullPointerException e)
    - catch (NullPointerException e)
- [ ] editPetEvent()
    - catch (NullPointerException e)
    - if (editPetTab == null)  + else
- [ ] editSelectedPet - 2 times .get(index)
- [ ] closeEditWindowEvent .get(index)
- [ ] saveSessionEvent() 
    - catch (NullPointerException e)
- [ ] saveSession() .get(index)
    - catch (FileNotFoundException e)
- [ ] removePetFromPetList() .get(index)
- [ ] updatePetList() .get(index)
- [ ] updateRightPane .get(index)
    - catch (IndexOutOfBoundsException e)
    - if (currentPet.getPortraitPic() == "")
- [ ] initializeSplitPane() if (petArrayList.size() > 0) + else
- [ ] mainMenuHeader() if (profileName == "") + else
- [ ] Submit button in addPetListener, if (petList.getPetArray().size() == count + 1)
- [ ] shorten fns

## Weight Management and Feeding Calculator for Cats & Dogs

Given the weight, age, and lifestyle needs of a cat or dog and the nutritional density of their diets, 
this application will suggest a daily volume or weight to feed that is appropriate for maintaining their weight. 
It will guide pet owners in long-term weight management by letting owners set a target weight 
and track their pet's weight progression over time. The obesity epidemic, affecting over half of cats and dogs in NA,
 is a major health concern in small animal medicine. This application can empower pet owners to understand their pet's
 nutritional needs and maintain healthy weights.
 
 User Stories
 Some user stories were modified upon GUI implementation to better suit user needs.
 - upon application launch, option to load a saved profile from a json file or to create a new profile. O
 - user may add new pets to their profile by inputting name and weight. The other pet traits are optional for creating a pet. Optional traits fully implemented currently: diet calories.
 - Warns user if they attempt to create a pet with a duplicate name (not case-sensitive). This change better suits the needs of the user while retaining duplication detection function. *Previous story: duplicate names were not allowed and the console prompted the user to try inputting again.*
 - View the current list of pets. One pet may be selected providing an individual display.
 - Select and edit a pet's traits.
 - Remove a pet from the list of current pets. This option is only available from the individual pet edit tab after selecting a pet for editing.
 - Dialog prompt to save the current session when user submits the "close app" or "exit profile" buttons. This better reflects the capabilities of the GUI since the previous function is now unnecessary. *Previous story: prompt to save current Pet to json file when exiting from individual Pet menu back to Main Menu*

 
 
 
 ## Resources used
 - John Zukowski - the Definitive Guide to Java Swing (2005, Apress)
 - Kishori Sharan - Beginning Java 8 APIs, Extensions and Libraries (2014, Apress)
 - Oracle Swing tutorials: <a href="https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html#gridbagConstraints">GridBagLayout Tutorial</a>, <a href="https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SplitPaneDemoProject/src/components/SplitPaneDemo.java"> JSplitPane Demo </a>, 
 
 https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FormattedTextFieldDemoProject/src/components/FormattedTextFieldDemo.java
 - Youtube video: <a href="https://www.youtube.com/watch?v=CUmvVhGqaVQ"> Scale ImageIcon automatically to JLabel size </a>
 - Icon made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com </a>

 
### *Todo wishlist:*
 - more trait fields for pets - e.g. species, gender, neuter status lifestyle, body condition score, picture portrait
 - track a pet's current weight with an attached date
 - *feeding history*
 - *edit weight history*
 - *more options for diet caloric measurements - kcalPerCup, kcalPerCan*
 - *imperial measurement conversions*
 - *expand on diet traits - e.g. fields for brand, wet/dry, etc.*
 - *add the current diet's caloric count and output recommended feeding amount*
 - *need RER and MER calculation functions and possibly stored vars*

