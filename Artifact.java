import java.util.Scanner;


public class Artifact {
    private int id, value, mobility, keyPattern;
    private String name, description;
    

    public Artifact(Scanner scanner, double version) {
        String line = Game.getCleanLine(scanner);
        //line 1 will be a D
        int idTrue = 0;
        Scanner temp = new Scanner(line);
        idTrue= temp.nextInt();
        //reads in new line
        line = Game.getCleanLine(scanner);
        readInfo(line);
         line = Game.getCleanLine(scanner);
        readDescription(line, scanner);
        //check version
        if (version >= 4) {
            if (idTrue < 0) {//character ID given
                Character character = Character.getCharacterByID(Math.abs(idTrue));
                character.addArtifact(this);
            } else if (idTrue > 0) {//place ID given
                Place p = Place.getPlaceByID(idTrue);

                p.addArtifact(this);
            } else {
                Place.getRandomPlace().addArtifact(this);
            }
        }else{
            Place place = Place.getPlaceByID(idTrue);
            place.addArtifact(this);
        }
    }

    private void readInfo(String line) {
        Scanner temp = new Scanner(line);
        id = temp.nextInt();
        value = temp.nextInt();
        mobility = temp.nextInt();
        keyPattern = temp.nextInt();
        name = temp.nextLine().trim();
        temp.close();
    }

    private void readDescription(String line, Scanner scanner) {
        Scanner temp = new Scanner(line);
        int numLines = temp.nextInt();
        for (int i = 0; i < numLines; i++) {
            String d = Game.getCleanLine(scanner);
            if (description == null)
                description = d + "\n";
            else
                description += d + "\n";
        }
    }


    
    public int getMobility() {
        return mobility;
    }
    
    public String getName() {
        return name;
    }
    public int getID() {
        return id;
    }
    
    public int getValue() {
        return value;
    }
    
    
    
    public int getKeyPattern() {
        return keyPattern;
    }
    public boolean match(String str) {
        return name.equalsIgnoreCase(str);
    }
    public int size() {
        return mobility;
    }
    public String getDescription() {
        return description;
    }
    
    public void use(Character character, Place place) {
        place.useArtifact(this);
    }
    public void print() {
    }

    public void display() {

    }
 }
