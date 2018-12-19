import java.util.Scanner;

/**
 * Author: Vineet
 * A place which will heal characters
 */
public class HealingPlace extends Place {

    public HealingPlace(Scanner scanner, double version) {
        super(scanner, version);
    }

    //a healing place will increase a character's health
    @Override
    public void applySpecialEffect(Character character) {
        character.increaseHealth(5);
        System.out.println(character.getName() + " is being healed by " + getName());
    }
}
