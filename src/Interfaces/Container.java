 package interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import processes.Location.GroundType;

public interface Container {
	
	public Set<Holdable> inventory = new HashSet<Holdable>();

	public Set<Holdable> getInventory();
	public String displayExits();
	public void look(Mobile currentPlayer);
	public void glance(Mobile currentPlayer);
	public void displayAll(Mobile currentPlayer);
	public String getName();
	public int getId();
	public void acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem) ;
	public Container getContainer();
	public GroundType getGroundType();
	public Holdable getHoldableFromString(String holdableString);
}
