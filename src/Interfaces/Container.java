 package interfaces;

import java.util.Collection;
import java.util.TreeMap;
import processes.ContainerErrors;

public interface Container {
	
//	public Set<Holdable> inventory = Collections.synchronizedSortedSet(new TreeSet<Holdable>());

	public TreeMap<String, Holdable> viewInventory();
	public String getName();
	public int getId();
	public double getMaxWeight();
	public double getCurrentWeight(); 
	public void changeWeight(double change);
	public ContainerErrors acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem);
	public Holdable getHoldableFromString(String holdableString);
	public Collection<Holdable> getListMatchingString(String holdableString);

}
