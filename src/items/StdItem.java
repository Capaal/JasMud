package items;

import java.io.File;
import java.util.*;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import interfaces.*;
import processes.*;
import processes.Equipment.EquipmentSlot;

@XStreamAlias("StdItem")
public class StdItem implements Holdable{
	
	protected final String name;
	protected final int id;		
	protected final String description;
	protected final double physicalMult;
	protected final double balanceMult; 
	protected final List<StdItem> components; // Add to weapon interface? Make a craftable interface?
	protected final boolean salvageable;
	protected final double weight;	
	protected final Set<EquipmentSlot> allowedEquipSlots;
	protected int quantity = 1;
	protected Container itemLocation;

	public StdItem(ItemBuilder build) {
		this.id = build.getId();
		this.name = build.getName();
		this.physicalMult = build.getDamageMult();
		this.description = build.getDescription();
		this.balanceMult = build.getBalanceMult();
		this.itemLocation = build.getItemContainer();		
//		this.maxDurability = build.getMaxDurability();
//		this.currentDurability = build.getCurrentDurability();
		this.allowedEquipSlots = build.getAllowedSlots();
		this.components = build.getComponents();
		this.salvageable = build.getSalvageable();
		this.weight = build.getWeight();		
	}
	
	@Override public String getName() {return name;}
	@Override public int getId() {return id;}
	@Override public String getDescription() {return description;}	
	public double getPhysicalMult() {return physicalMult;}
	public double getBalanceMult() {return balanceMult;}
	public double getWeight() {return weight;}
	@Override public Container getContainer() {return itemLocation;}	
	
	public int getQuantity() {return quantity;}
	
	public Set<EquipmentSlot> getAllowedEquipSlots() {return allowedEquipSlots;}
	public List<StdItem> getComponents() {return components;}
	
	public double getDamageMult() {
		return physicalMult;
	}	
	
	@Override
	public ContainerErrors moveHoldable(Container finalLocation) {
		ContainerErrors error = finalLocation.acceptItem(this);		
		if (error != null) {
			return error;
		}
		getContainer().removeItemFromLocation(this);		
		this.itemLocation = finalLocation;
		return error;
	}	
	
	@Override
	public void removeFromWorld() {
		this.getContainer().removeItemFromLocation(this);
		this.itemLocation = null;
	}
	
	// not sure how packs will work yet...
	@Override
	public void save() {
		WorldServer.getGameState().saveItem(this);		
	}

	@Override
	public int compareTo(Holdable other) {
		String thisItem = this.getName()+this.getId();
		String otherItem = other.getName()+other.getId();
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
		return newBuilder(new ItemBuilder());
	}
	
	public ItemBuilder newBuilder(ItemBuilder newBuilder) {
		newBuilder.setName(name);
		newBuilder.setDamageMult(physicalMult);
		newBuilder.setDescription(description);
		newBuilder.setBalanceMult(balanceMult);
		newBuilder.setItemContainer(itemLocation);
	//	newBuilder.setAllowedSlots(allowedEquipSlots);
		newBuilder.setComponents(components);
		newBuilder.setSalvageable(salvageable);	
		newBuilder.setWeight(weight);
		return newBuilder;
	}
	
	// Not game safe, but required for save/load
	public void setContainer(Container container) {
		this.itemLocation = container;
	}
	
	@Override 
	public String getInfo() { 
		StringBuilder s = new StringBuilder();
		s.append(this.getName());
		s.append(this.getId());
		return s.toString();
	}
	
	@Override
	public String getExamine() {
		return this.getDescription();
	}
	
	@ Override
	public String getShortDesc() {
		return this.getName();
	}
	
	public  void addToStack(int quantity) {
		throw new IllegalStateException("StdItems cannot add to quantity.");
	}
	
	public void removeFromStack(int qty) {
		throw new IllegalStateException("StdItems cannot remove from quantity.");
	}

	@Override
	public ContainerErrors moveHoldable(Container container, int quantity) {
		if (quantity != 1) {
			throw new IllegalArgumentException("StdItems cannot be anything but quantity 1.");
		}
		return moveHoldable(container);		
	}

	@Override
	public void delete() {
		File file = new File("./Items/" + this.getName() + this.getId() + ".xml");
		if (file.exists()) {
			file.delete();	
		} else {
			System.out.println("error deleteing item " + this.getName() + this.getId());
		}
	}

}
