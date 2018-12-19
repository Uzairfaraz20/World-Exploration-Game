
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//
//Author: Affan
//Used code from Vineet for help
//

public class AGui2 extends JFrame implements UserInterface {

    //all GUI components
    private JLabel playerLabel, placeLabel, healthValueLabel;
    private DefaultListModel artifactModel, artifactPlaceModel, placesModel, otherCharactersModel;
    private JList artifactList, artifactPlaceList, placesList, otherCharactersList;
    private JScrollPane artifactPane, artifactPlacePane, placesPane, otherCharactersPane;
    private JButton useArtifactButton, dropArtifactButton, getArtifactButton, endTurnButton, goButton, attackButton;
    private JProgressBar healthProgressBar;
    private JComboBox jcbList;
    private String returnString;
    private Character currentCharacter;
    private List<String> eventList;
    

    public static final Object communicationLock = new Object();

    //constructor
    public AGui2() {
        returnString = " ";
        setUpGui();
    }

    private void setUpGui() {
        eventList = new ArrayList<String>();

        //set up the frame
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setLocation(256, 100);
        this.setTitle("Affan's GUI");
        this.setBackground(Color.DARK_GRAY);

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 2, 2, 2);

        JLabel label = new JLabel(" ");
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        label.setBackground(Color.DARK_GRAY);
        add(label, constraints);

        playerLabel = new JLabel();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 3;
        add(playerLabel, constraints);

        label = new JLabel("is currently in the ");
        constraints.gridx = 2;
        constraints.gridy = 3;
        add(label, constraints);

        placeLabel = new JLabel();
        constraints.gridx = 3;
        constraints.gridy = 3;
        add(placeLabel, constraints);

        label = new JLabel("Bag Items");
        constraints.gridx = 2;
        constraints.gridy = 4;
        add(label, constraints);

        artifactModel = new DefaultListModel();
        artifactList = new JList(artifactModel);
        artifactPane = new JScrollPane(artifactList);
        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.8;
        constraints.weighty = 1.0;
        add(artifactPane, constraints);
        constraints.weighty = 0.0;

        useArtifactButton = new JButton("USE");
        constraints.gridx = 2;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        formatButton(useArtifactButton);
        add(useArtifactButton, constraints);
        addUseArtifactActionListener();

        dropArtifactButton = new JButton("DROP");
        constraints.gridx = 3;
        constraints.gridy = 6;
        formatButton(dropArtifactButton);
        add(dropArtifactButton, constraints);
        addDropArtifactActionListener();

        label = new JLabel("Items Nearby");
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(label, constraints);
        constraints.gridwidth = 1;

        artifactPlaceModel = new DefaultListModel();
        artifactPlaceList = new JList(artifactPlaceModel);
        artifactPlacePane = new JScrollPane(artifactPlaceList);
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.8;
        constraints.weighty = 1.0;
        add(artifactPlacePane, constraints);
        constraints.weighty = 0.0;

        getArtifactButton = new JButton("OBTAIN");
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        formatButton(getArtifactButton);
        add(getArtifactButton, constraints);
        addGetArtifactActionListener();

        endTurnButton = new JButton("FINISH");
        constraints.gridx = 0;
        constraints.gridy = 6;
        formatButton(endTurnButton);
        add(endTurnButton, constraints);
        addEndTurnButtonActionListener();

        label = new JLabel("Nearby Places");
        constraints.gridx = 4;
        constraints.gridy = 0;
        add(label, constraints);

        placesModel = new DefaultListModel();
        placesList = new JList(placesModel);
        placesPane = new JScrollPane(placesList);
        constraints.gridx = 4;
        constraints.gridwidth = 2;
        constraints.gridy = 1;
        add(placesPane, constraints);
        constraints.gridwidth = 1;
        
//        jcbList = new JComboBox(getStringArray(placesModel));
//        constraints.gridx = 4;
//        constraints.gridy = 2;
//        add(jcbList, constraints);

        goButton = new JButton("MOVE");
        constraints.gridx = 4;
        constraints.gridy = 2;
        formatButton(goButton);
        add(goButton, constraints);
        addGoButtonActionListener();

        label = new JLabel("Health");
        constraints.gridx = 0;
        constraints.gridy = 5;
        add(label, constraints);

//        healthProgressBar = new JProgressBar();
//        healthProgressBar.setMaximum(100);
//        constraints.gridx = 1;
//        constraints.gridy = 5;
//        add(healthProgressBar, constraints);
        
        healthValueLabel = new JLabel("HealthValue");
        //healthProgressBar.setMaximum(100);
        constraints.gridx = 1;
        constraints.gridy = 5;
        add(healthValueLabel, constraints);

        label = new JLabel("Characters Nearby");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(label, constraints);

        otherCharactersModel = new DefaultListModel();
        otherCharactersList = new JList(otherCharactersModel);
        otherCharactersPane = new JScrollPane(otherCharactersList);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weighty = 0.7;
        add(otherCharactersPane, constraints);
        constraints.weighty = 0;
        constraints.gridwidth = 1;

        attackButton = new JButton("ATTACK");
        constraints.gridx = 0;
        constraints.gridy = 2;
        formatButton(attackButton);
        add(attackButton, constraints);
        addAttackButtonActionListener();
        
        
        
        
    }
    
    private String[] getStringArray( DefaultListModel d) {
    	
    	return (String[]) d.toArray();
    }
    
    private void formatButton(JButton b) {
    	Font font = new Font("Papyrus", Font.BOLD, 12);
    	b.setBackground(Color.red);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFont(font);
        b.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
    }

    private void displayError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.WARNING_MESSAGE);
    }

    private void displayMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    //add an action listener for Attack button
    private void addAttackButtonActionListener() {
        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (otherCharactersList.isSelectionEmpty()) {
                    displayError("No character was selected to attack", "Cannot attack");
                    return;
                }
                String characterSelected = otherCharactersList.getSelectedValue().toString();
                returnString = "ATTACK " + characterSelected;
                synchronized (communicationLock) {
                    //signals getLine to return
                    communicationLock.notifyAll();
                }
            }
        });
    }

    //add an action listener for Move button
    private void addGoButtonActionListener() {
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (placesList.isSelectionEmpty()) {
                    displayError("No place selected to move to", "Cannot move");
                    return;
                }
                String placeSelected = placesList.getSelectedValue().toString().split(" -")[1];
                returnString = "GO " + placeSelected;
                synchronized (communicationLock) {
                    //signals getLine to return
                    communicationLock.notifyAll();
                }
            }
        });
    }

    //add an action listener for the UseArtifact button
    private void addUseArtifactActionListener() {
        useArtifactButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (artifactList.isSelectionEmpty()) {
                    displayError("No artifact was selected", "Cannot use artifact");
                    return;
                }
                String artifactSelected = artifactList.getSelectedValue().toString();
                returnString = "USE " + artifactSelected;
                synchronized (communicationLock) {
                    //signals getLine to return
                    communicationLock.notifyAll();
                }
            }
        });
    }

    //adds an action listener for the DropArtifactButton
    private void addDropArtifactActionListener() {
        dropArtifactButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (artifactList.isSelectionEmpty()) {
                    displayError("No artifact was selected", "Cannot perform a drop");
                    return;
                }
                String artifactSelected = artifactList.getSelectedValue().toString();
                returnString = "DROP " + artifactSelected;
                synchronized (communicationLock) {
                    //signals getLine to return
                    communicationLock.notifyAll();
                }
            }
        });
    }

    //adds an action listener for the GetArtifactButton
    private void addGetArtifactActionListener() {
        getArtifactButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (artifactPlaceList.isSelectionEmpty()) {
                    displayError("No artifact was selected", "Cannot perform a get");
                    return;
                }
                String artifactSelected = artifactPlaceList.getSelectedValue().toString();
                returnString = "GET " + artifactSelected;
                synchronized (communicationLock) {
                    //signal getLine to return
                    communicationLock.notifyAll();
                }
            }
        });
    }

    private void addEndTurnButtonActionListener() {
        endTurnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnString = "PASS";
                synchronized (communicationLock) {
                    //signal getLine to return
                    communicationLock.notifyAll();
                }
            }
        });
    }

    //loads the names of artifacts from a character's inventory
    private void loadArtifactList() {
        System.out.println("Loading Artifact list");
        Map<String, Artifact> inventory = currentCharacter.getInventory();
        artifactModel.clear();
        for (Map.Entry<String, Artifact> entry : inventory.entrySet()) {
            System.out.println("Adding " + entry.getKey());
            artifactModel.addElement(entry.getKey());
        }
        artifactList.setModel(artifactModel);
        artifactPane.revalidate();
    }

    private void loadPlaceInfo() {
        //clear the model first
        artifactPlaceModel.removeAllElements();
        Place place = currentCharacter.getCurrentPlace();
        placeLabel.setText(place.getName());
        for (Map.Entry<String, Artifact> entry : place.getArtifacts().entrySet()) {
            artifactPlaceModel.addElement(entry.getKey());
        }

        placesModel.clear();
        List<Direction> directionList = place.getDirections();
        for (Direction direction : directionList) {
            String s = direction.getTo().getName() + " -" + direction.getDirectionString().toUpperCase();
            placesModel.addElement(s);
        }

        otherCharactersModel.clear();
        Map<Integer, Character> characterMap = currentCharacter.getCurrentPlace().getCharacters();
        for (Map.Entry<Integer, Character> characterEntry : characterMap.entrySet()) {
            if (currentCharacter.getID() != characterEntry.getKey()) {
                otherCharactersModel.addElement(characterEntry.getValue().getName());
            }
        }
    }

    private void loadPlayerInfo() {
        playerLabel.setText(currentCharacter.getName());
        placeLabel.setText(currentCharacter.getCurrentPlace().getName());
        healthValueLabel.setText( Integer.toString( currentCharacter.getHealth() ) );
        loadArtifactList();
        loadPlaceInfo();
        healthProgressBar.setValue(currentCharacter.getHealth());
        displayMessage(currentCharacter.getName() + " it is now your turn", "Your move");
        displayPreviousEvents();
    }

    private void displayPreviousEvents() {
        //only show previous events if there are any
        if (eventList.size() == 0)
            return;
        String message = "";
        for (String s : eventList) {
            message += s + "\n";
        }
        displayMessage(message, "Moves other players have made");
        eventList.clear();
    }

    public void display(String tag, String message) {
        System.out.println("Display was called");
        //depending on the tag, do different things for a given message
        if (tag.equalsIgnoreCase(IO.LOAD_PLAYER)) {
            display_LOAD_PLAYER(message);

        } else if (tag == IO.DROP_ARTIFACT) {
            display_DROP_ARTIFACT(message);

        } else if (tag.equalsIgnoreCase(IO.END_TURN)) {
            displayMessage(currentCharacter.getName() + " you turn has ended", "End of turn");

        } else if (tag.equalsIgnoreCase(IO.PICKED_UP_ARTIFACT)) {
            display_PICKED_UP_ARTIFACT(message);

        } else if (tag.equalsIgnoreCase(IO.CHARACTER_MOVED)) {
            display_CHARACTER_MOVED(message);

        } else if (tag.equalsIgnoreCase(IO.USER_ERROR)) {
            displayError(message, "Error!");

        } else if (tag.equalsIgnoreCase((IO.CHARACTER_HEALTH_INCREASE)) || tag.equalsIgnoreCase(IO.CHARACTER_HEALTH_DECREASE)) {
            display_CHARACTER_HEALTH_CHANGED(message);

        } else if (tag.equalsIgnoreCase(IO.DIRECTION_LOCK_TOGGLED)) {
            display_DIRECTION_LOCK_TOGGLE(message);

        } else if (tag.equalsIgnoreCase(IO.CHARACTER_ATTACKED)) {
            display_CHARACTER_ATTACKED(message);

        } else if (tag.equalsIgnoreCase(IO.CHARACTER_DIED)){
            display_CHARACTER_DIED(message);
        }
    }

    //function to handle displaying a CHARACTER_DIED message
    private void display_CHARACTER_DIED(String message){
        displayMessage(message + " has died", "A character has died!");
        eventList.add(message + " has died"); 
        //reload place information to refresh the other characters pane
        loadPlaceInfo();
    }

    //function to handle displaying a CHARACTER_ATTACKED message
    private void display_CHARACTER_ATTACKED(String message) {
        String[] names = message.split(";");
        if (names[0].equalsIgnoreCase(currentCharacter.getName())) {
            displayMessage(currentCharacter.getName() + " you have attacked "
                    + names[2] + " with " + names[1], "You have attacked!");
        } else {
            eventList.add(String.format("%s has attacked %s with %s", names[0], names[2], names[1]));
        }
    }

    //function to handle displaying a DIRECTION_TOGGLE_MESSAGE
    private void display_DIRECTION_LOCK_TOGGLE(String message) {
        //simply display the message to the user
        String[] parameters = message.split(";");

        displayMessage(String.format("The direction %s from %s to %s had its locked toggle by %s"
                , parameters[2], parameters[1], parameters[3], parameters[0]), "Artifact used");
    }

    //function to handle displaying a LOAD_PLAYER message
    private void display_LOAD_PLAYER(String message) {
        int id = Integer.parseInt(message);
        currentCharacter = Character.getCharacterByID(id);
        loadPlayerInfo();
    }

    //a function to handle displaying a DROP_ARTIFACT message
    private void display_DROP_ARTIFACT(String message) {
        String[] parameters = message.split(";");
        if (parameters[0].equalsIgnoreCase(currentCharacter.getName())) {
            loadArtifactList();
            loadPlaceInfo();
            displayMessage(currentCharacter.getName()
                            + " you have dropped " + parameters[1] + " from your inventory"
                    , "You have drooped an artifact");
        }
    }

    //a function to handle displaying PICKED_UP_ARTIFACT messages
    private void display_PICKED_UP_ARTIFACT(String message) {
        String[] names = message.split(";");
        if (names[0].equalsIgnoreCase(currentCharacter.getName())) {
            //reload on-screen information
            loadPlaceInfo();
            loadArtifactList();
            displayMessage(currentCharacter.getName() + " you have picked up " + names[1],
                    "Artifact picked up");
        } else {
            eventList.add(names[0] + " has picked up " + names[1]);
        }
    }

    //a function to handle displaying CHARACTER_MOVED messages
    private void display_CHARACTER_MOVED(String message) {
        String[] names = message.split(";");
        if (names[0].equalsIgnoreCase(currentCharacter.getName())) {
            loadPlaceInfo();
            displayMessage(currentCharacter.getName() + " you have moved to "
                    + currentCharacter.getCurrentPlace().getName(), " You have moved");
        } else {
            //add message to be seen later
            String event = names[0] + " has moved to " + names[1] + " from " + names[2];
            eventList.add(event);
        }
    }

    //a function to handle displaying Character health change
    private void display_CHARACTER_HEALTH_CHANGED(String message) {
        String[] parameters = message.split(";");
        if (parameters[0].equalsIgnoreCase(currentCharacter.getName())) {
            displayMessage(currentCharacter.getName() + " you health has changed to " + parameters[1], "Health change");
            loadPlayerInfo();
        } else  {
            eventList.add(parameters[0] + " has his/her health changed to " + parameters[1]);
        }
    }

    public String getLine() {
        try {
            synchronized (communicationLock) {
                communicationLock.wait();
                System.out.println("Returning: " + returnString);
                return returnString;
            }
        } catch (InterruptedException exception) {
            displayError(exception.getMessage(), "Fatal Error!");
        }
        return null;
    }

}
