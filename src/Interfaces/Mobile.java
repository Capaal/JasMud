package interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import processes.Equipment.EquipmentEnum;
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
	public boolean isDead();
	public int getSpeed();
	public String getDescription();
	public String getShortDescription();
	public int getXpWorth();
	public void takeDamage(Set<Type> types, int d);
	public void tell(String msg);
	public void tellLine(String msg);
	public void addExperience(int exp);
	public void levelMobile();
	public void setContainer(Container newLoc);
	public Skill getCommand(String command);
	public void acceptItem(Holdable item);
	public int getMessagesSize() ;
	public void addBug(String bugMsg);
	public Creatable create();
	public void addEffect(Effect newEffect, int duration);
	public void addEffect(TickingEffect newEffect, int duration, int times);
	public int checkEffectsAgainstIncomingDamage(Set<Type> incomingTypes, int damage);
	public boolean hasEffect(Effect effect);
	public void removeEffect(Effect effect);
	public int getBaseDamage();
	public int getTick();
	public void affectMana(int mana);
	public boolean hasMana(int mana);
	public SkillBook getBook(String bookName);
//	public boolean hasWeaponType(Type type);
	public void removeItem(Holdable item);
	public void addBook(SkillBook skillBook, int progress);
	public boolean isControlled();
	public void controlStatus(boolean statusChange);
	public boolean save();
	public void setStartup(boolean b);
	public SendMessage getSendBack();
	public void equip(EquipmentEnum slot, Holdable item);
	public void unequip(Holdable item);
	public void unequipFromSlot(EquipmentEnum slot);
	public EquipmentEnum findEquipment(String itemName);
	public Holdable getEquipmentInSlot(EquipmentEnum slot);
	public void removeFromWorld();
	public void displayPrompt();
	public void setSendBack(SendMessage sendBack);
}
