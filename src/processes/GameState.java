package processes;

import interfaces.Mobile;
import items.StdItem;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
	
	
	public static BlockingQueue<QueueInformation> SkillQueue = new ArrayBlockingQueue<QueueInformation>(1024);
	
	public class QueueInformation {
		Skill skill;
		String fullCommand;
		Mobile currentPlayer;
		
		public QueueInformation(Skill skill, String fullCommand, Mobile currentPlayer) {
			this.skill = skill;
			this.fullCommand = fullCommand;
			this.currentPlayer = currentPlayer;
		}
	}
	
	public void addToQueue(Skill skill, String fullCommand, Mobile currentPlayer) {
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
