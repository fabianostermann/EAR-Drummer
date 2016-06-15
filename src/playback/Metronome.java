package playback;

import java.util.Vector;

public class Metronome implements Runnable{

	private Vector<MetronomeListener> listenerList = new Vector<>();
	
	private int ticks;
	private int tpm = 200;
	private float shuffle = 0.5f;
	
	private int durationOnBeat;
	private int durationOffBeat;
	private boolean offBeat;
	
	private int tick = 0;
	
	private boolean running = false;
	
	public Metronome(int ticks) {
		this.ticks = ticks;
		recalculateDuration();
	}
	
	public Metronome(int ticks, int tpm, float shuffle) {
		this.ticks = ticks;
		this.tpm = tpm;
		this.shuffle = shuffle;
		recalculateDuration();
	}
	
	public void start() {
		if (!isRunning()) {
			tick = 0;
			offBeat = false;
			running = true;
			new Thread(this).start();
		}
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		
		long startTime, sleepTime, deltaTime;
		
		while(running) {
			startTime = System.currentTimeMillis();
			
			//inform listeners
			for (MetronomeListener listener : this.listenerList) {
				listener.tick(this);
			}
			
			//set time to sleep
			if (offBeat) {
				sleepTime = durationOffBeat;
				offBeat = false;
			} else {
				sleepTime = durationOnBeat;
				offBeat = true;
			}
			
			try { //sleep
				deltaTime = System.currentTimeMillis()-startTime;
				if (sleepTime - deltaTime > 0)
					Thread.sleep(sleepTime - deltaTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			increaseTick();
		}
	}
	
	private void increaseTick() {
		this.tick++;
		if (this.tick >= this.ticks)
			this.tick = 0;
	}
	
	private void recalculateDuration() {
		this.durationOnBeat = (int)((float)(2 * shuffle * 60000) / tpm);
		this.durationOffBeat = (int)((float)(2 * (1 - shuffle) * 60000) / tpm);
	}

	public void addMetronomeListener(MetronomeListener listener) {
		this.listenerList.add(listener);
	}

	public int getTick() {
		return tick;
	}
	public int getTicks() {
		return ticks;
	}

	public int getTpm() {
		return tpm;
	}

	public void setTpm(int tpm) {
		this.tpm = tpm;
		this.recalculateDuration();
	}

	public float getShuffle() {
		return shuffle;
	}

	public void setShuffle(float shuffle) {
		this.shuffle = shuffle;
		this.recalculateDuration();
	}
	
	public boolean isRunning() {
		return running;
	}
	

}
