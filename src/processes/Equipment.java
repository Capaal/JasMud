package processes;

import java.util.HashMap;
import java.util.Map;

import interfaces.Holdable;
import interfaces.Mobile;

public class Equipment {
	
	private Holdable rightHand;
	private Holdable leftHand;
//	private Armor armor;
	private Mobile bondedMobile;
	
	public Equipment(Mobile mobile) {
		this.bondedMobile = mobile;
	}
	
	public Equipment(Equipment equipment, Mobile finishedMob) {
		bondedMobile = finishedMob;
		rightHand = equipment.rightHand;
		leftHand = equipment.leftHand;
	}

	public void wield(Holdable toWield, EquipmentSlot slot) {
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
				
			break;
			
			default:
				wield(toWield, EquipmentSlot.RIGHTHAND);
			break;
		}		
	}
	
	public void unwield(EquipmentSlot slot) {
		Holdable item = slot.getItem(this);
		if (item != null) {
			slot.unwield(this);
			bondedMobile.acceptItem(item);
		}
	}
	
	public void unwield(Holdable item) {
		if (rightHand != null && rightHand.equals(item)) {
			unwield(EquipmentSlot.RIGHTHAND);
		}
		if (leftHand != null && leftHand.equals(item)) {
			unwield(EquipmentSlot.LEFTHAND);
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
	//	if (armor != null) {
	//		sb.append(armor);
	//	} else {
	//		sb.append("none");
	//	}
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
				return equipment.rightHand;		// TODO	
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
		rightHand.setContainer(bondedMobile);
		leftHand.setContainer(bondedMobile);
		return this;
	}	
}	