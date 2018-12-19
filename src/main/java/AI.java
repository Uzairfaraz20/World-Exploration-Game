/**
 * Author: Uzair
 * An artificial intelligence DecisionMaker
 */
public class AI implements DecisionMaker {


    public Move getMove(Character character, Place place) {

        int randomNumber = (int) (Math.random() * 100);
        if (randomNumber < 55) { // if number is less than 55 do nothing (55% of time is a no move)
            return null;
        }
        //new location that NPC will try to move to
        Place newPlace = place.getRandomPlaceFrom();

        if (newPlace != null && newPlace.getID() != 1) { //NPCs cannot exit
            return new GoMove(character, newPlace);
        }
        return null;
    }

    public Move getRandomGoMove(Character character, Place place){
        int randomNumber = (int) (Math.random() * 100);
        if (randomNumber < 55) { // if number is less than 55 do nothing (55% of time is a no move)
            return null;
        }
        //new location that NPC will try to move to
        Place newPlace = place.getRandomPlaceFrom();

        if (newPlace != null && newPlace.getID() != 1) { //NPCs cannot exit
            return new GoMove(character, newPlace);
        }
        return null;
    }


}
