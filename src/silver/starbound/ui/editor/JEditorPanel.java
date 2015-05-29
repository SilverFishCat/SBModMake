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

package silver.starbound.ui.editor;

import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

/**
 * A panel for editing a starbound file.
 * 
 * @author SilverFishCat
 *
 */
public abstract class JEditorPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5945285729021767178L;
	/**
	 * A listener that listens to edit changes.
	 * 
	 * @author SilverFishCat
	 *
	 */
	public interface EditChangedListener{
		/**
		 * Called when the editor makes a possible change to the file.
		 */
		public void OnEditChanged(); // TODO: implement in class
	}
	
	private File _file;

	/**
	 * Create a new editor panel.
	 * 
	 * @param file The file to edit
	 */
	public JEditorPanel(File file) {
		super();
		
		setFile(file);
	}
	/**
	 * Create a new editor panel.
	 * 
	 * @param file The file to edit
	 * @param isDoubleBuffered Whether the panel is double buffered
	 */
	public JEditorPanel(File file, boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		
		setFile(file);
	}
	/**
	 * Create a new editor panel.
	 * 
	 * @param file The file to edit
	 * @param layout The editor's layout
	 */
	public JEditorPanel(File file, LayoutManager layout) {
		super(layout);
		
		setFile(file);
	}
	/**
	 * Create a new editor panel.
	 * 
	 * @param file The file to edit
	 * @param layout The editor's layout
	 * @param isDoubleBuffered Whether the panel is double buffered
	 */
	public JEditorPanel(File file, LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		
		setFile(file);
	}
	
	/**
	 * Set the file being edited.
	 * 
	 * @param file The file being edited
	 */
	public void setFile(File file){
		_file = file;
	}
	/**
	 * Get the file being editted.
	 * 
	 * @return The file being editted
	 */
	public File getFile(){
		return _file;
	}
	
	/**
	 * Save the edit changes into the file.
	 * 
	 * @throws IOException If there was an error saving the changes
	 */
	public abstract void save() throws IOException;
}
