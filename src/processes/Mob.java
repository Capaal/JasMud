package processes;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;

public class Mob extends Mobiles implements Serializable {

	protected boolean hostile = false;
	protected Quest mobQuest;
	protected String greetMsg;
	protected int id;
	
	public Mob(String name, Location mobLocation) {
		super(name, mobLocation);
		this.name = name;
		this.maxHp = 50;
		this.currentHp = 50;		
//		this.mobLocation = mobLocation;
		this.maxHit = 2; //player is 10
		this.speed = 3000;
		this.inventory = new ArrayList<Item>();
		this.greetMsg = (this.name + " looks at you curiously.");	
	}
	
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
	
}