 package interfaces;

import java.util.Collection;
import java.util.TreeMap;
import processes.ContainerErrors;

// Defines the Object as being capable of storing HOLDABLES.
// Defines adding and removing, viewing storied Holdables.
// Defines extra container mechanics: weight handling.
public interface Container {
	
	public TreeMap<String, Holdable> viewInventory();
	public String getName();
	public int getId();
	public double getMaxWeight();
	public double getCurrentWeight(); 
	public void changeWeight(double weight);
	public ContainerErrors acceptItem(Holdable newItem);
	public void removeItemFromLocation(Holdable oldItem);
	/**
	 * Attempts to locate the single most likely Holdable as designated by given string. Accepts both
	 * name and name+id with startsWith accuracy.
	 * @param holdableString String to use when searching for Holdable.
	 * @return Holdable or Null if nothing within an acceptable accuracy is found.
	 */
	public Holdable getHoldableFromString(String holdableString);
	/**
	 * Returns a List of possible Holdables based on the given string. Includes all matches including all IDs.
	 * @param holdableString String to be compared against.
	 * @return List of Holdables that match search constraints.
	 */
	public Collection<Holdable> getListMatchingString(String holdableString);

}
