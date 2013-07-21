package processes;
import items.Item;

import java.util.*;
import java.io.*;

import skills.Move;

import Interfaces.Container;
import Interfaces.Holdable;
import Interfaces.Wieldable;


// Represents basic truths about anything that can move on its own. It can be both controlled by a player,
// or be controlled by AI. Rat yes, hero mage yes, dragon yes, wind no, sun no, bird yes, ant yes, tree? No, make a sentient tree.
public class Mobiles implements Container, Holdable {

	protected String name;
	protected String password; //Name + password is required to enter any role.
	protected int id;
	protected int maxHp;
	protected int currentHp; 
	protected Location mobLocation; 
	protected boolean balance;// Should this be here?
	protected int physicalMult;
	protected boolean isDead;
	protected int speed;
	protected ArrayList<Holdable> inventory;
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
	protected HashMap<String, Command> allowedCommands = new HashMap<String, Command>();
	
	public Mobiles() {
	}
	
	public Mobiles(String name, Location mobLocation) {
		this.name = name;
		this.maxHp = 50;
		this.currentHp = 50;
		this.mobLocation = mobLocation;
		this.balance = true;
		this.physicalMult = 1;
		this.isDead = false;
		this.speed = 3000;
		this.description = "Very generic, and abstract.";
		this.shortDescription = "This is super Generic.";
		this.inventory = new ArrayList<Holdable>();
		this.bugList = new ArrayList<String>();
		this.messages = new ArrayList<String>();
		allowedCommands.put("north", new Move());
		allowedCommands.put("ne", new Move());
		allowedCommands.put("nw", new Move());
		allowedCommands.put("sw", new Move());
		allowedCommands.put("se", new Move());
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
	}
	
	public String getName() {
		return name;
	}
	
	//Turn into a compare password? Is that safer?
	public String getPassword() {
		return password;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCurrentHp() {
		return currentHp;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	public Location getMobLocation() {
		return mobLocation;
	}
	
	public boolean hasBalance() {
		return balance;
	}
	
	public boolean getIsDead() {
		return isDead;
	}
	
	public int getSpeed() {
		return speed;
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
	
	protected void tell(String msg) {
		if (this.sendBack == null) {
			sendBack = UsefulCommands.getPlayerPromptFromPlayer(this).getSendBack();
		}
		sendBack.printMessage(msg);
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
	
	
	
	
	public void setMobLocation(Location newLoc) {
		this.mobLocation = newLoc;
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
	
	public boolean commandAllowed(String command) {
		return allowedCommands.containsKey(command);
	}
	
	public Command getCommand(String command) {
		return allowedCommands.get(command);
	}
	
	public Set<String> getCommandKeySet() {
		return allowedCommands.keySet();
	}
	
	public void acceptItem(Holdable item) {
		inventory.add(item);
	}

	public int getMessagesSize() {
		return messages.size();
	}
	
	public void addBug(String bugMsg) {
		bugList.add(bugMsg);
	}

	
	
}
	