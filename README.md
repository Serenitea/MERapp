
# Pet Food Calculator

## Weight Management and Feeding Calculator for Cats & Dogs

Given the weight, age, and lifestyle needs of a cat or dog and the nutritional density of their diets, 
this application will suggest a daily volume or weight to feed that is appropriate for maintaining their weight. 
It will guide pet owners in long-term weight management by letting owners set a target weight 
and track their pet's weight progression over time. The obesity epidemic, affecting over half of cats and dogs in NA,
 is a major health concern in small animal medicine. This application can empower pet owners to understand their pet's
 nutritional needs and maintain healthy weights.
 
 User Stories
 Some user stories were modified upon GUI implementation to better fit GUI functionality.
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
 - Oracle Swing tutorials: <a href="https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html#gridbagConstraints">GridBagLayout Tutorial</a>, <a href="https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SplitPaneDemoProject/src/components/SplitPaneDemo.java"> JSplitPane Demo </a>, <a href="https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FormattedTextFieldDemoProject/src/components/FormattedTextFieldDemo.java"> FormattedTextField Demo </a>
 
 - Youtube video: <a href="https://www.youtube.com/watch?v=CUmvVhGqaVQ"> Scale ImageIcon automatically to JLabel size </a>
 - Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com </a>

##Phase 4: Task 2
Abstract class **Tab**'s subclasses: EditPetTab, AddNewPetTab, MainTab, IntroMenuPane, NewProfilePanel.
Interface **PetInputForm**'s implementing classes: EditPetTab, AddNewPetTab.

An abstract class, Tab, and an interface, PetInputForm, were used to implement type hierachy for the UI elements.  
Tab extends JPanel and contains subclasses for each visible UI interface displayed in the app's main JFrame window. 
New instances of appropriate Tab subclasses are added or removed from the JFrame window as needed.
The main benefit is organization and code clarity provided since the main app UI became much less cluttered.
Tab is very abstract due to the individually distinct GUI functions required by each UI interface. 
Hence, Tab contains only one abstract method, Tab.setActionListener, which sets the given ActionListener argument
to the JButtons appropriate for that subclass. 

The interface PetInputForm added further structure to the Tab subclasses that receives user input for Pet traits.
The implementing classes share common methods reflecting the basic features required for a user-input form for Pets:
 - setInputForm(): creates the UI component containing the editable form for Pet traits, 
 composed of user input text fields and text labels 
 - getInputtedPet(): creates and returns a Pet object constructed from user inputted parameters
 - valid____Input(): where ____ indicates a Pet trait, currently Name, Weight, and Diet Caloric Content.
 Boolean indicating whether the pet trait for that input is a valid entry.

