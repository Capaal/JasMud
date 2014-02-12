 package interfaces;

import java.util.ArrayList;

import processes.Location.GroundType;

public interface Container {
	
	public ArrayList<Holdable> inventory = new ArrayList<Holdable>();

	public ArrayList<Holdable> getInventory();
	public String displayExits();
	public void look(Mobile currentPlayer);
	public void glance(Mobile currentPlayer);
	public void displayAll(Mobile currentPlayer);
	// not all containers have names? maybe ids but not names.
	public String getName();
	public int getId();
	public void setName(String name);
	public void setDescription(String desc);
	public void acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem) ;
	public Container getContainer(String dir);
	/// no
	public GroundType getGroundType();
	public Holdable getHoldableFromString(String holdableString);
}
