package processes;

import interfaces.Container;
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

	private Map<EquipmentEnum, Holdable> equipmentToItemMap;
	private Map<Holdable, EquipmentEnum> itemToEquipmentMap;
	private Mobile currentPlayer;
		
	public Equipment() {
		equipmentToItemMap = new EnumMap<EquipmentEnum, Holdable>(EquipmentEnum.class);
		itemToEquipmentMap = new HashMap<Holdable, EquipmentEnum>();		
	}
		
	public Equipment(Map<EquipmentEnum, Holdable> set) {	
		equipmentToItemMap = new EnumMap<EquipmentEnum, Holdable>(EquipmentEnum.class);
		itemToEquipmentMap = new HashMap<Holdable, EquipmentEnum>();

		for (EquipmentEnum key : set.keySet()) {
			equip(key, set.get(key));
		}
	}
		
	// This allows gear switching, rather than unequiping then equiping.
	public void equip(EquipmentEnum k, Holdable v) {
		if (equipmentToItemMap.containsKey(k)) {
			forceEquip(k, v);
		}
	}
		
	public void forceEquip(EquipmentEnum k, Holdable v) {
		equipmentToItemMap.put(k, v);
		itemToEquipmentMap.put(v, k);	
		if (v != null) {
			v.setContainer(this);
		}
	}
	
	public void unequipSlot(EquipmentEnum slot) {
		equip(slot, null);
	}
	
	public void unequipItem(Holdable item) {
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

		@SuppressWarnings("unused")
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
	public Set<Holdable> getInventory() {
		return new HashSet<Holdable>(values());
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
	public void acceptItem(Holdable newItem) {
		System.out.println("Something just called accept item on equipment.");
		
	}

	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		// TODO Auto-generated method stub
		
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

	@Override
	public Container getContainer() {
		return currentPlayer;
	}
}
	

