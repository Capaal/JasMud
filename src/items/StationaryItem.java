package items;

import processes.ContainerErrors;
import processes.Location;
import processes.StdMob;

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
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new StationaryItemBuilder());
	}
	
	protected ItemBuilder newBuilder(StationaryItemBuilder newBuild) {
		super.newBuilder(newBuild);
		return newBuild;
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
