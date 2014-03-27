package interfaces;

import java.util.EnumSet;

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
	public EnumSet<EquipmentEnum> getAllowedEquipSlots();
	public boolean containsType(Type type);
	public double getDamageMult();
}
