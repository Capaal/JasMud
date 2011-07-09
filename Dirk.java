import java.io.*; // For Serializable.

public class Dirk extends Dagger implements Serializable, Item, Wieldable  {
	
//	private int baseDamage;
//	private int id;
//	private String name;
//	private String description;
	
	public Dirk(int id) {
		super(id);
		setDamage(11);
		setName("dirk");
		setDesc("A short blade which specializes in swift strikes.");
		setBalance(1500);
	}
	
	public String returnAttackerMSG(String target) {
		String MSG = ("You stab " + target + ".");
		return MSG;
	}
	
	public String returnAttackedMSG(String player) {
		String MSG = "You feel a painful puncture as " + player +  " stabs you from behind.";
		return MSG;
	}
}
