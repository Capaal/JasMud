package Quests;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.ItemBuilder;
import processes.Location;
import processes.MobileBuilder;
import processes.MobileDecorator;
import processes.Type;
import processes.WorldServer;

public class FarmerQuest extends Quest {
	
	private String bondedMobileName = "farmerjames";
	private int questLocationID = 4;
	private Mobile bondedMobile;
	
	public FarmerQuest() {
		beginningState = new stateOne(this);
		currentState = beginningState;
		
	}
	
	@Override
	public boolean stateRequiresItem(Holdable item, Mobile mobile) {
		return currentState.requiresItem(item, mobile);
	}
	
	protected boolean testRequirementsMet() {
		questLocation = WorldServer.gameState.viewLocations().get(questLocationID);
		bondedMobile = (Mobile) questLocation.getHoldableFromString(bondedMobileName);
		if (bondedMobile != null && !bondedMobile.isDead()) {
			return true;
		}
		return false;
	}
	
	private class stateOne extends State {		
		public stateOne(Quest quest) {
			super(quest);
		}
		@Override protected void runGreets() {sayHere("Farmer James say: Hello! Would you be willing to listen to my plight? (NOD to continue)");}
		@Override protected void runNods() {bondedQuest.progressQuest(new stateTwo(bondedQuest));}
		@Override protected void runNewState() {}
		@Override protected void runGives() {}
	}
	
	private class stateTwo extends State {
		
		public stateTwo(Quest quest) {
			super(quest);
		}
		@Override protected void runGreets() {
			sayHere("CRASH, BANG, CRACK");
			sayHere("Farmer James say: GAHH, did you hear that? (Nod to continue)");}
		@Override protected void runNods() {bondedQuest.progressQuest(new stateThree(bondedQuest));}
		@Override protected void runNewState() {
			sayHere("Farmer James say: Oh thank you! Yes, well, just the other day I heard a crash and a bang and saw someone causing a ruckus out by the barn.");
			sayHere("CRASH, BANG, CRACK");
			sayHere("Farmer James say: GAHH, did you hear that? (Nod to continue)");
		}
		@Override protected void runGives() {}
	}
	
	private class stateThree extends State {
		
		MobileBuilder questSkelly;
		ItemBuilder skeletonBody;
		
		public stateThree(Quest quest) {
			super(quest);
			createSkeletonBody();
			createSkeleton();
		}
		
		private void createSkeleton() {
			questSkelly = new MobileBuilder();
			questSkelly.setId(200);
			questSkelly.addSkillBook(WorldServer.gameState.getBook(1));
			questSkelly.addDecorator(MobileDecorator.DecoratorType.CHASING);
			questSkelly.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
			questSkelly.setLocation(questLocation);
			questSkelly.setName("Skeleton");
			questSkelly.dropOnDeath(skeletonBody);
			questSkelly.setDescription("A mostly fleshless humanoid.");
			questSkelly.setLoadOnStartUp(false);			
		}
		
		private void createSkeletonBody() {
			skeletonBody = new ItemBuilder();	
			skeletonBody.setId(150);
			skeletonBody.setName("skeleton");
			skeletonBody.setDescription("It's a dead skeleton!");	
		}
		
		@Override protected void runGreets() {}
		@Override protected void runNods() {}
		@Override protected void runNewState() { // SHOULD PROBABLY LOAD WHEN THATS A THING
			questSkelly.complete();
			Mobile finishedSkelly = (Mobile) questLocation.getHoldableFromString("skeleton200");
			finishedSkelly.informLastAggressor(bondedMobile);
			finishedSkelly.takeDamage(Type.SHARP, 0); // Required to trigger AGGRESSIVE decorator to start attacking.
			sayHere("Farmer James says: Ahhhh, help me!");
		}
		@Override protected void runGives() {
			sayHere("Ahhh, fantastic! I should really finish this quest!");
			// Return to start? Or delay until start
			// say things for saving me, please have a small reward
			// drop money or something and give xp (OH GOD MONEY AND XP)
		}
		@Override public boolean requiresItem(Holdable item, Mobile mobile) {
			if (item.getName().equals(skeletonBody.getName()) && item.getId() == skeletonBody.getId() 
					&& mobile.equals(bondedMobile)) {
				return true;
			}
			return false;
		}
	}
}
