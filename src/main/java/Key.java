import java.util.Scanner;

/**
 * Author: Affan
 * A artifact that can unlock places
 */
public class Key extends Artifact {

    public Key(Scanner scanner, double version) {
        super(scanner, version);
    }

    /**
     * Passes the artifact to the current place to unlock any directions it can
     */
    @Override
    public void use(Character character, Place place) {
        place.useKey(this);
    }
}
