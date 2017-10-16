package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageLoader {
	
	private static final String IMAGE_FOLDER = "./img/"; 
	private static final String MAP_FILENAME = "imgmap";
	
	private static final String COMMENT_CHAR = "#";
	
	public static Map<String, ImageIcon> image = new HashMap<>();
	
	static {
		List<String> labels = new LinkedList<String>();
		File mapFile = new File(IMAGE_FOLDER + MAP_FILENAME);
		try {
			RandomAccessFile raf = new RandomAccessFile(mapFile, "r");
			String line;
			while ((line = raf.readLine()) != null) {
				line = line.replaceAll(" ", "");
				if (line.contains(COMMENT_CHAR) || line.isEmpty())
					continue;
				labels.add(line);
			}
		} catch (Exception e) {
			System.err.println("Could not load map file: "+MAP_FILENAME);
			labels.clear();
		}
		
		File labelFile;
		for (String label : labels) {
			String[] labelArray = label.split("=");
			if (labelArray.length < 2)
				System.err.println("Label is not interpretable, skipped: "+label);
			else {
				labelFile = new File(IMAGE_FOLDER+labelArray[1]);
				if (labelFile.exists())
					image.put(labelArray[0], new ImageIcon(labelFile.getPath()));
				else
					System.err.println("ImageLoader: " + labelFile.getPath() + " does not exist and is not loaded.");
			}
		}
	}
	
	public static ImageIcon getImageIcon(String text) {
		if (image.containsKey(text.toLowerCase()))
			return image.get(text.toLowerCase());
		return null;
	}
	
	public static JButton createButton(String text) {
		ImageIcon icon = getImageIcon(text);
		if (icon != null)
			return new JButton(icon);
		else
			return new JButton(text);
	}
	
}
