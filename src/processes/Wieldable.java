package processes;
public interface Wieldable {
	
	public boolean returnWieldable();
	public String returnAttackerMSG(String target);
	public String returnAttackedMSG(String player);
	public int returnID();
	public String returnName();
	public int returnDamage();
	public String returnDescription();
	public void wieldHand(Player currentPlayer);
}
