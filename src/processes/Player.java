package processes;

import java.io.*;
import java.util.*;

import Interfaces.Attachment;

import skills.*;


//When editing: comment out registeredPlayers load in WorldServer, delete registeredPlayers in folder.
//Editing to become child class of Mob
// NOTE: I think all of this should be in Mobiles, Players should just be mobiles, this file should die.
//    Adding to above, it should not die, it is more like the basic model for most "heroes" people might play as.
//    In theory it is not a player, as it can be controlled by AI like anything else.

// I have started copying things from here that are true to players playing as a role,
// and moving them into mobiles, so duplicated things may exist, then this become "Hero" with general skills.
public class Player extends StdMob {

	protected int generals;
	protected int mage;   
	protected int survival;
	protected String prefix;
	protected String suffix;
	protected String sex;
	protected ArrayList<Quest> questList = new ArrayList<Quest>(); // Can only heroes complete quests? That makes sense... right?
	private ArrayList<Attachment> attachments; 
	private int missChance; // I think we're scrapping this.
	
//	transient SendMessage sendBack;
	protected static abstract class Init<T extends Init<T>> extends StdMob.Init<T> {
		public Init(int id, String name) {
			super(id, name);
			// TODO Auto-generated constructor stub
		}

		private String prefix;
		
		public T prefix(String val) {
			this.prefix = val;
			return self();
		}
		
		public Player build() {
			return new Player(this);
		}
	}
	
	public static class Builder extends Init<Builder> {
		public Builder(int id, String name) {
			super(id, name);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Builder self() {
			return this;
		}
	}
	
	
	protected Player(Init<?> build) {
		super(build);	
		this.prefix = build.prefix; 
		this.suffix = "";
		this.generals = 16;
		this.mage = 0;
		this.survival = 0;
		this.level = 1;		
	//	Defence variables
	//	this.shield = false;
	//	this.aura = false;	
		this.password = password;
		this.bugList = new ArrayList<String>();
		this.messages = new ArrayList<String>();
		this.attachments = new ArrayList<Attachment>();
		this.missChance = 0;
		allowedCommands.put("stab", new Stab());
//		this.sendBack = sendBack;
	}
	
	
	
	
	
	public String returnSex() {
		return this.sex;
	}
	
	public String fullName() {
		if (prefix.equals("null") && !suffix.equals("null")) {
			return name + ", " + suffix;
		} else if (!prefix.equals("null") && suffix.equals("null")) {
			return prefix + " " + name;
		} else if (!prefix.equals("null") && !suffix.equals("null")) {
			return prefix + " " + name + ", " + suffix;
		} else {
		return name;
		}
	}
	
	
	
	
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
	}
	
	
	
	public void checkHp() {
		
		
	}
	
	public void addAttachment(Attachment attach) {
		this.attachments.add(attach);
	}
	
	public boolean searchAttachments(Attachment attach) {
		return attachments.contains(attach);
	}
	
	public void removeAttachment(Attachment attach) {
		this.attachments.remove(attach);
	}
	
	public void affectMiss(int missChange) {
		if ((this.missChance + missChance) < 0) {
			this.missChance = 0;
		} else if ((this.missChance + missChance) > 100) {
			this.missChance = 100;
		} else {
			this.missChance += missChange;
		}
	}
	
	protected int damageAdjustments(int damage) {
		Random rand = new Random();
		int random = rand.nextInt(100) + 1;
		System.out.println(random);
		if (random <= missChance) {
//			sendBack.printMessage("Hiding has paid off, you dodge the attack.");
			return 0;
		}
		return damage;
	}

	
}