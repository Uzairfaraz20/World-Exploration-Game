import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Author: Uzair
 * Collaborator: Vineet
 * (Original data structure was ArrayList, changed to Map to maintain consistency with team code)
 * Kept same:
 * Abstract class to generally define a character in the game. Each character
 * will have a name and a description. The class already has concrete
 * implementations of constructors, static getCharacter(), print and display.
 * The subclasses must implement the makeMove function
 */
public abstract class Character {
    protected int ID;
    protected String name, description;
    protected Place currentPlace;
    protected Map<String, Artifact> artifacts;
    private int inventoryValue, inventoryMobility;
    protected DecisionMaker decisionMaker;
    private int health;
    private boolean isDead;

    protected static Map<Integer, Character> allCharacters =
            new HashMap<Integer, Character>();

    protected IO io;

    //scanner constructor
    protected Character(Scanner scanner, double version) {
        String line = Game.getCleanLine(scanner);
        @SuppressWarnings("resource")
        Scanner temp = new Scanner(line);
        int placeID = temp.nextInt();
        if (placeID > 0) {
            currentPlace = Place.getPlaceByID(placeID);
            if (currentPlace == null)
                throw new GDFSpecificationException("Invalid place ID of "
                        + placeID + "for a Character"); //throw custom exception of file specs
        } else if (placeID == 0)
            currentPlace = Place.getRandomPlace();
        else
            throw new GDFSpecificationException("Negative place ID (" +
                    placeID + ") given");

        //get the next line
        line = Game.getCleanLine(scanner);
        temp.close();
        temp = new Scanner(line);
        ID = temp.nextInt();
        this.name = temp.nextLine().trim();

        //get the next line
        line = Game.getCleanLine(scanner);
        temp.close();
        temp = new Scanner(line);
        int numLines = temp.nextInt();

        //get the description of the character.
        description = "";
        for (int i = 0; i < numLines; i++) {
            description += Game.getCleanLine(scanner);
        }

        this.artifacts = new HashMap<String, Artifact>();
        this.inventoryValue = 0;
        this.inventoryMobility = 0;
        allCharacters.put(this.ID, this);
        //add character to place
        currentPlace.addCharacter(this);
        this.health = 100; //every character by default starts out with 100 health
        io = IO.getInstance();
    }

    //constructor for manually entering in values
    protected Character(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.inventoryValue = 0;
        this.inventoryMobility = 0;
        this.artifacts = new HashMap<String, Artifact>();
        allCharacters.put(this.ID, this);
        this.currentPlace = Place.defaultStartPlace;
        this.health = 100; //default health of a 100
        io = IO.getInstance();
    }

    //gets character by ID given
    public static Character getCharacterByID(int ID) {
        return allCharacters.get(ID);
    }

    // getter to ID
    public int getID() {
        return ID;
    }

    // makes a move. Any subclass must implement this
    public abstract void makeMove();

    //prints out all information about the character
    public void print() {
        System.out.println("ID: " + ID + "\nName: " + name + "\nDescription: " + description);
    }

    //displays the name
    public void display() {
        System.out.println(name);
    }

    //adds artifact to character's inventory
    public void addArtifact(Artifact artifact) {
        artifacts.put(artifact.getName().toLowerCase(), artifact);
        inventoryMobility += artifact.getMobility();
        inventoryValue += artifact.getValue();
    }

    //removes the artifact from the character's inventory
    public void removeArtifact(Artifact artifact) {
        artifacts.remove(artifact.getName().toLowerCase());
        inventoryMobility -= artifact.getMobility();
        inventoryValue -= artifact.getValue();
    }

    //gets the artifact from the character's inventory
    public Artifact getArtifact(String name) {
        return artifacts.get(name.toLowerCase());
    }

    //gets the total value of the character's inventory
    public int getTotalInventoryValue() {
        return inventoryValue;
    }

    //gets the total mobility of the character's inventory
    public int getTotalInventoryMobility() {
        return inventoryMobility;
    }

    /**
     * @return A map containing artifacts, indexed by name
     */
    public Map<String, Artifact> getInventory() {
        return artifacts;
    }

    //returns character's name
    public String getName() {
        return name;
    }

    //returns a reference to the current place the character is in
    public Place getCurrentPlace() {
        return currentPlace;
    }

    //changes the place the character is in
    public void setCurrentPlace(Place place) {
        currentPlace = place;
    }

    //decrease or increase the health of the character
    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            die();
            return;
        }
        System.out.println(getName() + " has had his/her health decrease to " + health);
        io.display(IO.CHARACTER_HEALTH_DECREASE, String.format("%s;%s", getName(), health));
    }

    public void increaseHealth(int amount) {
        //do nothing if health is already maxed out
        if (health == 100)
            return;
        health += amount;
        //set a max health limit of 100
        if (health > 100)
            health = 100;
        System.out.println(getName() + "has had his/her health increased to " + health);
        io.display(IO.CHARACTER_HEALTH_INCREASE, String.format("%s;%s", getName(), health));
    }

    //removes the character from the game
    private void die() {
        //first remove all artifacts from the character
        for (Map.Entry<String, Artifact> entry : artifacts.entrySet()) {
            currentPlace.addArtifact(entry.getValue());
        }
        //then remove the character from the current place
        currentPlace.removeCharacter(this);
        //and then remove the character from the game
        Game.getGameInstance(null).removeCharacter(this);
        isDead = true;
        System.out.println("Character " + name + "has died");
        io.display(IO.CHARACTER_DIED, getName());
    }


    public boolean isDead() {
        return isDead;
    }

    //get the most powerful weapon in the character's inventory
    public Weapon getWeapon() {
        int maxDamage = 0;
        Weapon returnWeapon = null;
        for (Map.Entry<String, Artifact> entry : getInventory().entrySet()) {
            Artifact artifact = entry.getValue();
            if (artifact instanceof Weapon) {
                if (((Weapon) artifact).getDamageValue() > maxDamage)
                    returnWeapon = (Weapon) artifact;
            }
        }
        return returnWeapon;
    }

    public int getHealth() {
        return health;
    }

}

