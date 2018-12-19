
/**
 * In general will make a decision for a character and return a move object
 * that can be executed.
 */
public interface DecisionMaker {

    Move getMove(Character C, Place p); //Declaration of getMove


}
