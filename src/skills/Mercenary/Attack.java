package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;
import items.Weapon;
import processes.Location;
import processes.Equipment;
import processes.Skills;
import processes.Type;

public class Attack extends Skills {

    private int intensity = 8;
    private Holdable item;
    private StdItem possWeapon;
    private Weapon weapon;
    private double balAdjust = 1;       
    String possibleTarg;
 // private Collection<Mobile> targets = null;
    private Mobile target;
    
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
        //apply weapon effect + message
        //MercWeapon.MercEffect effectType = mercWeapon.getMercEffect();
		messageSelf("You attack " + target.getName() + " with your " + item.getName() + ".");
		messageTarget(currentPlayer.getName() + " attacks you with a " + item.getName() + ".", Arrays.asList(target));
        if (hasEffect()) {
    		Weapon weapon = (Weapon) possWeapon;
        	weapon.applyEffect(target);
    	}
		target.takeDamage(Type.SHARP, calculateDamage()); //calculate dmg once rather than mult times?
    	currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance()); 
    }

    private boolean preSkillChecks() {
        if (!hasBalance()) {return false;}
   //   if (!findTargets()) {return false;}
        if (!findTarget()) {return false;}
        if (!weaponWielded()) {return false;}
        return true;
    }
    
    private boolean findTarget() {
    	target = null;
        String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
        if (possibleTarg == "") {
            messageSelf("Specify target.");
            return false;
        }
        Location here = currentPlayer.getContainer();
        target = here.getMobileFromString(possibleTarg);
        if (target == null) {
        	 messageSelf("Can't find target.");
             return false;
        }
    	return true;
    }
    
    //below commented out was for aoe (all)
/*    private void regularRun(MercWeapon.MercEffect effectType) {
    	for (Mobile m : targets) {
    		if (effectType != null) {effectType.applyEffect(m);}
    		messageSelf("You attack " + m.getName() + " with your " + weapon.getName() + ".");
    		messageTarget(currentPlayer.getName() + " attacks you.", Arrays.asList(m));
    		m.takeDamage(Type.SHARP, calculateDamage()); //calculate dmg once rather than mult times?
    	}
    	if (targets.size() > 1) {
    		messageOthers(currentPlayer.getName() + " attacks everyone at once.", Arrays.asList(currentPlayer));
    	} else {
    		for (Mobile m : targets) {
    			messageOthers(currentPlayer.getName() + " attacks " + targets.iterator().next().getName() + ".", Arrays.asList(currentPlayer,m));
    		}
    	}
    }
    
    private boolean findTargets() {
    	targets = null;
        String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
        if (possibleTarg == "") {
            messageSelf("Specify target.");
            return false;
        }
    	Location here = currentPlayer.getContainer();
        if (possibleTarg.equals("all")) {
        	targets = here.getMobiles().values();
        	targets.remove(currentPlayer); //maybe remove friends or add an effect that's aoeEnemies etc
        	if (targets.isEmpty()) {
        		return false;
        	}
        	return true;
        }
        Mobile possTarg = here.getMobileFromString(possibleTarg);
        if (possTarg != null) {
        	targets = new ArrayList<Mobile>();
        	targets.add(possTarg);
        	}
        if (targets == null || targets.isEmpty()) {
            messageSelf("Can't find target.");
            return false;
        }
   //     if (isBlocking(finalTarget)) {  
  //          return false;
   //     }
        return true;
    } */

    private boolean weaponWielded() {
     //   if (currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND) == null && currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND) == null) {
     //       messageSelf("You are not wielding a weapon.");
     //       return false;
     //   }  
    	
        //righthand primary for now
    	item = currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND);
    	if (item == null) {
    		item = currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND);
    	}
    	
    	if (item == null) {
            messageSelf("You are not wielding a weapon.");
            return false;
        }
    	return true;
    }
    
    private boolean hasEffect() {
    	if (!(item instanceof StdItem)) {
    		return false;
    	} 
    	possWeapon = (StdItem) item;
    	if (item instanceof Weapon) {
    		weapon = (Weapon)item;
    		if (weapon.getMercEffect() != null) {
    			return true;
    		}
    	}
    	return false;
    } 
    
    private int calculateDamage() {
		double damageMult = possWeapon.getDamageMult();
		return (int) (damageMult * intensity);
	}
    
    private int calculateBalance() {
		return (int) (3000 * possWeapon.getBalanceMult() * balAdjust);
	}
    
}


