package processes;

import interfaces.Mobile;
import items.StdItem;

import java.util.*;

public class GameState {
	// Contains transient sockets of each connected Player.
	public static Set<PlayerPrompt> activeClients = new HashSet<PlayerPrompt>();	
	
	// Contains all Location objects. Location's id to location object.
	public static Map<Integer, Location> locationCollection = new TreeMap<Integer, Location>();
	
	// Mob list name + id
	public static Map<String, Mobile> mobList = new TreeMap<String, Mobile>();
	
	// List of all items name + id
	public static Map<String, StdItem> allItems = new TreeMap<String, StdItem>();
	
	// Collection of all skill books, Mobs then load a copy of each skill book.
	public static HashMap<Integer, SkillBook> AllSkillBooks = new HashMap<Integer, SkillBook>();
}
