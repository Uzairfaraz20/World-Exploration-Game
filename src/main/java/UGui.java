import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Uzair Mohammed
 * Uzair's UI
 */
public class UGui extends JFrame implements UserInterface {
	
    private String returnValue;
    private Character currentPlyrCharacter;
    private JProgressBar plyrHlthStatus;
    private List<String> activityList;
    public static final Object interactionLock = new Object();

    // gui controls
    private GridBagConstraints constraints;
    private JLabel playerLabel, placeLabel;
    private JTextArea displayArea;
    private Map<TextAttribute, Object> headAttributes,  subheadAttributes;
    private DefaultListModel placesDM, artifactInPlaceDM, inventoryDM, othersDM;
    private JList placesList, artifactInPlaceList, artifactInvList, othersList;
    private JScrollPane placesPane, artifactInPlacePane, inventoryPane, othersPane;
    private JButton placeBtn, nextPlyrTurnBtn, getArtBtn, useArtBtn, dropArttBtn, attackBtn;

	public UGui() {
        returnValue = " ";
        layoutGui();
    }
	
    private void layoutGui() {
        activityList = new ArrayList<String>();
        this.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 3, 6, 3);
        
        initFontAttributes();
        setFirstRow();
        setPlaceRow();
        setArtifactRow();
        setInventoryRow();
        setOthersRow() ;
        //setTextArea() ;
        makeVisible();

    }
    
    private void initFontAttributes() {
        headAttributes = new HashMap<TextAttribute, Object>();
        headAttributes.put(TextAttribute.FAMILY, Font.DIALOG);
        headAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        headAttributes.put(TextAttribute.SIZE, 18);
        
        subheadAttributes = new HashMap<TextAttribute, Object>();
        subheadAttributes.put(TextAttribute.FAMILY, Font.DIALOG);
        subheadAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_DEMIBOLD);
        subheadAttributes.put(TextAttribute.SIZE, 16);    	
    }
    
    private void setFirstRow() {
    	
    	 JPanel  headPanel = new JPanel ();
         headPanel.setBackground(Color.orange);
         headPanel.setForeground(Color.blue);
         constraints.gridheight = 2;
         constraints.gridwidth = 6;
         constraints.gridx = 0;
         constraints.gridy = 0;
         constraints.anchor = GridBagConstraints.EAST;
         add(headPanel, constraints);

         // health row 
         JLabel label = new JLabel("Player's Health Meter: ");
         constraints.gridheight = 1;
         constraints.gridwidth = 1;
         constraints.gridx = 0;
         constraints.gridy = 0;
         label.setFont(Font.getFont(headAttributes));
         headPanel.add(label, constraints);

         plyrHlthStatus = new JProgressBar();
         plyrHlthStatus.setMaximum(100);
         constraints.gridheight = 2;
         constraints.gridwidth = 5;
         constraints.gridx = 1;
         constraints.gridy = 0;
         headPanel.add(plyrHlthStatus, constraints);
         

         playerLabel = new JLabel();
         constraints.gridheight = 1;
         constraints.gridwidth = 1;
         constraints.gridx = 3;
         constraints.gridy = 0;
         constraints.anchor = GridBagConstraints.EAST;
         playerLabel.setFont(Font.getFont(headAttributes));
         headPanel.add(playerLabel, constraints);
         
          label = new JLabel("is located in Room ");
          constraints.gridheight = 1;
          constraints.gridwidth = 1;
         constraints.gridx = 4;
         constraints.gridy = 0;
         constraints.anchor = GridBagConstraints.WEST;
         label.setFont(Font.getFont(headAttributes));
         headPanel.add(label, constraints);

         placeLabel = new JLabel();
         constraints.gridheight = 1;
         constraints.gridwidth = 1;
         constraints.gridx = 5;
         constraints.gridy = 0;
         placeLabel.setFont(Font.getFont(headAttributes));
         headPanel.add(placeLabel, constraints);

         nextPlyrTurnBtn = new JButton("End Turn");
         constraints.gridheight = 1;
         constraints.gridwidth = 1;
         constraints.gridx = 6;
         constraints.gridy = 0;
         nextPlyrTurnBtn.setFont(Font.getFont(headAttributes));
         headPanel.add(nextPlyrTurnBtn, constraints);
         nextPlyrTurnBtn.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 returnValue = "PASS";
                 synchronized (interactionLock) {
                     interactionLock.notifyAll();
                 }
             }
         });       
         
    }
    
    
    
    private void setPlaceRow() {
        //Place

        JPanel  placePanel = new JPanel ();
        placePanel.setBackground(Color.orange);
        placePanel.setForeground(Color.blue);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(placePanel, constraints);
        
        JLabel label = new JLabel("To go to a place ");
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        label.setFont(Font.getFont(subheadAttributes));
        placePanel.add(label, constraints);

        
        placesDM = new DefaultListModel();
        placesList = new JList(placesDM);
        placesPane = new JScrollPane(placesList);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.8;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        placesList.setFont(Font.getFont(subheadAttributes));
        add(placesPane, constraints);
        constraints.weighty = 0;
        
        placeBtn = new JButton("Choose place");
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;
        placeBtn.setFont(Font.getFont(headAttributes));
        placePanel.add(placeBtn, constraints);
        placeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (placesList.isSelectionEmpty()) {
                    showErrorDialog("No place selected to move to", "Cannot move");
                    return;
                }
                String placeSelected = placesList.getSelectedValue().toString().split(" -")[1];
                returnValue = "GO " + placeSelected;
                synchronized (interactionLock) {
                    interactionLock.notifyAll();
                }
            }
        });
       
    }
    
    private void setArtifactRow() {
        //Artifact

        JPanel  artPanel = new JPanel ();
        artPanel.setBackground(Color.orange);
        artPanel.setForeground(Color.blue);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(artPanel, constraints);
        
        JLabel label = new JLabel("These are the Artifacts Present in the room, Select one to ");
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        label.setFont(Font.getFont(subheadAttributes));
        artPanel.add(label, constraints);
        constraints.gridwidth = 1;

        getArtBtn = new JButton("Get an Artifact");
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        getArtBtn.setFont(Font.getFont(headAttributes));
        artPanel.add(getArtBtn, constraints);
        getArtBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (artifactInPlaceList.isSelectionEmpty()) {
                    showErrorDialog("No artifact was selected", "Cannot perform a get");
                    return;
                }
                String artifactSelected = artifactInPlaceList.getSelectedValue().toString();
                returnValue = "GET " + artifactSelected;
                synchronized (interactionLock) {
                    interactionLock.notifyAll();
                }
            }
        });

        artifactInPlaceDM = new DefaultListModel();
        artifactInPlaceList = new JList(artifactInPlaceDM);
        artifactInPlacePane = new JScrollPane(artifactInPlaceList);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0.8;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        artifactInPlaceList.setFont(Font.getFont(subheadAttributes));
        add(artifactInPlacePane, constraints);
        constraints.weighty = 0.0;    	
    }
    
    private void setInventoryRow() {
        // Inventory
        
        JPanel  invPanel = new JPanel ();
        invPanel.setBackground(Color.orange);
        invPanel.setForeground(Color.blue);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 5;
        add(invPanel, constraints);
        
        JLabel label = new JLabel("With the below Inventory, You can");
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 0;
        label.setFont(Font.getFont(subheadAttributes));
        invPanel.add(label, constraints);

        dropArttBtn = new JButton("Drop an Artifact");
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;
        dropArttBtn.setFont(Font.getFont(headAttributes));
        invPanel.add(dropArttBtn, constraints);
        dropArttBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (artifactInvList.isSelectionEmpty()) {
                    showErrorDialog("No artifact was selected", "Cannot perform a drop");
                    return;
                }
                String artifactSelected = artifactInvList.getSelectedValue().toString();
                returnValue = "DROP " + artifactSelected;
                synchronized (interactionLock) {
                    interactionLock.notifyAll();
                }
            }
        });
        
        label = new JLabel("or You can ");
        constraints.gridx = 2;
        constraints.gridy = 0;
        label.setFont(Font.getFont(subheadAttributes));
        invPanel.add(label, constraints);

        useArtBtn = new JButton("Use an Artifact");
        constraints.gridwidth = 2;
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        useArtBtn.setFont(Font.getFont(headAttributes));
        invPanel.add(useArtBtn, constraints);
        useArtBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (artifactInvList.isSelectionEmpty()) {
                    showErrorDialog("No artifact was selected", "Cannot use artifact");
                    return;
                }
                String artifactSelected = artifactInvList.getSelectedValue().toString();
                returnValue = "USE " + artifactSelected;
                synchronized (interactionLock) {
                    interactionLock.notifyAll();
                }
            }
        });        
        
        inventoryDM = new DefaultListModel();
        artifactInvList = new JList(inventoryDM);
        inventoryPane = new JScrollPane(artifactInvList);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.weightx = 0.8;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        artifactInvList.setFont(Font.getFont(subheadAttributes));
        add(inventoryPane, constraints);
        constraints.weighty = 0.0;
    	
    }
    
    private void setOthersRow() {
       // Other characters
        
        JPanel  otherPanel = new JPanel ();
        otherPanel.setBackground(Color.orange);
        otherPanel.setForeground(Color.blue);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 7;
        add(otherPanel, constraints);
        
        JLabel label = new JLabel("These are the Other characters present, be nice and don't ");
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        label.setFont(Font.getFont(subheadAttributes));
        otherPanel.add(label, constraints);

        attackBtn = new JButton("Attack");
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        attackBtn.setFont(Font.getFont(headAttributes));
        otherPanel.add(attackBtn, constraints);
        attackBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (othersList.isSelectionEmpty()) {
                    showErrorDialog("No character was selected to attack", "Cannot attack");
                    return;
                }
                String characterSelected = othersList.getSelectedValue().toString();
                returnValue = "ATTACK " + characterSelected;
                synchronized (interactionLock) {
                    interactionLock.notifyAll();
                }
            }
        });
        
        othersDM = new DefaultListModel();
        othersList = new JList(othersDM);
        othersPane = new JScrollPane(othersList);
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.weighty = 0.7;
        othersList.setFont(Font.getFont(subheadAttributes));
        add(othersPane, constraints);
        constraints.weighty = 0;
        constraints.gridwidth = 1;
    }
    
    private void setTextArea() {
    	displayArea = new JTextArea("");
        displayArea.setBackground(Color.YELLOW);
        displayArea.setForeground(Color.blue);
        constraints.gridheight = 1;
        constraints.gridwidth = 6;
        constraints.gridx = 0;
        constraints.gridy = 9;
        displayArea.setSize(1000, 100);
        displayArea.setWrapStyleWord(true);
        displayArea.setAutoscrolls(true);
        displayArea.setLineWrap(true);
        displayArea.setEditable(false);
        displayArea.setFont(Font.getFont(subheadAttributes));
        this.add(displayArea, constraints);  	
    }

    private void makeVisible() {
        this.setTitle("Uzair's GUI");
        this.setSize(1780, 1080);
        this.setLocation(100, 10);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    
    private void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.WARNING_MESSAGE);
    }
   
    
    private void displayMessageDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        //displayArea.setText(message);
    }


    private void loadArtifactList() {
        System.out.println("Loading Artifact list");
        Map<String, Artifact> inventory = currentPlyrCharacter.getInventory();
        inventoryDM.clear();
        for (Map.Entry<String, Artifact> entry : inventory.entrySet()) {
            System.out.println("Adding " + entry.getKey());
            inventoryDM.addElement(entry.getKey());
        }
        artifactInvList.setModel(inventoryDM);
        inventoryPane.revalidate();
    }

    private void loadPlaceInfo() {
        artifactInPlaceDM.removeAllElements();
        Place place = currentPlyrCharacter.getCurrentPlace();
        placeLabel.setText(place.getName());
        for (Map.Entry<String, Artifact> entry : place.getArtifacts().entrySet()) {
            artifactInPlaceDM.addElement(entry.getKey());
        }

        placesDM.clear();
        List<Direction> directionList = place.getDirections();
        for (Direction direction : directionList) {
            String s = direction.getTo().getName() + " -" + direction.getDirectionString().toUpperCase();
            placesDM.addElement(s);
        }

        othersDM.clear();
        Map<Integer, Character> characterMap = currentPlyrCharacter.getCurrentPlace().getCharacters();
        for (Map.Entry<Integer, Character> characterEntry : characterMap.entrySet()) {
            if (currentPlyrCharacter.getID() != characterEntry.getKey()) {
                othersDM.addElement(characterEntry.getValue().getName());
            }
        }
    }

    private void loadPlayerInfo() {
        playerLabel.setText(currentPlyrCharacter.getName());
        placeLabel.setText(currentPlyrCharacter.getCurrentPlace().getName());
        loadArtifactList();
        loadPlaceInfo();
        plyrHlthStatus.setValue(currentPlyrCharacter.getHealth());
        displayMessageDialog(currentPlyrCharacter.getName() + " it is now your turn", "Your move");
        showOtherActivities();
    }

    // moves 

    private void characterMoved(String message) {
        String[] msgValues = message.split(";");
        if (msgValues[0].equalsIgnoreCase(currentPlyrCharacter.getName())) {
            loadPlaceInfo();
            displayMessageDialog(currentPlyrCharacter.getName() + " you have moved to "
                    + currentPlyrCharacter.getCurrentPlace().getName(), " Character moved");
        } else {
            String event = msgValues[0] + " has moved to " + msgValues[1] + " from " + msgValues[2];
            activityList.add(event);
        }
    }

    private void loadPlayer(String message) {
        int id = Integer.parseInt(message);
        currentPlyrCharacter = Character.getCharacterByID(id);
        loadPlayerInfo();
    }

    private void pickArtifact(String message) {
        String[] msgValues = message.split(";");
        if (msgValues[0].equalsIgnoreCase(currentPlyrCharacter.getName())) {
            loadPlaceInfo();
            loadArtifactList();
            displayMessageDialog(currentPlyrCharacter.getName() + " you have picked up " + msgValues[1],
                    "Artifact picked");
        } else {
            activityList.add(msgValues[0] + " has picked up " + msgValues[1]);
        }
    }

    private void dropArtifact(String message) {
        String[] msgValues = message.split(";");
        if (msgValues[0].equalsIgnoreCase(currentPlyrCharacter.getName())) {
            loadArtifactList();
            loadPlaceInfo();
            displayMessageDialog(currentPlyrCharacter.getName()
                            + " you have dropped " + msgValues[1] + " from your inventory"
                    , "Artifact drooped");
        }
    }

    private void attackCharacter(String message) {
        String[] msgValues = message.split(";");
        if (msgValues[0].equalsIgnoreCase(currentPlyrCharacter.getName())) {
            displayMessageDialog(currentPlyrCharacter.getName() + " Man, you have attacked "
                    + msgValues[2] + " with " + msgValues[1], "Character attack");
        } else {
            activityList.add(String.format("%s has attacked %s with %s", msgValues[0], msgValues[2], msgValues[1]));
        }
    }
    
    private void chracterKilled(String message){


        displayMessageDialog(message + " died", "Character Died");
        activityList.add(message + " died"); 
        loadPlaceInfo();
    }
    
    private void toggleDirectionLocked(String message) {
        String[] msgValues = message.split(";");
        displayMessageDialog(String.format("The direction %s from %s to %s had its locked toggle by %s"
                , msgValues[2], msgValues[1], msgValues[3], msgValues[0]), "Artifact used");
    }



    // health monitor
    
    private void healthUpdateForCharacter(String message) {
        String[] msgValues = message.split(";");
        if (msgValues[0].equalsIgnoreCase(currentPlyrCharacter.getName())) {
            displayMessageDialog(currentPlyrCharacter.getName() + " you health has changed to " + msgValues[1], "Health changed");
            loadPlayerInfo();
        } else  {
            activityList.add(msgValues[0] + " has his/her health changed to " + msgValues[1]);
        }
    }

    // show  activities
    
    private void showOtherActivities() {
        if (activityList.size() == 0) {
            return;
        }
        String message = "";
        for (String msg : activityList) {
            message += msg + "\n";
        }
        displayMessageDialog(message, "Let's see, who moved while we were busy ");
        activityList.clear();
    }


    // interface impl
    
    public void display(String tag, String message) {

        if (tag.equalsIgnoreCase(IO.LOAD_PLAYER)) {
            loadPlayer(message);

        } else if (tag.equalsIgnoreCase(IO.PICKED_UP_ARTIFACT)) {
            pickArtifact(message);

        } else if (tag == IO.DROP_ARTIFACT) {
            dropArtifact(message);

        } else if (tag.equalsIgnoreCase(IO.CHARACTER_MOVED)) {
            characterMoved(message);

        } else if (tag.equalsIgnoreCase(IO.DIRECTION_LOCK_TOGGLED)) {
            toggleDirectionLocked(message);

        } else if (tag.equalsIgnoreCase(IO.CHARACTER_ATTACKED)) {
            attackCharacter(message);

        } else if (tag.equalsIgnoreCase((IO.CHARACTER_HEALTH_INCREASE)) || tag.equalsIgnoreCase(IO.CHARACTER_HEALTH_DECREASE)) {
            healthUpdateForCharacter(message);

        } else if (tag.equalsIgnoreCase(IO.CHARACTER_DIED)){
            chracterKilled(message);

        } else if (tag.equalsIgnoreCase(IO.USER_ERROR)) {
            showErrorDialog(message, "Error!");

        } else if (tag.equalsIgnoreCase(IO.END_TURN)) {
            displayMessageDialog(currentPlyrCharacter.getName() + " you turn has ended", "End of turn");
        }
    }

    public String getLine() {
        try {
            synchronized (interactionLock) {
                interactionLock.wait();
                System.out.println("Returning: " + returnValue);
                return returnValue;
            }
        } catch (InterruptedException exception) {
            showErrorDialog(exception.getMessage(), "Oh no. I dont work :(");
        }
        return null;
    }
	
	
}