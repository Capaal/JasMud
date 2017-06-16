package processes;

public enum ContainerErrors {
	
	QTYFULL() { 
		@Override
		public String display(String name) {
			return "That " + name + " is full.";
		}
	},
	
	STATIONARY() { // TODO NOT A CONTAINER
		@Override
		public String display(String name) {
			return "That " + name + " is a stationary item and can't be moved.";
		}
	},
	
	WRONGTYPE() {
		@Override
		public String display(String name) {
			return "That " + name + " is the wrong type for that contaner.";
		}
	},
	
	TOOHEAVY() {
		@Override
		public String display(String name) {
			return "That " + name + " is too heavy.";
		}
	},
	
	WEIGHTFULL() {
		@Override
		public String display(String name) {
			return "";
		}
		
	};	
	

	
	private ContainerErrors() {}
	
//	public void doOnCreation(Mobile currentPlayer) {currentPlayer.displayPrompt();}
	public String display(String name) {
		return "";
	}
		
}
