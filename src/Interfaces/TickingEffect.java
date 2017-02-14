package interfaces;

public abstract class TickingEffect implements Comparable<TickingEffect>, Runnable {

	@Override
	public abstract void run();	
	
	public abstract void doOnCreation();
	
	public abstract void doOnDestruction();
	
	@Override
	public int compareTo(TickingEffect other) {
		if (getClass() == other.getClass()) {
			return 0;
		}
		return this.toString().compareTo(other.toString());
	}
}
