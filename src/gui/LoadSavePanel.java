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

public class LoadSavePanel extends JPanel {

	private static final long serialVersionUID = 1985141919514178009L;

	private final String TOP_FOLDER = "config";
	private final String SAVE_FOLDER;
	private final String DEFAULT_FILENAME = "default";
	
	private JMenuBar menuBar;
	private JMenu loadMenu = new JMenu("Load");
	private JTextField saveTagField;
	private JButton saveButton = ImageLoader.createButton("Save");
	private JButton deleteButton = ImageLoader.createButton("Delete");
	private JTextField infoLabel = new JTextField(30);
	
	private final LoadSaveable parent;
	
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
		this.menuBar.setEnabled(enabled);
		this.loadMenu.setEnabled(enabled);
		this.saveButton.setEnabled(enabled);
		this.deleteButton.setEnabled(enabled);
	}
	
	public LoadSavePanel(LoadSaveable loadSaveable, String saveFolder, String defaultSaveName) {
		super(new FlowLayout(FlowLayout.LEFT));
		
		memento.add(this);
		this.parent = loadSaveable;
		
		File topFolder = new File(TOP_FOLDER);
		if (!topFolder.exists())
			topFolder.mkdir();
		this.SAVE_FOLDER = "./"+TOP_FOLDER+"/"+saveFolder+"/";
		
		// TODO generate an unused defaultname number
		saveTagField = new JTextField(defaultSaveName, 15);
		
		menuBar = new JMenuBar();
		ImageIcon icon;
		if ((icon = ImageLoader.getImageIcon(loadMenu.getText())) != null) {
			loadMenu.setText(null);
			loadMenu.setIcon(icon);
		}
		menuBar.add(loadMenu);
		updateLoadMenu();
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filename = saveTagField.getText().trim();
				if(filename.isEmpty()) {
					infoLabel.setText("Filename is empty.");
					return;
				}
				File saveFile = new File(SAVE_FOLDER + filename);
				if (saveFile.exists()) {
					if (0 == JOptionPane.showConfirmDialog(null,
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
				updateLoadMenu();
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filename = saveTagField.getText().trim();
				if (filename.isEmpty()) {
					infoLabel.setText("Filename is empty.");
					return;
				}
				File deleteFile = new File(SAVE_FOLDER + filename);
				if (deleteFile.exists()) {
					if (0 == JOptionPane.showConfirmDialog(null,
							"Sure you want to delete '"+deleteFile.getName()+"'?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
						deleteFile.delete();
						infoLabel.setText(deleteFile.getName()+" deleted.");
						updateLoadMenu();
					}
					else
						infoLabel.setText(deleteFile.getName()+" not deleted.");
				}
				else
					infoLabel.setText(deleteFile.getName()+" does not exist.");
			}
		});
		
		infoLabel.setEditable(false);
		
		this.add(menuBar);
		this.add(saveTagField);
		this.add(saveButton);
		this.add(deleteButton);
		this.add(infoLabel);
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
					updateLoadMenu();
				}
			});
			loadMenu.add(item);
		}
		
		loadMenu.addSeparator();
		JMenuItem item = new JMenuItem("update list");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLoadMenu();
			}
		});
		loadMenu.add(item);
	}
	
	private void loadFile(File loadFile) {
		if (loadFile.exists()) {
			try {
				RandomAccessFile raf = new RandomAccessFile(loadFile, "r");
				parent.loadFromFile(raf);
				raf.close();
				saveTagField.setText(loadFile.getName());
				infoLabel.setText(loadFile.getName() + " loaded.");
			} catch (Exception ex) { ex.printStackTrace(); infoLabel.setText("Error:"+ex.getMessage()); }
		} else {
			infoLabel.setText("File '"+loadFile.getName()+"' does not exist.");
		}
	}
}
