package actions;

import interfaces.*;

import java.util.*;

import processes.*;
import processes.Skill.Syntax;

public class Say extends Action {

	@Override
	public boolean activate(Skill s) {
		Who who = Who.ALL;
		Where where = Where.HERE;	
		StringBuffer sb = new StringBuffer();
		sb.append(s.getCurrentPlayer().getName() + " says, \"");
		sb.append(s.getStringInfo(Syntax.LIST));
		
		
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
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			if (m != null && m.isControlled()) {
				m.tell(sb.toString());
			}
		}	
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='SAY' AND BLOCKPOS=" + position +";";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS) VALUES ('SAY', " 
				+ position + ");";
		SQLInterface.saveAction(sql);
	}
}
