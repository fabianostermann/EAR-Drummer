package gui;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface LoadSaveable {

	public void loadFromFile(RandomAccessFile raf) throws IOException ;

	public void saveToFile(RandomAccessFile raf) throws IOException ;
	
}
