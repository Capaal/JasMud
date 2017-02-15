package skills;

import java.util.Arrays;
import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;
import items.MercWeaponAttack;
import processes.Location;
import processes.Equipment;
import processes.Skills;
import processes.Type;

public class Attack extends Skills {

    private int intensity = 8;
    private Mobile finalTarget;
    private Holdable weapon;
    private StdItem mercWeapon;

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
        if(isMercWeapon()) {
        	mercWeapon.getMercEffect().applyEffect(finalTarget);
        }
        currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance()); 
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
    	finalTarget = null;
        String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
        if (possibleTarg == "") {
            messageSelf("Specify target.");
            return false;
        }
        Location here = currentPlayer.getContainer();
        Mobile possTarg = here.getMobileFromString(possibleTarg);
        if (possTarg != null) {finalTarget = possTarg;}
        if (finalTarget == null) {
            messageSelf("Can't find target.");
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
        //players are all righthanded for now
        weapon = currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND);
        return true;
    }
    
    private boolean isMercWeapon() {
    	if (!(weapon instanceof StdItem)) {
    		return false;
    	} 
    	mercWeapon = (StdItem)weapon;
    	if (mercWeapon.getMercEffect() == null) {
    		return false;
    	}
    	return true;
    }
    
    private int calculateDamage() {
		double damageMult = ((StdItem)weapon).getDamageMult();
		return (int) (damageMult * intensity);
	}
    
    private int calculateBalance() {
		return (int) (3000 * ((StdItem)weapon).getBalanceMult());
	}
}

//get the item and the method associated with the item
//does item or skill hold the method?
//if item, something needs to check if user is the right mercenary class

