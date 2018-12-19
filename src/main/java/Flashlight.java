import java.util.Scanner;

/**
 * Author: Affan
 * Artifact that allows a character to see the contents of a place
 */
public class Flashlight extends Artifact {

    //constructor
    public Flashlight(Scanner scanner, double version) {
        super(scanner, version);
    }

    //the use of the flashlight is allowing the user to look around a dark room
    @Override
    public void use(Character character, Place place) {
        //allows the character to look around in a dark area
        character.getCurrentPlace().display();
    }



}
