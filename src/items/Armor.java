package items;

import java.util.EnumSet;

import processes.Equipment.EquipmentSlot;

public class Armor extends StdItem {

	// Durability?
	private final int defenceMod;
	private final double balanceMult;
	
	public Armor(ArmorItemBuilder build) {
		super(build);
		this.defenceMod = build.getDefenceMod();
		this.balanceMult = build.getBalanceMult();
	}
	
	public int getDefenceMod() {
		return defenceMod;
	}
	
	public double getBalanceMult() {
		return balanceMult;
	}
	
	
	public static class ArmorItemBuilder extends ItemBuilder {
		
	// Durability?
		private int defenceMod = 5;
		private double balanceMult = .2;
		
		public ArmorItemBuilder() {
			super();
			allowedSlots = EnumSet.of(EquipmentSlot.ARMOR);
		}		
		
		public int getDefenceMod() {
			return defenceMod;
		}
		
		public void setDefenceMod(int newMod) {
			this.defenceMod = newMod;
		}
		
		public double getBalanceMult() {
			return balanceMult;
		}
		
		public void setBalanceMult(double newMult) {
			this.balanceMult = newMult;
		}
		
		@Override public StdItem produceType() {
			return new Armor(this);
		} 		
	}	

}
