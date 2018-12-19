
import java.util.Scanner;

/**
 * Author: Vineet
 * A class that handles getting input from the user on standard input
 */
public class UI implements DecisionMaker {

    //store parameters beforehand
    private Scanner sc;
    private Character character;
    private Place place;
    private IO io;

    //constrcutor
    public UI() {
        sc = KeyboardScanner.getKeyboardScanner();
        io = IO.getInstance();
    }

    //a function to handle the user input of go
    private Move go(String input[]) {
        //make sure the user entered a valid move command
        //figure out with direction is referenced
        if (input.length <= 1) { //if the user didn't specify a direction
            io.display(IO.USER_ERROR, "Please specify a place");
            return null;
        }
        String direction = input[1];
        Place newPlace = character.getCurrentPlace().followDirection(direction);
        if (newPlace == null) {
            if (character.getCurrentPlace().hasDirection(direction) &&       // see if it has a valid direction
                    character.getCurrentPlace().isDirectionLocked(direction)) {// and is not locked
                io.display(IO.USER_ERROR, "Cannot move because direction " + direction + " was locked");
            }
            io.display(IO.USER_ERROR, "Please try again");
            return null;
        }

        //perform Go because command is valid
        return new GoMove(character, newPlace);

    }

    //a function to handle the user input of look
    private Move look() {
        return new LookMove(place);
    }

    //a function to handle the user input of get
    private Move get(String[] input) {
        String artifactName = " ";
        if (input.length > 1) { //make sure user specified everything
            artifactName = input[1];
            for (int i = 2; i < input.length; i++) {
                artifactName = artifactName + " " + input[i];
            }
        } else {
            io.display(IO.USER_ERROR, "Please specify an artifact name to get");
            return null;
        }

        //check if exists
        Artifact artifact = character.getCurrentPlace().getArtifact(artifactName);
        if (artifact == null) {
            io.display(IO.USER_ERROR, "No artifact: " + artifactName + ", could be found");
            return null;
        }

        return new GetMove(character, place, artifact);
    }

    private Move inventory() { //do inventory
        return new InventoryMove(character);
    }

    //a function to handle user input of use
    private Move use(String input[]) {
        String artifactName = "";
        if (input.length > 1) {//check if all parameters were specified
            artifactName = input[1];
            for (int i = 2; i < input.length; i++) {
                artifactName = artifactName + " " + input[i];
            }
        } else {
            io.display(IO.USER_ERROR, "Please specify an artifact name to use");
            return null;
        }

        Artifact artifact = character.getArtifact(artifactName);
        if (artifact == null) {
            io.display(IO.USER_ERROR, "No artifact " + artifactName + " found");
            return null;
        }

        return new UseMove(artifact, character, place);
    }

    //a function to handle user input of drop
    private Move drop(String input[]) {
        String artifactName = " ";
        if (input.length > 1) { //make sure user specified everything
            artifactName = input[1];
            for (int i = 2; i < input.length; i++) {
                artifactName = artifactName + " " + input[i];
            }
        } else {
            io.display(IO.USER_ERROR, "Please specify an artifact name to drop");
            return null;
        }

        //get the artifact, if it exists
        Artifact artifact = character.getArtifact(artifactName);
        if (artifact == null) {
            io.display(IO.USER_ERROR, "No artifact: " + artifactName + ", could be found");
            return null;
        }

        return new DropMove(character, artifact);
    }

    //go a certain direction
    private Move goDirection(String[] input) {
        String direction = "";
        if (input.length >= 1) //if nothing was typed in
            direction = input[0];
        else {
            io.display(IO.USER_ERROR, "Please enter a valid command");
            return null;
        }
        Place newPlace = character.getCurrentPlace().followDirection(direction);
        if (newPlace == null) {
            io.display(IO.USER_ERROR, "There is no valid unlocked place in the direction: " + direction);
            io.display(IO.USER_ERROR, "Please try again");
            return null;
        }

        return new GoMove(character, newPlace);
    }

    private Move attack(String input[]) {
        String characterName = "";
        if (input.length > 1) {//check if all parameters were specified
            characterName = input[1];
            for (int i = 2; i < input.length; i++) {
                characterName = characterName + " " + input[i];
            }
        } else {
            io.display(IO.USER_ERROR, "Please specify an character to attack");
            return null;
        }

        Character victim = place.getCharacterByName(characterName);
        if (victim == null) {
            io.display(IO.USER_ERROR, "No character with name " + characterName + " in "
                    + place.getName());
            return null;
        }
        Weapon weapon = character.getWeapon();
        if (weapon == null) {
            io.display(IO.USER_ERROR, character.getName() + ", you don't have a weapon to attack with");
            return null;
        }
        return new AttackMove(character, weapon, victim);
    }

    public Move getMove(Character character, Place place) {
        this.character = character;
        this.place = place;

        Move move = null;
        while (move == null) {
            String input[] = (io.getLine().trim()).split(" ");
            if (input[0].equalsIgnoreCase("quit") ||
                    input[0].equalsIgnoreCase("exit")) {
                move = new ExitMove();

            } else if (input[0].equalsIgnoreCase("Look")) {
                move = look();

            } else if (input[0].equalsIgnoreCase("GET")) {
                move = get(input);

            } else if (input[0].equalsIgnoreCase("GO")) {
                move = go(input);

            } else if (input[0].equalsIgnoreCase("INVE") ||
                    input[0].equalsIgnoreCase("INVENTORY")) {
                move = inventory();

            } else if (input[0].equalsIgnoreCase("USE")) {
                move = use(input);

            } else if (input[0].equalsIgnoreCase("DROP")) {
                move = drop(input);

            } else if (input[0].equalsIgnoreCase("PASS")) {
                move = new PassMove();

            } else if (input[0].equalsIgnoreCase("ATTACK")) {
                move = attack(input);

            } else {
                move = goDirection(input);
            }
        }
        return move;
    }

    //does nothing in UI
    public void doAction(Character c, Place p) {

    }
}
