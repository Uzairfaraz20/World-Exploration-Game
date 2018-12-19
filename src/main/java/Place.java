import java.util.*;

/**
 * Author: Vineet
 * Representing a place. Keeps track of artifacts and characters that reside in th e
 * place
 */
public class Place {

    private static Map<Integer, Place> placesMap = new HashMap<Integer, Place>();

    /**
     * The default starting place for a character
     */
    public static Place defaultStartPlace;

    public static Place getPlaceByID(int id) {
        return placesMap.get(id);
    }

    /**
     * @return a random place from the collection of places
     */
    public static Place getRandomPlace() {
        Random generator = new Random();
        int randomValue = generator.nextInt(placesMap.entrySet().size() - 1);
        //make sure randomValue doesn't represent the id of the exit place
        while (randomValue == 1){
            randomValue = generator.nextInt(placesMap.entrySet().size() - 1);
        }

        int i = 0;

        for (Map.Entry<Integer, Place> entry : placesMap.entrySet()) {
            if (i == randomValue)
                return entry.getValue();
            i++;
        }
        return placesMap.entrySet().iterator().next().getValue();
    }

    private int ID;
    private String name;
    private String description;
    private List<Direction> directions;
    private Map<String, Artifact> artifactMap;
    private Map<Integer, Character> characters;
    protected IO io;

    //constructor
    public Place(Scanner scanner, double version) {
        directions = new ArrayList<Direction>();
        artifactMap = new HashMap<String, Artifact>();
        characters = new HashMap<Integer, Character>();

        //rea the first line
        String line = Game.getCleanLine(scanner);
        readIDName(line, version);

        //next line should be the number of descriptor lines
        line = Game.getCleanLine(scanner);
        readDescription(line, scanner);

        placesMap.put(ID, this);
        io = IO.getInstance();
    }

    //constructor that directly takes in values
    public Place(int ID, String name, String description) {
        directions = new ArrayList<Direction>();
        artifactMap = new HashMap<String, Artifact>();
        characters = new HashMap<Integer, Character>();

        this.ID = ID;
        this.name = name;
        this.description = description;
        placesMap.put(ID, this);
        io = IO.getInstance();
    }

    //parses the first line to get the ID and name
    private void readIDName(String line, double version) {
        Scanner temp = new Scanner(line);
        ID = temp.nextInt();
        name = temp.nextLine().trim();
        //make sure ID is valid
        if (ID == 0 || ID == 1) {
            throw new GDFSpecificationException("ID of " + ID + "is reserved, cannot use for " + name);
        }
        if (version >= 4.000 && ID < 0) {
            throw new GDFSpecificationException("\"Error! GDF version \" + version + \" does not allow for negative ID's.\"");
        }
        temp.close();
    }

    //reads the description of the place
    private void readDescription(String line, Scanner scanner) {
        Scanner temp = new Scanner(line);
        int num = temp.nextInt();
        for (int i = 0; i < num; i++) {
            String d = Game.getCleanLine(scanner);
            if (description == null)
                description = d + "\n";
            else
                description += d + "\n";
        }
        temp.close();
    }


    /**
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * @return id
     */
    public int getID() {
        return ID;
    }


    //returns description
    public String description() {
        return description;
    }

    //Adds a direction originating from this place
    public void addDirection(Direction direction) {
        //first check that the direction actual leads to someplace
        if (direction.getTo() == null)
            return;

        //first check if there are no duplicate directions
        for (Direction dir : directions) {
            if (dir.match(direction.getString()))
                return;
        }
        directions.add(direction);
    }

    /**
     * Adds artifacts to the place, to be picked up
     *
     * @param artifact to be added
     */
    public void addArtifact(Artifact artifact) {
        if (artifactMap.containsKey(artifact.getName().toLowerCase())) {
            return;
        }
        artifactMap.put(artifact.getName().toLowerCase(), artifact);
    }

    //gets artifact with specified name
    public Artifact getArtifact(String name) {
        return artifactMap.get(name.toLowerCase());
    }

    //add character to place
    public void addCharacter(Character character) {
        characters.put(character.getID(), character);
    }

    //remove character from place
    public void removeCharacter(Character character) {
        characters.remove(character.getID());
    }

    //for testing purposes
    public Map<Integer, Character> getCharacters() {
        return characters;
    }

    //for testing purposes
    public Map<String, Artifact> getArtifacts() {
        return artifactMap;
    }

    // Passes the artifact to all the direction from this place
    public void useKey(Artifact artifact) {
        for (Direction direction : directions) {
            direction.useKey(artifact);
        }
    }


    //For testing purposes only
    public boolean hasArtifact(String name) {
        if (artifactMap.get(name.toLowerCase()) == null)
            return false;
        else
            return true;
    }

    /**
     * Removes artifact from the place
     *
     * @param name of artifact to be removed
     */
    public void removeArtifactByName(String name) {
        artifactMap.remove(name.toLowerCase());
    }

    /**
     * Gets the place that the specified direction leads to
     *
     * @param direction to follow
     * @return THe place in the direction
     */
    public Place followDirection(String direction) {
        //find direction matching the string
        for (Direction dir : directions) {
            if (dir.match(direction)) {
                if (dir.isLocked())
                    return null;
                return dir.getTo();
            }
        }
        return null;
    }

    /**
     * @return A random that can be access through the current place. Mainly for
     * AI
     */
    public Place getRandomPlaceFrom() {
        if (directions.size() == 0)
            return null;  //there are no more places to go to
        int randomNum = new Random().nextInt(directions.size());
        if (!directions.get(randomNum).isLocked()) {
            return directions.get(randomNum).getTo();
        }
        //if the random didn't work, just get the first unlocked direction
        for (Direction dir : directions) {
            if (!dir.isLocked())
                return dir.getTo();
        }
        return null;
    }

    //checks if the direction is locked
    public boolean isDirectionLocked(String direction) {
        for (Direction dir : directions) {
            if (dir.match(direction)) {
                if (dir.isLocked())
                    return true;
                else
                    return false;
            }
        }
        return true;
    }


    // Checks if specified direction is present
    public boolean hasDirection(String direction) {
        for (Direction dir : directions) {
            if (dir.match(direction)) {
                return true;
            }
        }
        return false;
    }

    //prints out information
    public void print() {
        System.out.println(name + " (" + ID + ")");
        System.out.println(description);
        System.out.print("Artifacts present: ");
        for (String s : artifactMap.keySet()) {
            System.out.print(s + ", ");
        }
        System.out.println("\n");
        System.out.println("Directions: ");
        for (Direction direction : directions) {
            direction.print();
        }
        System.out.println("Characters present: ");
        for (Map.Entry<Integer, Character> entry : characters.entrySet()) {
            entry.getValue().print();
        }
    }

    public static void printAll() {
        for (Map.Entry<Integer, Place> entry : placesMap.entrySet()) {
            entry.getValue().print();
        }
    }

    //returns name
    public String getName() {
        return name;
    }

    public void display() {
        System.out.println(name + " (" + ID + ")");
        System.out.println(description);
        System.out.print("Artifacts present: ");
        for (String s : artifactMap.keySet()) {
            System.out.print(s + ", ");
        }
        System.out.print("\n"); //finish off the line
        System.out.println("Characters present: ");
        for (Map.Entry<Integer, Character> entry : characters.entrySet()) {
            entry.getValue().display();
        }
    }

    //if a place has a special effect, apply it to a character
    public void applySpecialEffect(Character character) {
    }

    //a LookMove object will call this function
    public void look() {
        this.display();
    }

    //function to check if the place has a weapon
    public boolean hasWeapon() {
        for (Map.Entry<String, Artifact> entry : artifactMap.entrySet()) {
            if (entry.getValue() instanceof Weapon)
                return true;
        }
        return false;
    }

    /**
     * @param name of the character
     * @return character variable
     */
    public Character getCharacterByName(String name) {
        for (Map.Entry<Integer, Character> entry : characters.entrySet()) {
            if (name.equalsIgnoreCase(entry.getValue().getName()))
                return entry.getValue();
        }

        return null;
    }

    /**
     * Gets a list of directions from the
     *
     * @return
     */
    public List<Direction> getDirections() {
        return directions;
    }


}