package processes;

import interfaces.*;

import java.util.*;
import skills.*;

// Used by players when playing "hero" like classes. They can learn and level and get new titles and perform quests.
// Where the line is drawn between StdMob and player classes will take time to figure out.

public class Player extends StdMob {

	protected int generals;
	protected int mage;   
	protected int survival;
	protected String prefix;
	protected String suffix;
	protected String sex;
	protected ArrayList<Quest> questList = new ArrayList<Quest>(); // Can only heroes complete quests? That makes sense... right?
	private HashMap<String, Effect> effectList; 
		
	protected static abstract class Init<T extends Init<T>> extends StdMob.Init<T> {
		
		private String prefix = "";		
		public Init(int id, String name) {super(id, name);}				
		public T prefix(String val) {this.prefix = val;return self();}		
		public Player build() {return new Player(this);}
	}
	
	public static class Builder extends Init<Builder> {
		public Builder(int id, String name) {super(id, name);}
		@Override
		protected Builder self() {return this;}
	}	
	
	protected Player(Init<?> build) {
		super(build);	
		this.prefix = build.prefix; 
		this.suffix = "";
		this.generals = 16;
		this.mage = 0;
		this.survival = 0;
		this.level = 1;		
		this.password = password;
		this.bugList = new ArrayList<String>();
		this.messages = new ArrayList<String>();
		this.effectList = new HashMap<String, Effect>();
	//	allowedCommands.put("stab", new Stab()); // This supposed to be here?
	}	
	
	public String returnSex() {return this.sex;}
	
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
	
	protected int damageAdjustments(int damage) {
		Random rand = new Random();
		int random = rand.nextInt(100) + 1;
		System.out.println(random);
		return damage;
	}
}