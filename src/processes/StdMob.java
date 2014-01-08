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

import processes.Location.GroundType;
import processes.Skill.Syntax;
import checks.BalanceCheck;
import checks.MoveCheck;
import checks.WeaponEquippedCheck;
import costs.BalanceCost;
import effectors.BleedEffect;
import effectors.DefenceEffect;
import effects.Bleed;
import effects.Defence;
import actions.Chance;
import actions.Damage;
import actions.Drop;
import actions.Examine;
import actions.Get;
import actions.Look;
import actions.Message;
import actions.Message.msgStrings;
import actions.Move;
import actions.Or;
import actions.Say;


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
	protected int baseDamage;
	protected TickClient tickClient;
	
	protected Map<String, SkillBook> skillBookList = new TreeMap<String, SkillBook>();
	
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
		this.baseDamage = build.baseDamage;
		this.tickClient = new TickClient(this);
		tickClient.start();
		
	/*	SkillBook skillBook = new SkillBook();
			
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
		skillBuild.addAction(new BalanceCost(false, Who.SELF, Where.HERE));		
		skillBuild.addAction(new Damage(10, Who.TARGET, Where.HERE));		
		skillBuild.addAction(new BleedEffect(10, Who.TARGET, Where.HERE));		
		skillBuild.addAction(new Chance(10, new Damage(-15, Who.SELF, Where.HERE)));		
		skillBuild.addType(Type.SHARP);
		skillBuild.addAction(new WeaponEquippedCheck(Type.SHARP, Who.SELF, Where.HERE));		
		skillBuild.addAction(new Message("You make a sharp slash at %s and then %s turns and fights back.", Who.TARGET, Where.HERE, msgStrings.TARGET, msgStrings.TARGET));
		skillBuild.addAction(new Message("You watch as %s slashes horribly at %s and %s turns to fight back.", Who.OTHERS, Where.HERE, msgStrings.SELF, msgStrings.TARGET, msgStrings.TARGET)); 
		skillBuild.addAction(new Message("%s slashes you painfully.", Who.TARGET, Where.HERE, msgStrings.SELF));	
		skillBuild.setFailMsg("You fail to slash.");
		skillBuild.setSyntax(Syntax.SKILL, Syntax.TARGET);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "getbalance");
		skillBuild.addAction(new BalanceCost(true, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "bleeddefence");
		skillBuild.addAction(new DefenceEffect(500, Type.BLEED, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "sharpdefence");
		skillBuild.addAction(new DefenceEffect(500, Type.SHARP, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "say");
		skillBuild.addAction(new Say());
		skillBuild.setSyntax(Syntax.SKILL, Syntax.LIST);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "examine");
		skillBuild.addAction(new Or(new Examine(Where.INVENTORY), new Examine(Where.HERE)));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.ITEM);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "get");
		skillBuild.addAction(new Get(Who.SELF, Where.HERE));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.ITEM);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "drop");
		skillBuild.addAction(new Drop(Who.SELF, Where.HERE, Where.HERE));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.ITEM);
		skillBuild.complete(skillBook);
		
		// Can't look north or anything...
		skillBuild.setup(this, "look");
		skillBuild.addAction(new Look(Where.ONEAWAY));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.DIRECTION);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "move");
		skillBuild.addAction(new BalanceCheck(true, Who.SELF, Where.HERE));
		skillBuild.addAction(new MoveCheck(GroundType.GROUND, Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s leaves to the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.MOVE));
		skillBuild.addAction(new Move(Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s enters from the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.OPPMOVE));
		skillBuild.setFailMsg("You cannot walk that way.");
		skillBuild.setSyntax(Syntax.DIRECTION);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "swim");
		skillBuild.addAction(new BalanceCheck(true, Who.SELF, Where.HERE));
		skillBuild.addAction(new MoveCheck(GroundType.WATER, Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s swims to the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.MOVE));
		skillBuild.addAction(new Move(Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s swims in from the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.OPPMOVE));
		skillBuild.setFailMsg("You cannot swim that way.");
		skillBuild.setSyntax(Syntax.DIRECTION);
		skillBuild.complete(skillBook);
		
		
		skillBookList.put("skillbook", skillBook);		
		*/
//		allowedCommands.put("design", new ArcaneBuilder());
//		allowedCommands.put("adjust", new Adjust());
//		allowedCommands.put("complete", new Complete());
		
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
	
	public Skill getCommand(String command) {
		for (SkillBook sb : skillBookList.values()) {
			Skill skill = sb.getSkill(command);
			if (skill != null) {
				return skill;
			}
		}
		return null;
	}
	
	@Override
	public SkillBook getBook(String bookName) {
		return skillBookList.get(bookName);
	}
	
	public void acceptItem(Holdable item) {inventory.add(item);}
	public int getMessagesSize() {return messages.size();}	
	public void addBug(String bugMsg) {bugList.add(bugMsg);}

	@Override
	public void setContainer(Container futureLocation) {mobLocation = futureLocation;}
	
	@Override
	public Container getContainer() {return mobLocation;}
	
	public void takeDamage(List<Type> types, int damage) {
		damage = runEffects(types, damage);
		this.currentHp = currentHp - damage;
		checkHp();
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
	
	public void removeItem(Holdable item) {
		if (inventory.contains(item)) {
			inventory.remove(item);
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
	@Override
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
	public void addBook(String bookName, SkillBook skillBook) {
		skillBookList.put(bookName, skillBook);
		
	}
	
	
}
	