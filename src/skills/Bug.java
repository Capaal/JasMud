package skills;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import processes.Command;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.UsefulCommands;

public class Bug implements Command {
	
	private static ArrayList<String> bugList = new ArrayList<String>();
	private static int size;
	
	private static Calendar now = Calendar.getInstance();
	
	public void Bug() {
		size = 0;
	}
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		
		
		int monthNow = now.get(Calendar.MONTH + 1);
		int yearNow = now.get(Calendar.YEAR);
		int dayNow = now.get(Calendar.DAY_OF_MONTH);
		int hourNow = now.get(Calendar.HOUR_OF_DAY);
		int minNow = now.get(Calendar.MINUTE);
	//	if (minNow < 10) {
	//		String dumbSoln = "0" + minNow;
	//		minNow = Integer.parseInt(dumbSoln);
	//	}
		String date = monthNow + "-" + dayNow + "-" + yearNow + " " + hourNow + ":" + minNow;
	
		String reportedText = UsefulCommands.returnTarget(fullCommand);
	
		size ++;
		//Edit to remove "bug" from write to file
		String thisBug = date + " " + currentPlayer.getName() + " #" + size + ": " + reportedText;
		bugList.add(thisBug);
		
		FileWriter fw = null;
			try {
				fw = new FileWriter("Bugs.txt", true);
			} catch (IOException e) {
		}

		try {
			fw.write(thisBug + System.getProperty("line.separator"));
			fw.close();
		} catch (IOException ee) {
		}
		currentPlayer.addBug(thisBug);
		playerPrompt.getSendBack().printMessage("Your bug has been reported, thank you. Type BUGS to check all bugs you have reported.");
	}

	public static void displayBugs(PlayerPrompt playerPrompt, String fullCommand) {
		for (int i = 0; i < size; i++) {
			playerPrompt.getSendBack().printMessage((bugList.get(i)));
		}
		
		
	}

}
