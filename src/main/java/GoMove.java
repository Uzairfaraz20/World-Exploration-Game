/**
 * Author: Vineet
 * Represent a character from going to one place to another
 */
public class GoMove implements Move {

    private Character character;
    private Place currentPlace;
    private Place nextPlace;
    private IO io;

    /**
     * @param character that is doing the moving
     * @param nextPlace the place the character will move to.
     */
    public GoMove(Character character, Place nextPlace) {
        this.character = character;
        this.currentPlace = character.getCurrentPlace();
        this.nextPlace = nextPlace;
        io = IO.getInstance();
    }

    //checks if the destination is going to an exit
    private void checkIfExit() {
        if (nextPlace.getID() == 1) {
            io.display(IO.PLAYER_EXITED, character.getName());
            System.exit(0);
        }
    }

    public void execute() {
        checkIfExit();
        //first remove character from current place
        currentPlace.removeCharacter(character);
        nextPlace.addCharacter(character);
        character.setCurrentPlace(nextPlace);
        io.display(IO.CHARACTER_MOVED, String.format("%s;%s;%s", character.getName(), currentPlace.getName(), nextPlace.getName()));
    }
}
