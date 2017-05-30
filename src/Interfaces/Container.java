 package interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import processes.ContainerErrors;

public interface Container {
	
//	public Set<Holdable> inventory = Collections.synchronizedSortedSet(new TreeSet<Holdable>());

	public TreeMap<String, Holdable> getInventory();
//	public String displayExits();// Should LOCATION be an interface, with this required method? But you can LOOK INSIDE containers?
//	public void look(Mobile currentPlayer); // Should LOCATION be an interface, with this required method? This is when you look inside?
//	public void glance(Mobile currentPlayer);// Should LOCATION be an interface, with this required method?
//	public void displayAll(Mobile currentPlayer);// Should LOCATION be an interface, with this required method?
	public String getName();
	public int getId();
	public int getMaxQty();
	public int getCurrentQty(); //merge into weight
//	public int getMaxWeight();
	public ContainerErrors acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem) ;
//	public Container getContainer(); // Can a Container HAVE a container? (Holdables are forced to have this, LOCATION would not)
//	public GroundType getGroundType(); // Should LOCATION be an interface, with this required method?
	public Holdable getHoldableFromString(String holdableString);
	public Collection<Holdable> getListMatchingString(String holdableString);

}
