package skills.Mercenary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.InformsAggro;
import interfaces.Mobile;
import items.StdItem;
import items.Weapon;
import processes.Location;
import processes.Equipment;
import processes.Skills;

public class DualAttack extends Skills implements InformsAggro {

    private int intensity = 8;
    private Holdable rightWeapon;
    private Holdable leftWeapon;
    private StdItem rmercWeapon;
    private StdItem lmercWeapon;
    private double balAdjust = 1;   
    private List<Mobile> targets;
    private List<Weapon.MercEffect> effectsToApply;
    
    //Mercenary class skill, probably needs better name. Attacks target using wielded weapon. If weapon has special effect, applies.
    //Uses right hand only - dual wield uses both hands+weapons
    public DualAttack(Mobile currentPlayer, String fullCommand) {
		super("dualattack", "Attacking with two weapons.", currentPlayer, fullCommand);
        super.syntaxList.add(Syntax.SKILL);
        super.syntaxList.add(Syntax.TARGET);
    }

    //may need to apply effects and damage once?
    @Override
	protected void performSkill() {
        checkMercWeapons();
        regularRun();
    	currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance()); 
    	informLastAggressor(currentPlayer, targets);
    }

    @Override
    protected boolean preSkillChecks() {
        if (!hasBalance()) {return false;}
        if (!findTarget()) {return false;}
        if (!weaponWielded()) {return false;}
        return true;
    }

    private void regularRun() {
    	for (Mobile m : targets) {
    		for (Weapon.MercEffect effect : effectsToApply) {
    			effect.applyEffect(m);
    		}
			messageSelf("You attack " + m.getName() + ".");
			messageTarget(currentPlayer.getName() + " attacks you.", Arrays.asList(m));
			m.takeDamage(calculateDamage()); //calculate dmg once rather than mult times?
    	}
    	if (targets.size() > 1) {
    		messageOthers(currentPlayer.getName() + " attacks everyone at once.", Arrays.asList(currentPlayer));
    	} else {
    		for (Mobile m : targets) {
    			messageOthers(currentPlayer.getName() + " attacks " + targets.iterator().next().getName() + ".", Arrays.asList(currentPlayer, m));
    		}
    	}
    }
    //preskill check
    private boolean findTarget() {
    	targets = null;
        String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
        if (possibleTarg == "") {
            messageSelf("Specify target.");
            return false;
        }
    	Location here = currentPlayer.getContainer();
        if (possibleTarg.equals("all")) {
        	targets = (List<Mobile>) here.viewMobiles().values();
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
    //preskill check
    private boolean weaponWielded() {
        if (currentPlayer.getEquipmentInSlot(Equipment.EquipmentSlot.LEFTHAND) == null || currentPlayer.getEquipmentInSlot(Equipment.EquipmentSlot.RIGHTHAND) == null) {
            messageSelf("You need a weapon in both hands for this skill.");
            return false;
        }
        rightWeapon = currentPlayer.getEquipmentInSlot(Equipment.EquipmentSlot.RIGHTHAND);
        leftWeapon = currentPlayer.getEquipmentInSlot(Equipment.EquipmentSlot.LEFTHAND);
        if (!(rightWeapon instanceof StdItem) || !(leftWeapon instanceof StdItem)) {
        	messageSelf("One of those items isn't a weapon.");
        	return false;
        	}
        return true;
    }
    
    private void checkMercWeapons() {
    	effectsToApply = new ArrayList<Weapon.MercEffect>();
    	rmercWeapon = (StdItem)rightWeapon;
    	if (rmercWeapon instanceof Weapon) {
    		Weapon rightWeapon = (Weapon) rmercWeapon;
            if (rightWeapon.getMercEffect() != null) {
            	effectsToApply.add(rightWeapon.getMercEffect());
        	} 
    	}
        lmercWeapon = (StdItem)leftWeapon;
    	if (lmercWeapon instanceof Weapon) {
    		Weapon leftWeapon = (Weapon) rmercWeapon;
            if (leftWeapon.getMercEffect() != null) {
            	effectsToApply.add(leftWeapon.getMercEffect());
        	} 
    	}
    }
    
    private int calculateDamage() {
		double damageMultr = rmercWeapon.getDamageMult();
		double damageMultl = lmercWeapon.getDamageMult();
		return (int) (((damageMultr * intensity) + (damageMultl * intensity)) *.9 );
	}
    
    private int calculateBalance() {
		return (int) (3000 * rmercWeapon.getBalanceMult() * balAdjust);
	}
}


