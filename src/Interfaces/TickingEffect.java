package interfaces;

public interface TickingEffect extends Effect, Runnable {

	@Override
	public void run();
}
