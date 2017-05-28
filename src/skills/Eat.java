package skills;

import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableMap;

import interfaces.Holdable;
import items.Herb;
import items.Herb.HerbType;
import items.HerbPouch;
import processes.Skills;
import processes.Skills.Syntax;

//currently only for herbs
public class Eat extends Skills{
	
	private String itemName;
	private Holdable itemToEat;
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
		
		if (itemToEat == null) { //herb in pouch (none in inv)
			itemToEat = finalPouch.getHoldableFromString(itemName);
			if (itemToEat instanceof Herb) {
				Herb toEat = (Herb)itemToEat;
				//remove stackable herb from herbpouch container
				toEat.removeFromStack(1); 
				messageSelf(toEat.use(currentPlayer));
			} else {
				messageSelf("You can't find a \"" + itemName + "\".");
			}
		} else {	//herb found in inv
			Herb herb = (Herb)itemToEat;	
			herb.removeFromStack(1); 
			messageSelf(herb.use(currentPlayer));
		}; 
		
		messageSelf("You eat " + itemName + "."); 
		
	}
	
	private boolean preSkillChecks() {
		itemName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemName.equals("")) {
			messageSelf("What are you trying to eat?");
			return false;
		}
		//find the item
		//need to check if eatName is an herbtype?
		itemToEat = currentPlayer.getHoldableFromString(itemName); //in inventory
		if (itemToEat == null) { //null = herb not in inv (maybe in pouch)		
			try {
				HerbType.valueOf(itemName.toUpperCase()); 
			} catch (IllegalArgumentException e) {
				messageSelf("You do not have a \"" + itemName + "\".");
				return false; //not an herb
			}
			//if has no pouches, return;
			possiblePouch = currentPlayer.getHoldableFromString("herbpouch"); 
			if (possiblePouch == null) { //no compatible pouch found
				messageSelf("You do not have a \"" + itemName + "\".");
				return false;
			} else if (possiblePouch instanceof HerbPouch) { //if player has a pouch
				finalPouch = findPouch();
				if (finalPouch == null) { 
					messageSelf("You do not have a \"" + itemName + "\".");
					return false;
				} else {
					return true;
				}
			}
			
		//if item is in inv and is an herb
		} else if (itemToEat instanceof Herb) { 
			return true;
		} else {
			messageSelf("You cannot eat the \"" + itemName + "\".");
			return false;
		}
		return true;
	}
	
	//find compatible pouch - returned pouch is guaranteed an herbpouch, same herbType, and qty>0
	private HerbPouch findPouch() {
	//	Collection<Holdable> inv = currentPlayer.getInventory().values();
	//	Iterator<Holdable> i = inv.iterator();
		Holdable possiblePouch;
	//	int pouchQty = 0;
		HerbPouch pouch = null;
		NavigableMap <String,Holdable> submap = currentPlayer.getListMatchingString("herbpouch");
		for (Holdable h : submap.values()) {
			pouch = (HerbPouch) h;
			if (pouch.getHoldableFromString(itemName) != null) {
				return pouch;
			} else {
				pouch = null;
			}
		}
		
	/*	while (i.hasNext()) {
			possiblePouch = i.next();
			if (possiblePouch.getName().equalsIgnoreCase("herbpouch")) {
				if (possiblePouch instanceof HerbPouch) {
	               pouch = (HerbPouch)possiblePouch;
	               if (pouch.getCurrentQty() > 0) {
	            	   Herb herbToEat = (Herb) pouch.getHoldableFromString(itemName);
	            	   pouchQty = herbToEat.getQuantity();
	            	   if (pouchQty>0 && herbToEat.getHerbType().toString().equalsIgnoreCase(itemName)) { 
	            		   return pouch; 
	            	   }
	               }
				}
			}
		} */
		return null;
	}

}
