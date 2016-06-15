package genetic;

import java.util.ArrayList;
import java.util.Observable;

public class ProgressObservable extends Observable {
	
	public static Object PROGRESS_CHANGED = new Object();
	public static Object STRINGLIST_CHANGED = new Object();
	public static Object ALARM_CHANGED = new Object();
	
	private int progressValue;
	private String progressString;
	
	private boolean alarm = false;
	
	private ArrayList<String> stringList;

	protected void setStringList(ArrayList<String> list) {
		this.stringList = list;
		
		setChanged();
		notifyObservers(STRINGLIST_CHANGED);
	}
	
	public ArrayList<String> getStringList() {
		return this.stringList;
	}
	
	public int getProgressValue() {
		return this.progressValue;
	}
	
	public void alarm(boolean b) {
		this.alarm = b;
		
		setChanged();
		notifyObservers(ALARM_CHANGED);
	}
	
	public boolean isAlarm() {
		return this.alarm;
	}
	
	protected void setProgress(int value, String string) {
		this.progressValue = value;
		this.progressString = string;
		
		setChanged();
		notifyObservers(PROGRESS_CHANGED);
	}
	
	protected void setProgressValue(int value) {
		this.progressValue = value;
		
		setChanged();
		notifyObservers(PROGRESS_CHANGED);
	}
	
	public String getProgressString() {
		return this.progressString;
	}
	
	protected void setProgressString(String string) {
		this.progressString = string;
		
		setChanged();
		notifyObservers(PROGRESS_CHANGED);
	}
	
}
