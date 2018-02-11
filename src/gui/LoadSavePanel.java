package gui;

import init.ImageLoader;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoadSavePanel extends JPanel {

	private final String TOP_FOLDER = "config";
	private final String SAVE_FOLDER;
	private final String DEFAULT_FILENAME = "default";
	
	private final String defaultSaveName;
	
	private JMenu loadMenu = new JMenu("Load");
	private JMenu saveMenu = new JMenu("Save");
	private JMenu deleteMenu = new JMenu("Delete");
	private JTextField infoLabel = new JTextField(30);
	
	private final LoadSaveable parent;
	
	/** holds all instances of this class
	 * (for loading defaults after startup) */
	private static final List<LoadSavePanel> memento = new LinkedList<>();

	public static void loadAllDefaultFiles() {
		for (LoadSavePanel lsp : memento)
			lsp.loadDefaultFile();
	}
	
	public void loadDefaultFile() {
		loadFile(new File(SAVE_FOLDER+DEFAULT_FILENAME));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.loadMenu.setEnabled(enabled);
		this.saveMenu.setEnabled(enabled);
		this.deleteMenu.setEnabled(enabled);
	}
	
	public LoadSavePanel(LoadSaveable loadSaveable, String saveFolder, String pDefaultSaveName) {
		super(new FlowLayout(FlowLayout.LEFT));
		
		memento.add(this);
		this.parent = loadSaveable;
		
		final String defaultSaveName = pDefaultSaveName;
		this.defaultSaveName = defaultSaveName;
		
		File topFolder = new File(TOP_FOLDER);
		if (!topFolder.exists())
			topFolder.mkdir();
		this.SAVE_FOLDER = "./"+TOP_FOLDER+"/"+saveFolder+"/";
		
		// TODO generate an unused default name number
//		saveTagField = new JTextField(defaultSaveName, 15);
		
		JMenuBar loadMenuBar = new JMenuBar();
		ImageIcon loadMenuIcon;
		if ((loadMenuIcon = ImageLoader.getImageIcon(loadMenu.getText())) != null) {
			loadMenu.setText(null);
			loadMenu.setIcon(loadMenuIcon);
		}
		loadMenuBar.add(loadMenu);
		
		JMenuBar saveMenuBar = new JMenuBar();
		ImageIcon saveMenuIcon;
		if ((saveMenuIcon = ImageLoader.getImageIcon(saveMenu.getText())) != null) {
			saveMenu.setText(null);
			saveMenu.setIcon(saveMenuIcon);
		}
		saveMenuBar.add(saveMenu);
		
//		saveMenu.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				String filename = (String) JOptionPane.showInputDialog(saveMenu,
//						"Filename to save:", "Save", JOptionPane.QUESTION_MESSAGE,
//						ImageLoader.getImageIcon("save"), null, defaultSaveName);
//				
//				if(filename == null || filename.isEmpty()) {
//					infoLabel.setText("Filename is empty.");
//					return;
//				}
//				File saveFile = new File(SAVE_FOLDER + filename);
//				if (saveFile.exists()) {
//					if (0 == JOptionPane.showConfirmDialog(saveMenu,
//							"Sure you want to overwrite '"+saveFile.getName()+"'?",
//							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
//						infoLabel.setText(saveFile.getName()+" overwritten.");
//					else {
//						infoLabel.setText(saveFile.getName()+" not saved.");
//						return;
//					}
//				}
//				else
//					infoLabel.setText(saveFile.getName()+" saved.");
//				
//				try {
//					RandomAccessFile raf = new RandomAccessFile(saveFile, "rw");
//					raf.setLength(0);
//					parent.saveToFile(raf);
//					raf.close();
//				} catch (Exception ex) { ex.printStackTrace(); infoLabel.setText("Error:"+ex.getMessage()); }
//				updateAllMenus();
//			}
//		});
		
		JMenuBar deleteMenuBar = new JMenuBar();
		ImageIcon deleteMenuIcon;
		if ((deleteMenuIcon = ImageLoader.getImageIcon(deleteMenu.getText())) != null) {
			deleteMenu.setText(null);
			deleteMenu.setIcon(deleteMenuIcon);
		}
		deleteMenuBar.add(deleteMenu);
		
		infoLabel.setEditable(false);
		updateAllMenus();
		
		this.add(loadMenuBar);
		this.add(saveMenuBar);
		this.add(deleteMenuBar);
		this.add(infoLabel);
	}
	
	private void updateAllMenus() {
		updateLoadMenu();
		updateSaveMenu();
		updateDeleteMenu();
	}

	private void updateLoadMenu() {
		
		loadMenu.removeAll();
		
		File saveFolder = new File(SAVE_FOLDER);
		if (!saveFolder.exists())
			saveFolder.mkdir();
		for (File file : saveFolder.listFiles()) {
			JMenuItem item = new JMenuItem(file.getName());
			final File loadFile = file;
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loadFile(loadFile);
					updateAllMenus();
				}
			});
			loadMenu.add(item);
		}
		
		loadMenu.addSeparator();
		JMenuItem item = new JMenuItem("update list");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateAllMenus();
			}
		});
		loadMenu.add(item);
	}
	
	private void updateSaveMenu() {
		
		saveMenu.removeAll();
		
		File saveFolder = new File(SAVE_FOLDER);
		if (!saveFolder.exists())
			saveFolder.mkdir();
		
		saveMenu.addSeparator();
		JMenuItem newFileItem = new JMenuItem("new file");
		newFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveNewFile();
				updateAllMenus();
			}
		});
		saveMenu.add(newFileItem);
		
		saveMenu.addSeparator();
		for (File file : saveFolder.listFiles()) {
			JMenuItem item = new JMenuItem(file.getName());
			final File saveFile = file;
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					saveExistingFile(saveFile);
					updateAllMenus();
				}
			});
			saveMenu.add(item);
		}
		
		saveMenu.addSeparator();
		JMenuItem updateItem = new JMenuItem("update list");
		updateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateAllMenus();
			}
		});
		saveMenu.add(updateItem);
	}
	
	private void updateDeleteMenu() {
		
		deleteMenu.removeAll();
		
		File saveFolder = new File(SAVE_FOLDER);
		if (!saveFolder.exists())
			saveFolder.mkdir();
		for (File file : saveFolder.listFiles()) {
			JMenuItem item = new JMenuItem(file.getName());
			final File deleteFile = file;
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					deleteFile(deleteFile);
					updateAllMenus();
				}
			});
			deleteMenu.add(item);
		}
		
		deleteMenu.addSeparator();
		JMenuItem item = new JMenuItem("update list");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateAllMenus();
			}
		});
		deleteMenu.add(item);
	}
	
	private void loadFile(File loadFile) {
		if (loadFile.exists()) {
			try {
				RandomAccessFile raf = new RandomAccessFile(loadFile, "r");
				parent.loadFromFile(raf);
				raf.close();
				infoLabel.setText(loadFile.getName() + " loaded.");
			} catch (Exception ex) { ex.printStackTrace(); infoLabel.setText("Error:"+ex.getMessage()); }
		} else {
			infoLabel.setText("File '"+loadFile.getName()+"' does not exist.");
		}
	}
	
	private void saveExistingFile(File saveFile) {
		if (saveFile.exists()) {
			try {
//				if (0 == JOptionPane.showConfirmDialog(saveMenu,
//						"Sure you want to overwrite '"+saveFile.getName()+"'?",
//						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
//					infoLabel.setText(saveFile.getName()+" overwritten.");
//				else {
//					infoLabel.setText(saveFile.getName()+" not saved.");
//					return;
//				}
				RandomAccessFile raf = new RandomAccessFile(saveFile, "rw");
				parent.saveToFile(raf);
				raf.close();
				infoLabel.setText(saveFile.getName() + " saved.");
			} catch (Exception ex) { ex.printStackTrace(); infoLabel.setText("Error:"+ex.getMessage()); }
		} else {
			infoLabel.setText("File '"+saveFile.getName()+"' does not exist.");
		}
	}
	
	private void saveNewFile() {
		String filename = (String) JOptionPane.showInputDialog(saveMenu,
				"Filename to save:", "Save", JOptionPane.QUESTION_MESSAGE,
				ImageLoader.getImageIcon("save"), null, defaultSaveName);
		
		if(filename == null || filename.isEmpty()) {
			infoLabel.setText("Filename is empty.");
			return;
		}
		File saveFile = new File(SAVE_FOLDER + filename);
		if (saveFile.exists()) {
			if (0 == JOptionPane.showConfirmDialog(saveMenu,
					"Sure you want to overwrite '"+saveFile.getName()+"'?",
					"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
				infoLabel.setText(saveFile.getName()+" overwritten.");
			else {
				infoLabel.setText(saveFile.getName()+" not saved.");
				return;
			}
		}
		else
			infoLabel.setText(saveFile.getName()+" saved.");
		
		try {
			RandomAccessFile raf = new RandomAccessFile(saveFile, "rw");
			raf.setLength(0);
			parent.saveToFile(raf);
			raf.close();
		} catch (Exception ex) { ex.printStackTrace(); infoLabel.setText("Error:"+ex.getMessage()); }
		updateAllMenus();
	}
	
	private void deleteFile(File deleteFile) {
		if (deleteFile.exists()) {
			try {
				if (0 != JOptionPane.showConfirmDialog(deleteMenu,
						"Sure you want to completely delete '"+deleteFile.getName()+"'?",
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
					infoLabel.setText(deleteFile.getName()+" not deleted.");
					return;
				}
				if (deleteFile.delete())
					infoLabel.setText(deleteFile.getName() + " deleted.");
				else
					infoLabel.setText(deleteFile.getName() + " cannot be deleted.");
			} catch (Exception ex) { ex.printStackTrace(); infoLabel.setText("Error:"+ex.getMessage()); }
		} else {
			infoLabel.setText("File '"+deleteFile.getName()+"' does not exist.");
		}
	}
}
