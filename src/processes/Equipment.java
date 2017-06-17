package processes;

import interfaces.Holdable;
import interfaces.Mobile;

import java.util.*;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

// NEEDS A CONVERTER for SAVE/LOAD for the currentPlayer pointer
// Two-handed weapons? shit Will probably need to check, and fill other hand, or have a special enum
public class Equipment {

	private Map<EquipmentEnum, Holdable> equipmentToItemMap;
	private Map<Holdable, EquipmentEnum> itemToEquipmentMap;
	private final Mobile currentPlayer;
		
	public Equipment(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		equipmentToItemMap = new EnumMap<EquipmentEnum, Holdable>(EquipmentEnum.class);
		itemToEquipmentMap = new HashMap<Holdable, EquipmentEnum>();		
	}
		
	public Equipment(Map<EquipmentEnum, Holdable> set, Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		equipmentToItemMap = new EnumMap<EquipmentEnum, Holdable>(EquipmentEnum.class);
		itemToEquipmentMap = new HashMap<Holdable, EquipmentEnum>();

		for (EquipmentEnum key : set.keySet()) {
			equip(key, set.get(key));
		}
	}
	
	public Equipment(Equipment toCopy, Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.equipmentToItemMap = new EnumMap<EquipmentEnum, Holdable>(toCopy.equipmentToItemMap);
		this.itemToEquipmentMap = new HashMap<Holdable, EquipmentEnum>(toCopy.itemToEquipmentMap);
	}
		
	// This allows gear switching, rather than unequiping then equiping.
	public void equip(EquipmentEnum k, Holdable v) {
		if (equipmentToItemMap.containsKey(k)) { // Is that a slot this mobile has available?
			forceEquip(k, v);
		}
	}
		
	// Replaces whatever was in slot with new Holdable.
	// MAJOR METHOD. Is the ACTUAL do-er of this object!
	// Actually this doesn't work. MoveHoldable moves item location to another (equipment is not a loc)
	public void forceEquip(EquipmentEnum slot, Holdable item) {
		handleOutgoingItem(slot);
		handleIncomingItem(slot, item);
	}
	
	private void handleOutgoingItem(EquipmentEnum slot) {
		Holdable outgoing = equipmentToItemMap.get(slot);
		if (outgoing != null) {
			outgoing.moveHoldable(currentPlayer);
			itemToEquipmentMap.remove(outgoing);	
		}
		equipmentToItemMap.put(slot, null);
	}
	
	private void handleIncomingItem(EquipmentEnum slot, Holdable item) {
		equipmentToItemMap.put(slot, item);
		itemToEquipmentMap.put(item, slot);	
	}
	
	public void remove(Holdable item) {
		EquipmentEnum slot = getKey(item);
		itemToEquipmentMap.remove(item);	
		equipmentToItemMap.put(slot, null);
	}
	
	public void unEquip(EquipmentEnum slot) {
		equip(slot, null);
	}
	
	public void unEquip(Holdable item) {
		EquipmentEnum key = itemToEquipmentMap.get(item);
		equip(key, null);
	}
	
	public Holdable getValue(EquipmentEnum key) {
		return equipmentToItemMap.get(key);
	}
	
	public EquipmentEnum getKey(Holdable val) {
		return itemToEquipmentMap.get(val);
	}
	
	public Collection<Holdable> values() {
		Collection<Holdable> values = getKeyToValMap().values();
		if (values.isEmpty()) {
			return new ArrayList<Holdable>();
		}
		return values;
	}
	
	public Map<EquipmentEnum, Holdable> getKeyToValMap() {
		return equipmentToItemMap;
	}
	
	public Map<Holdable, EquipmentEnum> getValToKeyMap() {
		return this.itemToEquipmentMap;
	}
	
	public static enum EquipmentEnum {
		HEAD(),
		LEFTEAR(),
		RIGHTEAR(),
		NECK(),
		CHEST(),
		LEFTHAND(),
		RIGHTHAND(),
		LEFTFINGER(),
		RIGHTFINGER(),
		LEGS(),
		FEET();
		
		private EquipmentEnum() {			
		}	
		
		public static EquipmentEnum fromString(String text) {
		    if (text != null) {
		    	for (EquipmentEnum b : EquipmentEnum.values()) {
		    		if (text.equalsIgnoreCase(b.toString())) {
		    			return b;
		    		}
		    	}
		    }
		    return null;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(currentPlayer.getName() + "'s equipment: ");
		for (Map.Entry<EquipmentEnum, Holdable> entry : equipmentToItemMap.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(entry.getKey().toString());
				sb.append(entry.getValue().getName());
			}
		}
		return sb.toString();
	}
	
	public boolean hasItem(Holdable item) {
		return itemToEquipmentMap.containsKey(item);
	}
}
	

