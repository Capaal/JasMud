package items;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import interfaces.Container;


@XStreamAlias("StationaryItem")
public class StationaryItem extends StdItem {

	public StationaryItem(ItemBuilder build) {
		super(build);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void moveHoldable(Container finalLocation) {
		System.out.println("Error: StationaryItem moveHoldable activated: " + finalLocation + " " + this.getName() + this.getId());
	}
	
	@Override
	public boolean canPickup() {
		return false;
	}


}
