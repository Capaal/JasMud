 package Interfaces;

import java.util.ArrayList;

public interface Container {

	
	public ArrayList<Holdable> inventory = new ArrayList<Holdable>();


	public ArrayList<Holdable> getInventory();
	public String displayExits();
	public void look(Mobile currentPlayer);
	public void glance(Mobile currentPlayer);
	public void displayAll(Mobile currentPlayer);
	public String getName();
	public int getId();
	public void setName(String name);
	public void setDescription(String desc);
	public void acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem) ;

}
