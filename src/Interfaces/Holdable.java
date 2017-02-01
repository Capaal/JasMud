package interfaces;

import processes.ItemBuilder;
import processes.Type;

public interface Holdable {
	
	
	public String getName();
	public int getId();
	public String getDescription();
	public Container getContainer();
	public boolean save();
	public boolean firstTimeSave();
	public void removeFromWorld();
	//public Set<EquipmentEnum> getAllowedEquipSlots(); // Is this true for ALL Holdables?
	public boolean containsType(Type type);
	//public double getDamageMult();
	public ItemBuilder newBuilder(); // Should maybe return a builder interface instead?
	public void moveHoldable(Container finalLocation);
}
