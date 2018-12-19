import java.util.Map;

/**
 * Author: Vineet
 * Represents an inventory command
 */
public class InventoryMove implements Move {

    private Character character;

    /**
     * @param character character that will have it's inventory contents
     */
    public InventoryMove(Character character) {
        this.character = character;
    }

    public void execute() {
        System.out.println(character.getName() + "'s inventory: --------------");
        Map<String, Artifact> artifactMap = character.getInventory();
        for (Map.Entry<String, Artifact> entry : artifactMap.entrySet()) {
            entry.getValue().display();
        }
        System.out.println("Total Value: " + character.getTotalInventoryValue()
                + ", Total Mobility: " + character.getTotalInventoryMobility());
        System.out.println("-----------------------------------------------");
    }
}
