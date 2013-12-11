package skills;

import interfaces.*;

import java.util.Iterator;
import java.util.StringTokenizer;

import processes.*;

public class Say implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		Mobile currentPlayer = playerPrompt.getCurrentPlayer();
		StringTokenizer st = UsefulCommands.getST(fullCommand);
		StringBuffer sb = new StringBuffer();
		sb.append(currentPlayer.getName() + " says, \"");
		if (st.hasMoreTokens()) {
			String str = st.nextToken();
			String firstWord = UsefulCommands.firstToCap(str);
			sb.append(firstWord);	
			while (st.hasMoreTokens()) {
				sb.append(" " + st.nextToken());
			}
		}
		sb.append("\".");
		Container sayLocation = currentPlayer.getContainer();
		for (Holdable m : sayLocation.getInventory()) {
			if (m instanceof Mobile) {
				((Mobile) m).tell(sb.toString());
			}
		}
	}
}
