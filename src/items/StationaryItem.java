package items;

import processes.ContainerErrors;
import processes.StdMob;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import interfaces.Container;

// Extension of StdItem that cannot be moved. Nor can it be created in any Container other than a Location.
@XStreamAlias("StationaryItem")
public class StationaryItem extends StdItem {

	public StationaryItem(ItemBuilder build) {
		super(build);
		// TODO Auto-generated constructor stub
	}
	
	public static class StationaryItemBuilder extends ItemBuilder {	
		@Override public void setItemContainer(Container itemLocation) {
			if (itemLocation instanceof StdMob) {
				this.itemContainer = ((StdMob)itemLocation).getContainer();
			} else {
				this.itemContainer = itemLocation;
			}
		}

		@Override public StdItem produceType() {
			return new StationaryItem(this);
		} 
	}	
	
	//not a containererror but ok TODO
	@Override
	public ContainerErrors moveHoldable(Container finalLocation) {
		return ContainerErrors.STATIONARY;
	}

	
	

}
