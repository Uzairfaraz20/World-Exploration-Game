
import java.util.Map;

/**
 * Author(s): Uzair and Vineet
 * Aggressive AI that "ambushes" all Players in a a place it, if it has a weapon
 * on hand. If it doesn't then, it will try to pick up a weapon. If there is no
 * weapon to pick up, it will try to move around if possible
 */
public class AggressiveAI extends AI {

    @Override
    public Move getMove(Character character, Place place) {
        if (hasWeapon(character)) {//can attack other players
            //attack the first player
            for (Map.Entry<Integer, Character> entry : place.getCharacters().entrySet()) {
                if (entry.getValue() instanceof Player)
                    return new AttackMove(character, getWeapon(character), entry.getValue());
            }
        } else {
            //try to pick up a weapon
            if (place.hasWeapon()) {
                for (Map.Entry<String, Artifact> entry : place.getArtifacts().entrySet()) {
                    Artifact artifact = entry.getValue();
                    if (artifact instanceof Weapon)
                        return new GetMove(character, place, artifact);
                }
            }
        }
        return getRandomGoMove(character, place); //if all else fails, randomly move
    }

    /**
     * checks if there is a weapon in the inventory
     *
     * @return boolean value
     */
    public boolean hasWeapon(Character character) {
        for (Map.Entry<String, Artifact> entry : character.getInventory().entrySet()) {
            if (entry.getValue() instanceof Weapon)
                return true;
        }
        return false;
    }

    /**
     * @param character from whom the weapon is from
     * @return the most dangerous weapon in the character's inventory
     */
    public Weapon getWeapon(Character character) {
        int maxDamage = 0;
        Weapon returnWeapon = null;
        for (Map.Entry<String, Artifact> entry : character.getInventory().entrySet()) {
            Artifact artifact = entry.getValue();
            if (artifact instanceof Weapon) {
                if (((Weapon) artifact).getDamageValue() > maxDamage)
                    returnWeapon = (Weapon) artifact;
            }
        }
        return returnWeapon;
    }
}
