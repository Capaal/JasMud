package processes;
public class DefenceLength extends Thread {
	
	protected SendMessage sendBack;
	protected Player currentPlayer;
	protected String defence;
	protected int time;
	protected String msg;
	
	public DefenceLength(Player currentPlayer, SendMessage sendBack, String defence, int time, String msg) {
		this.currentPlayer = currentPlayer;
		this.sendBack = sendBack;
		this.defence = defence;
		this.time = time;
		this.msg = msg;
	}
	
	public void run() {
		try {
			currentPlayer.startDefence(defence);
			Thread.sleep(time);
			currentPlayer.endDefence(defence);
			sendBack.printSpace();
			sendBack.printMessage(msg);
			String balance = "b";
			if (currentPlayer.balance == false) {
				balance = "-";
			}
			sendBack.printMessageLine(currentPlayer.currentHp + "/" + currentPlayer.maxHp + " " + balance + ": ");
		} catch (InterruptedException io) {
		}
	}
}