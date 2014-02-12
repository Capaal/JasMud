package actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import processes.UsefulCommands;
import interfaces.Action;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import interfaces.Action.Where;
import interfaces.Action.Who;
import items.StdItem;

public class EquipChange extends Action {

	private final Who who;
	private final Where where;
	private final boolean equip;

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
		if (loc != null && target != null) {
			for (Mobile m : target) {
				if (equip) {
					Holdable toMove = m.getHoldableFromString(s.getStringInfo(Syntax.ITEM));
					if (!(toMove instanceof StdItem)) {
						return false;
					}
					ArrayList<String> slots = ((StdItem)toMove).getAllowedEquipSlots();
					if (slots.size() == 0) {
						return false;
					}
					// ???
					String slot = slots.get(0);
					if (slots.size() > 1) {
						for (String st : slots) {
							if (m.getEquipment().getValue(st.toLowerCase()) == null) {
								slot = st;
								break;
							}
						}
					}
					m.equip(slot.toLowerCase(), (StdItem)toMove);
				
				} else {
					Collection<StdItem> e = m.getEquipment().values();
					String item = s.getStringInfo(Syntax.ITEM);
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
					}
					
				}
				
			}		
		}
		return true;
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
		SQLInterface.saveAction(sql);
	}

}
