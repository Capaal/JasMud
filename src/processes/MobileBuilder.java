package processes;

import interfaces.Cooldown;
import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder;
import items.StdItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import MobileAI.MobileDecorator.DecoratorType;
import processes.Equipment.EquipmentSlot;

// For preparing a new Mobile. Once all desired stat setters are called to move away from the defaults call complete() to create.
public class MobileBuilder {	

	private String name = "";
	private int id = -1;	
	private String password = "";
	private String description = "This is a mob.";
	private String shortDescription = ""; //blank for real players?
	private int maxHp = 100;
	private int currentHp = 100; //probably don't need this
	private int maxMana = 100;
	private int currentMana = 100;
	private int defense = 0;
	private double balanceMult = 1;
	private Location location = WorldServer.getGameState().viewLocations().get(1);
	private boolean isDead = false;
	private int xpWorth = 1;
	private int experience = 1;
	private int level = 1;
	private int age = 1;
	private boolean loadOnStartUp = false;
	private Map<String, Holdable> inventory = new TreeMap<String, Holdable>();
	private Equipment equipment = new Equipment(null);	
	private Stack<DecoratorType> decorators = new Stack<DecoratorType>();
	private Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	private List<ItemBuilder> dropsOnDeath = new ArrayList<ItemBuilder>();
	private boolean buildComplete = false;
	private Mobile finishedMob;
	private String className;
	private Set<Cooldown> cooldowns = new HashSet<Cooldown>();
	
	public MobileBuilder() {
	}	
	
	public boolean buildCompleted() {
		return buildComplete;
	}
	
	public void setName(String val) throws IllegalArgumentException {
		if (val != null && UsefulCommands.checkIfValidCharacters(val)) {
			name = val;
		} else {
			throw new IllegalArgumentException("Name given is invalid: " + val);
		}
	}
	
	public Equipment getEquipment() {
		if (finishedMob == null) {
			throw new IllegalStateException();
		}
		return new Equipment(equipment, finishedMob);
	}
	
	public void setEquipment(EquipmentSlot slot, StdItem item) {
		equipment.wield(item, slot);
	}	
	
	public int getDefense() {
		return defense;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public int getMaxHp() {
		return maxHp;
	}
	
	public int getMaxMana() {
		return maxMana;
	}
	
	public double getBalanceMult() {
		return balanceMult;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public void setIsDead(int val) {
		if (val == 1) {
			isDead = true;
		} else {
			isDead = false;
		}
	}

	public int getXpWorth() {return xpWorth;}

	public void setXpWorth(int xpWorth) {this.xpWorth = xpWorth;}

	public int getExperience() {return experience;}

	public void setExperience(int experience) {this.experience = experience;}

	public int getLevel() {return level;}

	public void setLevel(int level) {this.level = level;}

	public int getAge() {return age;}

	public void setAge(int age) {this.age = age;}

	public boolean isLoadOnStartUp() {return loadOnStartUp;}

	public void setLoadOnStartUp(boolean loadOnStartUp) {this.loadOnStartUp = loadOnStartUp;}

	public Map<String, Holdable> getInventory() {return new TreeMap<String, Holdable>(inventory);	}

	public void setInventory(TreeMap<String, Holdable> inventory) {this.inventory = inventory;	}

	public String getName() {return name;}

	public int getId() {return id;}

	public void setMaxHp() {
		maxHp = 100 + level * 5;
		if (currentHp > maxHp) {
			currentHp = maxHp;
		}
	}
	
	public boolean complete() {
		setMaxHp();	
		handleId();
		buildComplete = true;
		new StdMob(this);		
		return true;
	}
	
	public Mobile getFinishedMob() {return finishedMob;}
	
	private synchronized void handleId() {
		Map<String, Integer> idMap = WorldServer.getGameState().idMap;
		if (idMap.containsKey("mobile")) {
			this.id = idMap.get("mobile") + 1;
			idMap.put("mobile", this.id);			
		} else {
			this.id = 1;
			idMap.put("mobile",  this.id);
		}
	}
	
	public void addDecorator(DecoratorType aggresiveMobileDecorator) {
		this.decorators.push(aggresiveMobileDecorator);		
	}

	public boolean hasNextDecorator() {
		if (decorators.isEmpty()) {
			return false;
		}
		return true;
	}

	public DecoratorType getNextDecorator() {
		return decorators.pop();
	}

	public Map<SkillBook, Integer> getSkillBookList() {
		return skillBookList;
	}

	public void addSkillBook(SkillBook book, int level) {
		skillBookList.put(book, level);		
	}

	public void dropOnDeath(ItemBuilder skeletonBody) {
		dropsOnDeath.add(skeletonBody);		
	}

	public List<ItemBuilder> getDropsOnDeath() {
		return dropsOnDeath;
	}

	public void setFinishedMob(Mobile decoratedMob) {
		finishedMob = decoratedMob;
	}

	public Set<Cooldown> getCooldowns() {
		return cooldowns;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String classN) {
		this.className = classN;
	}	
}
