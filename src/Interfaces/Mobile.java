package interfaces;

import items.StdItem;

import java.util.Map;
import java.util.Set;

import effects.PassiveCondition;
import processes.ContainerErrors;
import processes.Equipment;
import processes.Equipment.EquipmentEnum;
import processes.InductionSkill;
import processes.Location;
import processes.SendMessage;
import processes.Skills;
import processes.SkillBook;
import processes.Type;

public interface Mobile extends Container {
	
	public String getName();
	public int getId();
	public String getPassword();
	public int getMaxHp();
	public int getCurrentHp();
	public int getDefense();
	public void addDefense(int i);
	public Location getContainer();
	public boolean hasBalance();
	public boolean isBlocking();
	public void changeBlocking(boolean b);
	public boolean isDead();
	public String getDescription();
	public String getShortDescription();
	public int getXpWorth();
	public void takeDamage(Type types, int d);
	public void tell(String msg);
	public void tellLine(String msg);
//	public void setContainer(Container newLoc);  SHOULD be handled by moveHoldable(finalLocation);
	public Skills getCommand(String command);
	public ContainerErrors acceptItem(Holdable item);
	public boolean addPassiveCondition(PassiveCondition newEffect, int duration);
	public boolean addActiveCondition(TickingEffect newEffect, int times);
//	public double getWeaponMultiplier(); TODO
	public boolean hasCondition(TickingEffect effect);
	public boolean hasCondition(PassiveCondition effect);
	public void removeCondition(TickingEffect effect);
	public void removeCondition(PassiveCondition effect);
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
	public void spawnDeathLoot();
	public Equipment getEquipment();
	public void addAllConditions(PassiveCondition conditions);
	public void createNewEffectManager();
	public void removeAllConditions(PassiveCondition conditions);
	public Set<PassiveCondition> getAllConditions();
	public boolean hasAllConditions(PassiveCondition conditions);
	public void moveHoldable(Location finalLocation);
	public void setContainer(Location container);
	public int compareTo(Mobile arg0);
//	public void setGender();
//	public String getGenderHimHer();
//	public String getGenderHeShe();
	
}
