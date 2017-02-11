package interfaces;

import items.StdItem;

import java.util.Map;

import processes.Equipment;
import processes.Equipment.EquipmentEnum;
import processes.InductionSkill;
import processes.Location;
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
	public int getDefense();
	public void addDefense(int i);
	@Override public Location getContainer();
	public boolean hasBalance();
	public boolean isDead();
	public String getDescription();
	public String getShortDescription();
	public int getXpWorth();
	public void takeDamage(Type types, int d);
	public void tell(String msg);
	public void tellLine(String msg);
//	public void setContainer(Container newLoc);  SHOULD be handled by moveHoldable(finalLocation);
	public Skills getCommand(String command);
	public void acceptItem(Holdable item);
	public boolean addEffect(Effect newEffect, int duration);
	public void addTickingEffect(TickingEffect newEffect, int duration, int times);
//	public double getWeaponMultiplier(); TODO
	public boolean hasEffect(Effect effect);
	public void removeEffect(Effect effect);
//	public void removeItem(Holdable item); Handled by Container
	public void addBook(SkillBook skillBook, int progress);
	public boolean isControlled();
	public void controlStatus(boolean statusChange);
	public void save();
//	public void setStartup(boolean b);
	public void equip(EquipmentEnum slot, StdItem item);
	public void unEquip(EquipmentEnum slot);
	public void unEquip(Holdable item);
	public EquipmentEnum findEquipment(String itemName);
	public Holdable getEquipmentInSlot(EquipmentEnum slot);
	public void removeFromWorld();
	public void displayPrompt();
	public void setSendBack(SendMessage sendBack);
//	public SendMessage getSendBack(); probably should not be freely available
	public void informLastAggressor(Mobile currentPlayer);
//	public boolean isCreating(); All creating stuff is probably being scrapped
//	public void startCreating();
//	public void stopCreating();
	public boolean isInducting();
	public void killInduction();
	public void setInduction(InductionSkill skill);
	public Map<SkillBook, Integer> viewSkillBooks();
	public void dropItemsOnDeath();
	public Equipment getEquipment();
	public void createNewEffectManager();
	
}
