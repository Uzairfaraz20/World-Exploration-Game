/**
 * Author: Vineet
 * Represents exiting a game. Will end the game if executed
 */
public class ExitMove implements Move{

    public void execute() {
        System.out.println("Exiting game");
        System.exit(0 );
    }
}
