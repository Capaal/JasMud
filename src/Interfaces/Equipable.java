package interfaces;

import java.util.EnumSet;

import processes.Equipment.EquipmentEnum;
import processes.Type;

public interface Equipable extends Holdable {
	public EnumSet<EquipmentEnum> getAllowedEquipSlots();
	public boolean containsType(Type type);
}
