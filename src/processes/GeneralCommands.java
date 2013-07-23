package processes;
import java.io.*;
import java.util.*;

public class GeneralCommands {

	protected Player currentPlayer;
	protected SendMessage sendBack;

	
	// Runs the Location method for looking.
	public static void doLook(Player currentPlayer, SendMessage sendBack) {
	   System.out.println("doLook accessed.");
		Location thisLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
		thisLocation.look(sendBack, currentPlayer.name);
	}
	// Punches.
	public static void doPunch(String fullCommand, Player currentPlayer, SendMessage sendBack) {
	   System.out.println("doPunch accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		if (target.equals("")) {
			sendBack.printMessage("You'll need to select a target first in order to punch them.");
		} else {
			attackPlayer(target, "You ball your hand into a fist and punch " + target + " solidly.", 
				currentPlayer.name + " punches you painfully in the gut.", 10, "physical", currentPlayer, sendBack, 2000);
		}
	}	
	// Method for display methods to others in the same location.
	// Probably break this up and put parts in UsefulCommands.
	public static void doSay(String fullCommand, Player currentPlayer) {
		System.out.println("doSay accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String thisSay = st.nextToken();
		StringBuffer sb = new StringBuffer();
		sb.append(currentPlayer.name + " says, \"");
		if (st.hasMoreTokens()) {
			String str = st.nextToken();
			String firstWord = UsefulCommands.firstToCap(str);
			sb.append(firstWord + " ");	
			if (st.hasMoreTokens()) {		
				sb.append(st.nextToken());
				while (st.hasMoreTokens()) {
					sb.append(" " + st.nextToken());
				}
			}
		}
		sb.append("\".");
		Iterator iter = WorldServer.activeClients.iterator();
		while (iter.hasNext()) {
			PlayerPrompt t = (PlayerPrompt) iter.next();
			if (t.currentPlayer.mobLocation == currentPlayer.mobLocation && 
					!t.currentPlayer.name.equals(currentPlayer.name)) {
				t.sendBack.printMessage(sb.toString());
			}
		}
	}
	// Generic method for upping level in skills. I.e. "generals" and "mage".
	public static void doLearn(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doLearn accessed");
		StringTokenizer sb = new StringTokenizer(fullCommand);
		String learn = sb.nextToken();
		if (sb.hasMoreTokens()) {
			String skill = sb.nextToken();
			currentPlayer.learnSkill(skill.toLowerCase(), sendBack);
		} else {
			sendBack.printMessage("You find yourself struggling to learn nothingness, perhaps starting with a real skill first would be helpful.");
		}
	}
	// Method for sending messages to others not in your location.
	public static void doTell(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doTell accessed.");
		StringTokenizer st = UsefulCommands.getST(fullCommand);
		String target = "";
		if (st.hasMoreTokens()) {
			target = st.nextToken();
		} else {
			sendBack.printMessage("Whom would like to send a message to?");
		}
		if (!target.equals("")) {
			StringBuffer sb = new StringBuffer();
			Iterator iter = WorldServer.activeClients.iterator();
			while (iter.hasNext()) {
				PlayerPrompt t = (PlayerPrompt) iter.next();
				int msgToSend = 0;
				if (t.currentPlayer.name.toLowerCase().equals(target.toLowerCase()) && 
						!t.currentPlayer.name.toLowerCase().equals(currentPlayer.name.toLowerCase())) {
					if (st.hasMoreTokens()) {
						msgToSend = 1;
						sb.append(UsefulCommands.firstToCap(st.nextToken()));
						while (st.hasMoreTokens()) {					
							sb.append(" " + st.nextToken());
						}
					}
				} else if (t.currentPlayer.name.equals(currentPlayer.name) && 
						t.currentPlayer.name.equals(target.toLowerCase())) {
					sendBack.printMessage("You're talking to yourself!");
					msgToSend = 2;
				}
				if (msgToSend == 1) {
					t.sendBack.printMessage(currentPlayer.name + " tells you, \"" + sb.toString() + "\".");
					break;
				} else if (msgToSend == 0 && WorldServer.mobList.containsKey(target.toLowerCase()) && 
						WorldServer.mobList.get(target.toLowerCase()) instanceof Player) {
					sendBack.printMessage("The player is not online.");
				} else if (msgToSend == 0 && !WorldServer.mobList.containsKey(target.toLowerCase())) {
					sendBack.printMessage("The player does not exist.");
				}
			}	
		}
	}
	// Displays all current skillsets available and levels.
	public static void doSkills(Player currentPlayer, SendMessage sendBack) {
		System.out.println("doSkills accessed.");
		String[] skillNames = new String[] {"Generals", "Mage", "Survival"};
		int[] levels = new int[] {currentPlayer.generals, currentPlayer.mage, currentPlayer.survival};
		for (int i = 0; i <=2; i++) {
			sendBack.printMessage(skillNames[i] + " " + levels[i]);
		}
	}
	// Glance is a shorter version of look, doesn't display description.
	public static void doGlance(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doGlance accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String command = st.nextToken();
		int direction = currentPlayer.mobLocation;
		Location glanceLocation = WorldServer.locationCollection.get(direction);
		Location thisLocation = WorldServer.locationCollection.get(direction);
		if (st.hasMoreTokens()) {
			String newDirection = st.nextToken();
			for (int i = 0; i < 12; i++) {
				if ((newDirection.toLowerCase().equals(thisLocation.dirAbbrev[i]) || 
						newDirection.toLowerCase().equals(thisLocation.dirNames[i])) && thisLocation.roomExits[i] != 0) {
					glanceLocation = WorldServer.locationCollection.get(thisLocation.roomExits[i]);
				}
			}
		}
		if (glanceLocation != null) {
			glanceLocation.glance(sendBack, currentPlayer.name);
		}
	}
	// "ab generals" displays all general skills available and a short description.
	/*public static void doAB(String fullCommand, SendMessage sendBack, Player currentPlayer) {
		System.out.println("doAB accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String fillAB = st.nextToken();
		if (st.hasMoreTokens()) {
			String guild = st.nextToken();
			if (st.hasMoreTokens()) {
				String skill = st.nextToken();
				if (guild.equals("generals")) {
					//add code to list ab generals skill only if desc exists
					if (WorldServer.allAbilities.containsKey(skill) && WorldServer.allAbilities.get(skill).skillLevel <= currentPlayer.generals) {
						sendBack.printMessage("Generals ability: " + UsefulCommands.firstToCap(skill) + ".");
						sendBack.printMessage(UsefulCommands.firstToCap(skill) + "     " + 
								WorldServer.allAbilities.get(skill).longDesc);
					} else if (!WorldServer.allAbilities.containsKey(skill)) {
						sendBack.printMessage("Could not find \"" + skill + "\" in General abilities.");
					}
				} else if (guild.equals("mage")) {
					if (WorldServer.mageAbilities.get(skill).skillLevel <= currentPlayer.mage) {
						sendBack.printMessage("Mage ability: " + UsefulCommands.firstToCap(skill + "."));
						sendBack.printMessage(UsefulCommands.firstToCap(skill) + "     " + 
								WorldServer.mageAbilities.get(skill).longDesc);
					}
				} else if (guild.equals("survival")) {
					if (WorldServer.survAbilities.get(skill).skillLevel <= currentPlayer.survival) {
						sendBack.printMessage("Survival ability: " + UsefulCommands.firstToCap(skill + "."));
						sendBack.printMessage(UsefulCommands.firstToCap(skill) + "     " + 
								WorldServer.survAbilities.get(skill).longDesc);
					}
				}
			} else {
				if (guild.equals("generals")) {
					sendBack.printMessage("All players have access to these commands:");
					Set<String> genTemp = WorldServer.allAbilities.keySet();					
					Iterator iter = genTemp.iterator();
					while (iter.hasNext()) {
						String skill = iter.next() + "";
						if (!WorldServer.allAbilities.get(skill).longDesc.equals("")) {
							sendBack.printMessage(skill + ": " + WorldServer.allAbilities.get(skill).shortDesc);
						}
					}					
					sendBack.printMessage("Type AB GENERALS <ability> for more details. Example: AB GENERALS LOOK.");
				} else if (guild.equals("mage")) {
					sendBack.printMessage("Mage abilities:  (Rank)");
					Set<String> temp = WorldServer.mageAbilities.keySet();
					for (int i = 0; i < WorldServer.mageAbilities.size(); i++) {
						printMageAB(currentPlayer, sendBack, temp, i);
					}		
				} else if (guild.equals("survival")) {
					sendBack.printMessage("Survival abilities:  (Rank)");
					Set<String> survTemp = WorldServer.survAbilities.keySet();
					Iterator iter = survTemp.iterator();
					while (iter.hasNext()) {
						String skill = iter.next() + "";
						if (WorldServer.survAbilities.get(skill).skillLevel <= currentPlayer.survival) {
							sendBack.printMessage(WorldServer.survAbilities.get(skill).shortDesc);
						}
					}
				}
			}
		}
	}
	
	//prints mage skills player knows
	public static void printMageAB(Player currentPlayer, SendMessage sendBack, Set<String> temp, int i) {
		System.out.println("printMageAB accessed.");
		Iterator iter = temp.iterator();
		while (iter.hasNext()) {
			String skill = iter.next() +"";
			if (WorldServer.mageAbilities.get(skill).skillLevel <= currentPlayer.mage && 
					WorldServer.mageAbilities.get(skill).commandNum == (300+i)) {
				sendBack.printMessage(WorldServer.mageAbilities.get(skill).shortDesc);
			}
		}
	}
	
	// Used to create items, such as "create weapon dagger" makes a new dagger.
	public static void doCreate(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doCreate accessed.");
		StringTokenizer st = UsefulCommands.getST(fullCommand);
		String itemType = null;
		boolean failure = false;
		boolean success = false;
		if (st.hasMoreTokens()) {
			itemType = st.nextToken().toLowerCase();
			if (WorldServer.possibleItems.containsKey(itemType)) {
				int caseNum = WorldServer.possibleItems.get(itemType);
				int posID = UsefulCommands.getPosID(1);
//				System.out.println(posID);
				Item item = null;
				switch (caseNum) {				
					case 0:
						item = new Dagger(posID);
					break;
					
					case 1:
						item = new Longsword(posID);
					break;
					
					case 2:
						item = new Dirk(posID);
					break;
				}
				
				WorldServer.allItems.add(item);
				currentPlayer.inventory.add(item);
				sendBack.printMessage("You have just created " + itemType + item.returnID());
						
			}
		} else {
			sendBack.printMessage("What would you like to create?");
			success = true;
		}
	}
*/
	// Wields an item, but that item must be wieldable.
	// This is a very inefficient method.	
	public static void doWield(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doWield accessed.");
		String itemFirst = UsefulCommands.returnTarget(fullCommand);
		String item = "";
		if (itemFirst.equals("")) {
			sendBack.printMessage("You should choose something to wield.");
		} else {
			if (WorldServer.possibleItems.containsKey(UsefulCommands.getOnlyStrings(itemFirst))) {
				Wieldable object = null;
				for (int i = 0; i < currentPlayer.inventory.size(); i++) {
					object = (Wieldable) currentPlayer.inventory.get(i);
					if (UsefulCommands.getOnlyStrings(itemFirst).equals(itemFirst)) {
						item = (itemFirst + object.returnID());
					} else {
						item = itemFirst;
					}
					if ((object.returnName() + object.returnID()).equals(item) && object instanceof Wieldable) {
						doUnwield(currentPlayer, sendBack, "unwield");
						object.wieldHand(currentPlayer);
						int weaponIndex = currentPlayer.inventory.indexOf(object);
						currentPlayer.inventory.remove(weaponIndex);
						sendBack.printMessage("You are now wielding " + object.returnName() + object.returnID() + ".");
						break;
					}
				}
			}
		}
	}
/*

	// Stabs an opponent, stronger than punch but requires a weapon.
	public static void doStab(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doStab accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		if (currentPlayer.wieldedRight instanceof Dagger) {
			Dagger weapon = (Dagger) currentPlayer.wieldedRight;
			if (target.equals("")) {
				sendBack.printMessage("You'll need to target something.");
			} else if (!target.equals("") && weapon != null) {
				weapon.doStab(target, currentPlayer, sendBack);
			} else {
				sendBack.printMessage("You'll need to wield something to do that.");
			}
		} else {
			sendBack.printMessage("You are not wielding anything.");
		}
	}
	*/
	public static void doUnwield(Player currentPlayer, SendMessage sendBack, String fullCommand) {
		System.out.println("doUnwield accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		if (currentPlayer.wieldedLeft != null && currentPlayer.wieldedLeft.returnName().equals(target.toLowerCase())) {
			Item wieldedLeftItem = (Item) currentPlayer.wieldedLeft;
			currentPlayer.inventory.add(wieldedLeftItem);
			currentPlayer.wieldedLeft = null;
			sendBack.printMessage("You have unwielded " + (wieldedLeftItem.returnName() + wieldedLeftItem.returnID()) + ".");
		} else if (currentPlayer.wieldedRight != null) {
			Item wieldedRightItem = (Item) currentPlayer.wieldedRight;
			currentPlayer.inventory.add(wieldedRightItem);
			currentPlayer.wieldedRight = null;
			sendBack.printMessage("You have unwielded " + (wieldedRightItem.returnName() + wieldedRightItem.returnID()) + ".");
		}
	}
	// Deletes an item.
	public static void doDelete(String fullCommand, SendMessage sendBack) {
		System.out.println("doDelete accessed");
		String toDelete = UsefulCommands.returnTarget(fullCommand);
		boolean deleted = false;
		if (!toDelete.equals("")) {
			String itemNumString = UsefulCommands.getOnlyNumerics(toDelete);
			if (itemNumString.length() != 0) {
				int itemNum = Integer.parseInt(itemNumString);
				Collection c = WorldServer.locationCollection.values();
				Iterator itera = c.iterator();
				while (itera.hasNext() && deleted == false) {
					Location deleteLocation = (Location) itera.next();
					for (int z = 0; z < deleteLocation.groundItems.size(); z++) {
						Item posItem = deleteLocation.groundItems.get(z);
						if (posItem.returnID() == Integer.parseInt(itemNumString) && 
								posItem.returnName().equals(UsefulCommands.getOnlyStrings(toDelete))) {
							deleteLocation.groundItems.remove(z);
							sendBack.printMessage("You have deleted " + toDelete + ".");
							deleted = true;
						}
					}
				}
				if (!deleted) {
					sendBack.printMessage("Item not found on the ground.");
				}
				for (int i = 0; i < WorldServer.allItems.size(); i++) {
					int temp = WorldServer.allItems.size();
					if (WorldServer.allItems.get(i).returnID() == itemNum){
						WorldServer.allItems.remove(i);
						Iterator iter = WorldServer.activeClients.iterator();
						while (iter.hasNext() && deleted == false) {
							PlayerPrompt t = (PlayerPrompt) iter.next();
							for (int j = 0; j < t.currentPlayer.inventory.size(); j++) {
								if (t.currentPlayer.inventory.get(j).returnID() == itemNum && 
										t.currentPlayer.inventory.get(j).returnName().equals(UsefulCommands.getOnlyStrings(toDelete))) {
									t.currentPlayer.inventory.remove(j);
									sendBack.printMessage("You have deleted " + toDelete + ".");
									deleted = true;									
								}
							}
							if (t.currentPlayer.wieldedRight != null && deleted == false) {
								if (t.currentPlayer.wieldedRight.returnID() == itemNum && 
										t.currentPlayer.wieldedRight.returnName().equals(UsefulCommands.getOnlyStrings(toDelete))) {
									t.currentPlayer.wieldedRight = null;
									sendBack.printMessage("You have deleted " + toDelete + ".");
									deleted = true;
								}
							}
						} 
						if (!deleted) {
							sendBack.printMessage("Item not found in inventory or wieldedRight.");
						}
					}
				}
			} else {
				sendBack.printMessage("Please specify type and number, ie \"dagger1\"");
			}
		}
	}
	// Displays what you are wielding, and what is in your inventory.
	public static void doInventory(Player currentPlayer, SendMessage sendBack) {
		System.out.println("doInventory accessed.");
		if (currentPlayer.wieldedRight != null) {
			sendBack.printMessage("You are wielding: " + currentPlayer.wieldedRight.returnName() + ".");
		} else {
			sendBack.printMessage("You are not wielding anything.");
		}
		sendBack.printMessageLine("You are currently holding");
		if (currentPlayer.inventory.size() == 0) {
			sendBack.printMessage(" Nothing what-so-ever.");
		} else {
			for (int j = 0; j < currentPlayer.inventory.size(); j++) {			
				sendBack.printMessageLine(", " + currentPlayer.inventory.get(j).returnName());
			}
		sendBack.printMessage(".");
		}
	}
	// Displays additional informatin on desired object, needs expanding.
	public static void doInfo(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doInfo accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String command = st.nextToken();
		String infoPlace = "";
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		while (st.hasMoreTokens()) {
			if (first == false) {
				sb.append(" ");
			}
			sb.append(st.nextToken());
			first = false;
		}
		infoPlace = sb.toString();
		if (infoPlace.equals("")) {
			sendBack.printMessage("What would you like to take stock of?");
		} else {
			if (infoPlace.equals("inventory") || infoPlace.equals("inv") || infoPlace.equals("i")) {
				if (currentPlayer.wieldedRight != null) {
					sendBack.printMessage("You are wielding " + currentPlayer.wieldedRight.returnName() + 
							currentPlayer.wieldedRight.returnID());
				} else {
					sendBack.printMessage("You are not wielding anything.");
				}
				sendBack.printMessage("You take stock of your gear: ");
				if (currentPlayer.inventory.size() == 0) {
					sendBack.printMessage("You are holding nothing.");
				} else {
					for (int j = 0; j < currentPlayer.inventory.size(); j++) {
						sendBack.printMessage(currentPlayer.inventory.get(j).returnName() + 
								currentPlayer.inventory.get(j).returnID());
					}
				}
			} else if (infoPlace.equals("wieldedRight")) {
				if (currentPlayer.wieldedRight != null) {
					sendBack.printMessage("You are wielding " + currentPlayer.wieldedRight.returnName() + 
							currentPlayer.wieldedRight.returnID());
				} else {
					sendBack.printMessage("You are not wielding anything.");
				}
			} else {
				boolean success = false;
				Location thisLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
				for (int i = 0; i < thisLocation.groundItems.size(); i++) {
					Item posItem = thisLocation.groundItems.get(i);
					String posItemName = posItem.returnName();
					if (posItemName.equals(infoPlace) || (posItemName + posItem.returnID()).equals(infoPlace)) {
						sendBack.printMessage(posItem.returnName() + posItem.returnID());
						success = true;
						break;
					}
				} 
				if (currentPlayer.wieldedRight != null) {
					if ((currentPlayer.wieldedRight.returnName().equals(infoPlace)) || ((currentPlayer.wieldedRight.returnName() + 
							currentPlayer.wieldedRight.returnID()).equals(infoPlace))) {
						sendBack.printMessage(currentPlayer.wieldedRight.returnName() + currentPlayer.wieldedRight.returnID());
						success = true;
					}
				}
				for (int i = 0; i < currentPlayer.inventory.size(); i++) {
					Item posItemInv = currentPlayer.inventory.get(i);
					String posItemInvName = posItemInv.returnName();
					if (posItemInvName.equals(infoPlace) || (posItemInvName + posItemInv.returnID()).equals(infoPlace)) {
						sendBack.printMessage(posItemInv.returnName() + posItemInv.returnID());
						success = true;
						break;
					}
				}
				Collection c = WorldServer.mobList.values();
				Iterator iter = c.iterator();
				while (iter.hasNext() && success == false) {
					StdMob mob = (StdMob) iter.next();
					if ((mob instanceof StdMob) && ((infoPlace.toLowerCase().equals(mob.name.toLowerCase()) ||
							(infoPlace.toLowerCase().equals(mob.name.toLowerCase() + mob.id))))) {
						sendBack.printMessage(mob.name + mob.id);
						success = true;
					}
				}
				if (infoPlace.equals("")) {
					sendBack.printMessage("What is it you would you like info about?");
				} else if (success == false) {
					sendBack.printMessage("You fail to gather any information.");
				}
			}
		}
	}
	// Drops an item on the ground.
	public static void doDrop(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doDrop accessed.");
		String dropItem = UsefulCommands.returnTarget(fullCommand);
		if (dropItem.equals("")) {
			sendBack.printMessage("You have not specified what you want to drop.");
		} else {
	   	for (int i = 0; i < currentPlayer.inventory.size(); i++) {
				Item posItem = currentPlayer.inventory.get(i);
				String posItemName = posItem.returnName();
					if (posItemName.equals(dropItem) || (posItemName + posItem.returnID()).equals(dropItem)) {
					Location dropLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
					dropLocation.groundItems.add(posItem);
//					posItem.itemLocation = currentPlayer.mobLocation;
					currentPlayer.inventory.remove(posItem);
					sendBack.printMessage("You drop " + posItemName + posItem.returnID() + " onto the ground.");
					break;
				}
			}
		} 
	}
	// Picks up items off of the ground in your location.
	public static void doGet(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doGet accessed.");
		StringTokenizer st = UsefulCommands.getST(fullCommand);
		String item = "";
		if (st.hasMoreTokens()) {
			Location thisLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
			item = st.nextToken();
			for (int i = 0; i < thisLocation.groundItems.size(); i++) {
				Item posItem = thisLocation.groundItems.get(i);
				String posItemName = posItem.returnName();
				if (posItemName.equals(item) || (posItemName + posItem.returnID()).equals(item)) {
					thisLocation.groundItems.remove(i);
	//				posItem.itemLocation = -1;
					currentPlayer.inventory.add(posItem);
					sendBack.printMessage("You pick up " + posItemName + posItem.returnID());
				}
			}
		} else {
			sendBack.printMessage("What would you like to get?");
		}
	}
	
	// New way to create locations
	public static void doCreateNewLoc(String fullCommand, SendMessage sendBack) {
		System.out.println("doCreateNewLoc accessed.");
	}
	
	
	// Method for creating new locations.
	public static void doCreateLoc(SendMessage sendBack) {
		System.out.println("doCreateLoc accessed.");
		boolean maker = true;
		while (maker == true) {
			FileWriter fw = null;
			try {
				fw = new FileWriter("LocationMakerLog.txt", true);
			} catch (IOException e) {
			}
			StringBuffer sb = new StringBuffer();
			sb.append("autoCreateLocation(");
			sendBack.printMessage("Quit locationMaker? Y/N ");
			String more = sendBack.getMessage();
			if (more.toLowerCase().equals("y")) {
				break;
			}
			Location newLocation = new Location();
			sendBack.printMessage("Location number? :");
			String locNum = sendBack.getMessage();		
			String overwrite = "";
			if (WorldServer.locationCollection.containsKey(Integer.parseInt(locNum)) == true) {
				sendBack.printMessage("Location " + locNum + " already exists. Overwrite? Y/N");
				overwrite = sendBack.getMessage();
				if (!overwrite.toLowerCase().equals("y")) {
					doCreateLoc(sendBack);
				}
			}
			if (overwrite.equals("")) {
				sb.append("\"" + locNum + "\", ");
				newLocation.number = locNum;
				sendBack.printMessage("Location name? :");
				String name = sendBack.getMessage();
				sb.append("\"" + name + "\", ");
				newLocation.name = name;
				sendBack.printMessage("Setting location directions. Input room number or 0.");
				// Requests input to set room exits, and automatically changes connected room's exits.			
				StringBuffer sb1 = new StringBuffer();
				boolean first = true;
				for (int i = 0; i <= 11; i++) {
					sendBack.printMessage(newLocation.dirNames[i] + "? :");
					String input = sendBack.getMessage();
					if (first == true) {
						sb1.append(input);
						first = false;
					} else {
						sb1.append(", " + input);
					}
					newLocation.roomExits[i] = Integer.parseInt(input);
					if (WorldServer.locationCollection.get(newLocation.roomExits[i]) != null && !input.equals("0")) {
						Location anotherLocation = WorldServer.locationCollection.get(newLocation.roomExits[i]);
						anotherLocation.roomExits[newLocation.exitsOpp[i]] = Integer.parseInt(locNum);
					}
				}
				sendBack.printMessage("Directions set.");
				sendBack.printMessage("Location description? :");
				String description = sendBack.getMessage();
				sb.append("\"" + description + "\", ");
				sendBack.printMessage("Location ground type? :");
				String groundType = sendBack.getMessage();
				newLocation.groundType = groundType;
				sb.append("\"" + groundType + "\", ");
				sb.append(sb1 + ");");
				newLocation.description = description;
				sendBack.printMessage("");
				newLocation.displayLocation(sendBack);
				sendBack.printMessage("");
				sendBack.printMessage("Is this correct? Y/N or leave :");
				String answer = sendBack.getMessage();
				if (answer.toLowerCase().equals("y")) {
					String text = sb.toString();
					try {
						fw.write(text + System.getProperty("line.separator"));
						fw.close();
					} catch (IOException ee) {
					}   				
					WorldServer.locationCollection.put(Integer.parseInt(locNum), newLocation);
				} else if (answer.toLowerCase().equals("leave")) {
					maker = false;
				}
			} else if (overwrite.toLowerCase().equals("y")) {
				Location oldLocation = WorldServer.locationCollection.get(Integer.parseInt(locNum));
				sendBack.printMessage("Change location Number? N or Location Number :");
				String changeNum = sendBack.getMessage();
				if (!changeNum.toLowerCase().equals("n")) {
					oldLocation.number = changeNum;
					sb.append("\"" + changeNum + "\", ");
				} else {
					sb.append("\"" + oldLocation.number + "\", ");
				}
				sendBack.printMessage("Change location name? N: or Location name :");
				String name = sendBack.getMessage();
				if (!name.toLowerCase().equals("n")) {
					oldLocation.name = name;
					sb.append("\"" + name + "\", ");
				} else {
					sb.append("\"" + oldLocation.name + "\", ");
				}
				sendBack.printMessage("Change Location directions? Y/N");
				String directions = sendBack.getMessage();
				StringBuffer sb2 = new StringBuffer();
				Boolean stillFirst = true;
				if (directions.toLowerCase().equals("y")) {
					for (int i = 0; i <= 11; i++) {
						sendBack.printMessage(oldLocation.dirNames[i] + "? :");
						String input = sendBack.getMessage();
						if (stillFirst = true) {
							sb2.append(input);
							stillFirst = false;
						} else {
							sb2.append(", " + input);
						}
						oldLocation.roomExits[i] = Integer.parseInt(input);
						if (WorldServer.locationCollection.get(oldLocation.roomExits[i]) != null && !input.equals("0")) {
							Location anotherLocation = WorldServer.locationCollection.get(newLocation.roomExits[i]);
							anotherLocation.roomExits[oldLocation.exitsOpp[i]] = Integer.parseInt(locNum);
						}
					} 
					sendBack.printMessage("Directions changed.");
				} else {
						for (int i = 0; i <= 11; i++) {
							String input = Integer.toString(oldLocation.roomExits[i]);
							if (stillFirst == true) {
								sb2.append(input);
								stillFirst = false;
							} else {
								sb2.append(", " + input);
							}
						}
				}
				sendBack.printMessage("Change description? \"N\" or type it. :");
				String description = sendBack.getMessage();
				if (!description.toLowerCase().equals("n")) {
					oldLocation.description = description;
					sb.append(description);
				} else {
					sb.append(oldLocation.description);
				}
				sb.append(sb2 + ");");
				sendBack.printMessage("");
				oldLocation.displayLocation(sendBack);
				sendBack.printMessage("");
				sendBack.printMessage("Is this correct? Y/N or leave :");
				String answer = sendBack.getMessage();
				if (answer.toLowerCase().equals("y")) {
					String text = sb.toString();
					try {
						fw.write(text + System.getProperty("line.separator"));
						fw.close();
					} catch (IOException ee) {
					}   	
					sendBack.printMessage("Old location modified.");
				} else if (answer.toLowerCase().equals("leave")) {
					maker = false;
				}
			}
		}
	}
	// Deletes locations.
	public static void doDeleteLoc(Player currentPlayer, SendMessage sendBack) {
		System.out.println("doDeleteLoc accessed.");
		boolean temp = true;
		while (temp) {
			sendBack.printMessage("Location number to be deleted?");
			String delete = sendBack.getMessage();
			if (currentPlayer.mobLocation != Integer.parseInt(delete)) {
				Location tempLoc = WorldServer.locationCollection.get(Integer.parseInt(delete));
				for (int i = 0; i < 11; i++) {
					if (tempLoc.roomExits[i] != 0) {
						Location tempLoc2 = WorldServer.locationCollection.get(tempLoc.roomExits[i]);
						tempLoc2.roomExits[tempLoc2.exitsOpp[i]] = 0;
					}
				}
				WorldServer.locationCollection.remove(Integer.parseInt(delete));
			} else {
				sendBack.printMessage("You are currently standing in this location! Please move away first.");
			}
			sendBack.printMessage("Continue deleting? Y/N");
			String thisDel = sendBack.getMessage();
			if (thisDel.toLowerCase().equals("n")) {
				temp = false;
			}
		}
	}
	// creates mobs.
	public static void doCreateMob(SendMessage sendBack) {
		System.out.println("doCreateMob accessed.");
		StdMob creature = new StdMob("test", 1);
		sendBack.printMessage("You are now creating a new Mob.");
		sendBack.printMessage("  What is the Mob name?");
		creature.name = sendBack.getMessage();
		creature.id = WorldServer.mobList.size() + 1;
		sendBack.printMessage("  How much life does the Mob have?");
		int hitPoints = Integer.parseInt(sendBack.getMessage());
		creature.maxHp = hitPoints;
		creature.currentHp = hitPoints;
		sendBack.printMessage("  How fast?");
		creature.speed = Integer.parseInt(sendBack.getMessage());
		sendBack.printMessage("  Damage?");
		creature.maxHit = Integer.parseInt(sendBack.getMessage());
		sendBack.printMessage("  How much experience does the creature give on death?");
		creature.XPWorth = Integer.parseInt(sendBack.getMessage());
		sendBack.printMessage("  Which room will this Mob be created in?");
		String room = sendBack.getMessage();
		creature.mobLocation = Integer.parseInt(room);
		sendBack.printMessage("  Greet message?");
		creature.greetMsg = sendBack.getMessage();
		sendBack.printMessage("  Description?");
		creature.description = sendBack.getMessage();
		sendBack.printMessage("  Short Description?");
		creature.shortDescription = sendBack.getMessage();
		sendBack.printMessage("You have created a Mob.");		
		FileWriter fw = null;
		try {
			fw = new FileWriter("MobMakerLog.txt", true);
		} catch (IOException e) {
		}
		StringBuffer sb = new StringBuffer();
		sb.append("autoGenerateMobs(\"" + creature.name + "\", \"" + creature.greetMsg + "\", " + hitPoints + 
				", " + creature.speed + ", " + creature.maxHit + ", " + creature.mobLocation + ", " + 
				creature.XPWorth + ", " + creature.description + ");");
		String text = sb.toString();
		try {
			fw.write(text + System.getProperty("line.separator"));
			fw.close();
		} catch (IOException ee) {
		} 
		WorldServer.mobList.put(creature.name.toLowerCase(), creature);
	}
// deletes mobs.
	public static void doDeleteMob(SendMessage sendBack) {
		System.out.println("doDeleteMob accessed.");
		sendBack.printMessage("Which Mob would you like to delete? Input name and number, ie orc12.");
		String next = sendBack.getMessage();
		WorldServer.mobList.remove(UsefulCommands.getOnlyStrings(next));
		sendBack.printMessage("You have deleted a Mob.");
		sendBack.printMessage("Would you like to delete another Mob? Y/N");
		if (sendBack.getMessage().toLowerCase().equals("y")) {
			doDeleteMob(sendBack);
		} else {
			sendBack.printMessage("Deleting ended.");
		}
	}
	
	// Attacks a player, or returns that the desired target is not a player or is not here.
	// Needs to be re-written more generically.
	public static void attackPlayer(String target, String attackerMsg, String targetMsg, 
			int baseDamage, String type, Player currentPlayer, SendMessage sendBack, int balanceLength) {
		System.out.println("attackPlayer accessed.");
		int dmg = 0;
		String mobNoNum = UsefulCommands.getOnlyStrings(target);
		String mobNum = UsefulCommands.getOnlyNumerics(target);
		StdMob mob = null;
		if (mobNum.equals("")) {
			mob = WorldServer.mobList.get(target.toLowerCase());
		} else {
			Set s = WorldServer.mobList.keySet();
			Iterator iter = s.iterator();
			while (iter.hasNext()) {
				StdMob mobb = (StdMob) WorldServer.mobList.get(iter.next());
				if ( (mobb instanceof StdMob) && (mobb.name.toLowerCase() + mobb.id).equals(target)) {
					mob = mobb;
					break;
				}	
				mob = null;
			}
		}
		if (mob != null) { //Code below is not a good idea....
			if (mob.dead == false) {
				if (mob.mobLocation == currentPlayer.mobLocation) {
					if (type.equals("")) {
						// no Defences
						dmg = baseDamage;
					} else if (type.toLowerCase().equals("physical")) {
						// Shield reduction.
						if (mob.shield == true) {
							dmg =(int) (baseDamage - (baseDamage*.15));
						} else {
							dmg = baseDamage;
						}
					} else if (type.toLowerCase().equals("magical")) {
						// Aura reduction.
						if (mob.aura == true) {
							dmg = (int) (baseDamage - (baseDamage*.15));
						} else {
							dmg = baseDamage;
						}
					}
					if (currentPlayer.balance == true) {
						sendBack.printMessage(attackerMsg);
						mob.takeDamage(dmg);	
						if (mob instanceof Player) {
							Player mobPlayer = (Player) mob;
							(UsefulCommands.getPlayerPromptFromPlayer(mobPlayer)).sendBack.printSpace();
							(UsefulCommands.getPlayerPromptFromPlayer(mobPlayer)).sendBack.printMessage(targetMsg);
							UsefulCommands.displayPrompt(mobPlayer);
						}
						DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", balanceLength, 
								"You have regained your balance.");
						newDefence.start();
						
						if (mob.currentHp <= 0 && mob.dead == false) {
							sendBack.printMessage("You have killed " + mob.shortDescription + ".");
							currentPlayer.experience += mob.XPWorth;
							currentPlayer.levelPlayer();
							if ((mob instanceof StdMob)) {	
								StdMob mobm = (StdMob) mob;
								DeathLength death = new DeathLength(mobm);
								death.start();
								mobm.hostile = false;
							}
						}
					
						if (((StdMob)mob).hostile == false && (mob instanceof StdMob)) {
							Thread mobAttack = new MobAttack(((StdMob)mob), currentPlayer);
							mobAttack.start();
							((StdMob)mob).hostile = true;
						}
					} else if (currentPlayer.balance == false) {
						sendBack.printMessage("You are off balance");
					}	
				} else {
					sendBack.printMessage("You can't find your target.");
				}	
			} else {
				sendBack.printMessage("Why kill what is already dead?");
			}
		} else {
			sendBack.printMessage("You can't find your target.");
		}
//		return noPlayer;
	}

		
	public static void doGreet(Player currentPlayer, SendMessage sendBack, String fullCommand) {
		System.out.println("doGreet accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		if (WorldServer.mobList.containsKey(target.toLowerCase()) && (WorldServer.mobList.get(target.toLowerCase()) instanceof StdMob)) {
			StdMob thisCreature = (StdMob) WorldServer.mobList.get(target.toLowerCase());
			if (currentPlayer.mobLocation == thisCreature.mobLocation && thisCreature instanceof StdMob) {
				sendBack.printMessage(thisCreature.greet(currentPlayer));	
			}
		} else {
			sendBack.printMessage("You greet the open air before you, being sure to utilize your vast vocabulary.");
		}
			
	}
	
	//This allows players to report bugs.
	public static void doBug(String fullCommand, SendMessage sendBack, Player currentPlayer) {
		System.out.println("doBug accessed.");
		int size = currentPlayer.bugList.size() + 1;
		
		Date currentDate = new Date(); //Perhaps make a separate date function accessible by all files.
		int monthNow = currentDate.getMonth() + 1;
		int yearNow = currentDate.getYear() + 1900;
		int hourNow = currentDate.getHours();
		int minNow = currentDate.getMinutes();
		if (minNow < 10) {
			String dumbSoln = "0" + minNow;
			minNow = Integer.parseInt(dumbSoln);
		}
		String date = monthNow + "-" + currentDate.getDate() + "-" + yearNow + " " + hourNow + ":" + minNow;
		
		//Edit to remove "bug" from write to file
		String thisBug = date + " " + currentPlayer.name + " #" + size + ": " + fullCommand;
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
		currentPlayer.bugList.add(thisBug);
		sendBack.printMessage("Your bug has been reported, thank you. Type BUGS to check all bugs you have reported.");
	}
	
	//This allows players to check their reported bugs.
	//Add "bugs all"?
	//"bugs delete <name> <number>" check if you really want to delete, then send a msg to player with admin comment
	public static void doBugCheck(Player currentPlayer, SendMessage sendBack, String fullCommand) {
		System.out.println("doBugCheck accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String delete = st.nextToken();
		if (fullCommand.toLowerCase().equals("bugs")) {
			sendBack.printMessage("Your recent bugs:");
			for (int i = 0; i < currentPlayer.bugList.size(); i++) {
				sendBack.printMessage(currentPlayer.bugList.get(i));
			}
		//Deletes bugs	
		} else if (st.nextToken().toLowerCase().equals("delete")) {
			String name = st.nextToken();
			int num = Integer.parseInt(st.nextToken());
			if (WorldServer.mobList.containsKey(name) && ((Player) WorldServer.mobList.get(name)).bugList.size() <= 
					num && WorldServer.mobList.get(name) instanceof Player) {
				sendBack.printMessage("Do you want to remove this report? Y/N:");
				sendBack.printMessage(((Player)WorldServer.mobList.get(name)).bugList.get(num - 1));
				if (sendBack.getMessage().toLowerCase().equals("y")) {
					((Player)WorldServer.mobList.get(name)).bugList.remove(num - 1);
					sendBack.printMessage("Was this bug RESOLVED, NOT A BUG, or OTHER?");
					String answer = sendBack.getMessage().toLowerCase();
					if (answer.equals("resolved")) {
						String resolved = "msg " + name + " Your reported bug has been resolved and removed from your list. Thank you!";
						doMsg(sendBack, resolved, currentPlayer);
					} else if (answer.equals("not a bug")) {
						String notBug = "msg " + name + " Your reported bug was not a bug and was removed from your list. Thank you.";
						doMsg(sendBack, notBug, currentPlayer);
					} else if (answer.equals("other")) {
						sendBack.printMessage("Please input comment:");
						String other = "msg " + name + " " + sendBack.getMessage();
						doMsg(sendBack, other, currentPlayer);
					}
					//code in send msg to player about bug deleted
					sendBack.printMessage("Bug report " + name + " " + num + " deleted.");
				}
			} else if (!WorldServer.mobList.containsKey(name)) {
				sendBack.printMessage("Player does not exist or bug report does not exist.");
			}
		}
	}
	
	//This allows admin to edit reported bugs.
	//Not actually currently restricted to admin.
	//This also screws up the saved file of bugs.
	public static void doBugEdit(SendMessage sendBack, String fullCommand) {
		System.out.println("doBugEdit accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String delete = st.nextToken();
		if (st.hasMoreTokens()) {
			String name = st.nextToken();
			if (WorldServer.mobList.containsKey(name) && WorldServer.mobList.get(name) instanceof Player) {
				ArrayList<String> tempBugList = ((Player)(WorldServer.mobList.get(name))).bugList;
				int num = Integer.parseInt(st.nextToken());
				if (num < tempBugList.size()) {
					String bugToEdit = tempBugList.get(num);
					sendBack.printMessage(bugToEdit);
					sendBack.printMessage("Edit this bug? Y/N:");
					if (sendBack.getMessage().toLowerCase().equals("y")) {
						sendBack.printMessage("Input appends to current message: ");
						tempBugList.set(num, bugToEdit + " - Edit by admin - " + sendBack.getMessage());
					}				
				}
			} else if (!WorldServer.mobList.containsKey(name)) {
				sendBack.printMessage("Player does not exist.");
			}
		}
	}

	//Messaging system same as Avalon - msg, rmsg/msgs, cmsg
	public static void doMsg(SendMessage sendBack, String fullCommand, Player currentPlayer) {
		System.out.println("doMsg accessed.");
		StringTokenizer st = new StringTokenizer(fullCommand);
		String delete = st.nextToken();
		String nameToSend = st.nextToken();
		if (!WorldServer.mobList.containsKey(nameToSend)) {
			sendBack.printMessage("Player does not exist.");
		} else if (((Player)WorldServer.mobList.get(nameToSend)) instanceof Player) {
			String msg = currentPlayer.name + ": ";
			while (st.hasMoreTokens()) {
				msg = msg + " " + st.nextToken();
			}
			((Player)WorldServer.mobList.get(nameToSend)).messages.add(msg);
			sendBack.printMessage("Message sent.");
			//Lets person know they got a message, if online.
			Iterator iter = WorldServer.activeClients.iterator();
			while (iter.hasNext()) {
				PlayerPrompt t = (PlayerPrompt) iter.next();
				if (t.currentPlayer.name.equals(nameToSend)) {
					t.sendBack.printMessage("You have received a message. Type RMSG to read your messages.");
				}
			}
		}
	}
	//Rmsg
	public static void doRmsg(SendMessage sendBack, Player currentPlayer) {
		System.out.println("doRmsg accessed.");
		if (currentPlayer.messages.size() == 0) {
			sendBack.printMessage("You have no messages.");
		} else {
			for (int i = 0; i < currentPlayer.messages.size(); i++) {
				sendBack.printMessage(currentPlayer.messages.get(i));
			}
		}
	}
	//Cmsg
	public static void doCmsg(SendMessage sendBack, Player currentPlayer) {
		System.out.println("doCmsg accessed.");
		while(currentPlayer.messages.size() > 0) {
			currentPlayer.messages.remove(0);
		}
		if (currentPlayer.messages.size() == 0) {
			sendBack.printMessage("Messages deleted.");
		}
	}
	
	//Stat
	public static void doStat(SendMessage sendBack, Player currentPlayer) {
		System.out.println("doStat accessed.");
		sendBack.printMessage(currentPlayer.fullName() + ".");
		sendBack.printMessage("Your health is: " + currentPlayer.currentHp + "/" + currentPlayer.maxHp + ".");
		sendBack.printMessage("Current level: " + currentPlayer.level + ".");
		sendBack.printMessage("Your current experience value is: " + currentPlayer.experience);
	}
	
	//Examines object
	public static void doExamine(String fullCommand, SendMessage sendBack, Player currentPlayer) {
		System.out.println("doExamine accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		boolean success = false;
		Location thisLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
		for (int i = 0; i < thisLocation.groundItems.size(); i++) {
			Item posItem = thisLocation.groundItems.get(i);
			String posItemName = posItem.returnName();
			if (posItemName.equals(target) || (posItemName + posItem.returnID()).equals(target)) {
				sendBack.printMessage(posItem.returnDescription());
				success = true;
				break;
			}
		} 
		if (currentPlayer.wieldedRight != null) {
			if ((currentPlayer.wieldedRight.returnName().equals(target)) || ((currentPlayer.wieldedRight.returnName() + 
					currentPlayer.wieldedRight.returnID()).equals(target))) {
				sendBack.printMessage(currentPlayer.wieldedRight.returnDescription());
				success = true;
			}
		}
		for (int i = 0; i < currentPlayer.inventory.size(); i++) {
			Item posItemInv = currentPlayer.inventory.get(i);
			String posItemInvName = posItemInv.returnName();
			if (posItemInvName.toLowerCase().equals(target.toLowerCase()) || 
					(posItemInvName.toLowerCase() + posItemInv.returnID()).equals(target.toLowerCase())) {
				sendBack.printMessage(posItemInv.returnDescription());
				success = true;
				break;
			}
		}
		Collection c = WorldServer.mobList.values();
		Iterator iter = c.iterator();
		while (iter.hasNext()) {
		//	Object temp = iter.next();
		//	if (!(temp instanceof Player)) {
				StdMob mob = (StdMob) iter.next();
				if (target.toLowerCase().equals(mob.name.toLowerCase()) ||
						(target.toLowerCase().equals(mob.name.toLowerCase() + mob.id))) {
					sendBack.printMessage(mob.description);	
					success = true;
					break;
				}
		//	}
		}
		if (target.equals("")) {
			sendBack.printMessage("What is it you would you like to examine?");
		} else if (success == false) {
			sendBack.printMessage("You fail to examine anything.");
		}
	}
	
	//Mapper
	public static void doMap(SendMessage sendBack, Player currentPlayer) {
		System.out.println("doMap accessed.");
		Location thisLocation = WorldServer.locationCollection.get(currentPlayer.mobLocation);
		Location nextLoc = new Location();
		int[] mapDir = new int[] {7, 0, 1, 6, -1, 2, 5, 4, 3};
		String[] connectors = new String[] {"\\", "|", "/", "-"};
		ArrayList<Integer> locNums = new ArrayList<Integer>();
		for (int i = 0; i <= WorldServer.locationCollection.size(); i++) { //Stores room numbers
			locNums.add(i+1);
		}
		sendBack.printMessage("Map of your immediate location:");
		
		for (int i = 0; i <= 2; i++) { //Prints map
			String mapPrint = "";
			for (int j = i*3; j <= i*3+2; j++) {
				if (mapDir[j] != -1 && thisLocation.roomExits[mapDir[j]] == 0) {
					mapPrint += "   ";
				} else if (mapDir[j] != -1 && thisLocation.roomExits[mapDir[j]] != 0) {
					mapPrint += "[ ]";
				}
				if (mapDir[j] == -1) {
					mapPrint += "[X]";
				}
			}
			if (!mapPrint.equals("         ")) {
				sendBack.printMessage(mapPrint);
			}
		}
	}
		
	//Gives levels to target player - GiveLevel <Player> <#>
	public static void doGiveLevel(SendMessage sendBack, Player currentPlayer, String fullCommand) {
		System.out.println("doGiveLevel accessed.");
		StringTokenizer str = new StringTokenizer(fullCommand);
		String delete = str.nextToken();
		if (str.hasMoreTokens()) {
			String player = str.nextToken();
			if (WorldServer.mobList.containsKey(player.toLowerCase()) && WorldServer.mobList.get(player.toLowerCase()) instanceof Player) {
				int lvl = Integer.parseInt(str.nextToken());
				Player temp = (Player)WorldServer.mobList.get(player);
				int futureXP = 0;
				if (temp.level <= 6 && temp.level > 1) {
					futureXP = WorldServer.Levels[(temp.level + lvl) - 1];
				} else if (temp.level == 1 && lvl > 0) {
					futureXP = WorldServer.Levels[(temp.level + lvl) - 1];
				} else {
					sendBack.printMessage("Player's level cannot be changed in that way.");
				}
				temp.experience += futureXP - temp.experience;
				temp.levelPlayer();
				sendBack.printMessage("Success.");
			} else {
				sendBack.printMessage("Player does not exist.");
			}
		} else {
			sendBack.printMessage("Syntax is GiveLevel <Player> <#>, please try again.");
		}
	}
	//Players can "give" items away
	public static void doGive(Player currentPlayer, String fullCommand, SendMessage sendBack) {
		System.out.println("doGive accessed.");
		StringTokenizer st = UsefulCommands.getST(fullCommand);
		String item = "";
		String to;
		String target;
		if (st.hasMoreTokens()) {
			item = st.nextToken();
		} else {
			sendBack.printMessage("What would you like to give away?");
		}	
		if (st.hasMoreTokens()) {
			to = st.nextToken();
		} else {
			sendBack.printMessage("Give item TO whom?");
		}
		StringBuffer sb = new StringBuffer();
		boolean count = false;
		while(st.hasMoreTokens()) {
			if (count == true) {
				sb.append(" ");
			}
			sb.append(st.nextToken());
			count = true;			
		}
		target = sb.toString();
		if (target.equals("")) {
			sendBack.printMessage("Whom would you like to give " + item + " to?");
		} else {		
			Item giveItem;
			boolean found = false;
			if (currentPlayer.wieldedRight != null) {
				if (currentPlayer.wieldedRight.returnName().toLowerCase().equals(item.toLowerCase()) ||
						(currentPlayer.wieldedRight.returnName() + currentPlayer.wieldedRight.returnID()).equals(item.toLowerCase())) {
					giveItem = (Item) currentPlayer.wieldedRight;
					found = true;
					doGiveItem(giveItem, target, item, currentPlayer, sendBack);
				}	
			} if (found == false) {
				for (int i = 0; i < currentPlayer.inventory.size(); i++) {
					giveItem = currentPlayer.inventory.get(i);
					if (giveItem.returnName().toLowerCase().equals(item.toLowerCase()) ||
							(giveItem.returnName() + giveItem.returnID()).equals(item.toLowerCase())) {
						found = true;
						doGiveItem(giveItem, target, item, currentPlayer, sendBack);
						break;				
					}
				}
			}
			if (found == false) {
				sendBack.printMessage("You are oddly unable to find the item you wish to give away.");
			}
		}
	}	
	//Actually moves the item from one thing to another.
	public static void doGiveItem(Item giveItem, String target, String item, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doGiveItem accessed.");
		if (giveItem.returnName().toLowerCase().equals(item.toLowerCase()) ||
				((giveItem.returnName().toLowerCase() + giveItem.returnID()).equals(item.toLowerCase()))) {
			StdMob mob = WorldServer.mobList.get(target.toLowerCase());	
			if ((mob.name.toLowerCase().equals(target.toLowerCase()) ||
					(mob.name.toLowerCase() + ((StdMob)mob).id).equals(target.toLowerCase())) && 
					mob.mobLocation == currentPlayer.mobLocation) {
				if (currentPlayer.wieldedRight == giveItem) {
					currentPlayer.wieldedRight = null;
				} else {
					currentPlayer.inventory.remove(giveItem);
				}
				mob.inventory.add(giveItem);
				if (mob instanceof Player) {
					Player mobPlayer = (Player) mob;
					PlayerPrompt t = UsefulCommands.getPlayerPromptFromPlayer(mobPlayer);
					t.sendBack.printMessage(currentPlayer.name + " has given you " +
					giveItem.returnName() + giveItem.returnID());
				}
				sendBack.printMessage("You have just given " + giveItem.returnName() +
					giveItem.returnID() + " to " + mob.name);
//				if (mob.mobQuest != null && mob.mobQuest.QuestSuccess.equals(item.toLowerCase())) {
//					mob.mobQuest.finishQuest(currentPlayer);
//				}
			}
		}
	}
	//Who
	public static void doWho(Player currentPlayer, String fullCommand, SendMessage sendBack) {
		System.out.println("doWho accessed.");
		Iterator iter = WorldServer.activeClients.iterator();
		while (iter.hasNext()) {
			PlayerPrompt t = (PlayerPrompt) iter.next();
			String locationName = (WorldServer.locationCollection.get(t.currentPlayer.mobLocation).name);
			sendBack.printMessage(t.currentPlayer.fullName() + ": " + locationName);
		}
	}
}