package interfaces;

import java.util.Map;

import processes.Equipment.EquipmentEnum;
import processes.InductionSkill;
import processes.SendMessage;
import processes.Skills;
import processes.SkillBook;
import processes.Type;

public interface Mobile extends Container, Holdable {
	
	public String getName();
	public int getId();
	public String getPassword();
	public int getMaxHp();
	public int getCurrentHp();
	public Container getContainer();
	public boolean hasBalance();
	public boolean isDead();
	public String getDescription();
	public String getShortDescription();
	public int getXpWorth();
	public void takeDamage(Type types, int d);
	public void tell(String msg);
	public void tellLine(String msg);
	public void setContainer(Container newLoc);
	public Skills getCommand(String command);
	public void acceptItem(Holdable item);
	public void addEffect(Effect newEffect, int duration);
	public void addTickingEffect(TickingEffect newEffect, int duration, int times);
	public int checkEffectsAgainstIncomingDamage(Type incomingType, int damage);
	public double getWeaponMultiplier();
	public boolean hasEffect(Effect effect);
	public void removeEffect(Effect effect);
	public void removeItem(Holdable item);
	public void addBook(SkillBook skillBook, int progress);
	public boolean isControlled();
	public void controlStatus(boolean statusChange);
	public boolean save();
	public void checkHp();
	public void setStartup(boolean b);
	public void equip(EquipmentEnum slot, Holdable item);
	public void unequip(Holdable item);
	public void unequipFromSlot(EquipmentEnum slot);
	public EquipmentEnum findEquipment(String itemName);
	public Holdable getEquipmentInSlot(EquipmentEnum slot);
	public void removeFromWorld();
	public void displayPrompt();
	public void setSendBack(SendMessage sendBack);
	public SendMessage getSendBack();
	public void informLastAggressor(Mobile currentPlayer);
	public boolean isCreating();
	public void startCreating();
	public void stopCreating();
	public boolean isInducting();
	public void killInduction();
	public void setInduction(InductionSkill skill);
	public Map<SkillBook, Integer> viewSkillBooks();
	public void dropItemsOnDeath();
}
