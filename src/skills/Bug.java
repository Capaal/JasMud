package skills;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import interfaces.Mobile;
import processes.Skills;

public class Bug extends Skills {
	
	private String reportedText;

	public Bug(Mobile currentPlayer, String fullCommand) {
		super("bug", "Recording and checking recorded bugs.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.MESSAGE);
	}
	
	@Override
	protected void performSkill() {
		if (reportedText.equalsIgnoreCase("list")) {
			String fileName = "Bugs.txt";
			String line = null;
			try {
				FileReader fileReader = new FileReader(fileName);
	            BufferedReader bufferedReader =  new BufferedReader(fileReader);
	            while((line = bufferedReader.readLine()) != null) {
	                messageSelf(line);
	            }   
	            bufferedReader.close();
			}
            
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '"  + fileName + "'");                  
	            // Or ex.printStackTrace();
	        }				
		} else {				
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			
			String thisBug = "(Date) " + dt.format(date) + " (Player) " + currentPlayer.getNameColored() + ": " + reportedText;
			
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
			messageSelf("Your bug has been reported.");
		}
	}
	
	protected boolean preSkillChecks() {
		reportedText = Syntax.MESSAGE.getStringInfo(fullCommand, this);
		if (reportedText.equals("")) {
			messageSelf("You can't submit an empty bug message.");
			return false;
		}
		return true;
	}

	@Override
	public String displaySyntax() {
		return "BUG [comments] or BUG LIST";
	}
	
}
