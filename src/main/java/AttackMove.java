/**
 * Author: Vineet
 * A class represent a character attacking another character with a weapon
 */
public class AttackMove implements Move {

    private Character attacker;
    private Weapon weapon;
    private Character victim;
    private IO io;

    /**
     * @param attacker: the character that is doing the attacks
     * @param weapon:   the weapon that will be used
     * @param victim:   the character that will be attacked
     */
    public AttackMove(Character attacker, Weapon weapon, Character victim) {
        this.attacker = attacker;
        this.weapon = weapon;
        this.victim = victim;
        io = IO.getInstance();
    }

    public void execute() {
        //first check if both payers are in the same place
        if (attacker.getCurrentPlace() != victim.getCurrentPlace()) {
            System.out.println(attacker.getName() + ", you cannot attack " +
                    victim.getName() + " because character is not in the same place as you");
            //return early
            return;
        }
        //check if they are not in a safe place
        if (victim.getCurrentPlace() instanceof SafePlace) {
            System.out.println("Cannot attack in a safe place");
            io.display(IO.USER_ERROR, "Cannot attack in a safe place");
            return;
        }
        System.out.println(attacker.getName() + " attacked " + victim.getName()
                + " with a " + weapon.getName());
        io.display(IO.CHARACTER_ATTACKED, String.format("%s;%s;%s",
                attacker.getName(), weapon.getName(), victim.getName()));

        weapon.use(victim, victim.getCurrentPlace());
    }
}
