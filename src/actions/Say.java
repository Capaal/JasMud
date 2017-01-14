package actions;

import interfaces.*;

import java.sql.SQLException;
import java.util.*;

import TargettingStrategies.*;
import processes.*;
import processes.Skill.Syntax;

public class Say extends Action {
	
	public Say() {
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		WhatStrategyInterface what = new TargetAllWhatStrategy();
		WhereStrategyInterface where = new WhereStrategyHere();	
		StringBuffer sb = new StringBuffer();
		sb.append(currentPlayer.getName() + " says, \"");
		sb.append(s.getStringInfo(Syntax.LIST, fullCommand));		
	/*	
		String fullCommand = s.getFullCommand();
		StringTokenizer st = UsefulCommands.getST(fullCommand);
		StringBuffer sb = new StringBuffer();
		sb.append(s.getCurrentPlayer().getName() + " says, \"");
		if (st.hasMoreTokens()) {
			String str = st.nextToken();
			String firstWord = UsefulCommands.firstToCap(str);
			sb.append(firstWord);	
			while (st.hasMoreTokens()) {
				sb.append(" " + st.nextToken());
			}
		}*/
		sb.append("\".");
//		ArrayList<Mobile> targs = who.findTarget(s, where.findLoc(s));
		for (Holdable m : what.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer))) {
			if (m != null && m instanceof Mobile && ((Mobile) m).isControlled()) {
				((Mobile)m).tell(sb.toString());
			}
		}	
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		return new Say();
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='SAY' AND BLOCKPOS=" + position +";";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS) VALUES ('SAY', " 
					+ position + ");";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Very specific case of message that is specifically formatted to represent someone talking out loud.");
		player.tell("There are no variables, as everything depends on input upon use of the skill.");
	}
}
