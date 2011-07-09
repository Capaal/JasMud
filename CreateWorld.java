import java.util.*;
import java.io.*;

public class CreateWorld {
	
	public static void createWorld() {
		createGenericStart();
		createNewbieIsland();
	}
	
	public static void createGenericStart() {
		autoCreateLocation("1", "Beach", "This is start.", "land", 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("2", "Road north of beach.", "This is North of start.", "land", 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0);
	}
	
	public static void createNewbieIsland() {
		autoCreateLocation("3", "Mud Intro Shack", "This will be the mud intro shack. A tattered book lies in the ground, begging to be read. (READ BOOK)", "land", 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("4", "East of road", "This is east of the road.", "land", 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0);
		autoCreateLocation("5", "On a bridge", "This is north of the beach on a bridge.", "land", 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("6", "Nearing a village", "There is a village to the north.", "land", 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("7", "On a road within a village", "You are standing in a small village. There is a bridge to the south.", "land", 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("8", "Within a shack east of the road", "It is dark in the shack.", "land", 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0);
		autoCreateLocation("9", "Within a shack west of the road", "It is dark in the shack.", "land", 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("10", "North side of the village", "You are on a road in the north side of the village.", "land", 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("11", "Within a shack west of the road", "It is dark in the shack.", "land", 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("12", "Within a shack east of the road", "It is dark in the shack.", "land", 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0);
		autoCreateLocation("13", "North of the village", "There is a village to the south.", "land", 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("14", "Entering a forest", "You stand on the southern edge of a forest. There is a village to the south.", "land", 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("15", "Forest east off the road", "You are east of the road in a forest.", "land", 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0);
		autoCreateLocation("16", "Within the forest", "You are deep in a forest", "land", 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("17", "On a trail in the forest", "You are on a trail in the middle of the forest.", "land", 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("18", "Forest south off the road", "You are within a forest", "land", 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("19", "Edge of the forest trail", "The trail towards the northwest ends here.", "land", 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("20", "Forest west off the road", "You are within a forest", "land", 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("21", "Deep in the forest", "You are deep within a forest", "land", 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0);
		autoCreateLocation("22", "Very deep in the forest", "It is dark in the forest", "land", 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("23", "Edge of the forest near a lake", "There is a lake to the north and a forest trail to the southeast.", "land", 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0);
		autoCreateLocation("24", "Southern edge of a lake", "There is a forest to the south", "water", 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0);
	}

	public static void autoCreateLocation(String locNum, String name, String desc, String groundType, 
			int n, int ne, int e, int se, int s, int sw, int w, int nw, int in, int out, int up, int d) {
		Location temp = new Location();
		temp.number = locNum;
		temp.name = name;
		temp.directionsBlank();
		temp.description = desc;
		temp.groundType = groundType;
		int[] tempExits = new int[] {n, ne, e, se, s, sw, w, nw, in, out, up, d};
		for (int i = 0; i <= 11; i++) {
			if (tempExits[i] != 0) {
				temp.roomExits[i] = tempExits[i];
				if (!temp.number.equals("1")) {
					Location anotherLocation = WorldServer.locationCollection.get(temp.roomExits[i]);
					anotherLocation.roomExits[temp.exitsOpp[i]] = Integer.parseInt(locNum);
				}
			}
		}
		WorldServer.locationCollection.put(Integer.parseInt(locNum), temp);
	}
	
	public static void generateItems() {
		for (int i = 0; i < WorldServer.allItems.size(); i++) {
			Item genItem = WorldServer.allItems.get(i);
		}
	}
	
	public static void generateMobs() {
		autoGenerateMobs("Goblin", "Gabaldy Gook! ... Dos we say dat?", 40, 3000, 15, 20,1, "A green goblin.", "a green goblin");
		autoGenerateMobs("Horse", "Neigh", 100, 3000, 10, 10, 1, "A proud warhorse, don't stand behind one of these.", "a grand warhorse");
		autoGenerateMobs("Farmer James", "Ahoy there, interested in a bit o' farmin'?", 2000, 5000, 20, 11, 1, "He looks like someone willing to send others on meaningless tasks...", "a useful person not worth killing");
	}
	
	public static void autoGenerateMobs(String name, String greetMsg, int life, int speed, int damage, 
			int room, int XPWorth, String description, String shortDescription) {
		WorldServer.mobList.remove(name.toLowerCase());
		Mob creature = new Mob(name, room);
		creature.id = WorldServer.mobList.size() + 1;
		creature.maxHp = life;
		creature.currentHp = life;
		creature.speed = speed;
		creature.maxHit = damage;
		creature.greetMsg = greetMsg;
		creature.XPWorth = XPWorth;
		creature.description = description;
		creature.shortDescription = shortDescription;
		WorldServer.mobList.put(name.toLowerCase(), creature);
	}
	
	public static void generateQuests() {
		Quest newQuest = new QuestFarmerJamesDagger();
		WorldServer.allQuests.add(newQuest);
	}
}