package init;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageLoader {
	
	private static final String IMAGE_FOLDER = "./img/"; 
	public static Map<String, ImageIcon> image = new HashMap<>(); 
	
	static {
		// write camel case, other cases will be tested (e.g. Delete instead of delete or DELETE)
		image.put("Delete", new ImageIcon(IMAGE_FOLDER+"delete.png"));
	}
	
	public static ImageIcon getImageIcon(String text) {
		if (image.containsKey(text))
			return image.get(text);
		if (image.containsKey(text.toLowerCase()))
			return image.get(text.toLowerCase());
		if (image.containsKey(text.toUpperCase()))
			return image.get(text.toUpperCase());
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
