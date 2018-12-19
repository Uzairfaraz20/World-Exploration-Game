import java.util.Scanner;

/**
 * Author: Affan
 * A Artifact that can either have positive or negative effects on health
 */
public class Potion extends Artifact {

    //constructor
    public Potion(Scanner scanner, double version) {
        super(scanner, version);
    }

    @Override
    public void use(Character character, Place place) {
        //use the potion
        int amount = getKeyValue();
        if (amount > 0) {
            character.increaseHealth(amount);
        } else if (amount < 0)
            character.decreaseHealth(amount);

        //remove the potion from player's inventory
        character.removeArtifact(this);
    }

    @Override
    public void display() {
        System.out.println("Artifact " + getName() + ", value: " + getName()
                + ", mobility: " + getMobility() + ", Health Effect: " + getKeyValue());
        System.out.println(getDescription());
    }
}
