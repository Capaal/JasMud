package processes;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import processes.Equipment.EquipmentSlot;
import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import interfaces.TickingEffect;
import items.Plant.PlantType;
import items.StdItem;

public class MobileDecorator implements Mobile {

	protected final Mobile decoratedMobile;
	protected static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	public MobileDecorator(Mobile decoratedMobile) {
		this.decoratedMobile = decoratedMobile;
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
	public void takeDamage(Type types, int d) {
		decoratedMobile.takeDamage(types, d);
	}

	@Override
	public void tell(String msg) {
		decoratedMobile.tell(msg);
	}

	@Override
	public void tellLine(String msg) {
		decoratedMobile.tellLine(msg);
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
	public boolean addActiveCondition(TickingEffect newEffect, int times) {
		return decoratedMobile.addActiveCondition(newEffect, times);
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
	public void save() {
		decoratedMobile.save();
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
	
	public void makeDecision() {}
	
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
		};	
		
		private DecoratorType() {}
		
		public Mobile getDecorator(Mobile m) {
			return m;
		}
	}


	public class AITask implements Runnable {
		
		private MobileDecorator mobAI;
		
		public AITask(MobileDecorator decoratedMobile) {
			this.mobAI = decoratedMobile;
		}
		
		public void run() {
			mobAI.makeDecision();
		}
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
	public void cooldownOn(PlantType p) {
		decoratedMobile.cooldownOn(p);
	}

	@Override
	public void cooldownOff(PlantType p) {
		decoratedMobile.cooldownOff(p);
	}

	@Override
	public boolean checkCooldown(PlantType p) {
		return decoratedMobile.checkCooldown(p);
	}

	@Override
	public String getNameColored() {
		return decoratedMobile.getNameColored();
	}

	@Override
	public TreeMap<String, Holdable> viewInventoryWithoutEquipment() {
		return decoratedMobile.viewInventoryWithoutEquipment();
	}

}
