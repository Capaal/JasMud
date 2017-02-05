package processes;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.*;

import Quests.Quest.Trigger;
import processes.Location.GroundType;

// EQUIPMENT SHOULD USE HOLDABLE not equipable, all StdItems can be wielded in hands, so this should be able to take anything.
// Most items won't qualify for slots.
// Should an interface exist for each slot. all items can be held in hands, chest in chest legs on legs
// And so on, and then I will extend StdItems to each of these new classes, the big difference being where they can be worn.
// Flaw might be, can anything be worn in multiple places?
// Two-handed weapons? shit
public class Equipment implements Container {

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
	
	private Map<EquipmentEnum, Holdable> getKeyToValMap() {
		return equipmentToItemMap;
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
		    		if (text.equalsIgnoreCase(b.name())) {
		    			return b;
		    		}
		    	}
		    }
		    return null;
		}
	}
	
	


	@Override // TODO
	public TreeMap<String, Holdable> getInventory() {
//		return new TreeMap<String, Holdable>(values());
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
	//	return currentPlayer.getId();
		return -1;
	}

	@Override
	public void acceptItem(Holdable newItem) {
		System.out.println("Something just called accept item on equipment.");
		
	}

	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Holdable getHoldableFromString(String holdableString) {
		// TODO Auto-generated method stub
		return null;
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
	

