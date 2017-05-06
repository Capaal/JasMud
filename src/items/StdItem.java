package items;

import java.util.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import interfaces.*;
import items.ItemBuilder.ItemType;
import processes.*;
import processes.Equipment.EquipmentEnum;

@XStreamAlias("StdItem")
public class StdItem implements Holdable{
	
	protected final String name;
	protected final int id;	
	protected final String description;
	protected final double physicalMult;
	protected final double balanceMult; 
	protected final double defenseMult; //for my iron potion
//	@XStreamOmitField
	protected Container itemLocation;	
//	protected final int maxDurability;
//  protected int currentDurability;
	protected final List<String> components; // Add to weapon interface? Make a craftable interface?
	protected final boolean salvageable; // same as components?
	protected MercWeapon.MercEffect mercEffect;
	
	protected final Set<EquipmentEnum> allowedEquipSlots;

	public StdItem(ItemBuilder build) {
		this.id = build.getId();
		this.name = build.getName();
		this.physicalMult = build.getDamageMult();
		this.description = build.getDescription();
		this.balanceMult = build.getBalanceMult();
		this.defenseMult = build.getDefenseMult();
		this.itemLocation = build.getItemContainer();		
//		this.maxDurability = build.getMaxDurability();
//		this.currentDurability = build.getCurrentDurability();
		this.allowedEquipSlots = build.getAllowedSlots();
		this.components = build.getComponents();
		this.salvageable = build.getSalvageable();
		this.mercEffect = build.getMercEffect();
		
		WorldServer.gameState.addItem(name + id, this);
		itemLocation.acceptItem(this);
	}
	
	@Override public String getName() {return name;}
	@Override public int getId() {return id;}
	@Override public String getDescription() {return description;}	
	public double getPhysicalMult() {return physicalMult;}
	public double getBalanceMult() {return balanceMult;}
	public double getDefenseMult() {return defenseMult;}
	@Override public synchronized Container getContainer() {return itemLocation;}	
	public void doOnAttack() {}; //for my mercenary attack skill
	
//	public int getMaxDurability() {return maxDurability;}
	
//	public synchronized int getCurrentDurability() {return currentDurability;}
//	public synchronized void setDurability(int newDurability) {
//		if (newDurability > maxDurability) {
//			newDurability = maxDurability;
//		}
//		this.currentDurability = newDurability;
//	}
	
	public Set<EquipmentEnum> getAllowedEquipSlots() {return allowedEquipSlots;}
	public List<String> getComponents() {return components;}
	
	public double getDamageMult() {
		return physicalMult;
	}
	
	
	
	@Override
	public void moveHoldable(Container finalLocation) {
		getContainer().removeItemFromLocation(this);
		finalLocation.acceptItem(this);
		this.itemLocation = finalLocation;
	}
	
	// Most of equiping is handled by StdMob
	public void equip(Mobile player) {
		getContainer().removeItemFromLocation(this);
		this.itemLocation = player;
	}
	
	
	@Override
	public void removeFromWorld() {
	//	save();
		this.getContainer().removeItemFromLocation(this);
		// Below triggered when I picked up a single ore??? TODO
		if (!WorldServer.gameState.removeItem(this.getName() + this.getId())) {
			System.out.println("Item tried to be removed that cannot be: " + this.getName() + this.getId());
		}
		this.itemLocation = null;
	}
	
	// not sure how packs will work yet...
	@Override
	public void save() {
		WorldServer.saveItem(this);		
	}
	
	private Object readResolve() {
		WorldServer.gameState.addItem(name + id, this);
	//	getContainer().acceptItem(this);
		return this;
	}

	@Override
	public int compareTo(Holdable other) {
		String thisItem = this.getName()+this.getId();
		String otherItem = other.getName()+other.getId();
		System.out.println(thisItem.compareToIgnoreCase(otherItem));
		return thisItem.compareToIgnoreCase(otherItem);
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!(obj instanceof StdItem)) {
	    	return false;
	    }
	    final Holdable other = (Holdable) obj;
	    String thisHoldable = this.getName() + this.getId();
	    String otherHoldable = other.getName() + other.getId();
	    return thisHoldable.equals(otherHoldable);
	 //   if ((thisHoldable.compareToIgnoreCase(otherHoldable)) >= 0) {
	  //  	return true;
	   // }
	//    return false;
	}
	
	public ItemBuilder newBuilder() {
		ItemBuilder newBuilder = new ItemBuilder();
		newBuilder.setId(this.id + 1);  // THIS IS REALLY BAD, need way of getting new ids
		newBuilder.setName(name);
		newBuilder.setDamageMult(physicalMult);
		newBuilder.setDescription(description);
		newBuilder.setBalanceMult(balanceMult);
		newBuilder.setDefenseMult(defenseMult);
		newBuilder.setItemContainer(itemLocation);
		newBuilder.setItemType(ItemType.STDITEM);
	//	newBuilder.setAllowedSlots(allowedEquipSlots);
		newBuilder.setComponents(components);
		newBuilder.setSalvageable(salvageable);		
		return newBuilder;
	}
	
	// Not game safe, but required for save/load
	public void setContainer(Container container) {
		this.itemLocation = container;
	}
	
	public MercWeapon.MercEffect getMercEffect() {
		return this.mercEffect;
	}

	@Override
	public boolean canPickup() {
		return true;
	}

}
