package skills;

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

public class DualAttack extends Skills {

    private int intensity = 8;
//    private Mobile finalTarget; //replaced with targets list
    private Holdable rightWeapon;
    private Holdable leftWeapon;
    private StdItem rmercWeapon;
    private StdItem lmercWeapon;
    private double balAdjust = 1;       
    String possibleTarg;
    private Collection<Mobile> targets = null;
    
    //Mercenary class skill, probably needs better name. Attacks target using wielded weapon. If weapon has special effect, applies.
    //Uses right hand only - dual wield uses both hands+weapons
    public DualAttack() {
        super.name = "dualattack";
        super.syntaxList.add(Syntax.SKILL);
        super.syntaxList.add(Syntax.TARGET);
    }

    @Override
	protected void performSkill() {
        if (!preSkillChecks()) {return;};	
        //apply right weapon
        if(isRightMercWeapon()) {
        	applyMercEffect(rmercWeapon);
        } else {
        	rmercWeapon = (StdItem)rightWeapon;
        	regularRun(null, rmercWeapon);
        }
        //apply left weapon
        if(isLeftMercWeapon()) {
        	applyMercEffect(lmercWeapon);
        } else {
        	rmercWeapon = (StdItem)leftWeapon;
        	regularRun(null, lmercWeapon);
        }
    	currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance()); 
    }

    private boolean preSkillChecks() {
        if (!hasBalance()) {return false;}
        if (!findTarget()) {return false;}
        if (!weaponWielded()) {return false;}
        return true;
    }


    
    private void regularRun(MercWeapon.MercEffect effectType, StdItem mercWeapon) {
    	for (Mobile m : targets) {
    		if (effectType != null) {effectType.applyEffect(m);}
    		messageSelf("You attack " + m.getName() + " with your " + mercWeapon.getName() + ".");
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
        if (currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND) == null || currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND) == null) {
            messageSelf("You need a weapon in both hands for this skill.");
            return false;
        }
        rightWeapon = currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND);
        leftWeapon = currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND);
        return true;
    }
    
    private boolean isRightMercWeapon() {
    	if (!(rightWeapon instanceof StdItem)) {
    		return false;
    	} else {
        	rmercWeapon = (StdItem)rightWeapon;
    	}
    	if (rmercWeapon.getMercEffect() == null) {
    		return false;
    	}
    	return true;
    }
    
    private boolean isLeftMercWeapon() {
    	if (!(leftWeapon instanceof StdItem)) {
    		return false;
    	} else {
        	lmercWeapon = (StdItem)leftWeapon;
    	}
    	if (lmercWeapon.getMercEffect() == null) {
    		return false;
    	}
    	return true;
    }
    
    private void applyMercEffect(StdItem mercWeapon) {
    	MercWeapon.MercEffect effectType = mercWeapon.getMercEffect();
    	if (effectType.equals(MercWeapon.MercEffect.FASTBALANCE)) {
    		balAdjust = 0.8;
    	} else if (effectType.equals(MercWeapon.MercEffect.HIGHDMG)) {
    		intensity = 9;
    	}
    	regularRun(effectType, rmercWeapon);
    }
    
    private int calculateDamage() {
		double damageMult = rmercWeapon.getDamageMult();
		return (int) (damageMult * intensity);
	}
    
    private int calculateBalance() {
		return (int) (3000 * rmercWeapon.getBalanceMult() * balAdjust);
	}
    
}


