import java.util.Scanner;


/**
 * Author: Vineet
 * class representing an artifact that can be in a place or be in a character's
 * inventory. Has value and mobility and can be used to unlcok directions.
 */
public class Artifact {

    private int id, value, mobility, keyValue;
    private String name, description;

    //constructor
    public Artifact(Scanner scanner, double version) {
        String line = Game.getCleanLine(scanner);

        //first line will be an D
        int idGiven = 0;
        Scanner temp = new Scanner(line);
        idGiven = temp.nextInt();


        //get the next line
        line = Game.getCleanLine(scanner);
        readInfo(line);

        line = Game.getCleanLine(scanner);
        readDescription(line, scanner);


        //now that the instance is set up, re-evaluate the first line if version 4.0+
        if (version >= 4.000) {
            if (idGiven < 0) {//character ID given
                Character character = Character.getCharacterByID(Math.abs(idGiven));
                if (character == null)//invalid ID was given
                    throw new GDFSpecificationException("Invalid character ID ("
                            + Math.abs(idGiven) + ") given");
                character.addArtifact(this);
            } else if (idGiven > 0) {//place ID given
                Place p = Place.getPlaceByID(idGiven);
                if (p == null)//invalid ID was given
                    throw new GDFSpecificationException("Invalid place ID ("
                            + idGiven + ") given");
                p.addArtifact(this);
            } else {
                Place.getRandomPlace().addArtifact(this);
            }
        } else {//version < 4.0
            Place place = Place.getPlaceByID(idGiven);
            if (place == null)
                throw new GDFSpecificationException("Invalid Place ID given");
            place.addArtifact(this);
        }
    }

    //reads the second line for general information
    private void readInfo(String line) {
        Scanner temp = new Scanner(line);
        id = temp.nextInt();
        value = temp.nextInt();
        mobility = temp.nextInt();
        keyValue = temp.nextInt();
        name = temp.nextLine().trim();
        temp.close();
    }

    //reads the description
    private void readDescription(String line, Scanner scanner) {
        Scanner temp = new Scanner(line);
        int numLines = temp.nextInt();
        for (int i = 0; i < numLines; i++) {
            String d = Game.getCleanLine(scanner);
            if (description == null)
                description = d + "\n";
            else
                description += d + "\n";
        }
    }

    //returns id
    public int getID() {
        return id;
    }

    //returns value
    public int getValue() {
        return value;
    }

    //retuns size
    public int size() {
        return mobility;
    }

    //return mobiliyu
    public int getMobility() {
        return mobility;
    }

    //returns name
    public String getName() {
        return name;
    }

    /**
     * @return descritpion
     */
    public String getDescription() {
        return description;
    }

    /**
     * Passes the artifact to the current place to unlock any directions it can
     * For GDF versions 5+, only subclass of place will be instantiated, with the
     * use function overrided
     */
    public void use(Character character, Place place) {
        place.useKey(this);
    }

    /**
     * For testing purposes
     *
     * @return key pattern
     */
    public int getKeyValue() {
        return keyValue;
    }

    public boolean match(String str) {
        return name.equalsIgnoreCase(str);
    }

    //prints information
    public void print() {
        System.out.print("ID: " + id);
        System.out.print("name: " + name);
        System.out.print(", value: " + value);
        System.out.print(", mobility: " + mobility);
        System.out.print(", keyValue: " + keyValue);
        System.out.println("\nDescription: " + description);
    }

    //displays information
    public void display() {
        System.out.println("Artifact " + name + ", value: " + value + ", mobility: " + mobility);
        System.out.println(description);
    }

}
