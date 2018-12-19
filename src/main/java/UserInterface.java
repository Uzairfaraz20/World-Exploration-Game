/**
 * Author: Vineet
 *  An interface that GUI's or text interfaces will implement to allow to a
 *  consistent way to communicate with other parts of the game
 */
public interface UserInterface {

    /**
     * Notify the user interface to display a message. The tag allows for identifying
     * different types of messages
     * @param tag
     * @param message
     */


    void display(String tag, String message);

    String getLine();

}
