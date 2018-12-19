import java.util.Map;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * Artifact that can be used to attack
 * Author: Affan
 * Collaborator: Vineet
 */
public class Weapon extends Artifact {
	
    private int attackValue;

    //constructor, sets the attackvalue of the weapon to the artifact value	
    public Weapon(Scanner scanner, double version) {
        super(scanner, version);
        attackValue = getValue();
    }

    //uses the weapon, charecter is the target, and place is the location
    @Override
    public void use(Character character, Place place) {
        //check if the place is enhanced
        if (place instanceof EnhancedPlace) //increases damage by 25%
            character.decreaseHealth((int) (getKeyValue() * 1.25));
        else {
        	character.decreaseHealth(getKeyValue());
        }
            
    }
    
	//uses it on all the other charecters in the room
	//parameter 1 is the user, place parameter is the location 
	public void useOnAll(Character c, Place p1 ) { 
		
		//check the room, if someone else is in the room, then attack them
		Map<Integer, Character> chars = p1.getCharacters();
		for (Character value : chars.values()) {
		    //doesnt attack the user 	
		    if(!value.equals(c)) {
			//checks if the place is enhanced, does double the damage
		    	if(p1 instanceof EnhancedPlace) {
		    		value.decreaseHealth(attackValue*2);
		    	}
		    	else {
		    		value.decreaseHealth(attackValue);
		    	}
		    	
		    }
		}
		
		
	}
    //returns the damage value
    public int getDamageValue() {
        return getKeyValue();
    }

    @Override
    public void display() {
        System.out.println("Artifact " + getName() + ", value: " + getName()
                + ", mobility: " + getMobility() + ", Attack Damage: " + getKeyValue());
        System.out.println(getDescription());
    }
}
