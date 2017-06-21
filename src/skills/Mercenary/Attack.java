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
import skills.Sleep;

public class Attack extends Skills {

    private final int intensity = 8;
    private final double balAdjust = 1; 
    private final int baseBalance = 3000;
    
    private StdItem possWeapon;
    String possibleTarg;
 // private Collection<Mobile> targets = null;
    private Mobile target;
    
    //Mercenary class skill, probably needs better name. Attacks target using wielded weapon. If weapon has special effect, applies.
    //Uses 1 hand only - dual wield uses both hands/weapons
    public Attack(Mobile currentPlayer, String fullCommand) {
		super("attack", "Attacking with style.", currentPlayer, fullCommand);
        super.syntaxList.add(Syntax.SKILL);
        super.syntaxList.add(Syntax.TARGET);
    }

    @Override
	protected void performSkill() {
        if (preSkillChecks()) {	
			messageSelf("You attack " + target.getName() + " with your " + possWeapon.getName() + ".");
			messageTarget(currentPlayer.getName() + " attacks you with a " + possWeapon.getName() + ".", Arrays.asList(target));
			//applies effect and poison
			//TODO message others
			if (possWeapon instanceof Weapon) {
				Weapon weapon = (Weapon) possWeapon;
				weapon.applyEffect(target);
			//	messageOthers(target.getName() + weapon.displayEffectOthers(),Arrays.asList(currentPlayer));
				if (!weapon.getAppliedList().isEmpty()) {
					messageTarget(weapon.applyPlant(target), Arrays.asList(target));
				}
				
	    	}
			target.takeDamage(Type.SHARP, calculateDamage());
	    	currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance()); 
        }
    }

    @Override
    protected boolean preSkillChecks() {
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
        target = currentPlayer.getContainer().getMobileFromString(possibleTarg);
        if (target == null) {
        	 messageSelf("Can't find target.");
             return false;
        }
    	return true;
    }
    
    //below commented out was for AOE (all)
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
        //righthand primary for now
    	possWeapon = (StdItem) currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND);
    	if (possWeapon == null) {
    		possWeapon = (StdItem) currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND);
    	}
    	
    	if (possWeapon == null) {
            messageSelf("You are not wielding a weapon.");
            return false;
        }
    	return true;
    }
 
    
    private int calculateDamage() {
		double damageMult = possWeapon.getDamageMult();
		return (int) (damageMult * intensity);
	}
    
    private int calculateBalance() {
		return (int) (baseBalance * possWeapon.getBalanceMult() * balAdjust);
	}
    
    @Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Attack(currentPlayer, fullCommand);
	}
}


