import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Author: Vineet
 * Represents a connection between two places
 */
public class Direction {

    private String direction;
    private int ID, lockPattern;
    private Place from, to;
    private boolean lock;
    private DirType type;
    private IO io;

    //enum to help with predefined directions
    private enum DirType {
        NONE("none", "none"),
        NORTH("north", "n"),
        SOUTH("south", "s"),
        EAST("east", "e"),
        WEST("west", "w"),
        UP("up", "u"),
        DOWN("down", "d"),
        NORTHEAST("northeast", "ne"),
        NORTHWEST("northwest", "nw"),
        SOUTHEAST("southeast", "se"),
        SOUTHWEST("southwest", "sw"),
        NORTHNORTHEAST("north-northeast", "nne"),
        NORTHNORTHWEST("north-northwest", "nnw"),
        EASTNORTHEAST("east-northeast", "ene"),
        WESTNORTHWEST("west-northwest", "wnw"),
        EASTSOUTHEAST("east-southeast", "ese"),
        WESTSOUTHWEST("west-southwest", "wsw"),
        SOUTHSOUTHEAST("south-southeast", "sse"),
        SOUTHSOUTHWEST("south-southwest", "ssw");

        private String text, abbreviation;

        DirType(String text, String abbreviation) {
            this.text = text;
            this.abbreviation = abbreviation;
        }

        @Override
        public String toString() {
            return text;
        }

        //checks if the given string matches either the full string or the
        //abbreviation
        public boolean match(String s) {
            return (s.equalsIgnoreCase(text) | s.equalsIgnoreCase(abbreviation));
        }
    }


    //constructor
    public Direction(Scanner scanner, double version) {
        String line = Game.getCleanLine(scanner);
        Scanner temp = new Scanner(line);
        ID = temp.nextInt();
        if (ID < 0)
            throw new GDFSpecificationException("Direction must have a non-negative ID. ID " + ID + " given");
        from = Place.getPlaceByID(temp.nextInt());

        try {
            temp.skip(" ");
        } catch (NoSuchElementException exception) {
            //do nothing
        }

        type = getDirType(temp.next());
        direction = type.toString();

        int destID = temp.nextInt();
        if (destID <= 0) {//if a negative ID was given
            lock = true;
            destID *= -1;
        } else
            lock = false;
        to = Place.getPlaceByID(destID);
        lockPattern = temp.nextInt();
        if (version >= 4.0 && lockPattern < 0)
            throw new GDFSpecificationException("Cannot have a negative lock pattern");

        if (to != null)
            from.addDirection(this);

        io = IO.getInstance();
    }

    //for testing purposes
    public Direction(int id, Place from, Place to, String dir) {
        this.ID = id;
        this.from = from;
        this.to = to;
        this.type = getDirType(dir);
        this.direction = type.toString();
        io = IO.getInstance();
    }


    /**
     * Gets the corresponding DirType for a string
     *
     * @param s: string
     * @return corresponding DirType
     */
    private DirType getDirType(String s) {
        s.trim(); //just in case
        //go through all the value of the DirType
        for (DirType dirType : DirType.values()) {
            if (dirType.match(s))
                return dirType;
        }
        return DirType.NONE;
    }

    /**
     * Checks if the given string matches the direction
     *
     * @param s: direction string
     * @return true if s matches direction, false otherwsise
     */
    public boolean match(String s) {
        if (type.match(s))
            return true;
        else
            return false;
    }

    //if the artifact matches the lock pattern, will toggle the lock on the direction
    public void useKey(Artifact artifact) {
        if (lockPattern == 0)
            return; //cannot unlock anything with a lock pattern of 0
        if (artifact.getKeyValue() == lockPattern) {
            lock = !lock;
            System.out.println(artifact.getName() + " used to toggle lock status of direction "
                    + direction + " from " + from.getName());
            io.display(IO.DIRECTION_LOCK_TOGGLED, String.format("%s;%s;%s;%s", artifact.getName(),
                    from.getName(), direction, to.getName()));
        }
    }

    /**
     * returns Place the direction leads to
     */
    public Place getTo() {
        return to;
    }

    /**
     * @return The origin of the direction
     */
    public Place getFrom() {
        return from;
    }

    /**
     * Locks the direction
     */
    public void lock() {
        lock = true;
    }

    /**
     * Unlocks the direction
     */
    public void unlock() {
        lock = false;
    }

    /**
     * @return boolean value depending if the direction is lcoked
     */
    public boolean isLocked() {
        return lock;
    }

    @Deprecated //gets the destination of the direction is unlocked.
    public Place follow() {
        if (lock) {
            return to;
        } else
            return from;
    }

    //prints out information
    public void print() {
        System.out.print("ID: " + ID);
        System.out.print(", From: " + from.name());
        System.out.print(", " + direction);

        if (to == null)
            System.out.print(", To: nowhere");
        else
            System.out.print(", To: " + to.name());

        if (lock)
            System.out.print(", Locked");
        else
            System.out.print(", Not Locked");

        System.out.print(", Lock Pattern: " + lockPattern);
        System.out.print("\n");
    }

    /**
     * For unit testing purposes
     *
     * @return standard string according to the DirType of the direction
     */
    public String getDirectionString() {
        return type.toString();
    }

    /**
     * @return direction string
     */
    public String getString() {
        return direction;
    }
}
