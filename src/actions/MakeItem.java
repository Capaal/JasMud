package actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import TargettingStrategies.*;
import interfaces.Action;
import interfaces.Mobile;
import items.StdItem;
import processes.CreateWorld;
import processes.ItemBuilder;
import processes.Skill;
import processes.Skill.Syntax;
import processes.UsefulCommands;
import processes.WorldServer;

public class MakeItem extends Action {
	
	private final WhatStrategyInterface what;
	private final WhereStrategyInterface where;

	public MakeItem() {
		this(new WhatStrategyItem(), new WhereStrategyHere());
	}
	
	public MakeItem(WhatStrategyInterface what, WhereStrategyInterface where) {
		this.what = what;
		this.where = where;
	}
	
	@Override
	//check if template item exists, then copy and create new
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		String toCopy = s.getStringInfo(Syntax.ITEM, fullCommand);
		Map<String, ItemBuilder> itemTemplates = CreateWorld.viewItemTemplates();
		ItemBuilder copying = itemTemplates.get(toCopy);
		if (copying == null) {
			return false;
		} else {
			Set<StdItem> allItems = WorldServer.gameState.viewAllItems();
			ItemBuilder newItem = new ItemBuilder();
			newItem.setId(allItems.size()+1);
			newItem.setName(toCopy);
			newItem.setDescription(copying.getDescription());
			newItem.setItemContainer(currentPlayer.getContainer());
			newItem.complete();
			return true;
		}
	}
	
	@Override
	public void insertOneself(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Object> selectOneself(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action newBlock(Mobile player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void explainOneself(Mobile player) {
		// TODO Auto-generated method stub

	}

}
