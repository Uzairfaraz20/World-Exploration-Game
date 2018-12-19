import java.util.Map;

/**
 * Author: Uzair
 * This AI is greedy. It will pickup all artifacts whether it needs them or not
 */
public class HoarderAI extends AI {

    @Override
    public Move getMove(Character character, Place place) {
        //check if there are any artifacts in the place
        //if so pick it up
        for (Map.Entry<String, Artifact> entry : place.getArtifacts().entrySet()) {
            return new GetMove(character, place, entry.getValue());
        }

        //if it didn't pick anything up, just randomly move around
        return getRandomGoMove(character, place);
    }
}