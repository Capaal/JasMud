package processes;

import interfaces.*;
import items.StdItem;

import java.util.*;

import Quests.Quest.Trigger;
import effects.*;
import processes.Equipment.EquipmentEnum;
import processes.Location.GroundType;
import processes.MobileDecorator.DecoratorType;

/**
 * A basic implentable of the interface Mobile, StdMob contains the basic methods for anything that can move around on it's own, whether
 * that is by a player or by AI.
 * <p>
 * Basic uses of this class should include rats, tigers, birds, bears. Heroes will probably need to be an extension of this class.
 * Undead might also be an extension, as they would have additional methods, or overwritten methods.
 * @author Jason
 */
public class StdMob implements Mobile, Container, Holdable {

	protected final String name;
	protected final String password;
	protected final int id;
	protected int maxHp;
	protected int currentHp; 
	protected Location mobLocation;
	protected boolean isDead;
	protected Equipment equipment;	
	
	protected String description;
	protected int xpWorth;
	protected String shortDescription;
	protected int experience;
	protected int level;
	protected int age; 
	protected SendMessage sendBack;
	protected boolean isControlled = false;
	protected boolean loadOnStartUp = false;
	protected TreeMap<String, Holdable> inventory = new TreeMap<String, Holdable>();
	protected InductionSkill inductionSkill = null;
	protected Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	protected List<ItemBuilder> dropsOnDeath;
	
	protected final EffectManager effectManager;
	protected Mobile lastAggressor;
	
//	private boolean creating = false;
	
	public StdMob(MobileBuilder build) {
		this.id = build.getId();
		this.name = build.getName();		
		this.password = build.getPassword();
		this.maxHp = build.getMaxHp();
		this.currentHp = getMaxHp();
		this.mobLocation = build.getLocation();
		this.isDead = build.isDead();
		this.description = build.getDescription();
		this.shortDescription = build.getShortDescription();	
		this.inventory.putAll(build.getInventory())	;
		this.equipment = build.getEquipment();
		this.dropsOnDeath = build.getDropsOnDeath();
		this.experience = build.getExperience();
		equipment.setOwner(this);
		effectManager = new EffectManager(this);
		this.skillBookList = build.getSkillBookList();		
		
		Mobile decoratedMob = decorate(build, this);
		build.setFinishedMob(decoratedMob);
		
		WorldServer.gameState.addMob(decoratedMob.getName() + decoratedMob.getId(), decoratedMob);
		decoratedMob.getContainer().acceptItem(decoratedMob);
	}
	
	private Mobile decorate(MobileBuilder build, Mobile m) {
		if (build.hasNextDecorator()) {
			DecoratorType nextDecorator = build.getNextDecorator();
			m = nextDecorator.getDecorator(m);
			return decorate(build, m);
		}
		return m;
	}
	
	@Override public String getName() {return name;}	
	@Override public boolean isDead() {return isDead;}	
	
	@Override public boolean hasBalance() {
		return !hasEffect(new Balance());
	}
	
	@Override public String getDescription() {
		return description;
	}
	
	@Override public String getShortDescription() {
		return shortDescription;
	}
	
	@Override public int getXpWorth() {
		return xpWorth;
	}
	
	@Override public Skills getCommand(String command) {
		for (SkillBook sb : skillBookList.keySet()) {
			Skills skill = sb.getSkill(command);
				return skill;		
		}
		return null;
	}
	
	@Override
	public void acceptItem(Holdable item) {
		inventory.put(item.getName() + item.getId(), item);
		// dropsOnDeath.add(item.newBuilder());  Not a good method of this, drop on death is for spawning new items.
	}
	
/*	@Override OUTDATEd, use moveHoldable();
	public synchronized void setContainer(Container futureLocation) {
		if (futureLocation instanceof Location) {	
			System.out.println("CRITICAL ERROR, MOBILE ATTEMPTED TO BE MOVED TO A NON-LOCATION");
		} else {
			this.mobLocation = (Location)futureLocation;
		}
	}*/ 
	
	@Override
	public synchronized void moveHoldable(Container finalLocation) {
		if (finalLocation instanceof Location) {	
			getContainer().removeItemFromLocation(this);
			finalLocation.acceptItem(this);
			this.mobLocation = (Location) finalLocation;
		} else {
			System.out.println("CRITICAL ERROR, MOBILE ATTEMPTED TO BE MOVED TO A NON-LOCATION");
		}		
	}
	
	@Override
	public synchronized Location getContainer() {
		return mobLocation;
	}
	
	@Override
	public synchronized void takeDamage(Type type, int damage) {
		damage = checkEffectsAgainstIncomingDamage(type, damage);
		if (currentHp < damage) {
			damage = currentHp;
		} 
		this.currentHp = currentHp - damage;
		checkHp();
		displayPrompt();
	}
	
	@Override
	public int checkEffectsAgainstIncomingDamage(Type incomingType, int damage) {
		return effectManager.checkEffectsAgainstIncomingDamage(incomingType, damage);
	}
	
/*	@Override TODO
	public double getWeaponMultiplier() {
		Holdable weapon = getEquipmentInSlot(EquipmentEnum.RIGHTHAND);
		if (weapon != null) {
			return weapon.getDamageMult();
		} else {
			weapon = getEquipmentInSlot(EquipmentEnum.LEFTHAND);
			if (weapon != null) {
				return weapon.getDamageMult();
			}
		}
		return 1.0;
	}*/
	
/*	@Override TODO
	public double getDamageMult() {
		return 1.0;
	}*/
	

	private void checkHp() {
		if (currentHp <= 0 && !isDead) {
			tell("You collapse to the ground, unable to fight on.");
			isDead = true;
			for (Holdable m : getContainer().getInventory().values()) {
				if (m instanceof Mobile && ((Mobile)m).isControlled() && !m.equals(this)) {
					((Mobile)m).tell(getName() + " drops to the floor, dead.");
				}
			}
			dropItemsOnDeath();
		}
	}	
	
	// Triggers only on death, used to spawn NEW items, not to drop held items.
	@Override
	public void dropItemsOnDeath() {
		if (dropsOnDeath != null) {
			for (ItemBuilder ib : dropsOnDeath) {
				ib.setItemContainer(getContainer());
				ib.complete();
			}
		}
	}
	
	@Override
	public void tell(String msg) {
		// NPCs will not have a sendBack object.
		if (this.sendBack != null) {
			sendBack.printMessage(msg);
		} 
	}
	@Override
	public void tellLine(String msg) {
		// NPCs will not have a sendback object.
		if (this.sendBack != null) {
			sendBack.printMessageLine(msg); // Prints msg on SAME line, does not create newLine.
		}	
	}
	
	/*@Override OUTDATED use removeItemFromLocation();
	public synchronized void removeItem(Holdable item) {
		if (inventory.contains(item)) {
			inventory.remove(item);
		}
		// DOES NOT DO ANYTHING IF IT DOESN"T CONTAIN? 
	}	*/
	
	// Put in Container AND remove from Container is complicated, but should be GUARANTEED in ONE LINE TODO
	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		if (inventory.containsValue(oldItem)) {
	//		inventory.remove(oldItem);
			String key = oldItem.getName() + oldItem.getId();
			inventory.remove(key);
		} else if (equipment.values().contains(oldItem)) {
			equipment.unequipItem((StdItem)oldItem);
			removeItemFromLocation(oldItem);
		} else {
			System.out.println("An item was just attempted to be moved from an inventory that probably shouldn't have gotten this far.");
		}
		
	}

	// Returns view of Inventory, allows editing of objects within (which should be limited) but not to the inventory list.
	@Override
	public TreeMap<String, Holdable> getInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	// TODO make Inventory a tree sorted by alphabetical order.
	@Override
	public Holdable getHoldableFromString(String holdableString) {
	//	System.out.println(inventory.containsValue(holdableString));
		
		
		Map.Entry<String,Holdable> answer = inventory.ceilingEntry(holdableString);
		if (answer != null && (answer.getKey().equals(holdableString) || answer.getValue().getName().equals(holdableString))) {
			return answer.getValue();
		}
		return null;
		
	/*	
		for (Holdable h : inventory) {
			String tempItemName = h.getName().toLowerCase();
			if (tempItemName.equals(holdableString) || (tempItemName + h.getId()).equals(holdableString)) {
				return h;
			}
		}
		// Below related to Equipment, which is a TODO item.
		Collection<Holdable> items =  equipment.values();
		for (Holdable item : items) {
			if (item != null) {
				String posName = item.getName().toLowerCase();					
				if (posName.equals(holdableString) || (posName + item.getId()).equals(holdableString)) {
					return item;
				}
			}
		}
		return null;	*/	
	}
	
/*	@Override
	public String displayExits() {
		return "You are being held by a person!";
	}*/
	@Override
	public void look(Mobile currentPlayer) {
		currentPlayer.tell("You see the player's inventory.");	
		for (Holdable h : inventory.values()) {
			currentPlayer.tell(h.getName() + h.getId());
		}
	}
	/*@Override
	public void glance(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void displayAll(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}*/
	
	
	@Override
	public void addEffect(Effect newEffect, int duration) {
		effectManager.registerEffectDestroyAfterXMilliseconds(newEffect, duration);
	}
	
	@Override
	public void addTickingEffect(TickingEffect newEffect, int duration, int times) {
		effectManager.registerEffectRepeatNTimesOverXMilliseconds(newEffect, times, duration);
	}
	
	@Override
	public void removeEffect(Effect effect) {
		effectManager.removeInstanceOf(effect);		
	}
	
	@Override
	public boolean hasEffect(Effect effect) {
		return effectManager.hasInstanceOf(effect);
	}

	// TODO equipment is work in progress
	@Override
	public void equip(EquipmentEnum slot, Holdable item) {
/*		if (inventory.remove(item)) {
			equipment.equip(slot, item);
		} else {
			System.out.println("Attempt to equip illegal item " + item.toString() + " into slot " + slot.toString());
		}*/
	}
	
	// TODO
	@Override
	public void unequip(Holdable item) {
		equipment.unequipItem(item);
	//	inventory.add(item);
	}
	
	@Override
	public Holdable getEquipmentInSlot(EquipmentEnum slot) {
		return equipment.getValue(slot);
	}
	
	//TODO
	@Override
	public EquipmentEnum findEquipment(String itemName) {
		Collection<Holdable> items =  equipment.values();
		for (Holdable item : items) {
			if (item != null) {
				String posName = item.getName().toLowerCase();					
				if (posName.equals(itemName) || (posName + item.getId()).equals(itemName)) {
					return equipment.getKey(item);
				}
			}
		}
		return null;
	}
	
	@Override
	public void unequipFromSlot(EquipmentEnum slot) {
		Holdable item = equipment.getValue(slot);
		equipment.unequipSlot(slot);		
//		inventory.add(item);
	}
	
	@Override
	public void addBook(SkillBook skillBook, int progress) {
		skillBookList.put(skillBook, progress);
		
	}
	@Override
	public boolean isControlled() {
		return isControlled;
	}
	
	@Override
	public void controlStatus(boolean statusChange) {
		isControlled = statusChange;
	}
	
	@Override
	public boolean save() {
	//	if (!saveStats()) {
	//		System.out.println("Failed save of stats for " + this.getName());
	//		return false;
	//		
	//	}
	//	if (!saveItems()) {
	//		System.out.println("Failed save of items for " + this.getName());
	//		return false;
	//	}	
		return true;
	}
	
	//TODO
	private boolean saveStats() {
		// Should update everything that we expect to change A LOT, like location and hp. Things like description would
		// probably be best somewhere else that get updated right when the change occurs.
		String updateStats = "UPDATE MOBSTATS SET MOBDESC='" + description + "', MOBSHORTD='" + shortDescription 
				+ "', MOBLOC=" + mobLocation.getId() + ", MOBCURRENTHP=" + currentHp + ", MOBDEAD='" + (isDead ? 1 : 0) + "', "
						+ "MOBCURRENTXP=" + experience + ", MOBCURRENTLEVEL=" + level + ", MOBAGE=" + age
						+ ", LOADONSTARTUP=" + (loadOnStartUp ? 1 : 0) + " WHERE MOBID=" + id + ";";
		WorldServer.databaseInterface.saveAction(updateStats);
		return true;
	}	
	
	private boolean saveItems() {
//		for (Holdable saveInventoryItem : inventory) {
//			if (!saveInventoryItem.save()) {
//				return false;
//			}
//		}
		for (Holdable saveEquipmentItem : equipment.values()) {
			if (saveEquipmentItem != null) {
				if (!saveEquipmentItem.save()) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public void setStartup(boolean b) {
		loadOnStartUp = b;		
	}	

	@Override
	public void removeFromWorld() {	
		if (!loadOnStartUp) { // WHY THIS?
//			for (Holdable inventoryItem : inventory) {
//				inventoryItem.removeFromWorld();
//			}
			for (Holdable equipmentItem : equipment.values()) {
				if (equipmentItem != null) {
					equipmentItem.removeFromWorld();
				}
			}
			save();
			effectManager.shutDown();
			if (isInducting()) {
				inductionSkill.shutDown();
			}
			mobLocation.removeItemFromLocation(this);
			WorldServer.gameState.removeMob(this.getName() + this.getId());
		}
	}
	
	@Override
	public void displayPrompt() {
		String balance = "b";
		if (!hasBalance()) {
			balance = "-";
		}
		tellLine(getCurrentHp() + "/" + getMaxHp() + " " + balance + ": ");
	}
	
	@Override
	public void setSendBack(SendMessage sendBack) {
		this.sendBack = sendBack;
		
	}
	
//	@Override TODO
//	public EnumSet<EquipmentEnum> getAllowedEquipSlots() {
//		return EnumSet.noneOf(EquipmentEnum.class);
//	}
	
	@Override
	public boolean containsType(Type type) {
		return false;
	}
	
	public static void insertNewBlankMob(String newName, String newPassword) throws IllegalStateException {
		int newId = findNewId(newName);
		String sql = "insert into mobstats (MOBID, MOBNAME, MOBPASS) values (" + newId + ", '" + newName + "', '" + newPassword + "');";
		WorldServer.databaseInterface.saveAction(sql);
		String insertBook = "insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values((SELECT MOBID FROM MOBSTATS"
				+ " WHERE MOBNAME='" + newName + "'), 1, 1) ON DUPLICATE KEY UPDATE MOBPROGRESS=1;";
		WorldServer.databaseInterface.saveAction(insertBook);
	}

/*	@Override
	public GroundType getGroundType() {
		// TODO Auto-generated method stub
		return GroundType.GROUND;
	}*/

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int getMaxHp() {
		return maxHp;
		// TODO Auto-generated method stub
	}

	@Override
	public int getCurrentHp() {
		// TODO Auto-generated method stub
		return currentHp;
	}

	// TODO Should we even allow this?
//	@Override
//	public SendMessage getSendBack() {
//		return sendBack;
//	}

	@Override
	public void informLastAggressor(Mobile aggressor) {
		this.lastAggressor = aggressor;
		
	}
	private static int findNewId(String name) {
/*		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN MOBSTATS ON sequencetable.sequenceid = mobstats.mobid"
				+ " WHERE mobstats.mobid IS NULL";		
		Object availableId = 1;
	//	Object availableId = (int) WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			findNewId(name);
		} else {
			if (WorldServer.gameState.checkForMob(name + availableId)) {
				throw new IllegalStateException("A mob of the id already exists.");
			}
			return (int)availableId;
		}
		return 0;*/
		return 1;
	}

	@Override
	public boolean firstTimeSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
/*	// TODO Leftover from GOdCreate
	@Override
	public boolean isCreating() {
		return creating;
	}
	
	// TODO Left over from god create
	@Override
	public void startCreating() {
		creating = true;
	}
	
	// TODO Left over from god create
	@Override
	public void stopCreating() {
		creating = false;
	}*/

	@Override
	public boolean isInducting() {
		if (inductionSkill == null) {
			return false;
		}
		return true;
	}

	@Override
	public void killInduction() {
		inductionSkill.kill();
		inductionSkill = null;
	}
	
	@Override
	public void setInduction(InductionSkill skill) {
		this.inductionSkill = skill;
	}
	
	@Override
	public Map<SkillBook, Integer> viewSkillBooks() {
		return new HashMap<SkillBook, Integer>(skillBookList);
	}

	@Override
	public int compareTo(Holdable other) {
		String thisItem = this.getName()+this.getId();
		String otherItem = other.getName()+other.getId();
		return thisItem.compareToIgnoreCase(otherItem);
	}	
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!(obj instanceof StdMob)) {
	    	return false;
	    }
	    final Holdable other = (Holdable) obj;
	    String thisHoldable = this.getName() + this.getId();
	    String otherHoldable = other.getName() + other.getId();
	    return thisHoldable.equals(otherHoldable);
	 //   if ((thisHoldable.compareToIgnoreCase(otherHoldable)) >= 0) {
	  //  	return true;
	   // }
	//    return false;
	}
}

	