package items;

import Interfaces.Container;
import Interfaces.Creatable;
import Interfaces.Holdable;

public interface Item extends Container, Holdable, Creatable {
	
	public int returnDamage();
	public String returnDescription();
	public String returnName();
	public int returnID();
	
	public void setItemLocation(Container con);
}