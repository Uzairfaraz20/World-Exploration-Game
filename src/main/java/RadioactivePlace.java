import java.util.Scanner;

/**
 * Author: Vineet
 * A place that has a strong radioactive presence. Will decrease the health
 * of characters
 */
public class RadioactivePlace extends Place {

    public RadioactivePlace(Scanner scanner, double version) {
        super(scanner, version);
    }

    /**
     * A character in the radioactive place will lose 5 health every turn
     *
     * @param character
     */
    @Override
    public void applySpecialEffect(Character character) {
        character.decreaseHealth(5);
        io.display(IO.PLACE_SPECIAL_EFFECT, String.format("%s;%s", character.getName(),
                "lost health for being in a radioactive place"));
    }
}
