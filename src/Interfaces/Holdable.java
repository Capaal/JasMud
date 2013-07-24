package Interfaces;

import java.util.ArrayList;

public interface Holdable {
	
	
	public String getName();
	public int getId();
	public String getDescription();
	public void setContainer(Container con);
	public Container getContainer();
	public String getShortDescription();
}
