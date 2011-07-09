import java.io.*; // For Serializable.

public class Longsword implements Serializable, Item, Wieldable  {
	
//	protected boolean wieldable = true;
	private int baseDamage = 15;
	private int id;
	private String name= "longsword";
	private String description = "A sword designed for balanced strength and speed.";
	
	public Longsword(int id) {
		this.id = id;
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
	
	public void wieldHand(Player currentPlayer) {
		currentPlayer.wieldedRight = this;
	}
	
	public String returnAttackerMSG(String target) {
		String MSG = ("You lunge forward, slashing at " + target + ".");
		return MSG;
	}
	
	public String returnAttackedMSG(String player) {
		String MSG = player +  " lunges towards you, gouging you painfully.";
		return MSG;
	}

}