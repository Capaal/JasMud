package processes;
import java.util.*;
import java.io.*;

public abstract class Mobiles implements Serializable {

	public String name;
	protected int maxHp;
	protected int currentHp; 
	private Location mobLocation; 
	protected boolean balance;
	protected boolean dead;
	protected int maxHit;
	protected int speed;
	protected Wieldable wieldedRight;
	protected Wieldable wieldedLeft;
	protected ArrayList<Item> inventory;
	protected String description;
	protected boolean shield;
	protected boolean aura;
	protected int XpWorth;
	protected String shortDescription;
	
	public Mobiles() {
	}
	
	public Mobiles(String name, Location mobLocation) {
		this.name = name;
		this.maxHp = 50;
		this.currentHp = 50;
		this.mobLocation = mobLocation;
		this.balance = true;
		this.dead = false;
		this.maxHit = 5;
		this.speed = 3000;
		this.shield = false;
		this.aura = false;
		this.description = "";
		this.shortDescription = "";
		this.inventory = new ArrayList<Item>();
	}
	
	public void takeDamage(int damage) {
		this.currentHp = currentHp - damage;
	}
	
	public void startDefence(String defence) {
		if (defence.equals("shield")) {
			this.shield = true;
		} else if (defence.equals("aura")) {
			this.aura = true;
		} else if (defence.equals("balance")) {
			this.balance = false; 
		}
	}

	public void endDefence(String defence) {
		if (defence.equals("shield")) {
			this.shield = false;
		} else if (defence.equals("aura")) {
			this.aura = false;
		} else if (defence.equals("balance")) {
			this.balance = true;
		}
	}	
	
	public boolean isDead() {
		return dead;
	}
	
	public Location getMobLocation() {
		return mobLocation;
	}
	
	public boolean hasAura() {
		return aura;
	}
	
	public boolean hasBalance() {
		return balance;
	}
	
	public int getCurrentHp() {
		return currentHp;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public int getXpWorth() {
		return XpWorth;
	}
	
	public void setMobLocation(Location newLoc) {
		this.mobLocation = newLoc;
	}
}