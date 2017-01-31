package processes;

import interfaces.Container;
import interfaces.Effect;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import actions.Godcreate;
import processes.Equipment.EquipmentEnum;
import processes.MobileDecorator.DecoratorType;

public class MobileBuilder {	

	private String name = "";
	private int id = -1;	
	private String password = "";
	private String description = "Generic.";
	private String shortDescription = "Short and Generic.";
	private int maxHp = 100;
	private int currentHp = 100;
	private Container location = WorldServer.gameState.viewLocations().get(1);
	private boolean isDead = false;
	private int xpWorth = 1;
	private int experience = 1;
	private int level = 1;
	private int age = 1;
	private boolean loadOnStartUp = false;
	private Set<Holdable> inventory = new HashSet<Holdable>();
	private Equipment equipment = new Equipment();	
	private ArrayList<Effect> effectList = new ArrayList<Effect>();	
	private Stack<DecoratorType> decorators = new Stack<DecoratorType>();
	private Map<SkillBook, Integer> skillBookList = new HashMap<SkillBook, Integer>();
	private List<ItemBuilder> dropsOnDeath = new ArrayList<ItemBuilder>();
	private boolean buildComplete = false;
	private StdMob finishedMob;
	
	public MobileBuilder() {
		// might change based on implementation.
		setEquipment(EquipmentEnum.HEAD,  null);
		setEquipment(EquipmentEnum.NECK,  null);
		setEquipment(EquipmentEnum.LEFTEAR,  null);
		setEquipment(EquipmentEnum.RIGHTEAR,  null);
		setEquipment(EquipmentEnum.LEFTHAND,  null);
		setEquipment(EquipmentEnum.RIGHTHAND,  null);
		setEquipment(EquipmentEnum.CHEST,  null);
		setEquipment(EquipmentEnum.LEGS,  null);
		setEquipment(EquipmentEnum.FEET,  null);
		setEquipment(EquipmentEnum.LEFTFINGER,  null);
		setEquipment(EquipmentEnum.RIGHTFINGER,  null);
	}	
	
	public boolean buildCompleted() {
		return buildComplete;
	}
	
	public void setId(int val) {id = val;}
	
	public void setName(String val) throws IllegalArgumentException {
		if (val != null && UsefulCommands.checkIfValidCharacters(val)) {
			name = val;
		} else {
			throw new IllegalArgumentException("Name given is invalid: " + val);
		}
	}
	
	public Equipment getEquipment() {
		return new Equipment(equipment);
	}
	
	public void setEquipment(EquipmentEnum slot, StdItem item) {
		equipment.forceEquip(slot, item);
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

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public Container getLocation() {
		return location;
	}

	public void setLocation(Container location) {
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

	public Set<Holdable> getInventory() {return new HashSet<Holdable>(inventory);	}

	public void setInventory(Set<Holdable> inventory) {this.inventory = inventory;	}

	public List<Effect> getEffectList() {return new ArrayList<Effect>(effectList);	}

	public void setEffectList(ArrayList<Effect> effectList) {this.effectList = effectList;	}

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
		if (id == -1) {
			try {
				setId();
			} catch (IllegalStateException e) {
				System.out.println("New mobile failed to obtain unique id, it was not created.");
				return false;
			} 	
		}
		buildComplete = true;
		finishedMob = new StdMob(this);		
		return true;
	}
	@Deprecated
	public StdMob getFinishedMob() {return finishedMob;} // DOES NOT WORK WITH DECORATORS
	
	private void setId() {
/*		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN mobstats ON sequencetable.sequenceid = mobstats.mobid"
				+ " WHERE mobstats.mobid IS NULL";			
		Object availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
			if (availableId == null || !(availableId instanceof Integer)) {
				throw new IllegalStateException("The mobile could not determine a valid id, it is invalid.");				
			} else {
				id = (int)availableId;
			}
		} else {
			id = (int)availableId;
		}	*/
	}

	public static boolean newMobile(Mobile player, MobileBuilder mobileBuilder) {
		String nextTask = Godcreate.askQuestion("What next? 1:name 2:password 3:longdescrption 4:shortdescription"
				+ " 5:level 6:currentHp 7:isdead 8:location 9:inventory 10:equipment 11:xpworth 12:loadonstartup"
				+ " 13:preview 14:complete 15:quit", player);
		switch(nextTask) {
		case "1":
		case "name":
			mobileBuilder.setName(Godcreate.askQuestion("What is the mobile's name?", player));
			return newMobile(player, mobileBuilder);
		case "2":
		case "password":
			mobileBuilder.setPassword(Godcreate.askQuestion("What is the mobile's password?", player));
			return newMobile(player, mobileBuilder);
		case "3":
		case "longdescription":
			mobileBuilder.setDescription(Godcreate.askQuestion("What is the mobile's long description?", player));
			return newMobile(player, mobileBuilder);
		case "4":
		case "shortdescription":
			mobileBuilder.setShortDescription(Godcreate.askQuestion("What is the mobile's short description?", player));
			return newMobile(player, mobileBuilder);
		case "5":
		case "level":
			try {
				mobileBuilder.setLevel(Integer.parseInt(Godcreate.askQuestion("What level will this mobile be at?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not formatted as a valid number.");
			}
			return newMobile(player, mobileBuilder);
		case "6":
		case "currentHp":
			try {
				mobileBuilder.setCurrentHp(Integer.parseInt(Godcreate.askQuestion("What hp will the mobile start at?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not formatted as a valid number.");
			}
			return newMobile(player, mobileBuilder);
		case "7":
		case "isdead":
			mobileBuilder.setIsDead(Boolean.parseBoolean(Godcreate.askQuestion("true/false is the mobile already dead?", player)));
			player.tell("isDead has been set to: " + mobileBuilder.isDead);
			return newMobile(player, mobileBuilder);
		case "8":
		case "location":		
			int locationId = Integer.parseInt(Godcreate.askQuestion("What location id will this mobile start in?", player));
			Location location = WorldServer.gameState.viewLocations().get(locationId);
			if (location != null) {
				mobileBuilder.setLocation(location);
			} else {
				player.tell("Could not find that location id.");
			}		
			return newMobile(player, mobileBuilder);
		case "9":
		case "inventory": // NOT IMPLEMENTED TODO
			player.tell("Setting up inventory is not implemented yet.");
			return newMobile(player, mobileBuilder);
		case "10":
		case "equipment": // NOT IMPLEMENTED TODO
			player.tell("Setting up equipment is not implemented yet.");
			return newMobile(player, mobileBuilder);
		case "11":
		case "xpworth":
			try {
				mobileBuilder.setXpWorth(Integer.parseInt(Godcreate.askQuestion("What is the mobile's xp worth?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not formatted as a valid number.");
			}
			return newMobile(player, mobileBuilder);
		case "12":
		case "loadonstartup":
			mobileBuilder.setLoadOnStartUp(Boolean.parseBoolean(Godcreate.askQuestion("true/false should the mobile load on startup?", player)));
			player.tell("loadOnStartUp has been set to: " + mobileBuilder.loadOnStartUp);
			return newMobile(player, mobileBuilder);
		case "13":
		case "preview":
			player.tell("Name: " + mobileBuilder.name);
			player.tell("password: " + mobileBuilder.password);
			player.tell("longdescription: " + mobileBuilder.description);
			player.tell("Shortdescription: " + mobileBuilder.shortDescription);
			player.tell("MaxHp: " + (100 + mobileBuilder.level * 5));
			player.tell("CurrentHp: " + mobileBuilder.currentHp);
			player.tell("level: " + mobileBuilder.level);
			player.tell("isDead: " + mobileBuilder.isDead);
			player.tell("Location: " + mobileBuilder.location.getId());
			player.tell("xpWorth: " + mobileBuilder.xpWorth);
			player.tell("LoadOnStartUp: " + mobileBuilder.loadOnStartUp);
			return newMobile(player, mobileBuilder);
		case "14":
		case "complete":
			Mobile newMob = new StdMob(mobileBuilder);
			newMob.getContainer().acceptItem(newMob);
			return true;
		case "15":
		case "quit":
		case "exit":
			return false;
		default:
			player.tell("That wasn't a valid option, remember, you can use the word, or the number.");
			return newMobile(player, mobileBuilder);
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

	public void addSkillBook(SkillBook book) {
		skillBookList.put(book, 100);		
	}

	public void dropOnDeath(ItemBuilder skeletonBody) {
		dropsOnDeath.add(skeletonBody);		
	}

	public List<ItemBuilder> getDropsOnDeath() {
		return dropsOnDeath;
	}	
}
