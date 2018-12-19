/**
 * Author: Vineet Patel
 * Class to handle input/out services. Implemented as a singleton, as all the
 * classes that have an IO variable, are actually referencing the same IO object
 */
public class IO {

    public static final int TEXT_INTERFACE = 0, VGUI_INTERFACE = 1, UGUI_INTERFACE = 2, GUI_3 = 3;

    //pre-defined TAGS
    public static final String LOAD_PLAYER = "<Player name to load>";
    public static final String DROP_ARTIFACT = "<Character name>;<name of artifact dropped>";
    public static final String END_TURN = "End turn for current player";
    public static final String PICKED_UP_ARTIFACT = "<Character name>;<name of artifact picked up>";
    public static final String CHARACTER_MOVED = "<name of character that has moved>;<old place>;<newplace>";
    public static final String USER_ERROR = "error that the user made";
    public static final String CHARACTER_HEALTH_DECREASE = "<Character name>;<new health after decrease>";
    public static final String CHARACTER_HEALTH_INCREASE = "<Character name>;><new health after increase>";
    public static final String DIRECTION_LOCK_TOGGLED = "<Artifact used>;<place from>;<direction>;<place to>";
    public static final String CHARACTER_ATTACKED = "<attacker>;<weapon name><artifact>";
    public static final String CHARACTER_DIED = "<name of character who has died>";
    public static final String PLACE_SPECIAL_EFFECT = "<name of character>;<message>>";
    public static final String PLAYER_EXITED = "<name of player who has exited>";

    //singleton implementation
    private static IO instance = null;

    public static IO getInstance() {
        if (instance == null)
            instance = new IO();
        return instance;
    }

    private UserInterface userInterface;


    //private default constructor
    private IO() {
        userInterface = new UGui();
    }

    //Provide output based on the string provided
    public void display(String tag, String message) {
        try {
            userInterface.display(tag, message);
        } catch (Exception ex) {
            System.out.println("There was an error:");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            userInterface.display(USER_ERROR, ex.getMessage() + ex.getStackTrace().toString());
        }
    }

    //gets input from users
    public String getLine() {
        return userInterface.getLine();
    }

    //Employs the Bridge design pattern to select among available user interface
    public void selectInterface(int defined_interface) {
        switch (defined_interface) {
        case VGUI_INTERFACE:
            userInterface = new VGui();
    
        	break;
        case UGUI_INTERFACE:
            userInterface = new UGui();
        	break;
        }
    }


}
