package interfaces;

import processes.ContainerErrors;

// This is the basiest base item hopefully.
// SHOULD only define something that can be HELD by CONTAINERs
public interface Holdable extends Comparable<Holdable> {	
	
	public String getName();
	public int getId();
	public String getDescription();
	public Container getContainer();
	public void save(); // should be pulled out to its own interface
//	public String firstTimeSave(XStream xstream); // should be pulled out to its own interface
	public void removeFromWorld();  // NOT the same as DELETE
	public ContainerErrors moveHoldable(Container finalLocation);
	public void setContainer(Container container);// Used for save/load. Not good to use otherwise?
	public String getInfo(); //name + id
	public String getShortDesc(); //qty + short descriptive name (emtpy herbpouch or aloe herbpouch instead of just herbpouch)
//  public String getShortLook(); //qty + short ground description	
	public String getExamine();
	public double getWeight();
	public int getQuantity();
	public ContainerErrors moveHoldable(Container container, int quantity);
	public void delete();
	public void removeFromStack(int qty);

}
