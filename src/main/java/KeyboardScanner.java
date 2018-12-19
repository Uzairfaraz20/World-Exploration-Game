import java.util.Scanner;

/**
 * Author: Vineet
 * A singleton encapsulating a scanner than gets input from the keyboard
 */
public class KeyboardScanner {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * @return scanner(System.in)
     */
    public static Scanner getKeyboardScanner(){
        return scanner;
    }
}
