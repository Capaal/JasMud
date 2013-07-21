package processes;

public class CreateWorld {
	
	public static void createWorld() {
		createGenericStart();
		createNewbieIsland();
	//	createTestArea();
	}
	
	public static void createTestArea() {
		Location loc;
		loc = new Location.Builder(1).name("1").description("This is east of the road.").groundType("land").build();
		loc = new Location.Builder(2).name("2").description("This is north of the beach on the bridge.").groundType("land").south(1, true).build();
		loc = new Location.Builder(3).name("3").description("There is a village to the north.").groundType("land").south(2, true).build();
		loc = new Location.Builder(4).name("4").description("You are standing in a small vilage. There is a bridge to the south.").groundType("land").west(3, true).southWest(1, true).build();
	}
	
	public static void createGenericStart() {
		Location loc;
		loc = new Location.Builder(1).name("Beach").description("This is start.").groundType("land").north(2, true).build();
		loc = new Location.Builder(2).name("Road north of beach").description("This is North of start.").groundType("land").south(1, true).build();
	}
	
	public static void createNewbieIsland() {
		Location loc;
		loc = new Location.Builder(3).name("Mud Intro Shack").description("This will be the mud intro shack. A tattered book lies in the ground, begging to be read. (READ BOOK)")
				.groundType("land").southEast(1, true).build();
		loc = new Location.Builder(4).name("East of road").description("This is east of the road.").groundType("land").west(2, true).build();
		loc = new Location.Builder(5).name("On a bridge").description("This is north of the beach on the bridge.").groundType("land").south(2, true).build();
		loc = new Location.Builder(6).name("Nearing a village").description("There is a village to the north.").groundType("land").south(5, true).build();
		loc = new Location.Builder(7).name("On a road within a village").description("You are standing in a small vilage. There is a bridge to the south.")
				.groundType("land").south(6, true).build();
		loc = new Location.Builder(8).name("Within a shack east of the road").description("It is dark in the shack.").groundType("land").west(7, true).build();
		loc = new Location.Builder(9).name("Within a shack west of the road").description("It is dark in the shack.").groundType("land").east(7, true).build();
		loc = new Location.Builder(10).name("North side of the village").description("You are on a road on the north side of the village.")
				.groundType("land").south(7, true).build();
		loc = new Location.Builder(11).name("Within a shack west of the road").description("It is dark in the shack.").groundType("land").east(10, true).build();
		loc = new Location.Builder(12).name("Within a shack east of the road").description("It is dark in the shack.").groundType("land").west(10, true).build();
		loc = new Location.Builder(13).name("North of the village").description("There is a village to the south.").groundType("land").south(10, true).build();
		loc = new Location.Builder(14).name("Entering a forest").description("You stand on the southern edge of a forest. There is a village to the south.")
				.groundType("land").south(13, true).build();
		loc = new Location.Builder(15).name("Forest east of the road").description("You are east of the road in a forest.").groundType("land").west(14, true).build();
		loc = new Location.Builder(16).name("Within the forest").description("You are deep in the forest.").groundType("land").south(15, true).build();
		loc = new Location.Builder(17).name("On a trail in the forest").description("You are on a trail in the middle of the forest.").groundType("land").southEast(14, true).build();
		loc = new Location.Builder(18).name("Forest south of the road").description("You are within a forest.").groundType("land").north(17, true).build();
		loc = new Location.Builder(19).name("Edge of the forest trail").description("The trail towards the northwest ends here.").groundType("land").southEast(17, true).build();
		loc = new Location.Builder(20).name("Forest west of the road").description("You are within a forest.").groundType("land").east(19, true).build();
		loc = new Location.Builder(21).name("Deep in the forest").description("You are deep within a forest.").groundType("land").northWest(20, true).build();
		loc = new Location.Builder(22).name("Very deep in the forest").description("It is dark in the forest.").groundType("land").northEast(21, true).build();
		loc = new Location.Builder(23).name("Edge of the forest near a lake").description("There is a lake to the north and a forest trail to the southeast.")
				.groundType("land").southEast(19, true).build();
		loc = new Location.Builder(24).name("Southern edge of a lake").description("There is a forest to the south.").groundType("water").south(23, true).build();
	}
}

	//public static void autoCreateLocation(int locNum, String name, String desc, String groundType, 
	//		int n, int ne, int e, int se, int s, int sw, int w, int nw, int up, int d, int in, int out) {
		
	//	Location temp = new Location()
	/*	Location temp = new Location();
		temp.setId(locNum);
		temp.setName(name);
		temp.directionsBlank(); // ????
		temp.setDescription(desc);
		temp.setGroundType(groundType);
		int[] tempExits = new int[] {n, ne, e, se, s, sw, w, nw, up, d, in, out};
		for (int i = 0; i <= 11; i++) {
			if (tempExits[i] != 0 && WorldServer.locationCollection.containsKey(tempExits[i])) {
				temp.setLocation(tempExits[i], i);
			//	if (!temp.number.equals("1")) {
					Location anotherLocation = temp.getLocation(i);
					anotherLocation.setLocation(temp, i);
			//	}
			}
		}
		WorldServer.locationCollection.put(locNum, temp);*/
	//}
	
	//public static void generateItems() {
	//	for (int i = 0; i < WorldServer.allItems.size(); i++) {
	//		Item genItem = WorldServer.allItems.get(i);
	//	}
	//}
	
	//public static void generateMobs() {
	/*	autoGenerateMobs("Goblin", "Gabaldy Gook! ... Dos we say dat?", 40, 3000, 15, 20,1, "A green goblin.", "a green goblin");
		autoGenerateMobs("Horse", "Neigh", 100, 3000, 10, 10, 1, "A proud warhorse, don't stand behind one of these.", "a grand warhorse");
		autoGenerateMobs("Farmer James", "Ahoy there, interested in a bit o' farmin'?", 2000, 5000, 20, 11, 1, "He looks like someone willing to send others on meaningless tasks...", "a useful person not worth killing");
*/	//}
	
/*	public static void autoGenerateMobs(String name, String greetMsg, int life, int speed, int damage, 
			int room, int XPWorth, String description, String shortDescription) {
		WorldServer.mobList.remove(name.toLowerCase());
		Mob creature = new Mob(name, room);
		creature.id = WorldServer.mobList.size() + 1;
		creature.maxHp = life;
		creature.currentHp = life;
		creature.speed = speed;
		creature.maxHit = damage;
		creature.greetMsg = greetMsg;
	//	creature.XPWorth = XPWorth;
		creature.description = description;
		creature.shortDescription = shortDescription;
		WorldServer.mobList.put(name.toLowerCase(), creature);
	}*/
	
//	public static void generateQuests() {
//		Quest newQuest = new QuestFarmerJamesDagger();
//		WorldServer.allQuests.add(newQuest);
//	}
//}