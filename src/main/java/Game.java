import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Author: Vineet
 * Stores all relevant information regarding the game. Also manages the game loop
 */
public class Game {
    private String name;
    private double version;
    private Map<String, Artifact> artifactsCollection;
    private List<Character> characters;
    private int currentCharecterIndex;
    private Scanner scanner;
    private int numPlayers; //number of Players
    private IO io;

    private static Game gameInstance;

    /**
     * A help function that will get the next line and clean it up
     *
     * @param sc scanner with the text file open
     * @return
     */
    public static String getCleanLine(Scanner sc) {
        if (!sc.hasNext())
            System.out.println("nothing left");
        while (true) {
            String line = sc.nextLine();
            //get everything before the //
            //check to see if the line has comments firt
            int commentIndex = line.indexOf("//");
            String truncatedLine = line;
            if (commentIndex >= 0)
                truncatedLine = line.substring(0, commentIndex);
            String trimmed = truncatedLine.trim(); //removed end spaces
            if (trimmed.length() > 0) //if there is anything there, return, or else loop
                return trimmed;
        }
    }

    public static Game getGameInstance(Scanner scanner) {
        if (gameInstance == null)
            gameInstance = new Game(scanner);
        return gameInstance;
    }


    public boolean isVersion(double ver) {
        return Math.abs(version - ver) < 1e-4;
    }

    //constructor
    private Game(Scanner scanner) {
        characters = new ArrayList<Character>();
        numPlayers = 0;
        new Place(1, "exit", "exit");
        this.scanner = scanner;

        //read the header line (first line)
        String line = getCleanLine(scanner);
        readHeader(line);

        if (version >= 4.0)
            readVersion4();
        else if (version >= 3.0)
            readVersion3();

        io = IO.getInstance();
        currentCharecterIndex = 0;
    }

    //a function to read the GDF according to version 4
    private void readVersion4() {
        //next section should be about places
        readPlaces();
        //next section should be about directions
        readDirections();
        //the next section should be about characters
        readCharacters();
        //the next section should be about artifacts
        readArtifacts();

        if (numPlayers == 0) //if the file didn't have any characters
            getPlayersFromUser();


    }

    private void readVersion3() {
        //read places
        readPlaces();
        //read directions
        readDirections();
        //read artifacts last
        readArtifacts();

        addPlayer();
    }

    //function to handle reading in the header the gdf file
    private void readHeader(String line) {
        Scanner tempScanner = new Scanner(line);
        String gdf = tempScanner.next();
        //check if the first word is GDF
        if (!gdf.equalsIgnoreCase("GDF"))
            throw new GDFSpecificationException("ERROR: GDF header not found in GDF fill");

        //get version number
        this.version = tempScanner.nextDouble();
        //truncate the double, to avoid any inconsistencies
        BigDecimal bigDecimal = new BigDecimal(this.version);
        System.out.println(this.version);
        bigDecimal.setScale(3, RoundingMode.DOWN);
        this.version = bigDecimal.doubleValue();

        this.name = tempScanner.nextLine().trim();
        tempScanner.close();
    }

    /**
     * function to handle all the reading in and creation of places
     * For gdp version 3+
     */
    private void readPlaces() {
        String line = getCleanLine(scanner);
        Scanner tempScanner = new Scanner(line);
        if (!tempScanner.next().equalsIgnoreCase("PLACES"))
            throw new GDFSpecificationException("Error: Places header missing");

        int numPlaces = tempScanner.nextInt();
        //get all the places by passing in scanner
        for (int i = 0; i < numPlaces; i++) {
            //if GDF version is 5+, then a place type would be specified
            Place place;
            if (version >= 5.0) {
                line = getCleanLine(scanner);
                tempScanner = new Scanner(line);
                place = getPlaceType(tempScanner.next());
            } else
                place = new Place(scanner, version);
            if (i == 0) //set the default place as the first place added
                Place.defaultStartPlace = place;
        }
    }

    private Place getPlaceType(String type) {
        Place place;
        if (type.equalsIgnoreCase("Normal"))
            place = new Place(scanner, version);
        else if (type.equalsIgnoreCase("Dark"))
            place = new DarkPlace(scanner, version);
        else if (type.equalsIgnoreCase("Healing"))
            place = new HealingPlace(scanner, version);
        else if (type.equalsIgnoreCase("Radioactive"))
            place = new RadioactivePlace(scanner, version);
        else if (type.equalsIgnoreCase("Safe"))
            place = new SafePlace(scanner, version);
        else if (type.equalsIgnoreCase("enhanced"))
            place = new EnhancedPlace(scanner, version);
        else
            throw new GDFSpecificationException("Invalid place type: " + type);
        return place;
    }

    //function to handle all the reading about directions
    private void readDirections() {
        //next clean line should be about directions
        String line = getCleanLine(scanner);
        Scanner tempScanner = new Scanner(line);
        if (!tempScanner.next().equalsIgnoreCase("DIRECTIONS")) //should be "DIRECTIONS"
            throw new GDFSpecificationException("Missing DIRECTIONS header");
        int numDirections = tempScanner.nextInt();

        for (int i = 0; i < numDirections; i++) {
            Direction direction = new Direction(scanner, version);
        }
    }

    //function to handle all the reading about characters
    private void readArtifacts() {
        String line = getCleanLine(scanner);
        Scanner tempScanner = new Scanner(line);
        if (!tempScanner.next().equalsIgnoreCase("ARTIFACTS"))
            return;
        int numArtifacts = tempScanner.nextInt();
        for (int i = 0; i < numArtifacts; i++) {
            Artifact artifact;
            if (version > 5.0)
                addArtifactType(version);
            else
                new Artifact(scanner, version);
        }
    }

    private void addArtifactType(double version) {
        String type = getCleanLine(scanner);
        if (type.equalsIgnoreCase("Weapon"))
            new Weapon(scanner, version);
        else if (type.equalsIgnoreCase("Food"))
            new Food(scanner, version);
        else if (type.equalsIgnoreCase("Flashlight"))
            new Flashlight(scanner, version);
        else if (type.equalsIgnoreCase("Key"))
            new Key(scanner, version);
        else if (type.equalsIgnoreCase("Potion"))
            new Potion(scanner, version);
        else if (type.equalsIgnoreCase("Normal"))
            new Artifact(scanner, version);
        else
            throw new GDFSpecificationException("Invalid Artifact type specified");
    }

    //function to handle all reading in characters
    private void readCharacters() {
        String line = getCleanLine(scanner);
        Scanner tempScanner = new Scanner(line);
        if (!tempScanner.next().equalsIgnoreCase("CHARACTERS"))
            return;

        int numCharacters = tempScanner.nextInt();
        for (int i = 0; i < numCharacters; i++) {
            //first read what type of character will be used
            String type;
            if (version > 5.0) {
                type = getCleanLine(scanner);
                addCharacterType5(type);
            } else {
                type = scanner.next();
                addCharacterType4(type);
            }
        }
    }

    //gets the character type, compiling with gdf version 4+
    private void addCharacterType4(String type) {
        if (type.equalsIgnoreCase("PLAYER")) {
            characters.add(new Player(scanner, version));
            numPlayers++;
        } else if (type.equalsIgnoreCase("NPC"))
            characters.add(new NPC(scanner, version));
        else
            throw new GDFSpecificationException("Invalid Character type of: " + type);
    }

    //gets the character type, compiling with gdf version 4+
    private void addCharacterType5(String type) {
        if (type.equalsIgnoreCase("PLAYER")) {
            characters.add(new Player(scanner, version));
            numPlayers++;
        } else if (type.equalsIgnoreCase("NPC"))
            characters.add(new NPC(scanner, version));
        else if (type.equalsIgnoreCase("Aggressive"))
            characters.add(new NPC(scanner, version, new AggressiveAI()));
        else if (type.equalsIgnoreCase("Passive"))
            characters.add(new NPC(scanner, version, new PassiveAI()));
        else if (type.equalsIgnoreCase("Hoarder"))
            characters.add(new NPC(scanner, version, new HoarderAI()));
        else if (type.equalsIgnoreCase("Bell"))
            characters.add(new NPC(scanner, version, new JohnBell()));
        else
            throw new GDFSpecificationException("Invalid character type of " + type);
    }

    public void getPlayersFromUser() {
        System.out.println("How many players will there be for this game?:");
        Scanner sc = KeyboardScanner.getKeyboardScanner();
        int num = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < num; i++) {
            addPlayer();
        }
    }

    /**
     * Called once, to let the first player start their turn
     */
    public void play() {
        boolean playing = true;
        while (playing) {
            for (Character character : characters) {
                if (character.isDead())
                    continue;
                if (character instanceof Player)
                    io.display(IO.LOAD_PLAYER, Integer.toString(character.getID()));
                //before a character makes a move, apply the place's special effect
                character.getCurrentPlace().applySpecialEffect(character);
                character.makeMove();
            }
        }
    }

    /**
     * Changes the turn to the next character
     */
    public void nextCharacterTurn() {
        currentCharecterIndex++;
        if (currentCharecterIndex >= characters.size())
            currentCharecterIndex = 0;

        characters.get(currentCharecterIndex).makeMove();
    }


    public void removeCharacter(Character character) {
        //check if a player was removed
        if (character instanceof Player)
            numPlayers--;
        if (numPlayers == 0) {
            System.out.println("All players have died, so game over");
            System.exit(0);
        }

    }

    //checks if a specific character ID is already in use.
    private boolean idTaken(int id) {
        for (Character character : characters) {
            if (character.getID() == id)
                return true;
        }
        return false;
    }

    //generates an unused character ID
    private int getUnusedCharacterID() {
        int id = 1;
        while (idTaken(id)) {
            id++;
        }
        return id;
    }

    //gets input from user to add more player
    public void addPlayer() {
        Scanner scanner = KeyboardScanner.getKeyboardScanner();
        System.out.println("Enter in a Player's name: ");
        String name = scanner.nextLine().trim();
        characters.add(new Player(getUnusedCharacterID(), name,
                "user inputted player"));
        numPlayers++;
    }

    //returns the number of players in the game
    public int getNumPlayers() {
        return numPlayers;
    }
}
