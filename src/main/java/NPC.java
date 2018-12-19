import java.util.Scanner;

/**
 * Author: Uzair
 * A non-person character
 */
public class NPC extends Character {


    //manual input constructor 
    public NPC(int i, String n, String d) {
        super(i, n, d);
        decisionMaker = new AI();
    }

    //scanner constructor
    public NPC(Scanner sc, double version) {
        super(sc, version);
        decisionMaker = new AI();
    }

    public NPC(Scanner scanner, double version, DecisionMaker decisionMaker) {
        super(scanner, version);
        this.decisionMaker = decisionMaker;
    }

    @Override //kept because of good format
    public void makeMove() {
        Move move = decisionMaker.getMove(this, getCurrentPlace());
        if (move != null) {
            move.execute();

        } else
            System.out.println(name + " decided to stay at " + currentPlace.getName());
    }
}