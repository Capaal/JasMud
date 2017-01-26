package actions;

import java.util.HashMap;
import java.util.Set;

import interfaces.Action;
import interfaces.Mobile;
import items.StdItem;
import processes.ItemBuilder;
import processes.Skills;
import processes.WorldServer;

public class makeDagger extends Action {

	public makeDagger() {
	}
	
	@Override
	public boolean activate(Skills s, String fullCommand, Mobile currentPlayer) {
		Set<StdItem> listItems = WorldServer.gameState.viewAllItems();
		int i = listItems.size() + 1;
		makeADagger(i);
		return true;
	}

	public static void makeADagger(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("dagger");
		newItem.setDescription("It's a dagger!");
		newItem.complete();
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
