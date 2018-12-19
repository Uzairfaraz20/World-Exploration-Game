/**
 * Author: Move
 * Represents a move that a Character can make. Based off the command design
 * pattern. All the character has to do it call the execute method. Implementation
 * details are up to the subclass that implement the Move Interface.
 */
public interface Move {
    /**
     * Executes whatever the move does
     */
    public void execute();
}
