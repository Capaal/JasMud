package processes;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;

//When editing: comment out registeredPlayers load in WorldServer, delete registeredPlayers in folder.
//Editing to become child class of Mob

public class Player extends Mobiles implements Serializable {

	protected int generals;
	protected int mage;   
	protected int survival;
	protected String password; 
	protected ArrayList<String> bugList;
	protected ArrayList<String> messages;
	protected int experience;
	protected int level;
	protected int age; 
	protected String prefix;
	protected String suffix;
	protected String sex;
	protected ArrayList<Quest> questList = new ArrayList<Quest>();
	 
	public Player(String name, String password, Location mobLocation) {
		super(name, mobLocation);	
		this.prefix = ""; 
		this.suffix = "";
		this.maxHp = 220;
		this.currentHp = 220;		
		this.generals = 16;
		this.mage = 0;
		this.survival = 0;
		this.level = 1;		
		//Defence variables
		this.shield = false;
		this.aura = false;	
		this.password = password;
		this.bugList = new ArrayList<String>();
		this.messages = new ArrayList<String>();
	}
	
	public void levelPlayer() {
		PlayerPrompt p = UsefulCommands.getPlayerPromptFromPlayer(this);
		if (this.level < WorldServer.Levels.length -1) {
			while (this.experience >= WorldServer.Levels[this.level]) {
				this.level += 1;
				p.sendBack.printMessage("Congratulations, you are now level " + this.level + "!");
				this.maxHp = (120 + this.level * 100);
			}
			if (this.level > 1) {
				while (this.experience < WorldServer.Levels[this.level - 1]) {
					this.level -= 1;
					p.sendBack.printMessage("Disaster! You are now level " + this.level + "!");
					this.maxHp = (120 + this.level * 100);
				}
			}
		}
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
	
	public void addExperience(int exp) {
		this.experience += exp;
	}
}