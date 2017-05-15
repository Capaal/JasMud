package skills;

import java.util.Collection;
import java.util.Iterator;

import interfaces.Holdable;
import items.Herb;
import items.Herb.HerbType;
import items.HerbPouch;
import processes.Skills;
import processes.Skills.Syntax;

//currently only for herbs
public class Eat extends Skills{
	
	private String herbName;
	private Holdable herbItem;
	private Holdable possiblePouch;
	private HerbPouch finalPouch;
	
	public Eat() {
		super.name = "eat";
		super.description = "Eating things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	

	@Override
	protected void performSkill() {
		if(!preSkillChecks()) {return;}
		
		if (herbItem == null) { //herb in pouch (none in inv)
			HerbType toEat = finalPouch.getHerbType();
			if (finalPouch.changeHerbs(-1,finalPouch.getHerbType()) == -1) {
				messageSelf(toEat.use(currentPlayer));
			} else {
				messageSelf("You can't find a \"" + herbName + "\".");
			} 
		} else {	//herb found in inv
			Herb herb = (Herb)herbItem;	
			messageSelf(herb.use(currentPlayer));
		}; 
		
		messageSelf("You eat " + herbName + "."); 
		
	}
	
	private boolean preSkillChecks() {
		herbName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (herbName.equals("")) {
			messageSelf("What are you trying to eat?");
			return false;
		}
		//find the item
		herbItem = currentPlayer.getHoldableFromString(herbName); //in inventory
		if (herbItem == null) { //null means herb not in inv (maybe in pouch)
			//if has no pouches, return;
			possiblePouch = currentPlayer.getHoldableFromString("herbpouch"); 
			if (possiblePouch == null) { //no compatible pouch found
				messageSelf("You do not have a \"" + herbName + "\".");
				return false;
			} else if (possiblePouch instanceof HerbPouch) { //if player has a pouch
				finalPouch = findPouch();
				if (finalPouch == null) { 
					messageSelf("You do not have a \"" + herbName + "\".");
					return false;
				} else {
					return true;
				}
			}
		//if item is in inv and is an herb
		} else if (herbItem instanceof Herb) { 
			return true;
		} else {
			messageSelf("You cannot eat the \"" + herbName + "\".");
			return false;
		}
		return true;
	}
	
	//find compatible pouch - only need 1 with same herbtype
	private HerbPouch findPouch() {
		Collection<Holdable> inv = currentPlayer.getInventory().values();
		Iterator<Holdable> i = inv.iterator();
		int pouchQty = 0;
		Holdable possiblePouch;
		HerbPouch pouch;
		while (i.hasNext()) {
			possiblePouch = i.next();
			if (possiblePouch.getName().equalsIgnoreCase("herbpouch")) {
				if (possiblePouch instanceof HerbPouch) {
	               pouch = (HerbPouch)possiblePouch;
	               pouchQty = pouch.getHerbQty();
	               if (pouchQty>0 && pouch.getHerbType().toString().equalsIgnoreCase(herbName)) { 
	            	   return pouch; 
	               }
				}
			}
		}
		return null;
	}

}
