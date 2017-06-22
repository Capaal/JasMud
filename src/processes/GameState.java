package processes;

import interfaces.Mobile;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// Collections.synchronizedSortedSet(new TreeSet<Holdable>());
// Might need to synchronize some of these.

public class GameState {
	// Contains transient sockets of each connected Player.
	private Set<PlayerPrompt> activeClients = new HashSet<PlayerPrompt>();	
	
	// All players
	private Map<String, Mobile> allPlayersList = new TreeMap<String, Mobile>();
	
	// Contains all Location objects. Location's id to location object.
	private Map<Integer, Location> locationCollection = new TreeMap<Integer, Location>();
	
	// Mob list name + id
	private Map<String, Mobile> mobList = new TreeMap<String, Mobile>();
	
	// Collection of all skill books, Mobs then load a copy of each skill book. id/book
	private HashMap<Integer, SkillBook> AllSkillBooks = new HashMap<Integer, SkillBook>();
	
	public static ScheduledExecutorService effectExecutor  = Executors.newScheduledThreadPool(1);
	
	public boolean addClient(PlayerPrompt pp) {
		return activeClients.add(pp);
	}
	
	public boolean removeClient(PlayerPrompt pp) {
		return activeClients.remove(pp);
	}
	
	public Set<PlayerPrompt> viewActiveClients() {
		return new HashSet<PlayerPrompt>(activeClients);
	}
	
	//make sure add doesn't override and can only add unique
	public void addNewPlayer(String name, Mobile mob) {
		allPlayersList.put(name, mob);
	}
	
	private Map<String, Mobile> deletedPlayers = new TreeMap<String, Mobile>();
	//maybe make this harder - add to a deleted playersList instead of just removed forever
	public void deletePlayer(String name) {
		deletedPlayers.put(name, allPlayersList.get(name));
		allPlayersList.remove(name);
	}
	
	public Map<String, Mobile> viewAllPlayers() {
		return new TreeMap<String, Mobile>(allPlayersList);
	}
	
	public Set<SkillBook> viewAllBooks() {
		return new HashSet<SkillBook>(AllSkillBooks.values());
	}
	
	public boolean checkForBookId(int Id) {
		return AllSkillBooks.containsKey(Id);
	}
	
	public SkillBook getBook(int Id) {
		return AllSkillBooks.get(Id);
	}
	
	public void addBook(int bookId, SkillBook sb) {
		AllSkillBooks.put(bookId, sb);
	}
	
	public void addMob(String nameId, Mobile mob) {
		mobList.put(nameId, mob);
	}
	
	public void removeMob(String nameId) {
		mobList.remove(nameId);
	}
	
	public boolean checkForMob(String nameId) {
		return mobList.containsKey(nameId);
	}
	
	public Map<String, Mobile> viewMobs() {
		return new TreeMap<String, Mobile>(mobList);
	}
	
	public Map<Integer, Location> viewLocations() {
		return new TreeMap<Integer, Location>(locationCollection);
	}
	
	public void addLocation(int id, Location location) {
		locationCollection.put(id, location);
	}
	
	public boolean checkForLocation(int id) {
		return locationCollection.containsKey(id);
	}
	
	public static BlockingQueue<Runnable> SkillQueue = new ArrayBlockingQueue<Runnable>(1024);
	
	public void addToQueue(Runnable skill) {
		try {
			SkillQueue.put(skill);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Runnable takeFromQueue() {
		try {
			return SkillQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}	
}
