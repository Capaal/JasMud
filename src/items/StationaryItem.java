package items;

import processes.ContainerErrors;

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
	
	//not a containererror but ok TODO
	@Override
	public ContainerErrors moveHoldable(Container finalLocation) {
		return ContainerErrors.STATIONARY;
	}


}
