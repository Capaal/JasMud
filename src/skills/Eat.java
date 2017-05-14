package skills;

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
	private HerbType herbFromPouch;
	Holdable possiblePouch;
	
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
			//need to sort through all pouches
			if (((HerbPouch)possiblePouch).changeHerbs(-1,herbFromPouch) == -1) {
				messageSelf(herbFromPouch.use(currentPlayer));
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
			//search through all pouches in inventory/equipped TODO
			//if has no pouches, return;
			possiblePouch = currentPlayer.getHoldableFromString("herbpouch");
			if (possiblePouch instanceof HerbPouch) {
				HerbType typeToGet = ((HerbPouch)possiblePouch).getHerbType();
				if (typeToGet == null) {
					messageSelf("You do not have a \"" + herbName + "\".");
					return false;
				}
				if (typeToGet.name().equalsIgnoreCase(herbName)) {
					herbFromPouch = typeToGet;
					return true;
				} else {
					//if not in inv or any pouch
					messageSelf("You do not have a \"" + herbName + "\".");
					return false;
				}
			//check if the item is an herb
			} 
			return false;
		//if item is in inv and is an herb
		} else if (herbItem instanceof Herb) { 
			return true;
		} else {
			messageSelf("You cannot eat " + herbName + ".");
			return false;
		}
	}

}
