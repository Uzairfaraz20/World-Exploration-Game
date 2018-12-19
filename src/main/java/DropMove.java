/**
 * Author: Vineet
 * Represents a character dropping an artifact at a place
 */
public class DropMove implements Move {

    private Character character;
    private Artifact artifact;
    private IO io;

    /**
     * @param character that will do the dropping
     * @param artifact  that will be dropped
     */
    public DropMove(Character character, Artifact artifact) {
        this.character = character;
        this.artifact = artifact;
        io = IO.getInstance();
    }

    //removes the artifact from the character's inventory and puts in the
    //player's current place
    public void execute() {
        character.getCurrentPlace().addArtifact(artifact);
        character.removeArtifact(artifact);
        System.out.println(character.getName() + " has dropped " + artifact.getName());
        io.display(IO.DROP_ARTIFACT, String.format("%s;%s", character.getName(), artifact.getName()));
    }
}
