package silver.starbound.ui.editor;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
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

public class PlainJsonEditor implements FileEditor{
	@Override
	public JEditorPanel getEditorPanel(File file) {
		return new JPlainJsonEditor(file);
	}
	@Override
	public boolean isExtensionMatchingEditor(String extension) {
		return false;
	}
	@Override
	public String toString() {
		return "Plain JSON";
	}
	
	public static class JPlainJsonEditor extends JEditorPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4703882376222824924L;
		
		private JsonTreeModel _model;

		public JPlainJsonEditor(File file) {
			super(file);
			
			initialize();
		}

		public JPlainJsonEditor(File file, boolean isDoubleBuffered) {
			super(file, isDoubleBuffered);
			
			initialize();
		}

		public JPlainJsonEditor(File file, LayoutManager layout,
				boolean isDoubleBuffered) {
			super(file, layout, isDoubleBuffered);
			
			initialize();
		}

		public JPlainJsonEditor(File file, LayoutManager layout) {
			super(file, layout);
			
			initialize();
		}
		
		public void initialize(){
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
