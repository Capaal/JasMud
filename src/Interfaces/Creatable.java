package Interfaces;

import processes.Location;


public interface Creatable extends Holdable {
	public Creatable create();

	public void setItemLocation(Container futureLocation);
}
