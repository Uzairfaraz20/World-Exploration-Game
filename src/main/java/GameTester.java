import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Runs the game for you
 * Author: Vineet Patel
 */
public class GameTester {


    public static void main(String[] args) {
        System.out.println("Vineet Patel, vpate43");
        System.out.println("Uzair Mohammed, umoham4");
        System.out.println("Affan Farid, afarid4");


        Game game;

        boolean numPlayersSpecified = (args.length > 1);
        int numPlayers = -1;
        if (numPlayersSpecified)
            numPlayers = Integer.parseInt(args[1]);
        String filename;

        try {
            filename = args[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("No command line argument given, please give a filename");
            Scanner scan = new Scanner(System.in);
            filename = scan.nextLine().trim();
        }
        File file;
        boolean prompt = true;
        while (prompt) {
            Scanner scanner;
            try {
                file = new File(filename);
                scanner = new Scanner(file);
                try {
                    game = Game.getGameInstance(scanner);
                    //check if a second command args was provided and it if the file satisfies it
                    if (numPlayersSpecified && game.getNumPlayers() < numPlayers) {
                        //more players need to be added
                        System.out.println("You need to enter in more players");
                        while (game.getNumPlayers() < numPlayers) {
                            game.addPlayer();
                        }
                    }
                    game.play();
                    prompt = false;
                } catch (GDFSpecificationException exception) {
                    System.out.println("GDF Specification Error:");
                    System.out.println(exception.getMessage());
                    System.out.println("You may try entering another file");
                    System.exit(1);
                }
            } catch (FileNotFoundException exception) {
                System.out.println("File not found, enter in another one:");
                Scanner scan = new Scanner(System.in);
                filename = scan.nextLine().trim();
                file = new File(filename);
            }
        }
    }
}
