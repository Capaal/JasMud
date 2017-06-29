package processes;

import interfaces.*;
import items.ItemBuilder;
import items.Plant.PlantType;
import items.StdItem;

import java.util.*;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import effects.*;
import processes.Equipment.EquipmentSlot;
import processes.MobileDecorator.DecoratorType;
import skills.MoveFollow;

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
	protected TreeMap<String, Holdable> inventory;
	@XStreamOmitField
	protected InductionSkill inductionSkill = null;
//	@XStreamOmitField
	protected Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	protected List<ItemBuilder> dropsOnDeath;
	@XStreamOmitField
	protected EffectManager effectManager;
	@XStreamOmitField
	protected Mobile lastAggressor;
	protected ArrayList<String> messages;
	protected double currentWeight;
	@XStreamOmitField
	protected ArrayList<Mobile> followers = new ArrayList<Mobile>();
	@XStreamOmitField
	protected Mobile following;
	protected Set<Cooldown> cooldowns; 
	
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
		this.inventory = new TreeMap<String, Holdable>();
		this.inventory.putAll(build.getInventory())	;
	
		this.dropsOnDeath = build.getDropsOnDeath();
		this.experience = build.getExperience();
		effectManager = new EffectManager(this);		
		this.skillBookList = build.getSkillBookList();		
		this.equipment = build.getEquipment();
		this.cooldowns = build.getCooldowns();
		WorldServer.getGameState().addMob(decoratedMob.getName() + decoratedMob.getId(), decoratedMob);
		WorldServer.getGameState().addNewPlayer(this.name, decoratedMob);
		decoratedMob.getContainer().acceptItem(decoratedMob);
		this.messages = new ArrayList<String>();		
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
	@Override public String getNameColored() {return UsefulCommands.ANSI.GREEN + name + UsefulCommands.ANSI.SANE;}	
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
			if (skill != null) {
				return skill;	
			}
		}
		return null;
	}
	
	@Override
	public synchronized ContainerErrors acceptItem(Holdable item) {
		if ((getCurrentWeight() + item.getWeight()) > getMaxWeight()) {
			return ContainerErrors.QTYFULL;
		}
		inventory.put(item.getName().toLowerCase() + item.getId(), item);
		changeCurrentWeight(item.getWeight());
		return null;
	}
	
	@Override
	public synchronized void removeItemFromLocation(Holdable oldItem) {
		if (inventory.containsValue(oldItem)) {
			String key = oldItem.getName() + oldItem.getId();
			inventory.remove(key);
			changeCurrentWeight(-oldItem.getWeight());
		} else if (equipment.hasItem(oldItem)){
			equipment.unwield(oldItem);
			removeItemFromLocation(oldItem);
		} else {
			//TODO make this bug comment better
			System.out.println("StdMob removeItemFromLocation: An item was just attempted to be moved from an inventory that probably shouldn't have gotten this far.");
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
		if(!(damage < 0)) {  //adjust dmg for defense
			damage = damage - defense;
		}
		if (currentHp < damage) { //adjust dmg so hp won't be neg
			damage = currentHp;
		} 
		if (currentHp == maxHp && damage < 0) { //stop displaying prompt when no effect
			return;
		}
		if ((currentHp - damage) > maxHp) { // healed to max life
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
			for (Mobile m : getContainer().getMobiles().values()) {
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
	public TreeMap<String, Holdable> viewInventory() {
		TreeMap<String, Holdable> inventoryView = new TreeMap<String, Holdable>(this.inventory);
		inventoryView.putAll(equipment.viewEquipment());
		return inventoryView;
	}
	
	@Override
	public TreeMap<String, Holdable> viewInventoryWithoutEquipment() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	// TODO SHOULD REWRITE AS: Try ceiling, then try range? Or ceiling, then floor, then range. ceiling is USUALLY correct
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		TreeMap<String, Holdable> inventoryView = viewInventory();
		holdableString = holdableString.toLowerCase();		
		String ceiling = inventoryView.ceilingKey(holdableString);
		String floor = inventoryView.floorKey(holdableString);			
		String hasNum = UsefulCommands.getOnlyNumerics(holdableString);
		if (ceiling != null) {
			if (!hasNum.equals("") && ceiling.equalsIgnoreCase(holdableString)) {
				return inventoryView.get(ceiling);				
			} else if (ceiling.toLowerCase().startsWith(holdableString)) {
				return inventoryView.get(ceiling);				
			}
		}
		if (floor != null) {
			if (!hasNum.equals("") && floor.equalsIgnoreCase(holdableString)) {
				return inventoryView.get(floor);				
			} else if (floor.toLowerCase().startsWith(holdableString)) {
				return inventoryView.get(floor);				
			}
		}
		return null;	
		
		/*
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
		return null;*/
	}	
	
	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		TreeMap<String, Holdable> inventoryView = viewInventory();
		holdableString = holdableString.toLowerCase();		
		SortedMap<String, Holdable> subMap = inventoryView.subMap(holdableString, true, holdableString + Character.MAX_VALUE, true);		
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
	public EnumSet<PassiveCondition> getAllPassiveEffects() {
		return effectManager.getAllPassiveEffects();
	}
	
	@Override 
	public void equip(EquipmentSlot slot, StdItem item) {	
		String key = item.getName() + item.getId();
		if (inventory.remove(key) != null) {
			changeCurrentWeight(-item.getWeight());
			equipment.wield(item, slot);
		} else if (equipment.hasItem(item)) {
			equipment.unwield(item);
			equip(slot, item);
		}
	}
	
//	@Override
//	public Equipment getEquipment() {
//		return equipment;
//	}
	
	@Override
	public void unEquip(Holdable item) {
		equipment.unwield(item);
	}
	
	@Override
	public void unEquip(EquipmentSlot slot) {
		equipment.unwield(slot);
	}
	
	@Override
	public Holdable getEquipmentInSlot(EquipmentSlot slot) {
		return equipment.getEquipmentInSlot(slot);
	}
	
	//TODO
/*	@Override
	public EquipmentSlot findEquipment(String itemName) {
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
	}*/
	
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
		WorldServer.getGameState().saveMobile(this);
	}	

	@Override
	public void removeFromWorld() {	
//		save();
		if (isInducting()) {
			inductionSkill.shutDown();
		}
	//	for (Holdable h : new HashSet<Holdable>(inventory.values())) {
	//		h.removeFromWorld();
	//	}
		mobLocation.removeItemFromLocation(this);
	//	WorldServer.gameState.removeMob(this.getName() + this.getId());
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
	
	public ArrayList<String> getAllMessages() {
		return messages;
	}
	
	public void addMessage(String msg) {
		messages.add(msg);
	}
	
	public String readMessage(int i) {
		return messages.get(i);
	}
	
	public void deleteMessage(int i) {
		messages.remove(i);
	}

	@Override
	public void informLastAggressor(Mobile aggressor) {
		this.lastAggressor = aggressor;		
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
		if (inductionSkill != null) {
			inductionSkill.interrupt();
			inductionSkill = null;
		}
	}
	
	@Override
	public void setInduction(InductionSkill skill) {
		this.inductionSkill = skill;
	}
	
	@Override
	public Map<SkillBook, Integer> viewSkillBooks() {
		return new HashMap<SkillBook, Integer>(skillBookList);
	}
	
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
		followers = new ArrayList<Mobile>();
    	getContainer().acceptItem(this);
    	effectManager = new EffectManager(this); 
    	controlStatus(true);
		for (Holdable h: inventory.values()) {
			h.setContainer(this);
		}
		return this;
	}

	// Not safe to use? But required for save load
	@Override
	public void setContainer(Location container) {
		this.mobLocation = (Location) container;
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

	@Override
	public void moveFollowers(String fullCommand) {
		for (Mobile m : followers) {
			WorldServer.getGameState().addToQueue(new MoveFollow(m, fullCommand));
		}
	}

	@Override
	public void stopFollowing() {
		if (following != null) {
			following.removeFollower(this);
			following = null;
		}
	}

	@Override
	public void setFollowing(Mobile finalTarget) {
		following = finalTarget;
	}

	@Override
	public void addFollower(Mobile newFollower) {
		followers.add(newFollower);		
	}
	
	@Override
	public void removeFollower(Mobile follower) {
		followers.remove(follower);
	}
	
	@Override
	public void addCooldown(Cooldown c) {
		cooldowns.add(c);
	}
	
	@Override
	public void removeCooldown(Cooldown c) {
		cooldowns.remove(c);
	}
	
/*	@Override
	public void cooldownOn(PlantType p) {
		eatPlantCooldowns.put(p, true);
	}
	
	@Override
	public void cooldownOff(PlantType p) {
		eatPlantCooldowns.put(p, false);
	}*/
	
	@Override
	public boolean isOnCooldown(Cooldown c) {
		for (Cooldown cd : cooldowns) {
			System.out.println(cd.equals(c));
			System.out.println(c.equals(cd));
		}
		System.out.println("Check " +cooldowns);
		System.out.println(cooldowns.contains(c));
		return cooldowns.contains(c);

	}


}

	