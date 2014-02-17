package interfaces;

import java.util.EnumSet;

import processes.Type;
import processes.Equipment.EquipmentEnum;

public interface Equipable extends Holdable {

	public boolean containsType(Type type);
	public EnumSet<EquipmentEnum> getAllowedEquipSlots();
}
