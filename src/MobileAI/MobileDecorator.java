package MobileAI;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Quests.Quest;
import processes.ContainerErrors;
import processes.Location;
import processes.SendMessage;
import processes.SkillBook;
import processes.Skills;
import processes.Equipment.EquipmentSlot;
import skills.InductionSkill;
import effects.PassiveCondition;
import interfaces.Cooldown;
import interfaces.Holdable;
import interfaces.Mobile;
import interfaces.TickingEffect;
import items.StdItem;

// Decorator Pattern abstract for AI actions.
public abstract class MobileDecorator implements Mobile {

	protected final Mobile decoratedMobile;
	
	public MobileDecorator(Mobile decoratedMobile) {
		this.decoratedMobile = decoratedMobile;
	}
	
	protected abstract void makeDecision();
	
	// Factory Enum to set-up AI action decorators.
	public enum DecoratorType {
			
		AGGRESSIVE() {
			@Override
			public Mobile getDecorator(Mobile m) {
				return new AggresiveMobileDecorator(m);
			}
		},
		CHASING() {
			@Override
			public Mobile getDecorator(Mobile m) {
				return new ChasingMobileDecorator(m);
			}
		},
		GREETSONENTER() {
			@Override
			public Mobile getDecorator(Mobile m) {
		//		((Location)currentPlayer.getContainer()).notifyQuest(Quest.Trigger.GREETS);
				return new EntersMobileDecorator(m, () -> m.getContainer().notifyQuest(Quest.Trigger.GREETS));
			}
		};	
		
		private DecoratorType() {}
		
		public abstract Mobile getDecorator(Mobile m);
	}

	@Override
	public TreeMap<String, Holdable> viewInventory() {
		return decoratedMobile.viewInventory();
	}

	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		decoratedMobile.removeItemFromLocation(oldItem);

	}

	@Override
	public Holdable getHoldableFromString(String holdableString) {
		return decoratedMobile.getHoldableFromString(holdableString);
	}

	@Override
	public String getName() {
		return decoratedMobile.getName();
	}

	@Override
	public int getId() {
		return decoratedMobile.getId();
	}

	@Override
	public String getPassword() {
		return decoratedMobile.getPassword();
	}

	@Override
	public int getMaxHp() {
		return decoratedMobile.getMaxHp();
	}

	@Override
	public int getCurrentHp() {
		return decoratedMobile.getCurrentHp();
	}

	@Override
	public Location getContainer() {
		return decoratedMobile.getContainer();
	}

	@Override
	public boolean hasBalance() {
		return decoratedMobile.hasBalance();
	}

	@Override
	public boolean isDead() {
		return decoratedMobile.isDead();
	}

	@Override
	public String getDescription() {
		return decoratedMobile.getDescription();
	}

	@Override
	public String getShortDescription() {
		return decoratedMobile.getShortDescription();
	}

	@Override
	public int getXpWorth() {
		return decoratedMobile.getXpWorth();
	}

	@Override
	public void takeDamage(int d) {
		decoratedMobile.takeDamage(d);
	}
	
	@Override
	public void takeDamageIgnoresArmor(int damage) {
		decoratedMobile.takeDamage(damage);
	}

	@Override
	public void tell(String msg) {
		decoratedMobile.tell(msg);
	}

	@Override
	public Skills getCommand(String command) {
		return decoratedMobile.getCommand(command);
	}

	@Override
	public ContainerErrors acceptItem(Holdable item) {
		return decoratedMobile.acceptItem(item);
	}

	@Override
	public boolean addPassiveCondition(PassiveCondition newEffect, int duration) {
		return decoratedMobile.addPassiveCondition(newEffect, duration);
	}

	@Override
	public void addActiveCondition(TickingEffect newEffect, int times) {
		decoratedMobile.addActiveCondition(newEffect, times);
	}

	@Override
	public boolean hasCondition(TickingEffect effect) {
		return decoratedMobile.hasCondition(effect);
	}

	@Override
	public void removeCondition(PassiveCondition effect) {
		decoratedMobile.removeCondition(effect);
	}
	
	@Override
	public boolean hasCondition(PassiveCondition effect) {
		return decoratedMobile.hasCondition(effect);
	}

	@Override
	public void removeCondition(TickingEffect effect) {
		decoratedMobile.removeCondition(effect);
	}

	@Override
	public void addBook(SkillBook skillBook, int progress) {
		decoratedMobile.addBook(skillBook, progress);
	}

	@Override
	public boolean isControlled() {
		return decoratedMobile.isControlled();
	}

	@Override
	public void controlStatus(boolean statusChange) {
		decoratedMobile.controlStatus(statusChange);
	}

	@Override
	public void equip(EquipmentSlot slot, StdItem item) {
		decoratedMobile.equip(slot, item);
	}

	@Override
	public void unEquip(Holdable item) {
		decoratedMobile.unEquip(item);
	}
	
	@Override
	public void unEquip(EquipmentSlot slot) {
		decoratedMobile.unEquip(slot);
	}


	@Override
	public Holdable getEquipmentInSlot(EquipmentSlot slot) {
		return decoratedMobile.getEquipmentInSlot(slot);
	}

	@Override
	public void removeFromWorld() {
		decoratedMobile.removeFromWorld();
	}

	@Override
	public void displayPrompt() {
		decoratedMobile.displayPrompt();
	}

	@Override
	public void setSendBack(SendMessage sendBack) {
		decoratedMobile.setSendBack(sendBack);
	}

	@Override
	public void informLastAggressor(Mobile aggressor) {
		decoratedMobile.informLastAggressor(aggressor);		
	}	

	@Override
	public boolean isInducting() {
		return decoratedMobile.isInducting();
	}

	@Override
	public void killInduction() {
		decoratedMobile.killInduction();		
	}
	
	@Override
	public void setInduction(InductionSkill skill) {
		decoratedMobile.setInduction(skill);
	}

	@Override
	public Map<SkillBook, Integer> viewSkillBooks() {
		return decoratedMobile.viewSkillBooks();
	}

	@Override
	public void spawnDeathLoot() {
		decoratedMobile.spawnDeathLoot();
	}

	@Override
	public void moveHoldable(Location finalLocation) {
		decoratedMobile.moveHoldable(finalLocation);
		
	}

	@Override
	public int compareTo(Mobile arg0) {
		return decoratedMobile.compareTo(arg0);
	}

	@Override
	public void setContainer(Location container) {
		decoratedMobile.setContainer(container);
		
		}
	public int getDefense() {
		return decoratedMobile.getDefense();
	}		
	public void addDefense(int i) {
		decoratedMobile.addDefense(i);
	}

	@Override
	public boolean isBlocking() {
		return decoratedMobile.isBlocking();
	}

	@Override
	public void changeBlocking(boolean b) {
		decoratedMobile.changeBlocking(b);		
	}

	@Override
	public double getMaxWeight() {
		return decoratedMobile.getMaxWeight();
	}

	@Override
	public double getCurrentWeight() {
		return decoratedMobile.getCurrentWeight();
	}

	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		return decoratedMobile.getListMatchingString(holdableString);
	}

	@Override
	public void changeWeight(double change) {
		decoratedMobile.changeWeight(change);
	}

	@Override
	public Set<TickingEffect> getAllActiveConditions() {
		return decoratedMobile.getAllActiveConditions();
	}

	@Override
	public void moveFollowers(String fullCommand) {
		decoratedMobile.moveFollowers(fullCommand);
	}

	@Override
	public void stopFollowing() {
		decoratedMobile.stopFollowing();
		
	}

	@Override
	public void setFollowing(Mobile finalTarget) {
		decoratedMobile.setFollowing(finalTarget);
	}

	@Override
	public void addFollower(Mobile currentPlayer) {
		decoratedMobile.addFollower(currentPlayer);
	}

	@Override
	public void removeFollower(Mobile follower) {
		decoratedMobile.removeFollower(follower);
	}

	@Override
	public EnumSet<PassiveCondition> getAllPassiveEffects() {
		return decoratedMobile.getAllPassiveEffects();
	}

	@Override
	public String getNameColored() {
		return decoratedMobile.getNameColored();
	}

	@Override
	public TreeMap<String, Holdable> viewInventoryWithoutEquipment() {
		return decoratedMobile.viewInventoryWithoutEquipment();
	}

	@Override
	public String getClassName() {
		return decoratedMobile.getClassName();
	}
	
	@Override
	public void addCooldown(Cooldown c) {
		decoratedMobile.addCooldown(c);
	}
	
	@Override
	public void removeCooldown(Cooldown c) {
		decoratedMobile.removeCooldown(c);
	}
	
	@Override
	public boolean isOnCooldown(Cooldown c) {
		return decoratedMobile.isOnCooldown(c);
	}
	
	@Override
	public void changeBalanceMult(double change) {
		decoratedMobile.changeBalanceMult(change);
	}
	
	@Override
	public double getBalanceMult() {
		return decoratedMobile.getBalanceMult();
	}
	
	@Override
	public int getCurrentMana() {
		return decoratedMobile.getCurrentMana();
	}
	
	@Override
	public void changeMana(int change) {
		decoratedMobile.changeMana(change);
	}

	@Override
	public void informEntered(Mobile newMob) {
		decoratedMobile.informEntered(newMob);
	}

}
