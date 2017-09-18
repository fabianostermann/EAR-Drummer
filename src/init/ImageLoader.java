package init;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageLoader {
	
	private static final String IMAGE_FOLDER = "./img/"; 
	public static Map<String, ImageIcon> image = new HashMap<>(); 
	
	private static final String[] tags = {
		"delete", "save", "pause", "start", "play"
	};
	
	static {
		// write camel case, other cases will be tested (e.g. Delete instead of delete or DELETE)
		File file;
		for (String tag : tags) {
			file = new File(IMAGE_FOLDER+tag+".png");
			if (file.exists())
				image.put(tag, new ImageIcon(file.getPath()));
			else
				System.err.println("ImageLoader: " + file.getPath() + " does not exist and is not loaded.");
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
