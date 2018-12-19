/**
 * Author: Uzair
 * John bell character is the opposite of the aggressive AI.
 * // He brings good fortune and will bless you with +10 health
 */
public class JohnBell extends AI {

    @Override
    public Move getMove(Character character, Place place) {
        //every player in a room that a John Bell character enters gains 10 health
        for (Character otherCharacters : place.getCharacters().values()) {
            if (!character.equals(otherCharacters))
                otherCharacters.increaseHealth(10);
        }
        System.out.println("John Bell has blessed everyone in " + place.getName());
        return null;
    }

}
