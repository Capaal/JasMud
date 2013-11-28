package processes;

import skills.*;
import items.*;

// to create a location connection, .south(2, "north") means if I go south from that location, I go to location with the id 2, and
// if I go north from there, it will take me back to the location I just made.

// This is a comment that should only be in "Fixing" branch.

public class CreateWorld {
	
	public static void createWorld() {
		createGenericStart();
		createNewbieIsland();

		createMobs();

		createAllItems();

	//	createTestArea();
	}
	
	public static void createMobs() {
		StdMob mob = new StdMob.Builder(1, "James").description("He is a farmer names James.").shortDescription("A farmer.").location(WorldServer.locationCollection.get(1)).build();
		mob = new Player.Builder(2, "A Player.").build();
	}
	
	public static void createTestArea() {
		Location loc;
		loc = new Location.Builder(1).name("1").description("This is east of the road.").groundType("land").build();
		loc = new Location.Builder(2).name("2").description("This is north of the beach on the bridge.").groundType("land").south(1, "north").build();
		loc = new Location.Builder(3).name("3").description("There is a village to the north.").groundType("land").south(2, "south").build();
		loc = new Location.Builder(4).name("4").description("You are standing in a small vilage. There is a bridge to the south.").groundType("land").west(3, "east").southWest(1, "northeast").build();
	}
	
	public static void createGenericStart() {
		Location loc;
		loc = new Location.Builder(1).name("Beach").description("This is start.").groundType("land").build();
		loc = new Location.Builder(2).name("Road north of beach").description("This is North of start.").groundType("land").south(1, "north").build();
	}
	
	public static void createNewbieIsland() {
		Location loc;
		loc = new Location.Builder(3).name("Mud Intro Shack").description("This will be the mud intro shack. A tattered book lies in the ground, begging to be read. (READ BOOK)")
				.groundType("land").southEast(1, "northwest").build();
		loc = new Location.Builder(4).name("East of road").description("This is east of the road.").groundType("land").west(2, "east").build();
		loc = new Location.Builder(5).name("On a bridge").description("This is north of the beach on the bridge.").groundType("land").south(2, "north").build();
		loc = new Location.Builder(6).name("Nearing a village").description("There is a village to the north.").groundType("land").south(5, "north").build();
		loc = new Location.Builder(7).name("On a road within a village").description("You are standing in a small vilage. There is a bridge to the south.")
				.groundType("land").south(6, "north").build();
		loc = new Location.Builder(8).name("Within a shack east of the road").description("It is dark in the shack.").groundType("land").west(7, "east").build();
		loc = new Location.Builder(9).name("Within a shack west of the road").description("It is dark in the shack.").groundType("land").east(7, "west").build();
		loc = new Location.Builder(10).name("North side of the village").description("You are on a road on the north side of the village.")
				.groundType("land").south(7, "north").build();
		loc = new Location.Builder(11).name("Within a shack west of the road").description("It is dark in the shack.").groundType("land").east(10, "west").build();
		loc = new Location.Builder(12).name("Within a shack east of the road").description("It is dark in the shack.").groundType("land").west(10, "east").build();
		loc = new Location.Builder(13).name("North of the village").description("There is a village to the south.").groundType("land").south(10, "north").build();
		loc = new Location.Builder(14).name("Entering a forest").description("You stand on the southern edge of a forest. There is a village to the south.")
				.groundType("land").south(13, "north").build();
		loc = new Location.Builder(15).name("Forest east of the road").description("You are east of the road in a forest.").groundType("land").west(14, "east").build();
		loc = new Location.Builder(16).name("Within the forest").description("You are deep in the forest.").groundType("land").south(15, "north").build();
		loc = new Location.Builder(17).name("On a trail in the forest").description("You are on a trail in the middle of the forest.").groundType("land").southEast(14, "northwest").build();
		loc = new Location.Builder(18).name("Forest south of the road").description("You are within a forest.").groundType("land").north(17, "south").build();
		loc = new Location.Builder(19).name("Edge of the forest trail").description("The trail towards the northwest ends here.").groundType("land").southEast(17, "northwest").build();
		loc = new Location.Builder(20).name("Forest west of the road").description("You are within a forest.").groundType("land").east(19, "west").build();
		loc = new Location.Builder(21).name("Deep in the forest").description("You are deep within a forest.").groundType("land").northWest(20, "southeast").build();
		loc = new Location.Builder(22).name("Very deep in the forest").description("It is dark in the forest.").groundType("land").northEast(21, "southwest").build();
		loc = new Location.Builder(23).name("Edge of the forest near a lake").description("There is a lake to the north and a forest trail to the southeast.")
				.groundType("land").southEast(19, "northwest").build();
		loc = new Location.Builder(24).name("Southern edge of a lake").description("There is a forest to the south.").groundType("water").south(23, "north").build();
	}
	
	// StdItem dagger = new StdItem.Builder("Dagger", 1).physicalMult(1.1).description("Short a sharp.").shortDescription("a dagger").itemCommands("stab").balanceMult(.8).maxCondition(100).
	//		itemLocation(WorldServer.locationCollection.get(1)).build(); 
	public static void createAllItems() {
		
		StdItem dagger = new StdItem.Builder("Dagger", 1).physicalMult(1.1).description("Short a sharp.").shortDescription("a dagger").itemCommands("stab").balanceMult(.8).maxCondition(100).
				itemLocation(WorldServer.locationCollection.get(1)).build();
		StdItem sword = new StdItem.Builder("Sword", 1).physicalMult(1.2).description("Long and sharp.").shortDescription("a sword").itemCommands("stab").balanceMult(.9).maxCondition(150).
				itemLocation(WorldServer.locationCollection.get(1)).build();
		StdItem staff = new StdItem.Builder("Staff", 1).physicalMult(1.4).description("Long and wooden.").shortDescription("a staff").itemCommands("stab").balanceMult(1.2).maxCondition(180).
				itemLocation(WorldServer.locationCollection.get(1)).build();
		StdItem vase = new StdItem.Builder("Vase", 1).physicalMult(.2).description("It's a vase.").shortDescription("a vase").balanceMult(1.0).maxCondition(10).
				itemLocation(WorldServer.locationCollection.get(1)).build();
		StdItem ring = new StdItem.Builder("Ring", 1).physicalMult(.2).description("A small gold ring.").shortDescription("a ring").balanceMult(1.0).maxCondition(10).
				itemLocation(WorldServer.locationCollection.get(1)).build();
		StdItem helmet = new StdItem.Builder("Helmet", 1).physicalMult(.4).description("It goes on your head.").shortDescription("a helmet").balanceMult(1.0).maxCondition(10).
				itemLocation(WorldServer.locationCollection.get(1)).build();
		
	}
}