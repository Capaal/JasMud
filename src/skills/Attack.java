package skills;

import java.util.Arrays;
import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class Attack extends Skills {

    private int intensity = 8;
    private Mobile finalTarget;
    private Holdable weapon;

    //Mercenary class skill, probably needs better name. Attacks target using wielded weapon. If weapon has special effect, applies.
    //Uses right hand only - dual wield uses both hands+weapons
    public Attack() {
        super.name = "attack";
        super.syntaxList.add(Syntax.SKILL);
        super.syntaxList.add(Syntax.TARGET);
    }

    @Override
	protected void performSkill() {
        if (!preSkillChecks()) {return;};	
        finalTarget.takeDamage(Type.SHARP, calculateDamage()); //type based on item
        //apply weapon effect + message
        currentPlayer.addEffect(new Balance(), calculateBalance()); 
        messageSelf("You attack " + finalTarget.getName() + " with your " + weapon.getName() + ".");
		messageTarget(currentPlayer.getName() + " attacks you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " attacks " + finalTarget.getName() + ".", Arrays.asList(currentPlayer, finalTarget));
    }

    private boolean preSkillChecks() {
        if (!hasBalance()) {return false;}
        if (!findTarget()) {return false;}
        if (!weaponWielded()) {return false;}
        return true;
    }

    private boolean findTarget() {
        String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
        if (possibleTarg == "") {
            messageSelf("Specify target.");
            return false;
        }
        Location here = currentPlayer.getContainer();
        Holdable possTarg = finalLoc.getHoldableFromString(possibleTarg);
        if (possTarg != null && possTarg instanceof Mobile) {finalTarget = (mobile)possTarg};
        if (finalTarget == null) {
            messageSelf("Target not here.");
            return false;
        }
        if (isBlocking(finalTarget)) {  // Probably not complete still
            return false;
        }
        return true;
    }

    private boolean weaponWielded() {
        if (currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND) == null && currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND) == null) {
            messageSelf("You are not wielding a weapon.");
            return false;
        }
        return true;
    }
    
    private int calculateDamage() {
		double damageMult = ((Weapon)weapon).getBalanceMult();
		return (int) (damageMult * intensity);
	}
    
    private int calculateBalance() {
		return (int) (3000 * ((Weapon)weapon).getBalanceMult());
	}

//get the item and the method associated with the item
//does item or skill hold the method?
//if item, something needs to check if user is the right mercenary class

