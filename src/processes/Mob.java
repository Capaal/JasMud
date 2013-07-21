package processes;
import java.io.*;
import java.util.*;


//I think this should be killed, and be just mobiles, so that players and mobs are the same thing, thus interchangable.
// It is not a specific type of creature, a tiger is a mobiles, not tiger->mob->mobiles, Hero->Mobiles, Guard->mobiles.
// THere might be steps in between like Guard->Human->Mobiles, I dunno yet.

public class Mob extends Mobiles {

	protected boolean hostile = false; // Into AI
	protected Quest mobQuest; // Uhh, revist quests?
	protected String greetMsg; // I'm not sure. This will be AI
	protected int id;
	
	public Mob(String name, Location mobLocation) {
		super(name, mobLocation);
		this.name = name;
		this.maxHp = 50;
		this.currentHp = 50;		
//		this.mobLocation = mobLocation;
		this.maxHit = 2; //player is 10
		this.speed = 3000;
	//	this.inventory = new ArrayList<Item>();
		this.greetMsg = (this.name + " looks at you curiously.");	
	}
	 //AI
	public String greet(Player currentPlayer) {		
//		if (this.mobQuest != null) {
//			return mobQuest.greetQuest(currentPlayer);
//		} else {
			return this.greetMsg;
//		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setHostile(Boolean isHostile) {
		hostile = isHostile;
	}
	
	public boolean isHostile() {
		return hostile;
	}
	
	public void startAttacks(Player currentPlayer) {
		Thread mobAttack = new MobAttack(this, currentPlayer);
		mobAttack.start();
		hostile = true;
	}
	
	protected int damageAdjustments(int damage) {
		return damage;
	}

	public String returnName() {
		return name;
	}

	public int returnID() {
		return id;
	}

	public String returnDescription() {
		return shortDescription;
	}
	
	
}