/**
 * Author: Vineet
 * Represents a character using a artifact at a place
 */
public class UseMove implements Move {

    private Artifact artifact;
    private Character character;
    private Place place;

    /**
     * @param artifact that will be used
     * @param character the character that is using it
     * @param place at where the character is using the artifact
     */
    public UseMove(Artifact artifact, Character character, Place place){
        this.artifact = artifact;
        this.character = character;
        this.place = place;
    }

    public void execute() {
        artifact.use(character, place);
        System.out.println(character.getName() + " has used " + artifact.getName());
    }
}
