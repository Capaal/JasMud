package processes;

import skills.*;

public class PossibleCommands {

	public static void create() {		
		// This might still exist as a list of ALL possible skills, don't know if I'd need that or not.
		// I am moving skills into each mobiles, so mobiles has all moves, players/heroes have firebolt, and so on.
		WorldServer.skillCommands.put("north", new Move());
		WorldServer.skillCommands.put("ne", new Move());
		WorldServer.skillCommands.put("nw", new Move());
		WorldServer.skillCommands.put("sw", new Move());
		WorldServer.skillCommands.put("se", new Move());
		WorldServer.skillCommands.put("northeast", new Move());
		WorldServer.skillCommands.put("east", new Move());
		WorldServer.skillCommands.put("south", new Move());
		WorldServer.skillCommands.put("southeast", new Move());		
		WorldServer.skillCommands.put("southwest", new Move());
		WorldServer.skillCommands.put("west", new Move());
		WorldServer.skillCommands.put("northwest", new Move());
		WorldServer.skillCommands.put("up", new Move());
		WorldServer.skillCommands.put("down", new Move());
		WorldServer.skillCommands.put("in", new Move());
		WorldServer.skillCommands.put("out", new Move());
		WorldServer.skillCommands.put("swim", new Swim());	
		
		WorldServer.skillCommands.put("look", new Look());
		WorldServer.skillCommands.put("examine", new Examine());
//		WorldServer.skillCommands.put("map", new Map());
		WorldServer.skillCommands.put("create", new Create());
		WorldServer.skillCommands.put("bug", new Bug());
		WorldServer.skillCommands.put("bugs", new Bugs());
	}		
}

		