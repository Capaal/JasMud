package interfaces;

import java.util.EnumSet;
import java.util.Set;

import processes.Equipment.EquipmentEnum;
import processes.Type;

public interface Holdable {
	
	
	public String getName();
	public int getId();
	public String getDescription();
	public void setContainer(Container con);
	public Container getContainer();
	public boolean save();
	public void removeFromWorld();
	public Set<EquipmentEnum> getAllowedEquipSlots();
	public boolean containsType(Type type);
	public double getDamageMult();
}
