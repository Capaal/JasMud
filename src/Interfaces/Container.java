 package Interfaces;

import java.util.ArrayList;

public interface Container {
	
	public ArrayList<Holdable> groundItems = new ArrayList<Holdable>();

	public void acceptItem(Holdable item);



}
