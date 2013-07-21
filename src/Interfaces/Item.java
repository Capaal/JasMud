package Interfaces;


public interface Item extends Container, Holdable, Creatable {
	
	public int getDamage();
	public String getDescription();
	public String getName();
	public int getId();
	
	public void setItemLocation(Container con);
}