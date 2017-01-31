package processes;

import interfaces.Mobile;
import items.StdItem;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GameState {
	// Contains transient sockets of each connected Player.
	private Set<PlayerPrompt> activeClients = new HashSet<PlayerPrompt>();	
	
	// Contains all Location objects. Location's id to location object.
	private Map<Integer, Location> locationCollection = new TreeMap<Integer, Location>();
	
	// Mob list name + id
	private Map<String, Mobile> mobList = new TreeMap<String, Mobile>();
	
	// List of all items name + id
	private Map<String, StdItem> allItems = new TreeMap<String, StdItem>();
	
	// Collection of all skill books, Mobs then load a copy of each skill book. id/book
	private HashMap<Integer, SkillBook> AllSkillBooks = new HashMap<Integer, SkillBook>();
	
	public boolean addClient(PlayerPrompt pp) {
		return activeClients.add(pp);
	}
	
	public boolean removeClient(PlayerPrompt pp) {
		return activeClients.remove(pp);
	}
	
	public Set<PlayerPrompt> viewActiveClients() {
		return new HashSet<PlayerPrompt>(activeClients);
	}
	
	public Set<StdItem> viewAllItems() {
		return new HashSet<StdItem>(allItems.values());
	}
	
	public void addItem(String nameId, StdItem newItem) {
		allItems.put(nameId, newItem);
	}
	
	public boolean removeItem(String nameId) {
		return allItems.remove(nameId) != null;
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
	
	public static BlockingQueue<QueueInformation> SkillQueue = new ArrayBlockingQueue<QueueInformation>(1024);
	
	public class QueueInformation {
		Skills skill;
		String fullCommand;
		Mobile currentPlayer;
		
		public QueueInformation(Skills skill, String fullCommand, Mobile currentPlayer) {
			this.skill = skill;
			this.fullCommand = fullCommand;
			this.currentPlayer = currentPlayer;
		}
	}
	
	public void addToQueue(Skills skill, String fullCommand, Mobile currentPlayer) {
		try {
			SkillQueue.put(new QueueInformation(skill, fullCommand, currentPlayer));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public QueueInformation takeFromQueue() {
		try {
			return SkillQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
