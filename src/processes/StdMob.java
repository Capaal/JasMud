package processes;

import skills.*;
import skills.Arcane.*;
import skills.Arcane.SetTargets.Target;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

import java.text.MessageFormat;
import java.util.*;
import java.io.*;

import effects.Bleed;
import effects.Defence;
import actions.Chance;
import actions.Check;
import actions.Check.CheckType;
import actions.Cost;
import actions.Cost.CostType;
import actions.Damage;
import actions.Effecter;
import actions.Effecter.EffectType;
import actions.Message;


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
	protected int xpWorth;
	protected String shortDescription;
	protected ArrayList<String> bugList;
	protected ArrayList<String> messages;
	protected int experience;
	protected int level;
	protected int age; 
	protected SendMessage sendBack;
	protected TreeMap<String, Command> allowedCommands;	
	protected int baseDamage;
	protected TickClient tickClient;
	
	protected HashMap<String, SkillBook> skillBookList = new HashMap<String, SkillBook>();
	
	protected ArrayList<Effect> effectList;
	
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
		this.effectList = build.effectList;
		this.allowedCommands = build.allowedCommands;
		this.baseDamage = build.baseDamage;
		this.tickClient = new TickClient(this);
		tickClient.start();
		
	//	effectList.put("piercedefence", new PierceDefence()); // Temporary for testing
		
		allowedCommands.put("north", new Move());
		allowedCommands.put("move", new Move());
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
		allowedCommands.put("swim", new Swim());

		allowedCommands.put("look", new Look());
		allowedCommands.put("examine", new Examine());
		allowedCommands.put("get", new Get());  //temporary assumption that all mobs can get
		allowedCommands.put("create", new Create());
		allowedCommands.put("drop", new Drop());
		allowedCommands.put("say", new Say());
		
		SkillBook skillBook = new SkillBook();
	//	SkillBuilder skillBuild = new SkillBuilder();
		
	//	skillBuild.setup(this, "slash");
	//	skillBuild.addAction(1, new SetTargets(0, Target.SINGLE));
	//	skillBuild.addAction(2, new Damage(10));
	//	skillBuild.addAction(0, new Bleed(20));
	//	skillBuild.addAction(0, new ManaCheck(5));
	//	skillBuild.addAction(2, new ManaCost(5));
	//	skillBuild.addAction(2, new PersonalDesc("You slash violently at {1}."));
	//	skillBuild.complete(skillBook);
		
/*		skillBuild.setup(this, "slash");
		skillBuild.setDamage("15");
		skillBuild.setMana("5");
		skillBuild.setEffect("bleed 20");		
		skillBuild.setPersonalDesc("You slash violently at {1}.");
		skillBuild.setTargetDesc("{0} slashes violently at you!");
		skillBuild.setCanSeeDesc("{0} slashes violently at {1}");
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "flurry");
		skillBuild.setDamage("5");
		skillBuild.setTarget("ALL");
		skillBuild.setPersonalDesc("You slash violently at {1}.");
		skillBuild.setTargetDesc("{0} slashes violently at you!");
		skillBuild.setCanSeeDesc("{0} slashes violently at {1}");
		skillBuild.complete(skillBook);*/
		
		
		
		// BELOW IS BUILT USING NEW ACTIONS USING FORMAT ACTION(ie damage) ACTIONS RELATED INFO(10 target projectile)
		
		// build new skill called slash
		// check balance self
		// damage 20 target here
		
		//***** "slash" is a test skill that does odd stuff, currently it does:
		// Check balance true, 10 damage to target here, bleed effect on target for 500? s,
		// It is type sharp, and requires a weapon that is type sharp, It displays messages to everyone in that location.
		// And then it takes away balance, balance does not come back yet... It has a chance to heal yourself. 10% for 15
		
		SkillBuilder skillBuild = new SkillBuilder();
		
		skillBuild.setup(this, "slash");
//		skillBuild.addAction(new Check("1", CheckType.BALANCE, Who.SELF, Where.HERE)); // Don't need this anymore, cost includes a check
		// But then we get the problem, that a fail causes lose of balance, I guess they can include a check also if they want to avoid.
		
		skillBuild.addAction(new Cost(1, CostType.BALANCE, Who.SELF, Where.HERE));
		
		skillBuild.addAction(new Damage(10, Who.TARGET, Where.HERE));
		
		skillBuild.addAction(new Effecter(10, EffectType.BLEED, Type.NULL, Who.TARGET, Where.HERE));
		
		skillBuild.addAction(new Chance(10, new Damage(-15, Who.SELF, Where.HERE)));
		
		skillBuild.addType(Type.SHARP);
		skillBuild.addAction(new Check(Type.SHARP.name(), CheckType.WEAPON, Who.SELF, Where.HERE));
		
		
		skillBuild.addAction(new Message("You make a sharp slash at %s and then %s turns and fights back.", Where.HERE, Who.TARGET, Who.TARGET, Who.TARGET));
		skillBuild.addAction(new Message("You watch as %s slashes horribly at %s and %s turns to fight back.", Where.HERE, Who.OTHERS, Who.SELF, Who.TARGET, Who.TARGET)); 
		skillBuild.addAction(new Message("%s slashes you painfully.", Where.HERE, Who.TARGET, Who.SELF));
				
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "getbalance");
		skillBuild.addAction(new Cost(-1, CostType.BALANCE, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "bleeddefence");
		skillBuild.addAction(new Effecter(500, EffectType.DEFENCE, Type.BLEED, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "slashdefence");
		skillBuild.addAction(new Effecter(500, EffectType.DEFENCE, Type.SHARP, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		
		
		
		skillBookList.put("skillbook", skillBook);		
		
		allowedCommands.put("design", new ArcaneBuilder());
		allowedCommands.put("adjust", new Adjust());
		allowedCommands.put("complete", new Complete());
		
		WorldServer.mobList.put(name + id, this);
	}
	
	protected static abstract class Init<T extends Init<T>> {
	
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
		private ArrayList<Holdable> inventory = new ArrayList<Holdable>();
		private TreeMap<String, Command> allowedCommands = new TreeMap<String, Command>();
		private String password = "";
		private ArrayList<Effect> effectList = new ArrayList<Effect>();
		
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
	
	public SortedSet<String> getCommandKeySet() {
		return new TreeSet<String>(allowedCommands.keySet());
	//	return allowedCommands.keySet();
	}	
	@Override
	public SkillBook getBook(String bookName) {
		return skillBookList.get(bookName);
	}
	
	public Collection<Command> getCommandValueSet() {return allowedCommands.values();}	
	public void acceptItem(Holdable item) {inventory.add(item);}
	public int getMessagesSize() {return messages.size();}	
	public void addBug(String bugMsg) {bugList.add(bugMsg);}
	public void acceptCommands(HashMap<String, Command> givenCommands) {allowedCommands.putAll(givenCommands);}	
	public void acceptCommand(String comName, Command command) {allowedCommands.put(comName,  command);}
	// Doesn't do nothin.
	public void removeCommands(HashMap<String, Command> removedCommands) {	}
	@Override
	public void setContainer(Container futureLocation) {mobLocation = futureLocation;}
	@Override
	public Container getContainer() {return mobLocation;}
	
	public void takeDamage(List<Type> types, int damage) {
//		damage = damageAdjustments(damage);
		damage = runEffects(types, damage);
		this.currentHp = currentHp - damage;
		checkHp();
	}
	
	// Incoming damage may be negated by armor, enchatments, active skills, whatever.
/*	protected int damageAdjustments(int damage) {
		Random rand = new Random();
		int random = rand.nextInt(100) + 1;
		System.out.println(random);
	//	if (random <= missChance) {
		//	sendBack.printMessage("Hiding has paid off, you dodge the attack.");
	//		return 0;
	//	}
		return damage;
	}*/
	
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
	@Override
	public ArrayList<Holdable> getInventory() {
		return new ArrayList<Holdable>(this.inventory);
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
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setDescription(String desc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addEffect(Effect effect) {
		effectList.add(effect);
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
	
	public boolean hasEffect(Effect effect) {
		return effectList.contains(effect);		
	}
	
	public void runTickEffects() {
		Iterator iter = effectList.iterator();
		while (iter.hasNext()) {
			Effect effect = (Effect) iter.next();
			effect.doTickEffect();
			if (effect.wasRemoved()) {
				iter.remove();
				removeEffect(effect);
			}
		}
	}
	
	public int runEffects(List<Type> incomingTypes, int damage) {
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
		return tickClient.getTick();
	}
	
	public boolean hasMana(int mana) {
		return true;
	}
	
	public void affectMana(int mana) {
		//meh
	}

	public void setBalance(boolean value) {
		this.balance = value;
	}
	
	public boolean hasWeaponType(Type type) {
		return true; // Should actually test for correct equipped weapons.
	}
	
	
}
	