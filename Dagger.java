import java.io.*; // For Serializable.

public class Dagger implements Serializable, Item, Wieldable  {
	
//	protected boolean wieldable = true;
	private int baseDamage;
	private int id;
	private String name;
	private String description;
	private int weaponBalance;
	
	public Dagger(int id) {
		this.id = id;
		this.weaponBalance = 1800;
		this.baseDamage = 13;
		this.name = "dagger";
		this.description = "A five inch blade perfect for targetted strikes.";
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDamage(int damage) {
		this.baseDamage = damage;
	}
	
	public void setDesc(String desc) {
		this.description = desc;
	}
	
	public void setBalance(int balance) {
		this.weaponBalance = balance;
	}
	
	public int returnDamage() {
		return this.baseDamage;
	}
	
	public String returnDescription() {
		return this.description;
	}
	
	public String returnName() {
		return name;
	}
	
	public int returnID() {
		return this.id;
	}
	
	public boolean returnWieldable() {
		return true;
	}
	
	public String returnAttackerMSG(String target) {
		String MSG = ("You deftly approach and stab " + target + ".");
		return MSG;
	}
	
	public String returnAttackedMSG(String player) {
		String MSG = "You feel a painful puncture as " + player +  " stabs you from behind.";
		return MSG;
	}
	
	public int returnWeaponBalance() {
		return this.weaponBalance;
	}
	
	public void doStab(String target, Player currentPlayer, SendMessage sendBack) {
		GeneralCommands.attackPlayer(target, returnAttackerMSG(target), returnAttackedMSG(currentPlayer.name),
			this.baseDamage, "physical", currentPlayer, sendBack, weaponBalance);
	}
	
	public void wieldHand(Player currentPlayer) {
		currentPlayer.wieldedRight = this;
	}
}