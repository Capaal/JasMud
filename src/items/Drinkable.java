package items;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder.ItemType;
import processes.StdMob;
import processes.Type;


//almost same as stackable, except can't split quantity (sips)
public class Drinkable extends StdItem {
	
	// Probably handle recovering IDs at some point
	private int maxSips;
	private int currentSips;
	private DrinkType type;
//	protected ArrayList<DrinkType> drinkTypes = new ArrayList<DrinkType>();

	public Drinkable(ItemBuilder build) {
		super(build);
		this.maxSips = build.getMaxSips();
		this.currentSips = maxSips;
		this.type = build.getDrinkType();
	}
	
	@Override public boolean save() {
		return true;
	}
	
	public int getSips() {
		return currentSips;
	}
	
	public boolean changeSips(int number) {
		currentSips = currentSips - number;
		if (currentSips < 0) {
			currentSips = 0;
			return false;
		}
		if (currentSips > maxSips) {
			currentSips = maxSips;
			return false;
		}
		return true;
	}
	
	public String drink(Mobile currentPlayer) {
		return type.drink(currentPlayer);
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setQuantity(this.currentSips);
		newBuild.setDescription(this.description);
		newBuild.setItemType(ItemType.DRINKABLE);
		return newBuild;
	}
	
	public enum DrinkType {
		HEALTH() {
			@Override public String drink(Mobile currentPlayer) {
				currentPlayer.takeDamage(Type.BLUNT, -50);
				return "The warm liquid heals you a bit.";
			}
		},
		
		DEFENSE() {
			@Override public String drink(Mobile currentPlayer) {
				currentPlayer.addDefense(10);
				return "The potion makes you feel tougher.";
			}
		};
	
		private DrinkType() {}
		
		public String drink(Mobile currentPlayer) {
			return "Wrong method, tells the coders they screwed up.";
		}
		
	}
	
	
	
}
