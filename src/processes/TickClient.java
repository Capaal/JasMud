package processes;

import interfaces.Mobile;
import interfaces.Tickable;

public class TickClient extends Thread {
	
	
	
	/* 
	 * I want to be able to change TIME on a whim, if I feel like it is happening too rarely for something like balance.
	 * But I also want to be able to make skills with an exact length of time, like 4 seconds.
	 * So Those skills need to be able to define how many ticks they care about without caring how often the system ticks.
	 * 
	 * We could just choose a arbitrary value, like 1000 milliseconds (1 second) and then normalize by that.
	 * So, if TIME = 1000 then 1000 / 1000 = 1, so 1 tick?
	 * 500 / 1000 = .5 but then it can't be an int, and is this really normalized?
	 * Well, if a skill claims it wants 4 ticks (4 seconds) then at 500 ms per tick it'll take 8 ticks, like it should before
	 * it sees the ticks where it wants to be.
	 * but if I want 350 / 1000 = .35 thats 11.42857 ticks or something, what happens if we go over? we're losing accuracy for skills.
	 * Plus, it makes them have to be more flexible for what is a valid time to trigger, but we'd only lose... 150 ms.
	 * 
	 * Lets just try this idea for now, setting balance to trigger at 250, and hopefully thats not too often.
	 * 
	 * But, do we want a ticktimer per client?
	 */
	
	private final long ONESECONDNANO = 1000000000;
	private long lastLoopTime = System.nanoTime();
	private final int TICKSPEED = 4; // Ticks per second	
	private Mobile currentPlayer; // Each mobile has their own tick time thread, overkill?
	private boolean isRunning = true;
	
	
	long howOften;
	Tickable registeredTickable;
	public TickClient(long howOften, Tickable registeredTickable) {
		this.howOften = howOften;
		this.registeredTickable = registeredTickable;
	//	super();
	//	this.currentPlayer = currentPlayer;
	}
	
	public void run() {
		while (isRunning) {
			long startTime = System.nanoTime();
			try {
				Thread.sleep(howOften);
				} catch(InterruptedException ie) {
				}
			long endTime = System.nanoTime();
			long timePassed = endTime - startTime / 1000000;
			registeredTickable.tick(timePassed);
		//	tickCount = tickCount + TIME / 1000; // Tick now represents seconds.
			//currentPlayer.runTickEffects();
		}
		
	}
	
//	public long getTick() {
//		return tickCount;
//	}
	
	/* public void runNTimes(Runnable task, int maxRunCount, long period, TimeUnit unit, ScheduledExecutor executor) {
        new FixedExecutionRunnable(task, maxRunCount).runNTimes(period, unit, exeutor);
    }

    class FixedExecutionRunnable implements Runnable {
        private final AtomicInteger runCount = new AtomicInteger();
        private final Runnable delegate;
        private volatile ScheduledFuture<?> self;
        private final int maxRunCount;

        public FixedExecutionRunnable(Runnable delegate, int maxRunCount) {
            this.delegate = delegate;
            this.maxRunCount = maxRunCount;
        }

        public void run() {
            delegate.run();
            if(runCount.incrementAndGet() == maxRunCount) {
                self.cancel(false);
            }
        }

        public void runNTimes(ScheduledExecutorService executor, long period, TimeUnit unit) {
            self = executor.scheduleAtFixedRate(this, 0, period, unit);
        }
    }*/
}
