import java.util.*;



public class Swim extends Move {
	
	public Swim() {
		super();
		leaveMSG = " swims to";
		enterMSG = " swims in from";
	}
	
	
	//Pulls out direction of swim
	public static int detDir(Location thisLocation, String fullCommand, int commandNum) {
		for (int i = 0; i < 12; i++) { 
			if (thisLocation.dirNames[i].toLowerCase().equals(UsefulCommands.returnTarget(fullCommand)) || 
					thisLocation.dirAbbrev[i].toLowerCase().equals(UsefulCommands.returnTarget(fullCommand))) {
				commandNum = i;
//		  		setFutureLoc(WorldServer.locationCollection.get(thisLocation.roomExits[commandNum]));
			}
		}
		return commandNum;
	}
	
	
	//Over-rides Move same method
	public static boolean detGroundType(SendMessage sendBack, Location thisLocation, String fullCommand, int commandNum) {
		Location futureLoc = detFutureLoc(thisLocation, commandNum);
		if (thisLocation.roomExits[commandNum] != 0) {
			if (thisLocation.groundType.equals("land") && (futureLoc.groundType.equals("land"))) {
				sendBack.printMessage("You flop onto the ground and flail wildly, but don't seem to make much progress.");
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	//Finally moves mob
//	public void moveMob(SendMessage sendBack, String fullCommand, Player currentPlayer, 
//		Location thisLocation, int commandNum) {
//		super.moveMob(sendBack, currentPlayer, thisLocation, commandNum, this.futureLoc);
//	}

}