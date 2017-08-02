package processes;

import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder;
import items.StdItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

// Might need to synchronize some of these.
// Game state and save state.
public class GameState {	
	
	private Map<String, Mobile> allPlayersList = new TreeMap<String, Mobile>();
	
	public Map<String, Integer> idMap = new HashMap<String, Integer>();

	public Map<String, ItemBuilder> itemTemplates = new TreeMap<String, ItemBuilder>(); //list of all template items
	
	private Map<String, Mobile> deletedPlayers = new TreeMap<String, Mobile>();
	
	private HashMap<Integer, SkillBook> AllSkillBooks = new HashMap<Integer, SkillBook>();
	
	public int maxLocationId;
	
	private Map<String, ArrayList<SkillBook>> classToBooks = new TreeMap<String, ArrayList<SkillBook>>();
	public void addClass(String className, ArrayList<SkillBook> books) {
		classToBooks.put(className, books);
	}
	public ArrayList<SkillBook> getBooksFromClass(String className) {
		return classToBooks.get(className);
	}
	
	// Contains transient sockets of each connected Player.
	@XStreamOmitField
	private Set<PlayerPrompt> activeClients = new HashSet<PlayerPrompt>();
	
	// Contains all Location objects. Location's id to location object.
	@XStreamOmitField
	public Map<Integer, Location> locationCollection = new TreeMap<Integer, Location>();
	
	// Mob list name + id
	@XStreamOmitField // TODO Should load mobs or generate on createWorld? I vote generate
	private Map<String, Mobile> mobList = new TreeMap<String, Mobile>();
	
	// Collection of all skill books, Mobs then load a copy of each skill book. id/book	
	
	@XStreamOmitField
	private ScheduledExecutorService effectExecutor;
	
	@XStreamOmitField
	public static BlockingQueue<Runnable> SkillQueue;	
	
	public GameState() {
		effectExecutor  = Executors.newScheduledThreadPool(1);
		SkillQueue = new ArrayBlockingQueue<Runnable>(1024);
		maxLocationId = 0;
	}
	
	private Object readResolve() {
		effectExecutor  = Executors.newScheduledThreadPool(1);
		SkillQueue = new ArrayBlockingQueue<Runnable>(1024);
		activeClients = new HashSet<PlayerPrompt>();
		locationCollection = new TreeMap<Integer, Location>();
		mobList = new TreeMap<String, Mobile>();
		AllSkillBooks = new HashMap<Integer, SkillBook>();
		effectExecutor  = Executors.newScheduledThreadPool(1);
		SkillQueue = new ArrayBlockingQueue<Runnable>(1024);
		for (Mobile m : allPlayersList.values()) {
			m.getContainer().removeItemFromLocation(m);
		}
		return this;		
	}
	
	public ScheduledExecutorService getEffectExecutor() {
		return effectExecutor;
	}
	
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
	
	//maybe make this harder - add to a deleted playersList instead of just removed forever
	public void deletePlayer(String name) {
		deletedPlayers.put(name, allPlayersList.get(name));
		allPlayersList.remove(name);
	}
	
	public Map<String, Mobile> viewAllPlayers() {
		return new TreeMap<String, Mobile>(allPlayersList);
	}
	
	public Mobile getPlayer(String playerName) {
		return allPlayersList.get(playerName);
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
	
	public void saveItem(Holdable item) {
		if (item.getContainer() != null) {
			FileOutputStream fos = null;
			try {
				WorldServer.xstream.toXML(item, new FileWriter(new File("./Items/" + item.getName()+item.getId()+ ".xml")));
			} catch(Exception e) {
			    e.printStackTrace(); // this obviously needs to be refined.
			} finally {
			    if(fos!=null) {
			        try{ 
			            fos.close();
			        } catch (IOException e) {
			            e.printStackTrace(); // this obviously needs to be refined.
			        }
			    }
			}
		}
	}

	public boolean loadSavedItems() {		
		File[] roots;
		File fileDir = new File("./Items");
		roots = fileDir.listFiles();       
	    for (File file : roots) {
	    	if(file.exists() && (file.getName().endsWith(".xml")||file.getName().endsWith(".XML"))) {
	        	try{		    			       
			        Object item = WorldServer.xstream.fromXML(file);  
			        ((StdItem)item).getContainer().acceptItem((StdItem)item);
			    }catch(Exception e){
			        System.err.println("Error in XML Read item: " + file.getName() + e.getMessage());
			        return false;
			    }
	    	}
	    }
	    return true;
	}
}
