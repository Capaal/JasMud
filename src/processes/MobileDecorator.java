package processes;

import java.util.EnumSet;
import java.util.Set;

import processes.Equipment.EquipmentEnum;
import processes.Location.GroundType;
import interfaces.Container;
import interfaces.Effect;
import interfaces.Holdable;
import interfaces.Mobile;
import interfaces.TickingEffect;

public class MobileDecorator implements Mobile {

	protected final Mobile decoratedMobile;
	
	public MobileDecorator(Mobile decoratedMobile) {
		this.decoratedMobile = decoratedMobile;
	}

	@Override
	public Set<Holdable> getInventory() {
		return decoratedMobile.getInventory();
	}

	@Override
	public String displayExits() {
		return decoratedMobile.displayExits();
	}

	@Override
	public void look(Mobile currentPlayer) {
		decoratedMobile.look(currentPlayer);

	}

	@Override
	public void glance(Mobile currentPlayer) {
		decoratedMobile.glance(currentPlayer);

	}

	@Override
	public void displayAll(Mobile currentPlayer) {
		decoratedMobile.displayAll(currentPlayer);

	}

	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		decoratedMobile.removeItemFromLocation(oldItem);

	}

	@Override
	public GroundType getGroundType() {
		return decoratedMobile.getGroundType();
	}

	@Override
	public Holdable getHoldableFromString(String holdableString) {
		return decoratedMobile.getHoldableFromString(holdableString);
	}

	@Override
	public EnumSet<EquipmentEnum> getAllowedEquipSlots() {
		return decoratedMobile.getAllowedEquipSlots();
	}

	@Override
	public boolean containsType(Type type) {
		return decoratedMobile.containsType(type);
	}

	@Override
	public double getDamageMult() {
		return decoratedMobile.getDamageMult();
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
	public Container getContainer() {
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
	public void setContainer(Container newLoc) {
		decoratedMobile.setContainer(newLoc);
	}

	@Override
	public Skill getCommand(String command) {
		return decoratedMobile.getCommand(command);
	}

	@Override
	public void acceptItem(Holdable item) {
		decoratedMobile.acceptItem(item);

	}

	@Override
	public void addEffect(Effect newEffect, int duration) {
		decoratedMobile.addEffect(newEffect, duration);
	}

	@Override
	public void addTickingEffect(TickingEffect newEffect, int duration,
			int times) {
		decoratedMobile.addTickingEffect(newEffect, duration, times);
	}

	@Override
	public int checkEffectsAgainstIncomingDamage(Type incomingType, int damage) {
		return decoratedMobile.checkEffectsAgainstIncomingDamage(incomingType, damage);
	}

	@Override
	public double getWeaponMultiplier() {
		return decoratedMobile.getWeaponMultiplier();
	}

	@Override
	public boolean hasEffect(Effect effect) {
		return decoratedMobile.hasEffect(effect);
	}

	@Override
	public void removeEffect(Effect effect) {
		decoratedMobile.removeEffect(effect);
	}

	@Override
	public void removeItem(Holdable item) {
		decoratedMobile.removeItem(item);
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
	public boolean save() {
		return decoratedMobile.save();
	}

	@Override
	public void checkHp() {
		decoratedMobile.checkHp();
	}

	@Override
	public void setStartup(boolean b) {
		decoratedMobile.setStartup(b);
	}

	@Override
	public void equip(EquipmentEnum slot, Holdable item) {
		decoratedMobile.equip(slot, item);
	}

	@Override
	public void unequip(Holdable item) {
		decoratedMobile.unequip(item);
	}

	@Override
	public void unequipFromSlot(EquipmentEnum slot) {
		decoratedMobile.unequipFromSlot(slot);
	}

	@Override
	public EquipmentEnum findEquipment(String itemName) {
		return decoratedMobile.findEquipment(itemName);
	}

	@Override
	public Holdable getEquipmentInSlot(EquipmentEnum slot) {
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
	public SendMessage getSendBack() {
		return decoratedMobile.getSendBack();
	}

	@Override
	public void informLastAggressor(Mobile aggressor) {
		decoratedMobile.informLastAggressor(aggressor);
		
	}

}
