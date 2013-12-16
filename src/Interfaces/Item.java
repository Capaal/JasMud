package interfaces;

public interface Item extends Holdable, Creatable {	
	
	public int getDamage();
	public String getDescription();
	public String getName();
	public int getId();	
	public void setContainer(Container con);
}