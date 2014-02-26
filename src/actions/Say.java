package actions;

import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

import java.sql.SQLException;
import java.util.*;

import processes.*;
import processes.Skill.Syntax;

public class Say extends Action {
	
	public Say() {
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		Who who = Who.ALL;
		Where where = Where.HERE;	
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
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer))) {
			if (m != null && m.isControlled()) {
				m.tell(sb.toString());
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
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS) VALUES ('SAY', " 
					+ position + ");";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Say failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Very specific case of message that is specifically formatted to represent someone talking out loud.");
		player.tell("There are no variables, as everything depends on input upon use of the skill.");
	}
}
