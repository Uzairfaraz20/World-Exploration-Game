import java.util.Scanner;

/**
 * Collaborator: Affan
 * A Artifact that combats hunger while increasing health
 */
public class Food extends Artifact {

    private int healthValue;
    private int numUses;
    private int numUsesLeft;

    //constructor, sets the health value of the food to whatever the artifact value is
    //sets the default number of uses to 1
    public Food(Scanner scanner, double version) {
        super(scanner, version);
        healthValue = getKeyValue();
    }

    @Override
    public void use(Character character, Place place) {
        //uses the food to increase the player's health
        character.increaseHealth(healthValue);
        //remove artifact from player's inventory
        character.removeArtifact(this);
    }

    @Override
    public void display() {
        System.out.println("Artifact " + getName() + ", value: " + getName()
                + ", mobility: " + getMobility() + ", Nutritional Value: " + getKeyValue());
        System.out.println(getDescription());
    }
}
