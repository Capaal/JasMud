package processes;

import skills.*;
import java.util.*;
import java.io.*;

import Interfaces.*;

// Represents basic truths about anything that can move on its own. It can be both controlled by a player,
// or be controlled by AI. Rat yes, hero mage yes, dragon yes, wind no, sun no, bird yes, ant yes, tree? No, make a sentient tree.
public class StdMob implements Mobile, Container, Holdable, Creatable {

	protected final String name;
	protected String password; //Name + password is required to enter any role.
	protected final int id;
	protected int maxHp;
	protected int currentHp; 
	protected Container mobLocation; 
	protected boolean balance;// Should this be here?
	protected int physicalMult;
	protected boolean isDead;
	protected int speed;
	public ArrayList<Holdable> inventory;
	protected String description;
	//protected boolean shield;
	//protected boolean aura;
	protected int xpWorth;
	protected String shortDescription;
	protected ArrayList<String> bugList;
	protected ArrayList<String> messages;
	protected int experience;
	protected int level;
	protected int age; 
	protected SendMessage sendBack;
	protected HashMap<String, Command> allowedCommands;	
	
	protected StdMob(Init<?> build) {
		this.name = build.name;
		this.id = build.id;
		this.password = build.password;
		this.maxHp = build.maxHp;
		this.currentHp = maxHp;
		this.mobLocation = build.location;
		this.balance = true;
		this.physicalMult = build.physicalMult;
		this.isDead = false;
		this.speed = build.speed;
		this.description = build.description;
		this.shortDescription = build.shortDescription;
		this.inventory = build.inventory;
		this.bugList = new ArrayList<String>();
		this.messages = new ArrayList<String>();
		this.allowedCommands = build.allowedCommands;
		allowedCommands.put("north", new Move());
		allowedCommands.put("ne", new Move());
		allowedCommands.put("nw", new Move());
		allowedCommands.put("sw", new Move());
		allowedCommands.put("se", new Move());
		allowedCommands.put("e", new Move());
		allowedCommands.put("n", new Move());
		allowedCommands.put("s", new Move());
		allowedCommands.put("w", new Move());
		allowedCommands.put("l", new Look());
		allowedCommands.put("northeast", new Move());
		allowedCommands.put("east", new Move());
		allowedCommands.put("south", new Move());
		allowedCommands.put("southeast", new Move());		
		allowedCommands.put("southwest", new Move());
		allowedCommands.put("west", new Move());
		allowedCommands.put("northwest", new Move());
		allowedCommands.put("up", new Move());
		allowedCommands.put("down", new Move());
		allowedCommands.put("in", new Move());
		allowedCommands.put("out", new Move());

		allowedCommands.put("look", new Look());
		allowedCommands.put("examine", new Examine());
		allowedCommands.put("get", new Get());  //temporary assumption that all mobs can get
		allowedCommands.put("create", new Create());
		allowedCommands.put("drop", new Drop());
		WorldServer.mobList.put(name, this);
	}
	
	protected static abstract class Init<T extends Init<T>> {
	
		private final String name;
		private final int id;		
		private String description = "Generic.";
		private String shortDescription = "Short and Generic.";
		private int maxHp = 10;
		private Container location = WorldServer.locationCollection.get(1);
		private int physicalMult = 1;
		private int speed = 3000;
		private int xpWorth = 1;
		private ArrayList<Holdable> inventory = new ArrayList<Holdable>();
		private HashMap<String, Command> allowedCommands = new HashMap<String, Command>();
		private String password = "";
		
		protected abstract T self();		
		
		public Init(int id, String name) {
			if (WorldServer.mobList.containsKey(name + id)) {
				throw new IllegalStateException("A mobile already exists with that name and id.");
			}
			this.id = id;
			this.name = name;
		}		
		public T password(String val) {password = val;return self();}		
		public T description(String val) {description = val;return self();}		
		public T shortDescription(String val) {shortDescription = val;return self();}		
		public T maxHp(int val) {maxHp = val;return self();}		
		public T location(Container val) {location = val;return self();}		
		public T physicalMult(int val) {physicalMult = val;return self();}		
		public T speed(int val) {speed = val;return self();}		
		public T inventory(Item val) {inventory.add(val);return self();}		
		public T commands(String name, Command val) {allowedCommands.put(name, val);return self();}		
		public T xpWorth(int val) {xpWorth = val;return self();}		
		public StdMob build() {return new StdMob(this);}}
	
	public static class Builder extends Init<Builder> {
		public Builder(int id, String name) {
			super(id, name);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	public String getName() {return name;}	
	//Turn into a compare password? Is that safer?
	public String getPassword() {return password;}	
	public int getId() {return id;}	
	public int getCurrentHp() {return currentHp;}	
	public int getMaxHp() {return maxHp;}	
	public Container getMobLocation() {return mobLocation;}	
	public boolean hasBalance() {return balance;}	
	public boolean getIsDead() {return isDead;}	
	public int getSpeed() {return speed;}	
	public String getDescription() {return description;}	
	public String getShortDescription() {return shortDescription;}	
	public int getXpWorth() {return xpWorth;}	
	public boolean commandAllowed(String command) {return allowedCommands.containsKey(command);}	
	public Command getCommand(String command) {return allowedCommands.get(command);}	
	public Set<String> getCommandKeySet() {return allowedCommands.keySet();}	
	public Collection<Command> getCommandValueSet() {return allowedCommands.values();}	
	public void acceptItem(Holdable item) {inventory.add(item);}
	public int getMessagesSize() {return messages.size();}	
	public void addBug(String bugMsg) {bugList.add(bugMsg);}
	public void acceptCommands(HashMap<String, Command> givenCommands) {allowedCommands.putAll(givenCommands);}	
	// Doesn't do nothin.
	public void removeCommands(HashMap<String, Command> removedCommands) {	}
	@Override
	public void setContainer(Container futureLocation) {mobLocation = futureLocation;}
	@Override
	public Container getContainer() {return mobLocation;}
	
	public void takeDamage(int damage) {
		damage = damageAdjustments(damage);
		this.currentHp = currentHp - damage;
		checkHp();
	}
	
	// Incoming damage may be negated by armor, enchatments, active skills, whatever.
	protected int damageAdjustments(int damage) {
		Random rand = new Random();
		int random = rand.nextInt(100) + 1;
		System.out.println(random);
	//	if (random <= missChance) {
		//	sendBack.printMessage("Hiding has paid off, you dodge the attack.");
	//		return 0;
	//	}
		return damage;
	}
	
	protected void checkHp() {
		if (currentHp <= 0) {
			tell("You colapse to the ground, unable to fight on.");
			isDead = true;
		}
	}
	
	public void tell(String msg) {
		if (this.sendBack == null) {
			sendBack = UsefulCommands.getPlayerPromptFromPlayer(this).getSendBack();
		}
		sendBack.printMessage(msg);
	}
	
	public void tellLine(String msg) {
		if (this.sendBack == null) {
			sendBack = UsefulCommands.getPlayerPromptFromPlayer(this).getSendBack();
		}
		sendBack.printMessageLine(msg);
	}
	/* To be figured out again?
	public void startDefence(String defence) {
		if (defence.equals("shield")) {
			this.shield = true;
		} else if (defence.equals("aura")) {
			this.aura = true;
		} else if (defence.equals("balance")) {
			this.balance = false; 
		}
	} */
/* To be figured out again?
	public void endDefence(String defence) {
		if (defence.equals("shield")) {
			this.shield = false;
		} else if (defence.equals("aura")) {
			this.aura = false;
		} else if (defence.equals("balance")) {
			this.balance = true;
		}
	}*/	
	
	public void addExperience(int exp) {
		this.experience += exp;
		levelMobile();
	}
	
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
	
	// I think anything could learn a new skill, but the below is crap.
	/*
	public void learnSkill(String skill, SendMessage sendBack) {
		if (skill.equals("generals")) {
			this.generals = (this.generals + 1);
			sendBack.printMessage("You've obtained the rank of " + generals + " in generals.");
		} else if (skill.equals("mage")) {
			this.mage = (this.mage + 1);
			sendBack.printMessage("You've obtained the rank of " + mage + " in mage.");
		} else if (skill.equals("survival")) {
			this.survival = (this.survival + 1);
			sendBack.printMessage("You've obtained the rank of " + survival + " in mage.");
		}
	}*/
	

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
	
	public boolean removeItem(Holdable item) {
		if (inventory.contains(item)) {
			inventory.remove(item);
			return true;
		} else {
			return false;
		}
	}	

	@Override
	public Creatable create() {
		// TODO Auto-generated method stub
		return null;
	}	
}
	