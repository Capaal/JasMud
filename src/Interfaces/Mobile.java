package interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;




//import processes.Command;
import processes.Location;
import processes.SendMessage;
import processes.Skill;
import processes.SkillBook;
import processes.Type;

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
	public void takeDamage(List<Type> types, int d);
	public void tell(String msg);
	public void tellLine(String msg);
	public void addExperience(int exp);
	public void levelMobile();
	public void setContainer(Container newLoc);
//	public boolean commandAllowed(String command);
	public Skill getCommand(String command);
//	public Set<String> getCommandKeySet();
//	public Collection<Command> getCommandValueSet();
	public void acceptItem(Holdable item);
	public int getMessagesSize() ;
	public void addBug(String bugMsg);
	public Creatable create();
//	public void acceptCommands(HashMap<String, Command> givenCommands);
//	public void removeCommands(HashMap<String, Command> removedCommands);
	public void addEffect(Effect effect);
//	public Effect getEffect(String effect); Can't figure out if I need this yet or not. Can't be a String atm though.
	public void runTickEffects();
	public int runEffects(List<Type> incomingTypes, int damagey);
	public boolean hasEffect(Effect effect);
	public void removeEffect(Effect effect);
	public int getBaseDamage();
	public int getTick();
	public void affectMana(int mana);
	public boolean hasMana(int mana);
	public SkillBook getBook(String bookName);
//	public void acceptCommand(String comName, Command command);
	public void setBalance(boolean value);
	public boolean hasWeaponType(Type type);
	public void removeItem(Holdable item);
	public void addBook(String string, SkillBook skillBook);
	public boolean isControlled();
	public void controlStatus(boolean statusChange);
}
