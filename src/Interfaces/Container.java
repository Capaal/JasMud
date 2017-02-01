 package interfaces;

import java.util.HashSet;
import java.util.Set;
import processes.Location.GroundType;

public interface Container {
	
	public Set<Holdable> inventory = new HashSet<Holdable>();

	public Set<Holdable> getInventory();
	public String displayExits();// Should LOCATION be an interface, with this required method?
	public void look(Mobile currentPlayer); // Should LOCATION be an interface, with this required method?
	public void glance(Mobile currentPlayer);// Should LOCATION be an interface, with this required method?
	public void displayAll(Mobile currentPlayer);// Should LOCATION be an interface, with this required method?
	public String getName();
	public int getId();
	public void acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem) ;
	public Container getContainer(); // Can a Container HAVE a container? (Holdables are forced to have this, LOCATION would not)
	public GroundType getGroundType(); // Should LOCATION be an interface, with this required method?
	public Holdable getHoldableFromString(String holdableString);
}
