package processes;

import interfaces.*;
import items.StdItem;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import effects.Balance;
import processes.Equipment.EquipmentEnum;
import processes.Location.GroundType;

/**
 * A basic implentable of the interface Mobile, StdMob contains the basic methods for anything that can move around on it's own, whether
 * that is by a player or by AI.
 * <p>
 * Basic uses of this class should include rats, tigers, birds, bears. Heros will probably need to be an extension of this class.
 * Undead might also be an extension, as they would have additional methods, or overwritten methods.
 * @author Jason
 */
//TODO Make a MobileBuilder that handles creation.
public class StdMob implements Mobile, Container, Holdable, Creatable {

	protected final String name;
	protected final String password; //Name + password is required to enter any role.
	protected final int id;
	protected int maxHp;
	protected int currentHp; 
	protected Container mobLocation; 
	protected int physicalMult; //???
	protected boolean isDead;
	protected int speed; // ??? Or should this and physical mult be class constants?
	protected Set<Holdable> inventory; // Make this be combined with equipment as its own class?
	protected Equipment equipment;	
	
	protected String description;
	protected int xpWorth;
	protected String shortDescription;
	protected ArrayList<String> bugList;
	protected ArrayList<String> messages;
	protected int experience;
	protected int level;
	protected int age; 
	protected SendMessage sendBack;
	protected int baseDamage;
	protected TickClient tickClient;
	protected boolean isControlled = false;
	protected boolean loadOnStartUp = false;
	
	protected Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	
	protected ArrayList<Effect> effectList;
	
	/*
	 * Constructor for StdMob takes in an internal builder class that represents all the needed data, though does not require everything
	 * to be not null.
	 */
	protected StdMob(MobileBuilder build) {
		this.name = build.name;
		this.id = build.id;
		this.password = build.password;
		this.maxHp = build.maxHp;
		this.currentHp = maxHp;
		this.mobLocation = build.location;
	//	this.physicalMult = build.physicalMult;
		this.isDead = false;
	//	this.speed = build.speed;
		this.description = build.description;
		this.shortDescription = build.shortDescription;
		this.inventory = build.inventory;
		this.equipment = build.equipment;
		equipment.setOwner(this);
		
		this.bugList = new ArrayList<String>();
		this.messages = new ArrayList<String>();
		this.effectList = build.effectList;
//		this.tickClient = new TickClient(this);
//		tickClient.start();
		WorldServer.mobList.put(name + id, this);
	}
	
	/**
	 * Builder class for StdMob, used to gather all details of the Mobile being put together before officially finishing.
	 * <p>
	 * Written very generically because it extends mobile and will be extended by other classes and it allows super() hiarchies
	 * to work with the internal builder classes, other classes should follow suit that extend StdMob.
	 * @author Jason	 *
	 * @param <T> Generic way of representing the builder implemented by extended class Mobile
	 */
/*	protected static abstract class Init<T extends Init<T>> {
	
		private final String name;
		private final int id;		
		private String description = "Generic.";
		private String shortDescription = "Short and Generic.";
		private int maxHp = 100;
		private Container location = WorldServer.locationCollection.get(1);
		private int physicalMult = 1;
		private int speed = 3000;
		private int xpWorth = 1;
		private int baseDamage = 5;
		private Set<Holdable> inventory = new HashSet<Holdable>();
		private Equipment equipment = new Equipment();
		
		private String password = "";
		private ArrayList<Effect> effectList = new ArrayList<Effect>();
		
		protected abstract T self();		
		
		/**
		 * Constuctor for StdMob builder, checks that desired Mobile isn't already built, then initiates the only REQUIRED information.
		 * @param id Mobile id desired.
		 * @param name Mobile name desired.
		 */
	/*	public Init(int id, String name) {
			if (name == null) {
				throw new IllegalArgumentException("Name may not be null.");
			}
			Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");
			Matcher matcher = pattern.matcher(name);
			if (WorldServer.mobList.containsKey(name + id)) {
				throw new IllegalStateException("A mobile already exists with that name and id.");
			}
			if (name.matches(".*\\d.*") || matcher.find() || name.equals("")) {
				throw new IllegalArgumentException("Name is considered invalid: " + name);	
			}
			this.id = id;
			this.name = name;
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
		
		
		
		// The below are effectively the Builder's constructor methods. As they will be gathering data for the finalized StdMob.
		public T password(String val) {password = val;return self();}		
		public T description(String val) {description = val;return self();}		
		public T shortDescription(String val) {shortDescription = val;return self();}		
		public T maxHp(int val) {maxHp = val;return self();}		
		public T location(Container val) {location = val;return self();}		
		public T physicalMult(int val) {physicalMult = val;return self();}		
		public T speed(int val) {speed = val;return self();}		
		public T inventory(Holdable val) {inventory.add(val);return self();}
		public T equipment(EquipmentEnum slot, StdItem val) {equipment.forceEquip(slot, val); return self();}
		
		public T xpWorth(int val) {xpWorth = val;return self();}	
		public T baseDamage(int val) {baseDamage = val;return self();}
		public T effect(Effect effect) {effectList.add(effect); return self();}
		public StdMob build() {return new StdMob(this);}}	
	
	public static class Builder extends Init<Builder> {
		public Builder(int id, String name) {
			super(id, name);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}*/
	
	public String getName() {return name;}	
	//Turn into a compare password? Is that safer?
	public String getPassword() {return password;}	
	public int getId() {return id;}	
	public int getCurrentHp() {return currentHp;}	
	public int getMaxHp() {return maxHp;}	
	public boolean isDead() {return isDead;}	
	public int getSpeed() {return speed;}	
	public boolean hasBalance() {
		if (hasEffect(Balance.class)) {
			return false;
		}
		return true;
		}
	public String getDescription() {return description;}	
	public String getShortDescription() {return shortDescription;}	
	public int getXpWorth() {return xpWorth;}	
	// why the null check?
	public Skill getCommand(String command) {
		for (SkillBook sb : skillBookList.keySet()) {
			Skill skill = sb.getSkill(command);
			if (skill != null) {
				return skill;
			}
		}
		return null;
	}
	
	//Refactor with other book stuff?
	// copy or original?
	@Override
	public SkillBook getBook(String bookName) {
		for (SkillBook sb : skillBookList.keySet()) {
			if (sb.getName().equals(bookName)) {
				return sb;
			}
		}
		return null;
	}
	
	public void acceptItem(Holdable item) {inventory.add(item);}
	// Not implemented
	public int getMessagesSize() {return messages.size();}	
	// Not implemented
	public void addBug(String bugMsg) {bugList.add(bugMsg);}

	@Override
	public synchronized void setContainer(Container futureLocation) {this.mobLocation = futureLocation;}
	
	@Override
	public synchronized Container getContainer() {return mobLocation;}
	
	// should the runEffects be in the skill?
	// Take damage should be here, so that an undead knows it takes double damage from holy without the skill needing to know this is undead.
	@Override
	public void takeDamage(Set<Type> types, int damage) {
		damage = runEffects(types, damage);
		if (currentHp < damage) {
			damage = currentHp;
		}
		this.currentHp = currentHp - damage;
		checkHp();
	}
	
	// Needs to actually involving dying...
	protected void checkHp() {
		if (currentHp <= 0 && !isDead) {
			tell("You colapse to the ground, unable to fight on.");
			isDead = true;
		}
	}	
	
	public void tell(String msg) {
		if (this.sendBack != null) {
			sendBack.printMessage(msg);
		} 
	}

	public void tellLine(String msg) {
		if (this.sendBack != null) {
			sendBack.printMessageLine(msg);
		}	
	}
	// Not implemented
	public void addExperience(int exp) {
		this.experience += exp;
		levelMobile();
	}
	// Not implemented
	//Check this? Good for mobiles or too specific?
	public void levelMobile() {
		if (this.level < WorldServer.Levels.length -1) { // Too specific?
			while (this.experience >= WorldServer.Levels[this.level]) {
				this.level += 1;
				tell("Congratulations, you are now level " + this.level + "!");
				this.maxHp = (120 + this.level * 100); // Needs to raise current hp as well?
			}
			while (this.experience < WorldServer.Levels[this.level - 1] && this.level > 1) {
				this.level -= 1;
				tell("Disaster! You are now level " + this.level + "!");
				this.maxHp = (120 + this.level * 100); 
				if (currentHp > maxHp) {
					currentHp = maxHp;
				}
			}
			
		}
	}
	
	
	/* Goes into AI?
	public void checkHp(SendMessage enemySendBack, Player enemyPlayer) {
		if (this.currentHp <= 0 && !dead) {
			enemySendBack.printMessage("You have killed " + shortDescription + ".");
			enemyPlayer.addExperience(xpWorth);
			if ((this instanceof Mob)) {	
				DeathLength death = new DeathLength((Mob) this);
				death.start();
				Mob isThis = (Mob) this;
				isThis.hostile = false;
			}
		}
	}*/
	
	
	public synchronized void removeItem(Holdable item) {
		if (inventory.contains(item)) {
			inventory.remove(item);
		}
		// TODO
		// DOES NOT DO ANYTHING IF IT DOESN"T CONTAIN? 
	}	

	@Override
	public Creatable create() {
		// TODO Auto-generated method stub
		return null;
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
	// SHould I allow this?
/*	@Override
	public Equipment getEquipment() {
		return equipment;
	}*/
	
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
	public void addEffect(Effect effect) {
		effectList.add(effect);
		WorldServer.executor.schedule(effect, effect.getDuration(), TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void removeEffect(Effect effect) {
		System.out.println(effectList.toString());
		effectList.remove(effect);		
		System.out.println(effectList.toString());
	}
	
//	public Effect getEffect(String effect) {
//		if (hasEffect(effect)) {
//			return effectList.get(effect);
//		}
//		return null;
//	}
	
	public boolean hasEffect(Class<? extends Effect> effect) {
		System.out.println(effect);
		for (Effect e: effectList) {
			if (e.getClass().isInstance(effect)) {
				return true;
			}
		}
		return false;
	}
	
	public void runTickEffects() {
		Iterator<Effect> iter = effectList.iterator();
		while (iter.hasNext()) {
			Effect effect = (Effect) iter.next();
			effect.doTickEffect();
			if (effect.wasRemoved()) {
				iter.remove();
				removeEffect(effect);
			}
		}
	}
	@Override
	public int runEffects(Set<Type> incomingTypes, int damage) {
		Iterator iter = effectList.iterator();
		while (iter.hasNext()) {
			Effect effect = (Effect) iter.next();
			damage = effect.doRunEffect(incomingTypes, damage);
			if (effect.wasRemoved()) {
				iter.remove();
				removeEffect(effect);
			}
		}
		return damage;
	}
	
	@Override
	public int getBaseDamage() {
		return baseDamage;
	}
	
	@Override
	public int getTick() {
//		return tickClient.getTick();
		return 0;
	}
	
	public boolean hasMana(int mana) {
		// TODO
		return true;
	}
	
	public void affectMana(int mana) {
		// TODO
		//meh
	}

	//TODO
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
	
	/*public boolean hasWeaponType(Type type) {
		StdItem right = equipment.getValue("rightarm");
		StdItem left = equipment.getValue("leftarm");
		if (right != null && right.containsType(type)) {
			return true;
		} else if (left != null && left.containsType(type)) {
			return true;
		}
		return false;
	}*/
	
	@Override
	public Container getContainer(String dir) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GroundType getGroundType() {
		return GroundType.CONTAINER;
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
	
	/*public static Mobile newMobile() {
		// This should make sure that an id is saved for this mobile in the database, and that we know it here.
		// Then it should load in a default baseline for that character, things like moved and the such.
		// I was thinking there could be packets in java that define what skills they have, but this might be linked
		// With how learning works for unlocking skills, which I haven't figured out yet.
		// So, it loads a packet with certain skills like move, or it could copy a default player on the database, which would
		// in effect give it the basic skills and such.
		
		Mobile godSkin = SQLInterface.loadPlayer("defaultGod", "defaultGod");
		
		String insertStats = "INSERT INTO MOBSTATS (MOBNAME, MOBPASS, MOBDESC, MOBSHORTD, MOBLOC, MOBTYPE, LOADONSTARTUP) values "
				+ "('" + name + "', '" + password + "', '" + description + "', '" + shortDescription + "', " + mobLocation.getId()
				+ ", '" + this.getClass().getSimpleName() + "', " + loadOnStartUp + ");";
		System.out.println(insertStats);
		return SQLInterface.saveAction(insertStats);
	}*/
	
	
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
	public SendMessage getSendBack() {
		return sendBack;
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean containsType(Type type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void insertNewBlankMob(String newName, String newPassword) throws SQLException {
		String sql = "insert into mobstats (MOBID, MOBNAME, MOBPASS) values (NULL, '" + newName + "', '" + newPassword + "');";
		SQLInterface.saveAction(sql);
		String insertBook = "insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values((SELECT MOBID FROM MOBSTATS"
				+ " WHERE MOBNAME='" + newName + "'), 1, 1) ON DUPLICATE KEY UPDATE MOBPROGRESS=1;";
		SQLInterface.saveAction(insertBook);
	}
}

	