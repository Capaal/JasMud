package actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import processes.Equipment.EquipmentEnum;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import interfaces.Action;
import interfaces.Container;
import interfaces.Equipable;
import interfaces.Holdable;
import interfaces.Mobile;
import interfaces.Action.Where;
import interfaces.Action.Who;
import items.StdItem;

public class EquipChange extends Action {

	private final Who who;
	private final Where where;
	private final boolean equip;
	
	public EquipChange() {
		this(Who.SELF, Where.HERE, true);
	}

	public EquipChange(Who who, Where where, boolean equip) {
		this.who = who;
		this.where = where;
		this.equip = equip;
	}
	/*
	 * So far, the options I see for handling wielding:
	 * I can do it the obvious route, have a variable for each slot I expect to need. Head, left arm, right arm, chest etc.
	 * This way it'll be obvious what is equipped where, and things affecting a certain body part will have an obvious target.
	 * Downsides are that it is fairly limiting, you can only have those slots I make, so you can't do 4 armed creatures or whatever.
	 * This method is also simple to look at and understand, however items will be moving from inventory to these slots and back.
	 * So lots of moving things, and you'll have to interate through these options to find items as well as everywhere else.
	 * 
	 * Second option is a second inventory called something like equipment where items will be moved to when they are equipped.
	 * Things in this inventory will know where they are equipped, and the list will be small to look through when interacting with something
	 * equipped in a particular spot.
	 * A slight alternative is a map, so you'll know even quicker what is equipped where, but harder to integrate with inventory stuff because
	 * of the different type of list.
	 * Upsides to this method is that you gain the ability for infinite slots, you could have 10 helms equipped, though you'd have to find a good
	 * way to limit this, so somewhere in the mob it'll need to be stated what slots are allowed, this supports the map idea where the slots
	 * must already have been defined based on the current creature, and then they can be filled.
	 * Downsides are that it is more complicated and integrates more annoyingly with current inventory set-up because of the different types,
	 * also the simpler version without the map has some flaws about limiting.
	 * Also both lists will need to be iterated through often.
	 * In addition, what if a skill can target a particular arm, how do they break arm 4? Is it worth even allowing this many arms?
	 * 
	 * Third option, there is just one inventory, however items are tagged in that list as equipped in a certain slot, maybe that slot
	 * is an enum, so it is designated as hand or head. One list is very simple, of notice as well, if the list was organizable
	 * not only would the pack be easier for players to interact with, but search speed would increase exponentially (though putting
	 * and removing things would slow down). However, if you don't know what you are looking for, still integrating through it all.
	 * Upsides are that one list is simple, and the item knows where it is equipped, and the number of slots in infinite.
	 * However limiting slots could be incredible difficult also the list would be iterated through on a constant basis.
	 * Just to find if something is equipped, or for constant wielding/unwielding, or just checks to be sure a weapon is equipped.
	 * This option in first glance is great, but you have to think about what actions happen a lot, if you make a list of what actions
	 * happen most often and it is : weapon check, equip, unequip, drop
	 * then it would be horribly slow, but if the list is : get drop use item equip unequip weapon check
	 * Then it would be great because you don't tend to care much about what is equipped. Still not really better than two inventories
	 * though, just simpler to deal with.
	 * 
	 * Forth option, completely re-construct how inventories work. Make them their own class. Call them something different.
	 * This new class's job is handling of all gear and supplies. It handles equipment, packs, pouches, bottles and inventories.
	 * It might also handle loot dropping, coin purse, herbs. Do we need herb pouches? do we need multiple pouches? I'd like to keep them...
	 * This class would use items from the above, except that it should allow for more complicated inventories and equipment.
	 * This SOUNDS like the best option, however it would take time to create and is complicated. Also it doesn't solve the problem.
	 * It just encourages something complicated that would be a very complete solution, a solution that would have to come from the above.
	 * It could have useful things under the hood, or lots of hiarchy/inheritance for different types of inventories/containers defining
	 * special rules we don't know we want. Intergration with the bank, as well as options for the players like sorting or whatever.
	 * 
	 * I think the SOLUTION is to decide if I care about 4-hands, if I do, then I MUST do a map, otherwise just simple variables
	 * would work fine for each slot for the item to be set into. If I want 4-hands, it also must define how skills target slots.
	 * I must also think about all differences in pouches and packs. SIGH ok, I dunno, dinner.
	 * 
	 * building block a monster
	 * 
	 */
	
	
	@Override
	public boolean activate(Skill s) {
		ArrayList<Container> loc = where.findLoc(s);
		ArrayList<Mobile> target = who.findTarget(s, loc);
		String slotString = s.getStringInfo(Syntax.SLOT);
		EquipmentEnum slotEnum;
		if (slotString.toLowerCase().equals("left")) {
			slotEnum = EquipmentEnum.LEFTHAND;
		} else if (slotString.toLowerCase().equals("right")) {
			slotEnum = EquipmentEnum.RIGHTHAND;
		} else {
			slotEnum = null;
		}
		if (loc != null && target != null) {
			for (Mobile m : target) {
				if (equip) {
					Equipable toMove = (Equipable) m.getHoldableFromString(s.getStringInfo(Syntax.ITEM));
					if (!(toMove instanceof Equipable)) {
						return false;
					}
					EnumSet<EquipmentEnum> slots = ((Equipable)toMove).getAllowedEquipSlots();
					if (slots == null) {
						return false;
					}
					if (slotEnum == null) {
						for (EquipmentEnum st : slots) {
							if (m.getEquipmentInSlot(st) == null) {
								slotEnum = st;
								break;
							}
						}
					}
					m.equip(slotEnum, toMove);				
				} else {
					if (slotEnum == null) {
						slotEnum = m.findEquipment(s.getStringInfo(Syntax.ITEM));
					/*	Collection<StdItem> e = m.getEquipment().values();
						String slot = s.getStringInfo(Syntax.SLOT);
						boolean success = false;
						for (StdItem i : e) {
							String posName = i.getName().toLowerCase();					
							if (posName.equals(item) || (posName + i.getId()).equals(item)) {
								m.getEquipment().unequipItem(i);
								success = true;
							}
						}
						if (!success) {
							return false;
						}*/
					}
					m.unequipFromSlot(slotEnum);					
				}				
			}		
		}
		return true;
	}
	
	/*private EquipmentEnum determineSlot() {
		EquipmentEnum slot;
		if (slot.equals(null)) {
			for (EquipmentEnum st : slots) {
				if (m.getEquipmentInSlot(st) == null) {
					slot = st;
					break;
				}
			}
		}
		return slot;
	}*/
	@Override
	public Action newBlock(Mobile player) {
		Who newWho = who;
		Where newWhere = where;
		boolean newEquip = equip;
		String answerEquip = Godcreate.askQuestion("Is this equiping the item? true/false.", player);
		if ("true".equals(answerEquip) || "false".equals(answerEquip)) {
			newEquip = Boolean.parseBoolean(answerEquip);
		} else {
			player.tell("Error, your answer was not detected as a boolean.");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to equip the item? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new EquipChange(newWho, newWhere, newEquip);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='EQUIPCHANGE' AND BLOCKPOS=" + position + " AND BOOLEANONE='" 
				+ equip + "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	@Override
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) VALUES ('EQUIPCHANGE', " 
				+ position + ", '" + equip + "', '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Equipchange failed to insert itself: " + sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Used to equip and unequip a Mobile.");
		player.tell("True means equip: " + equip + " Who: " + who.toString() + "Where: " + where.toString());
	}
}
