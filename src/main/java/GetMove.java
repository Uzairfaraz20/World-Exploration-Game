/**
 * Author: Vineet
 * Represents a character getting an artifact from a place
 */
public class GetMove implements Move {

    private Character character;
    private Place place;
    private Artifact artifact;
    private IO io;

    /**
     * @param character that will get the artifact
     * @param place     from where the artifact is coming from
     * @param artifact  that will be added to the character's collection
     */
    public GetMove(Character character, Place place, Artifact artifact) {
        this.character = character;
        this.place = place;
        this.artifact = artifact;
        io = IO.getInstance();
    }

    public void execute() {
        //remove the artifact from th place
        place.removeArtifactByName(artifact.getName());
        //add artifact to character
        character.addArtifact(artifact);
        io.display(IO.PICKED_UP_ARTIFACT, String.format("%s;%s", character.getName(), artifact.getName()));
    }
}
