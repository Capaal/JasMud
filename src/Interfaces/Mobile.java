package interfaces;

import items.Plant.PlantType;
import items.StdItem;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import effects.PassiveCondition;
import processes.ContainerErrors;
import processes.Equipment.EquipmentSlot;
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
	public Skills getCommand(String command);
	public ContainerErrors acceptItem(Holdable item);
	public boolean addPassiveCondition(PassiveCondition newEffect, int duration);
	public boolean addActiveCondition(TickingEffect newEffect, int times);
//	public double getWeaponMultiplier(); TODO
	public boolean hasCondition(TickingEffect effect);
	public boolean hasCondition(PassiveCondition effect);
	public void removeCondition(TickingEffect effect);
	public void removeCondition(PassiveCondition effect);
	public void addBook(SkillBook skillBook, int progress);
	public boolean isControlled();
	public void controlStatus(boolean statusChange);
	public void save();
	public void equip(EquipmentSlot slot, StdItem item);
	public void unEquip(EquipmentSlot slot);
	public void unEquip(Holdable item);
	public Holdable getEquipmentInSlot(EquipmentSlot slot);
	public void removeFromWorld();
	public void displayPrompt();
	public void setSendBack(SendMessage sendBack);
	public void informLastAggressor(Mobile currentPlayer);
	public boolean isInducting();
	public void killInduction();
	public void setInduction(InductionSkill skill);
	public Map<SkillBook, Integer> viewSkillBooks();
	public void spawnDeathLoot();
	public void moveHoldable(Location finalLocation);
	public void setContainer(Location container);
	public int compareTo(Mobile arg0);
	public Set<TickingEffect> getAllActiveConditions();
	public void moveFollowers(String fullCommand);
	public void stopFollowing();
	public void setFollowing(Mobile finalTarget);
	public void addFollower(Mobile currentPlayer);
	public void removeFollower(Mobile follower);
	EnumSet<PassiveCondition> getAllPassiveEffects();
	public void addCooldown(Cooldown c);
	public void removeCooldown(Cooldown c);
	String getNameColored();
	String getClassName();
	public TreeMap<String, Holdable> viewInventoryWithoutEquipment();
	public boolean isOnCooldown(Cooldown c);
	public void changeBalanceMult(double change);
	public double getBalanceMult();
}
