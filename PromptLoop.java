public class PromptLoop extends Thread {
	
	protected SendMessage sendBack;
	protected Player currentPlayer;
	
	public PromptLoop(SendMessage sendBack, Player currentPlayer) {
		this.sendBack = sendBack;
		this.currentPlayer = currentPlayer;
	}
	
	public void run() {
		while (true) {
			try {
			Thread.sleep(10);
			} catch(InterruptedException ie) {
			}
			if (currentPlayer.currentHp <= 0) {
				sendBack.printMessage("Oh dear, you are dead.");
				if (currentPlayer.experience > 0) {
					currentPlayer.experience -= 5;
					if (currentPlayer.experience < 0) {
						currentPlayer.experience = 0;
					}
				}
				if (currentPlayer.experience < WorldServer.Levels[currentPlayer.level-1]) {
					currentPlayer.level -= 1;
					sendBack.printMessage("You have lost a level.");
					currentPlayer.maxHp = (10 + currentPlayer.level * 10);
				}
				currentPlayer.currentHp = currentPlayer.maxHp;
				currentPlayer.shield = false;
			}
		}
	}
}