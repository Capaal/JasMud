package actions;

import interfaces.Action;
import interfaces.Mobile;

import java.util.HashMap;

import TargettingStrategies.WhereStrategyHere;
import TargettingStrategies.WhatStrategySelf;
import TargettingStrategies.WhatStrategyInterface;
import TargettingStrategies.WhereStrategyInterface;
import processes.Skill;
import processes.Type;

//Delete this?
//Work in progress. Need to rework targetting strategies?
public class Give extends Action {

	private final WhatStrategyInterface whatItem;
//	private final WhatTargettingStrategy whatSelf;
	private final WhatStrategyInterface whatMob;
//	private final WhereTargettingStrategy where; 
	
//	public Give() {
	//	this(new TargetTargetWhatStrategy(), new TargetTargetWhatStrategy());
//	}
	
	public Give(WhatStrategyInterface whatItem, WhatStrategyInterface whatMob) {
		this.whatItem = whatItem;
		this.whatMob = whatMob;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		// TODO Auto-generated method stub
		return false;
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
