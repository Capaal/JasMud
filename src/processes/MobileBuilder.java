package processes;

import interfaces.Container;
import interfaces.Effect;
import interfaces.Holdable;
import items.StdItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import processes.Equipment.EquipmentEnum;

/**
 * Builder class for StdMob, used to gather all details of the Mobile being put together before officially finishing.
 * <p>
 * Written very generically because it extends mobile and will be extended by other classes and it allows super() hiarchies
 * to work with the internal builder classes, other classes should follow suit that extend StdMob.
 * @author Jason
 */
public class MobileBuilder {	

	public String name;
	public int id;		
	public String description = "Generic.";
	public String shortDescription = "Short and Generic.";
	public int maxHp = 100;
	public int currentHp = 100;
	public Container location = WorldServer.locationCollection.get(1);
	public boolean isDead = false;
	public int xpWorth = 1;
	public int experience = 1;
	public int level = 1;
	public int age = 1;
	public boolean loadOnStartUp = false;
	public Set<Holdable> inventory = new HashSet<Holdable>();
	public Equipment equipment = new Equipment();	
	public String password = "";
	public ArrayList<Effect> effectList = new ArrayList<Effect>();		
	
	/**
	 * Constuctor for StdMob builder, checks that desired Mobile isn't already built, then initiates the only REQUIRED information.
	 * @param id Mobile id desired.
	 * @param name Mobile name desired.
	 */
	public MobileBuilder() {
		// might change based on implementation.
		equipment(EquipmentEnum.HEAD,  null);
		equipment(EquipmentEnum.NECK,  null);
		equipment(EquipmentEnum.LEFTEAR,  null);
		equipment(EquipmentEnum.RIGHTEAR,  null);
		equipment(EquipmentEnum.LEFTHAND,  null);
		equipment(EquipmentEnum.RIGHTHAND,  null);
		equipment(EquipmentEnum.CHEST,  null);
		equipment(EquipmentEnum.LEGS,  null);
		equipment(EquipmentEnum.FEET,  null);
		equipment(EquipmentEnum.LEFTFINGER,  null);
		equipment(EquipmentEnum.RIGHTFINGER,  null);
	}		
	
	public void id(int val) {id = val;}
	public void name(String val) {
		if (UsefulCommands.checkIfValidCharacters(val)) {
			name = val;
		} else {
			throw new IllegalArgumentException("Name contains an invalid character, or is blank.");
		}
	}
	
	
	// The below are effectively the Builder's constructor methods. As they will be gathering data for the finalized StdMob.
	public void password(String val) {password = val;}		
	public void description(String val) {description = val;}		
	public void shortDescription(String val) {shortDescription = val;}		
	public void currentHp(int val) {currentHp = val;}
	public void isDead(boolean val) {isDead = val;}
	public void isDead(int val) {
		if (val == 1) {
			isDead = true;
		} else {
			isDead = false;
		}
	}
	public void location(Container val) {location = val;}			
	public void inventory(Holdable val) {inventory.add(val);}
	public void equipment(EquipmentEnum slot, StdItem val) {equipment.forceEquip(slot, val); }
	public void xpWorth(int val) {xpWorth = val;}	
	public void effect(Effect effect) {effectList.add(effect); }
	public void experience(int val) {experience = val;}
	public void level(int val) {level = val;}
	public void age(int val) {age = val;}
	public void loadOnStartUp(boolean val) {loadOnStartUp = val;}
	
	public void setMaxHp() {
		maxHp = 100 + level * 5;
	}
	
	public StdMob complete() {
		setMaxHp();
		return new StdMob(this);
	}
}
