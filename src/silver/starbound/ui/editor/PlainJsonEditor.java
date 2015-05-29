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

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import silver.json.swing.JsonTreeCellRenderer;
import silver.json.swing.JsonTreeModel;
import silver.json.swing.PlainJsonTreeCellEditor;

/**
 * <p>A plain json file editor.</p>
 * 
 * <p>Uses a plain jtree to view and edit a json file.</p>
 * 
 * @author SilverFishCat
 *
 */
public class PlainJsonEditor implements FileEditor{
	@Override
	public JEditorPanel getEditorPanel(File file) {
		return new JPlainJsonEditorPanel(file);
	}
	@Override
	public boolean isExtensionMatchingEditor(String extension) {
		return false;
	}
	@Override
	public String toString() {
		return "Plain JSON";
	}
	
	/**
	 * A panel allowing plain json editing.
	 * 
	 * @author SilverFishCat
	 *
	 */
	public static class JPlainJsonEditorPanel extends JEditorPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4703882376222824924L;
		
		private JsonTreeModel _model;

		/**
		 * Create a plain json editor panel.
		 * 
		 * @param file
		 */
		public JPlainJsonEditorPanel(File file) {
			super(file);
			
			initialize();
		}
		
		/**
		 * Insert and set all the components in the panel.
		 */
		private void initialize(){
			setLayout(new BorderLayout());
			
			JTree tree = new JTree();
			
			try {
				_model = new JsonTreeModel(new JsonParser().parse(new FileReader(getFile())));
				tree.setModel(_model);
				tree.setCellRenderer(new JsonTreeCellRenderer());
				tree.setCellEditor(new PlainJsonTreeCellEditor());
				tree.setEditable(true);
			} catch (JsonIOException | JsonSyntaxException
					| FileNotFoundException e) {
				e.printStackTrace();
				
				tree.setEnabled(false);
			}
			
			add(new JScrollPane(tree));
		}

		@Override
		public void save() throws IOException {
			
		}

	}
}
