package silver.starbound.ui.util;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileUtil {
	
	/**
	 * Select a directory.
	 * Opens a dialog to allow the user to select a directory.
	 * 
	 * @return A directory selected by the user, null if user cancelled
	 */
	public static File selectDirectory(Component parentComponent){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Select folder");
		int dialogResult = fc.showOpenDialog(parentComponent);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
	/**
	 * Select a file.
	 * Opens a dialog to allow the user to select a file.
	 * 
	 * @param title The title of the dialog
	 * @return A file selected by the user, null if user cancelled
	 */
	public static File selectFile(Component parentComponent, String title){
		return selectFile(parentComponent, title, null);
	}
	/*private File selectFileFromDirectory(String title, File directory){
		return selectFile(title, directory, null);
	}*/
	/**
	 * Select a file.
	 * Opens a dialog to allow the user to select a file.
	 * 
	 * @param title The title of the dialog
	 * @param defaultFile The default file choosen
	 * @return A file selected by the user, null if user cancelled
	 */
	public static File selectFile(Component parentComponent, String title, File defaultFile){
		return selectFile(parentComponent, title, null, defaultFile);
	}
	/**
	 * Select a file.
	 * Opens a dialog to allow the user to select a file.
	 * 
	 * @param title The title of the dialog
	 * @param directory The directory to look in
	 * @param defaultFile The default file choosen
	 * @return A file selected by the user, null if user cancelled
	 */
	public static File selectFile(Component parentComponent, String title, File directory, File defaultFile){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle(title);
		if(defaultFile != null){
			fc.setSelectedFile(defaultFile);
		}
		else if(directory != null)
			fc.setCurrentDirectory(directory);
		int dialogResult = fc.showOpenDialog(parentComponent);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
	/**
	 * Create a file.
	 * 
	 * @param file The file to create
	 * @return True if file create, false otherwise
	 */
	public static boolean createFile(Component parentComponent, File file){
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Can't create file", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}
