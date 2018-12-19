import java.util.Scanner;

public class DarkPlace extends Place {

    public DarkPlace(Scanner scanner, double version) {
        super(scanner, version);
    }

    //a dark place will not allow for a player to look

    @Override
    public void look() {
        System.out.println(getName() + " is too dark!");
    }
}
