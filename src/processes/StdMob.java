package processes;

import interfaces.*;
import items.StdItem;
import java.sql.SQLException;
import java.util.*;
import effects.Balance;
import processes.Equipment.EquipmentEnum;
import processes.Location.GroundType;

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
	protected Container mobLocation;
	protected boolean isDead;
	
	protected Set<Holdable> inventory; // Make this be combined with equipment as its own class? TODO
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
	
	protected Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	
	protected final EffectManager effectManager;
	
	protected StdMob(MobileBuilder build) {
		this.name = build.name;
		this.id = build.id;
		this.password = build.password;
		this.maxHp = build.maxHp;
		this.currentHp = maxHp;
		this.mobLocation = build.location;
		this.isDead = false;
		this.description = build.description;
		this.shortDescription = build.shortDescription;
		this.inventory = build.inventory;
		this.equipment = build.equipment;
		equipment.setOwner(this);
		effectManager = new EffectManager();
		WorldServer.mobList.put(name + id, this);
	}
	
	public String getName() {return name;}	
	public boolean isDead() {return isDead;}	
	
	public boolean hasBalance() {
		return !hasEffect(new Balance());
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public int getXpWorth() {
		return xpWorth;
	}
	
	public Skill getCommand(String command) {
		for (SkillBook sb : skillBookList.keySet()) {
			Skill skill = sb.getSkill(command);
				return skill;		
		}
		return null;
	}
	
	@Override
	public void acceptItem(Holdable item) {
		inventory.add(item);
	}
	
	@Override
	public synchronized void setContainer(Container futureLocation) {
		this.mobLocation = futureLocation;
	}
	
	@Override
	public synchronized Container getContainer() {
		return mobLocation;
	}
	
	@Override
	public void takeDamage(Set<Type> types, int damage) {
		damage = checkEffectsAgainstIncomingDamage(types, damage);
		if (currentHp < damage) {
			damage = currentHp;
		}
		this.currentHp = currentHp - damage;
		checkHp();
	}
	
	@Override
	public int checkEffectsAgainstIncomingDamage(Set<Type> incomingTypes, int damage) {
		return effectManager.checkEffectsAgainstIncomingDamage(incomingTypes, damage);
	}
	
	protected void checkHp() {
		if (currentHp <= 0 && !isDead) {
			tell("You colapse to the ground, unable to fight on.");
			isDead = true;
		}
	}	
	
	public void tell(String msg) {
		// NPCs will not have a sendBack object.
		if (this.sendBack != null) {
			sendBack.printMessage(msg);
		} 
	}

	public void tellLine(String msg) {
		// NPCs will not have a sendback object.
		if (this.sendBack != null) {
			sendBack.printMessageLine(msg);
		}	
	}
	
	public synchronized void removeItem(Holdable item) {
		if (inventory.contains(item)) {
			inventory.remove(item);
		}
		// TODO
		// DOES NOT DO ANYTHING IF IT DOESN"T CONTAIN? 
	}	

	@Override
	public Set<Holdable> getInventory() {
		return new HashSet<Holdable>(this.inventory);
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		for (Holdable h : inventory) {
			String tempItemName = h.getName().toLowerCase();
			if (tempItemName.equals(holdableString) || (tempItemName + h.getId()).equals(holdableString)) {
				return h;
			}
		}
		Collection<Holdable> items =  equipment.values();
		for (Holdable item : items) {
			if (item != null) {
				String posName = item.getName().toLowerCase();					
				if (posName.equals(holdableString) || (posName + item.getId()).equals(holdableString)) {
					return item;
				}
			}
		}
		return null;		
	}
	
	@Override
	public String displayExits() {
		return "You are being held by a person!";
	}
	@Override
	public void look(Mobile currentPlayer) {
		currentPlayer.tell("You see the player's inventory.");		
	}
	@Override
	public void glance(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void displayAll(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		if (inventory.contains(oldItem)) {
			inventory.remove(oldItem);
		} else if (equipment.values().contains(oldItem)) {
			equipment.unequipItem((StdItem)oldItem);
			removeItemFromLocation(oldItem);
		} else {
			System.out.println("An item was just attempted to be moved from an inventory that probably shouldn't have gotten this far.");
		}
		
	}
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

	@Override
	public void equip(EquipmentEnum slot, Holdable item) {
		if (inventory.remove(item)) {
			equipment.equip(slot, item);
		} else {
			System.out.println("Attempt to equip illegal item " + item.toString() + " into slot " + slot.toString());
		}
	}
	
	@Override
	public void unequip(Holdable item) {
		equipment.unequipItem(item);
		inventory.add(item);
	}
	
	@Override
	public Holdable getEquipmentInSlot(EquipmentEnum slot) {
		return equipment.getValue(slot);
	}
	
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
		inventory.add(item);
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
		if (!saveSkills()) {
			System.out.println("Failed save of skills for " + this.getName());
			return false;
		}
		if (!saveStats()) {
			System.out.println("Failed save of stats for " + this.getName());
			return false;
		}
		if (!saveItems()) {
			System.out.println("Failed save of items for " + this.getName());
			return false;
		}	
		return true;
	}
	
	private boolean saveSkills() {			
		for (SkillBook sb : skillBookList.keySet()) {
			if (sb.getToBeSave()) {
				String insertBook = "insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values(" + id + ", " + sb.getId() + ", " + skillBookList.get(sb) +
						") ON DUPLICATE KEY UPDATE mobprogress=" + skillBookList.get(sb) + ";";
				try {
					SQLInterface.saveAction(insertBook);
				} catch (SQLException e) {
					System.out.println("Skillbook " + sb.getName() + " failed to save it's progress table via: " + insertBook);
					return false;
				}
				if (!sb.save()) {
					return false;
				}	
			}
		}	
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
		try {
			SQLInterface.saveAction(updateStats);
			return true;
		} catch (SQLException e) {
			System.out.println(this.getName() + " failed to save via sql: " + updateStats);
			e.printStackTrace();
			return false;
		}
	}	
	
	private boolean saveItems() {
		for (Holdable saveInventoryItem : inventory) {
			if (!saveInventoryItem.save()) {
				return false;
			}
		}
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

	public void removeFromWorld() {		
		for (Holdable inventoryItem : inventory) {
			inventoryItem.removeFromWorld();
		}
		for (Holdable equipmentItem : equipment.values()) {
			if (equipmentItem != null) {
				equipmentItem.removeFromWorld();
			}
		}
		save();
		effectManager.shutDown();
		mobLocation.removeItemFromLocation(this);
		WorldServer.mobList.remove(this.getName() + this.getId());
		
	}
	
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
	
	@Override
	public EnumSet<EquipmentEnum> getAllowedEquipSlots() {
		return EnumSet.noneOf(EquipmentEnum.class);
	}
	
	@Override
	public boolean containsType(Type type) {
		return false;
	}
	
	public static void insertNewBlankMob(String newName, String newPassword) throws IllegalStateException {
		String sql = "insert into mobstats (MOBID, MOBNAME, MOBPASS) values (NULL, '" + newName + "', '" + newPassword + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Failed to insert blank mobile via: " + sql);
			System.out.println(e.toString());
			throw new IllegalStateException("Database out of sync.");
		}
		String insertBook = "insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values((SELECT MOBID FROM MOBSTATS"
				+ " WHERE MOBNAME='" + newName + "'), 1, 1) ON DUPLICATE KEY UPDATE MOBPROGRESS=1;";
		try {
			SQLInterface.saveAction(insertBook);
		} catch(SQLException e) {
			System.out.println("Failed to insert blank mobile via: " + insertBook);
			System.out.println(e.toString());
			throw new IllegalStateException("Database out of sync.");
		}
	}

	@Override
	public GroundType getGroundType() {
		// TODO Auto-generated method stub
		return GroundType.GROUND;
	}

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
		// TODO Auto-generated method stub
		return maxHp;
	}

	@Override
	public int getCurrentHp() {
		// TODO Auto-generated method stub
		return currentHp;
	}

	@Override
	public SendMessage getSendBack() {
		return sendBack;
	}
}

	