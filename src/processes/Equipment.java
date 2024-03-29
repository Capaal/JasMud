package processes;

import java.util.HashMap;
import java.util.Map;

import interfaces.Holdable;
import interfaces.Mobile;
import items.Armor;

// Equipment including armor and weapons, Possibly backpacks and pouches?
public class Equipment {
	
	private Holdable rightHand;
	private Holdable leftHand;
	private Armor armor;
	
	private final Mobile bondedMobile;
	
	public Equipment(Mobile mobile) {
		this.bondedMobile = mobile;
	}
	
	public Equipment(Equipment equipment, Mobile finishedMob) {
		bondedMobile = finishedMob;
		rightHand = equipment.rightHand;
		leftHand = equipment.leftHand;
		armor = equipment.armor;
	}

	/**
	 * Point an item as being in an equipment slot. Swaps out previous Holdable in slot to Mobile's inventory.
	 * @param toWield Holdable to put into wielding slot. Must be allowed in slot.
	 * @param slot EquipmentSlot enum defining allowed equipment slots.
	 * @throws IllegalArgumentException if the Holdable cannot be placed in the desired slot.
	 */
	public void wield(Holdable toWield, EquipmentSlot slot) {
		if (!toWield.getAllowedEquipSlots().contains(slot)) {
			throw new IllegalArgumentException("Holdable may not occupy a slot it does not allow.");
		}
		switch(slot) {
			case RIGHTHAND:
				unwield(slot);
				rightHand = toWield;
			break;
			
			case LEFTHAND:
				unwield(slot);
				leftHand = toWield;
			break;
			
			case ARMOR:
				unwield(slot);
				armor = (Armor)toWield;
				bondedMobile.addDefense(((Armor)toWield).getDefenceMod());
				bondedMobile.changeBalanceMult(((Armor)toWield).getBalanceMult());
			break;
			
			default:
				wield(toWield, EquipmentSlot.RIGHTHAND);
			break;
		}		
	}
	
	/**
	 * Removes whatever Holdable may be in desired slot and places in Mobile's inventory.
	 * @param slot EquipmentEnum defining slot to unwield from.
	 */
	public void unwield(EquipmentSlot slot) {
		Holdable item = slot.getItem(this);
		if (item != null) {
			slot.unwield(this);
			bondedMobile.acceptItem(item);
		}
	}
	
	/**
	 * Unwields a Holdable and places in Mobiles Inventory when the Slot of item is unknown.
	 * @param item Holdable to search for an remove.
	 */
	public void unwield(Holdable item) {
		if (rightHand != null && rightHand.equals(item)) {
			unwield(EquipmentSlot.RIGHTHAND);
		}
		if (leftHand != null && leftHand.equals(item)) {
			unwield(EquipmentSlot.LEFTHAND);
		}
		if (armor != null && armor.equals(item)) {
			unwield(EquipmentSlot.ARMOR);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Right: ");
		if (rightHand != null) {
			sb.append(rightHand);
		} else {
			sb.append("nothing");
		}
		sb.append(" and Right: ");
		if (leftHand != null) {
			sb.append(leftHand);
		} else {
			sb.append("nothing");
		}
		sb.append(System.lineSeparator());
		sb.append("Armor: ");
		if (armor != null) {
			sb.append(armor);
		} else {
			sb.append("none");
		}
		return sb.toString();
			
	}
	
	public static enum EquipmentSlot {
		RIGHTHAND() {
			public Holdable getItem(Equipment equipment) {
				return equipment.rightHand;			
			}
			
			public void unwield(Equipment equipment) {
				equipment.rightHand = null;
			}
		},
		LEFTHAND(){
			public Holdable getItem(Equipment equipment) {
				return equipment.leftHand;			
			}
			
			public void unwield(Equipment equipment) {
				equipment.leftHand = null;
			}
		},
		ARMOR(){
			public Holdable getItem(Equipment equipment) {
				return equipment.armor;		
			}
			public void unwield(Equipment equipment) {
				equipment.bondedMobile.addDefense(-equipment.armor.getDefenceMod());
				equipment.bondedMobile.changeBalanceMult(-equipment.armor.getBalanceMult());
				equipment.armor = null;
			}
		};
		
		private EquipmentSlot() {			
		}
		
		public void unwield(Equipment equipment) {
			System.out.println("Should not be called. equipment");
		}
		
		public static EquipmentSlot fromString(String text) {
		    if (text != null) {
		    	for (EquipmentSlot b : EquipmentSlot.values()) {
		    		if (b.toString().toLowerCase().startsWith(text.toLowerCase())) {
		    			return b;
		    		}
		    	}
		    }
		    return null;
		}

		public Holdable getItem(Equipment equipment) {
			System.out.println("Bad call on equipment enum getItem.");
			return null;
		}
	}

	public boolean hasItem(Holdable oldItem) {
		if (rightHand == oldItem || leftHand == oldItem) {
			return true;
		} else {
			return false;
		}
	}

	public Holdable getEquipmentInSlot(EquipmentSlot slot) {
		return slot.getItem(this);
	}
	
	public Map<String, Holdable> viewEquipment() {
		Map<String, Holdable> equipmentView = new HashMap<String, Holdable>();
		if (rightHand != null){
			equipmentView.put(rightHand.getName()+rightHand.getId(), rightHand);
		}
		if (leftHand != null) {
			equipmentView.put(leftHand.getName()+leftHand.getId(), leftHand);
		}
		return equipmentView;
	}
	
	private Object readResolve() {
		if (rightHand != null) rightHand.setContainer(bondedMobile);
		if (leftHand != null) leftHand.setContainer(bondedMobile);
		return this;
	}	
}	