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
	 * @wbp.parser.constructor
	 */
	public JsonViewerDialog(JsonElement element) {
		this(element, null);
	}
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
	
	public JsonElement getJsonElement(){
		return this._element;
	}
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
