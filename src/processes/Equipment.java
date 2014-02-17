package processes;

import interfaces.Container;
import interfaces.Equipable;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.*;

import processes.Location.GroundType;

// EQUIPMENT SHOULD USE HOLDABLE not equipable, all StdItems can be wielded in hands, so this should be able to take anything.
// Most items won't qualify for slots.
// Should an interface exist for each slot. all items can be held in hands, chest in chest legs on legs
// And so on, and then I will extend StdItems to each of these new classes, the big difference being where they can be worn.
// Flaw might be, can anything be worn in multiple places?
public class Equipment implements Container {

	private Map<EquipmentEnum, Equipable> equipmentToItemMap;
	private Map<Equipable, EquipmentEnum> itemToEquipmentMap;
	private Mobile currentPlayer;
		
	public Equipment() {
		equipmentToItemMap = new EnumMap<EquipmentEnum, Equipable>(EquipmentEnum.class);
		itemToEquipmentMap = new HashMap<Equipable, EquipmentEnum>();		
	}
		
	public Equipment(Map<EquipmentEnum, Equipable> set) {	
		equipmentToItemMap = new EnumMap<EquipmentEnum, Equipable>(EquipmentEnum.class);
		itemToEquipmentMap = new HashMap<Equipable, EquipmentEnum>();

		for (EquipmentEnum key : set.keySet()) {
			equip(key, set.get(key));
		}
	}
		
	// This allows gear switching, rather than unequiping then equiping.
	public void equip(EquipmentEnum k, Equipable v) {
		if (equipmentToItemMap.containsKey(k)) {
			forceEquip(k, v);
		}
	}
		
	public void forceEquip(EquipmentEnum k, Equipable v) {
		equipmentToItemMap.put(k, v);
		itemToEquipmentMap.put(v, k);	
		if (v != null) {
			v.setContainer(this);
		}
	}
	
	public void unequipSlot(EquipmentEnum slot) {
		equip(slot, null);
	}
	
	public void unequipItem(Equipable item) {
		EquipmentEnum key = itemToEquipmentMap.get(item);
		equip(key, null);
	}
	
	public Equipable getValue(EquipmentEnum key) {
		return equipmentToItemMap.get(key);
	}
	
	public EquipmentEnum getKey(Equipable val) {
		return itemToEquipmentMap.get(val);
	}
	
	public Collection<Equipable> values() {
		Collection<Equipable> values = getKeyToValMap().values();
		if (values.isEmpty()) {
			return new ArrayList<Equipable>();
		}
		return values;
	}
	
	private Map<EquipmentEnum, Equipable> getKeyToValMap() {
		return equipmentToItemMap;
	}
	
	public void setOwner(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public static enum EquipmentEnum {
		HEAD(EquipmentType.WEAR),
		LEFTEAR(EquipmentType.WEAR),
		RIGHTEAR(EquipmentType.WEAR),
		NECK(EquipmentType.WEAR),
		CHEST(EquipmentType.WEAR),
		LEFTHAND(EquipmentType.WIELD),
		RIGHTHAND(EquipmentType.WIELD),
		LEFTFINGER(EquipmentType.WEAR),
		RIGHTFINGER(EquipmentType.WEAR),
		LEGS(EquipmentType.WEAR),
		FEET(EquipmentType.WEAR);
	
		private EquipmentType equipmentType;
		
		private EquipmentEnum(EquipmentType eType) {
			this.equipmentType = eType;
		}		
		
		private static enum EquipmentType {
			WIELD(),
			WEAR();			
			private EquipmentType() {}
		}
	}


	@Override
	public ArrayList<Holdable> getInventory() {
		return new ArrayList<Holdable>(values());
	}

	@Override
	public String displayExits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void look(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void glance(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayAll(Mobile currentPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		return currentPlayer.getId();
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String desc) {
		// TODO Auto-generated method stub
		
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
	public Container getContainer(String dir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroundType getGroundType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Holdable getHoldableFromString(String holdableString) {
		// TODO Auto-generated method stub
		return null;
	}
}
	

