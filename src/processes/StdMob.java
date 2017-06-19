package processes;

import interfaces.*;
import items.ItemBuilder;
import items.StdItem;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import effects.*;
import processes.Equipment.EquipmentEnum;
import processes.MobileDecorator.DecoratorType;

/**
 * A basic implentable of the interface Mobile, StdMob contains the basic methods for anything that can move around on it's own, whether
 * that is by a player or by AI.
 * <p>
 * Basic uses of this class should include rats, tigers, birds, bears. Heroes will probably need to be an extension of this class.
 * Undead might also be an extension, as they would have additional methods, or overwritten methods.
 * @author Jason
 */

@XStreamAlias("StdMob")
public class StdMob implements Mobile, Container{

	protected final String name;
	protected final String password;
	protected final int id;
	protected int maxHp;
	protected int currentHp; 
//	@XStreamOmitField
	protected Location mobLocation;
	protected boolean isDead;
	protected boolean isBlocking = false;
	protected final Equipment equipment;	
	protected int defense;
	protected String description; //displays on examine
	protected int xpWorth;
	protected String shortDescription; //displays on look as (name + shortDesc)
	protected int experience;
	protected int level;
	protected int age; 
	@XStreamOmitField
	protected SendMessage sendBack;
	protected boolean isControlled = false;
//	protected boolean loadOnStartUp = false;
	protected TreeMap<String, Holdable> inventory = new TreeMap<String, Holdable>();
	protected InductionSkill inductionSkill = null;
	@XStreamOmitField
	protected Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	protected List<ItemBuilder> dropsOnDeath;
	@XStreamOmitField
	protected EffectManager effectManager;
	@XStreamOmitField
	protected Mobile lastAggressor;
	protected ArrayList<String> messages;
	protected Set<PassiveCondition> allConditions;
	protected double currentWeight;
	protected Lock lock = new ReentrantLock();
	
	public StdMob(MobileBuilder build) {
		Mobile decoratedMob = decorate(build, this);
		build.setFinishedMob(decoratedMob);
		this.id = build.getId();
		this.name = build.getName();		
		this.password = build.getPassword();
		this.maxHp = build.getMaxHp();
		this.currentHp = getMaxHp();
		this.defense = build.getDefense();
		this.mobLocation = build.getLocation();
		this.isDead = build.isDead();
		this.description = build.getDescription();
		this.shortDescription = build.getShortDescription();	
		this.inventory.putAll(build.getInventory())	;
		this.dropsOnDeath = build.getDropsOnDeath();
		this.experience = build.getExperience();
		createNewEffectManager();		
		this.skillBookList = build.getSkillBookList();		
		this.equipment = build.getEquipment();
		this.allConditions = EnumSet.noneOf(PassiveCondition.class);
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
	
	
	// SHOULD MOVE TO a readResolve for XStream
	@Override public void createNewEffectManager() {
		effectManager = new EffectManager(this);
	}
	
	@Override public String getName() {return name;}	
	@Override public boolean isDead() {return isDead;}	
	
	@Override public boolean hasBalance() {
		return !hasCondition(PassiveCondition.BALANCE);
	}
	
	@Override public String getDescription() {
		return description;
	}
	
	@Override public String getShortDescription() {
		return name + shortDescription;
	}
	
	@Override public void addAllConditions(PassiveCondition condition) {
		allConditions.add(condition);
	}
	
	@Override public void removeAllConditions(PassiveCondition condition) {
		allConditions.remove(condition);
	}
	
	@Override public Set<PassiveCondition> getAllConditions() {
		return allConditions;
	}
	
	public boolean hasAllConditions(PassiveCondition condition) {
		return allConditions.contains(condition);
	}
	
	public void addDefense(int i) {
		//should calculate from equipment, effectors?, decorators, potions, herbs, etc
		this.defense = this.defense + i;
	}
	
	@Override public int getDefense() {
		return defense;
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
	public ContainerErrors acceptItem(Holdable item) {
		lock.lock();
		try {
			if ((getCurrentWeight() + item.getWeight()) > getMaxWeight()) {
				return ContainerErrors.QTYFULL;
			}
			inventory.put(item.getName().toLowerCase() + item.getId(), item);
			changeCurrentWeight(item.getWeight());
			return null;
		} finally {
			lock.unlock();
		}
	}
	
	// Put in Container AND remove from Container is complicated, but should be GUARANTEED in ONE LINE TODO
		@Override
		public void removeItemFromLocation(Holdable oldItem) {
			lock.lock();
			try {
				if (inventory.containsValue(oldItem)) {
					String key = oldItem.getName() + oldItem.getId();
					inventory.remove(key);
					changeCurrentWeight(-oldItem.getWeight());
				} else if (equipment.hasItem(oldItem)){
					equipment.remove(oldItem);
					removeItemFromLocation(oldItem);
					changeCurrentWeight(-oldItem.getWeight());
				} else {
					//TODO make this bug comment better
					System.out.println("StdMob removeItemFromLocation: An item was just attempted to be moved from an inventory that probably shouldn't have gotten this far.");
				}
			} finally {
				lock.unlock();
			}
		}
	
	@Override
	public synchronized void moveHoldable(Location finalLocation) {
			getContainer().removeItemFromLocation(this);
			finalLocation.acceptItem(this);
			this.mobLocation = finalLocation;	
	}
	
	@Override
	public synchronized Location getContainer() {
		return mobLocation;
	}
	
	@Override
	//TODO should be two methods or rename to changeLife
	public synchronized void takeDamage(Type type, int damage) {
		if(!(damage < 0)) {  //healing ignores defense
			damage = damage - defense;
		}
		if (currentHp < damage) {
			damage = currentHp;
		} 
		if ((currentHp - damage) > maxHp) {
			currentHp = maxHp;
		} else {
			this.currentHp = currentHp - damage;
			System.out.println(this.getName() + " damage: " + damage);
		}
		checkHp();
		displayPrompt();
	}	

	private void checkHp() {
		if (currentHp <= 0 && !isDead) {
			tell("You collapse to the ground, unable to fight on.");
			isDead = true;
			for (Holdable m : getContainer().getInventory().values()) {
				if (m instanceof Mobile && ((Mobile)m).isControlled() && !m.equals(this)) {
					((Mobile)m).tell(getName() + " drops to the floor, dead.");
				}
			}
			spawnDeathLoot();
		}
	}	
	
	public boolean isBlocking() {
		return isBlocking;
	}
	
	public void changeBlocking(boolean b) {
		isBlocking = b;
	}
	
	// Triggers only on death, used to spawn NEW items, not to drop held items.
	@Override
	public void spawnDeathLoot() {
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
	
	
	
	private void changeCurrentWeight(double change) {
		currentWeight += change;
	}

	// Returns view of Inventory, allows editing of objects within (which should be limited) but not to the inventory list.
	@Override
	public TreeMap<String, Holdable> getInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	// TODO SHOULD REWRITE AS: Try ceiling, then try range? Or ceiling, then floor, then range. ceiling is USUALLY correct
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		holdableString = holdableString.toLowerCase();
		String ceiling = inventory.ceilingKey(holdableString);
		String floor = inventory.floorKey(holdableString);
//		NavigableMap<String, Holdable> subMap = null;
//		if (ceiling != null && floor != null) {
//			subMap = inventory.subMap(floor, true, ceiling, true);
//		}
		
//		System.out.println(floor + " to " + ceiling + " with submap of " + subMap);
//		System.out.println(inventory.keySet());
		
		if (ceiling != null) {
			if ((ceiling.equalsIgnoreCase(holdableString) || inventory.get(ceiling).getName().equalsIgnoreCase(holdableString))) {
				return inventory.get(ceiling);
			}
		}
		if (floor != null) {
			if ((floor.equalsIgnoreCase(holdableString) || inventory.get(floor).getName().equalsIgnoreCase(holdableString))) {
				return inventory.get(floor);
			}
		} 
	//	if (ceiling != null && floor != null) {
	//		System.out.println(inventory.keySet());
	//		for (String s : inventory.keySet()) {
	//			if ((s.equalsIgnoreCase(holdableString) || inventory.get(s).getName().equalsIgnoreCase(holdableString))) {
	//				return inventory.get(s);
	//			}
	//		}
	//	}
		return null;
	}	
	
	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();		
		SortedMap<String, Holdable> subMap = inventory.subMap(holdableString, true, holdableString + Character.MAX_VALUE, true);		
		Collection<Holdable> set = new TreeSet<Holdable>(subMap.values());
		System.out.println("StdMob getListMatchingString: " + set.toString());		
		if (set.isEmpty() || set == null) {
			Holdable h = getHoldableFromString(holdableString);
			if (h != null) {
				set.add(h);
			}
		}
		return set;
	}
	
	@Override
	public boolean addPassiveCondition(PassiveCondition newEffect, int duration) {
		if (effectManager.hasCondition(newEffect)) {
			return false;
		}
		if (duration == -1) {
			return effectManager.registerPermanentPassiveEffect(newEffect);
		}
		return effectManager.registerPassiveEffectDestroyAfterXMilliseconds(newEffect, duration);
	}
	
	@Override
	public boolean addActiveCondition(TickingEffect newEffect, int times) {
		return effectManager.registerActiveEffect(newEffect, times);
	}
	
	@Override
	public void removeCondition(TickingEffect oldEffect) {
		effectManager.unRegisterEffect(oldEffect);	
	}	

	@Override
	public void removeCondition(PassiveCondition oldEffect) {
		effectManager.unRegisterEffect(oldEffect);	
	}
	
	@Override
	public boolean hasCondition(TickingEffect effect) {
		return effectManager.hasCondition(effect);
	}
	
	@Override
	public boolean hasCondition(PassiveCondition effect) {
		return effectManager.hasCondition(effect);
	}

	@Override
	public Set<TickingEffect> getAllActiveConditions() {
		return effectManager.getAllActiveEffects();
	}
	
	@Override 
	public void equip(EquipmentEnum slot, StdItem item) {		
		item.equip(this);
		equipment.equip(slot, item);
	}
	
	@Override
	public Equipment getEquipment() {
		return equipment;
	}
	
	@Override
	public void unEquip(Holdable item) {
		equipment.unEquip(item);
	}
	
	@Override
	public void unEquip(EquipmentEnum slot) {
		equipment.unEquip(slot);
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
	public void addBook(SkillBook skillBook, int progress) {
		if (skillBookList == null) {
			skillBookList = new HashMap<SkillBook, Integer>();
		}
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
	public void save() {
		WorldServer.saveMobile(this);
	}	

	@Override
	public void removeFromWorld() {	
		save();
		effectManager.shutDown();
		if (isInducting()) {
			inductionSkill.shutDown();
		}
		for (Holdable h : new HashSet<Holdable>(inventory.values())) {
			h.removeFromWorld();
		}
		mobLocation.removeItemFromLocation(this);
		WorldServer.gameState.removeMob(this.getName() + this.getId());
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
	
	public static void insertNewBlankMob(String newName, String newPassword) throws IllegalStateException {
		int newId = findNewId(newName);
		String sql = "insert into mobstats (MOBID, MOBNAME, MOBPASS) values (" + newId + ", '" + newName + "', '" + newPassword + "');";
	//	WorldServer.databaseInterface.saveAction(sql);
		String insertBook = "insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values((SELECT MOBID FROM MOBSTATS"
				+ " WHERE MOBNAME='" + newName + "'), 1, 1) ON DUPLICATE KEY UPDATE MOBPROGRESS=1;";
//		WorldServer.databaseInterface.saveAction(insertBook);
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
	}

	@Override
	public int getCurrentHp() {
		return currentHp;
	}
	
	public ArrayList<String> getMessages() {
		return messages;
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
	
	// TODO
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
	public boolean isInducting() {
		if (inductionSkill == null) {
			return false;
		}
		return true;
	}

	@Override
	public void killInduction() {
		inductionSkill.interrupt();
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

	// TODO
	public int compareTo(Mobile other) {
		String thisItem = this.getName()+this.getId();
		String otherItem = other.getName()+other.getId();
		return thisItem.compareToIgnoreCase(otherItem);
	}	
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!(obj instanceof Mobile)) {
	    	return false;
	    }
	    final Mobile other = (Mobile) obj;
	    String thisHoldable = this.getName() + this.getId();
	    String otherHoldable = other.getName() + other.getId();
	    return thisHoldable.equals(otherHoldable);
	 //   if ((thisHoldable.compareToIgnoreCase(otherHoldable)) >= 0) {
	  //  	return true;
	   // }
	//    return false;
	}
	
	private Object readResolve() {
    	addBook(CreateWorld.generalSkills.duplicate(),100);
    	WorldServer.gameState.addMob(name + id, this);
    	getContainer().acceptItem(this);
    	createNewEffectManager();	 
    	controlStatus(true);
		for (Holdable h: inventory.values()) {
			h.setContainer(this);
		}
		for (Holdable h : equipment.values()) {
			if (h != null) {
				h.setContainer(this);
			}
		}
		return this;
	}

	// Not safe to use? But required for save load
	@Override
	public void setContainer(Location container) {
		this.mobLocation = (Location) container;
	
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getMaxWeight() {
		return 100;
	}

	@Override
	public double getCurrentWeight() {
		return currentWeight;
	}

	@Override public void changeWeight(double change) {
		this.currentWeight += change;
	}



}

	