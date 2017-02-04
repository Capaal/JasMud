package interfaces;

// SHOULD only define something that can be HELD by CONTAINERs
public interface Holdable extends Comparable<Holdable> {	
	
	public String getName();
	public int getId();
	public String getDescription();
	public Container getContainer();
	public boolean save(); // should be pulled out to its own interface
	public boolean firstTimeSave(); // should be pulled out to its own interface
	public void removeFromWorld();  // NOT the same as DELETE
	//public Set<EquipmentEnum> getAllowedEquipSlots(); // Is this true for ALL Holdables?
//	public boolean containsType(Type type);
	//public double getDamageMult(); Might be true of items, but not all holdables (like mobiles)
	public void moveHoldable(Container finalLocation);
}
