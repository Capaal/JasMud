package skills.Mercenary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;
import items.MercWeapon;
import processes.Location;
import processes.Equipment;
import processes.Skills;
import processes.Type;

public class Attack extends Skills {

    private int intensity = 8;
//    private Mobile finalTarget; //replaced with targets list
    private Holdable weapon;
    private StdItem mercWeapon;
    private double balAdjust = 1;       
    String possibleTarg;
    private Collection<Mobile> targets = null;
    
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
        if(isMercWeapon()) {
        	MercWeapon.MercEffect effectType = mercWeapon.getMercEffect();
        	if (effectType.equals(MercWeapon.MercEffect.FASTBALANCE)) {
        		balAdjust = 0.8;
        	} else if (effectType.equals(MercWeapon.MercEffect.HIGHDMG)) {
        		intensity = 9;
        	}
        	regularRun(effectType);
        } else {
        	mercWeapon = (StdItem)weapon;
        	regularRun(null);
        }
    	currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance()); 
    }

    private boolean preSkillChecks() {
        if (!hasBalance()) {return false;}
        if (!findTarget()) {return false;}
        if (!weaponWielded()) {return false;}
        return true;
    }

    private void regularRun(MercWeapon.MercEffect effectType) {
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
    
    private boolean findTarget() {
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
  //      if (isBlocking(finalTarget)) {  this is not even a thing
  //          return false;
  //      }
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
    	mercWeapon = (StdItem)weapon; //all weapons should be StdItems, so this should handle fine nonMercWeapons too
    	if (mercWeapon.getMercEffect() == null) {
    		return false;
    	}
    	return true;
    }
    
    private int calculateDamage() {
		double damageMult = mercWeapon.getDamageMult();
		return (int) (damageMult * intensity);
	}
    
    private int calculateBalance() {
		return (int) (3000 * mercWeapon.getBalanceMult() * balAdjust);
	}
    
}


