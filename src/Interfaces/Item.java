package interfaces;

import processes.Command;
import processes.StdMob;
import skills.*;


public interface Item extends Holdable, Creatable {
	
	
	public int getDamage();
	public String getDescription();
	public String getName();
	public int getId();
//	final Command[] givenCommands = new Command[] {new Get()};
	
	public void setContainer(Container con);

	
//	public void giveCommands(StdMob mob);
//	public void removeCommands(StdMob mob);
}