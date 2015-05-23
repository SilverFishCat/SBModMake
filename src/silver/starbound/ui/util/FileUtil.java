//The MIT License (MIT)
//
//Copyright (c) 2015 , SilverFishCat@GitHub
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.


package silver.starbound.ui.util;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * General file utillities.
 * 
 * @author SilverFishCat
 *
 */
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
