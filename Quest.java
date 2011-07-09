import java.io.*;
import java.util.*;

public interface Quest {

	public void testQuest(String fullCommand, Player currentPlayer);
	public void testFinishQuest(String fullCommand, SendMessage sendBack);
	public void startQuest(SendMessage sendBack);
	public void finishQuest(SendMessage sendBack, String fullCommand);

}