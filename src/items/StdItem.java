package items;

import interfaces.*;

import java.util.ArrayList;
import processes.*;

public class StdItem implements Item, Holdable, Creatable {
	
	private Double physicalMult;
	private final int id;
	private final String name;
	private String description;
	private String shortDescription;
	private Double balanceMult;
	private Container itemLocation;
	public ArrayList<String> itemCommands;
	private int maxCondition;
	private int currentCondition;

	// Example of a Dagger build: 
	// StdItem dagger = new StdItem.Builder("Dagger", 1).physicalMult(1.1).description("Short a sharp.").shortDescription("a dagger").itemCommands("stab").balanceMult(.8).maxCondition(100).itemLocation(WorldServer.locationCollection.get(1)).build(); 
	
	public StdItem(Init<?> build) {
		itemCommands = new ArrayList<String>();
		itemCommands = build.itemCommands;
		this.id = build.id;
		this.name = build.name;
		this.physicalMult = build.physicalMult;
		this.description = build.description;
		this.balanceMult = build.balanceMult;
		this.itemLocation = build.itemLocation;
		this.itemCommands = build.itemCommands;		
		this.shortDescription = build.shortDescription;
		this.maxCondition = build.maxCondition;
		this.currentCondition = maxCondition;
		WorldServer.allItems.put(name + id, this);
		itemLocation.acceptItem(this);
	}
	
	protected static abstract class Init<T extends Init<T>> {
		
		private Double physicalMult = 1.0;
		private final int id;
		private final String name;
		private String description = "default";
		private String shortDescription = "shortDefault";
		private Double balanceMult = 1.0;
		private int maxCondition = 1000;
		private Container itemLocation = WorldServer.locationCollection.get(1);
		private ArrayList<String> itemCommands = new ArrayList<String>();
		
		protected abstract T self();		
		
		public Init(String name, int id) {
			if (WorldServer.allItems.containsKey(name + id)) {
				throw new IllegalStateException("A item already exists with that name and id.");
			}
			this.id = id;
			this.name = name;
		}
		
		public T physicalMult(Double val) {physicalMult = val;return self();}		
		public T description(String val) {description = val;return self();}		
		public T shortDescription(String val) {shortDescription = val;return self();}		
		public T maxCondition(int val) {maxCondition = val;return self();}		
		public T itemLocation(Container val) {itemLocation = val;return self();}		
		public T balanceMult(Double val) {balanceMult = val;return self();}		
		public T itemCommands(String name) {itemCommands.add(name);return self();}				
		public StdItem build() {return new StdItem(this);}
	}
	
	public static class Builder extends Init<Builder> {
		public Builder(String name, int id) {super(name, id);}
		@Override
		protected Builder self() {return this;}
	}
	@Override
	public Creatable create() {		
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 10;
	}
	@Override
	public String getDescription() {return description;}	
	@Override
	public String getShortDescription() {return shortDescription;}
	@Override
	public String getName() {return name;}
	@Override
	public int getId() {return id;}
	@Override
	public void setContainer(Container con) {
		// TODO Auto-generated method stub		
	}	
	public Container getContainer() {return itemLocation;}
	
	public boolean hasCommand(String command) {
		if (itemCommands.contains(command)) {
			return true;
		}
		return false;
	}
	public double getPhysicalMult() {
		return physicalMult;
	}
}
