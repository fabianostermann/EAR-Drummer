package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoadSavePanel extends JPanel {

	private static final long serialVersionUID = 1985141919514178009L;

	private final String SAVE_FOLDER;
	
	private JMenuBar menuBar;
	private JMenu loadMenu;
	private JTextField saveTagField = new JTextField("MyDrummer", 15);
	private JButton saveButton = new JButton("Save");
	private JButton deleteButton = new JButton("Delete");
	private JTextField infoLabel = new JTextField(30);
	
	private final LoadSaveable parent;
	
	public LoadSavePanel(LoadSaveable loadSaveable, String saveFolder) {
		super(new FlowLayout(FlowLayout.LEFT));
		
		this.parent = loadSaveable;
		this.SAVE_FOLDER = saveFolder;
		
		menuBar = new JMenuBar();
		loadMenu = new JMenu("Load");
		menuBar.add(loadMenu);
		updateLoadMenu();
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File saveFile = new File(SAVE_FOLDER + saveTagField.getText().trim());
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
				} catch (IOException ioe) { ioe.printStackTrace(); }
				updateLoadMenu();
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File deleteFile = new File(SAVE_FOLDER + saveTagField.getText().trim());
				if (deleteFile.exists()) {
					if (0 == JOptionPane.showConfirmDialog(null,
							"Sure you want to delete '"+deleteFile.getName()+"'?",
							"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
						infoLabel.setText(deleteFile.getName()+" deleted.");
						deleteFile.delete();
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
					if (loadFile.exists()) {
						try {
							RandomAccessFile raf = new RandomAccessFile(loadFile, "r");
							parent.loadFromFile(raf);
							raf.close();
						} catch (IOException ioe) { ioe.printStackTrace(); }
						saveTagField.setText(loadFile.getName());
					}
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
	

	
}
