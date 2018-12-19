/**
 * Author: Vineet
 * a move that once execute, will display the current place of a character and
 * its description
 */
public class LookMove implements Move{

    private Place currentPlace;

    /**
     * @param place that will be displayed
     */
    public LookMove(Place place){
        this.currentPlace = place;
    }

    public void execute() {
        currentPlace.look();
    }
}
