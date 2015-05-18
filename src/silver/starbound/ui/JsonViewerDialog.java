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

package silver.starbound.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import javax.swing.BoxLayout;
import javax.swing.JTree;
import javax.swing.JScrollPane;

import silver.json.swing.JsonTreeCellRenderer;
import silver.json.swing.JsonTreeModel;

/**
 * A dialog displaying a JsonElement inside a JTree component.
 * 
 * @author SilverFishCat
 *
 */
public class JsonViewerDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8612592963135198030L;
	
	private final JPanel contentPanel = new JPanel();

	private JsonElement _element;
	private JTree treeJsonContainer;

	/**
	 * Create the dialog.
	 * 
	 * @param element The json element to display as the root of the JTree
	 * @wbp.parser.constructor
	 */
	public JsonViewerDialog(JsonElement element) {
		this(element, null);
	}
	/**
	 * Create the dialog.
	 * Uses the given string as the dialog's title.
	 * 
	 * @param element The json element to display as the root of the JTree
	 * @param title The dialog's title
	 */
	public JsonViewerDialog(JsonElement element, String title) {
		if(title == null)
			title = "";
		
		setTitle(title);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				treeJsonContainer = new JTree();
				treeJsonContainer.setModel(new JsonTreeModel(JsonNull.INSTANCE));
				treeJsonContainer.setCellRenderer(new JsonTreeCellRenderer());
				scrollPane.setViewportView(treeJsonContainer);
			}
		}
		
		setJsonElement(element);
	}
	
	/**
	 * Get the json element displayed in the dialog.
	 * 
	 * @return The json element displayed in the dialog
	 */
	public JsonElement getJsonElement(){
		return this._element;
	}
	/**
	 * Set the json element to be displayed in the dialog.
	 * 
	 * @param element The new json element to display in the dialog
	 */
	public void setJsonElement(JsonElement element){
		this._element = element;
		
		treeJsonContainer.setModel(new JsonTreeModel(this._element));
	}
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			JsonViewerDialog dialog = new JsonViewerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
