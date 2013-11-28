package Interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import processes.Command;
import processes.Location;
import processes.SendMessage;
import skills.Arcane.SkillBook;



public interface Mobile extends Container, Holdable, Creatable {
	
	public String getName();
	public int getId();
	public String getPassword();
	public int getMaxHp();
	public int getCurrentHp();
	public Container getContainer();
	public boolean hasBalance();
	public boolean getIsDead();
	public int getSpeed();
	public String getDescription();
	public String getShortDescription();
	public int getXpWorth();
	public void takeDamage(double d);
	public void tell(String msg);
	public void tellLine(String msg);
	public void addExperience(int exp);
	public void levelMobile();
	public void setContainer(Container newLoc);
	public boolean commandAllowed(String command);
	public Command getCommand(String command);
	public Set<String> getCommandKeySet();
	public Collection<Command> getCommandValueSet();
	public void acceptItem(Holdable item);
	public int getMessagesSize() ;
	public void addBug(String bugMsg);
	public Creatable create();
	public void acceptCommands(HashMap<String, Command> givenCommands);
	public void removeCommands(HashMap<String, Command> removedCommands);
	public void addEffect(String effectName, Effect effect);
	public Effect getEffect(String effect);
	public void runEffects();
	public boolean hasEffect(String effect);
	public void removeEffect(String effect);
	public int getBaseDamage();
	public int getTick();
	public void affectMana(int mana);
	public boolean hasMana(int mana);
	public SkillBook getBook(String bookName);
	public void acceptCommand(String comName, Command command);
	
}