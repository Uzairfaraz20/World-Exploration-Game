
import java.util.Scanner;

/**
 * Author: Uzair
 * A character that will be controlled by an actual human being
 */
public class Player extends Character {



    //scanner constructor
    public Player(Scanner sc, double version) {
        super(sc, version);
        decisionMaker = new UI();
    }

    //manual input constructor 
    public Player(int id, String name, String description){
        super(id, name, description);
        decisionMaker = new UI();
    }

    //determines if the move is a move is one that ends the character's turn
    private boolean endTurn(Move move) {
        return move instanceof PassMove;
    }

    @Override //kept because of good format
    public void makeMove() {
        //notifies the UserInterface to display information about the player
        Move m;
        do {
            m = decisionMaker.getMove(this, currentPlace);
            m.execute();
        } while (!endTurn(m));
        io.display(IO.END_TURN, "Ending turn");
    }

}
