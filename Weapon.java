import java.io.*; // For Serializable.

public class Weapon implements Serializable, Item, Wieldable  {
	
//	protected boolean wieldable = true;
	private int baseDamage;
	private int id;
	private String name;
	private String description;
	
	public Weapon(int id, int dmg, String name, String desc) {
		this.baseDamage = dmg;
		this.id = id;
		this.name = name;
		this.description = desc;
	}
	
	public int returnDamage() {
		return this.baseDamage;
	}
	
	public String returnDescription() {
		return this.description;
	}
	
	public String returnName() {
		return this.name;
	}
	
	public int returnID() {
		return this.id;
	}
	
	public boolean returnWieldable() {
		return true;
	}
}