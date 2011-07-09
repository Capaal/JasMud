import java.io.*;
import java.util.*;



public class QuestNewbieFirst implements Quest, Serializable {

	private boolean questDone;

	//interaction with two objects - stone and slab
	//put stone on slab, questDone = true

	public QuestNewbieFirst() {
		this.questDone = false;
	}

	public void testQuest(String fullCommand, SendMessage currentPlayer) {
		if (!questDone) { // && player is at location 5
			//player tries to go north
			//display message: a gate between two suspicious looking stone slabs bars your path. (Go SW and READ BOOK to help solve this quest.)
		} else if (questDone) { // && player is at location 5
			//display message: congratulations on mastering some basic MUD commands! You may now move forward.
		}
	}
	public void testFinishQuest(String fullCommand, SendMessage sendBack) {}
	public void startQuest(SendMessage sendBack) {}
	public void finishQuest(SendMessage sendBack, String fullCommand) {}
	

}