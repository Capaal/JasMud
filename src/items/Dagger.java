package items;

import java.io.*; // For Serializable.

import processes.Player;
import processes.SendMessage;
import Interfaces.Container;
import Interfaces.Holdable;
import Interfaces.Wieldable;

public class Dagger implements Item, Wieldable  {
	
	private int baseDamage;
	private int id;
	private String name;
	private String description;
	private int weaponBalance;
	private Container itemLocation;
	
	public Dagger(int id) {
		this.id = id;
		this.weaponBalance = 1800;
		this.baseDamage = 13;
		this.name = "dagger";
		this.description = "A five inch blade perfect for targetted strikes.";
	}
	
	public Dagger create() {
		Dagger dagger = new Dagger(id);
		id++;
		return dagger;		
	}
	
	public void setItemLocation(Container con) {
		itemLocation = con;
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
	//	GeneralCommands.attackPlayer(target, returnAttackerMSG(target), returnAttackedMSG(currentPlayer.name),
	//		this.baseDamage, "physical", currentPlayer, sendBack, weaponBalance);
	}
	
	public void wieldHand(Player currentPlayer) {
//		currentPlayer.wieldedRight = this;
	}

	@Override
	public void acceptItem(Holdable item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


}