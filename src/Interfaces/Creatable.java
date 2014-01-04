package interfaces;

import processes.Location;


public interface Creatable extends Holdable {
	public Creatable create();

	public void setContainer(Container futureLocation);
}
