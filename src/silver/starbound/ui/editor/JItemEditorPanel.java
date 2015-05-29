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

import silver.starbound.data.Item;
import silver.starbound.data.Item.Rarity;
import silver.starbound.ui.util.FileUtil;
import silver.starbound.util.JsonUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * An editor panel for starbound items.
 * 
 * @author SilverFishCat
 *
 */
public class JItemEditorPanel extends JEditorPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8384357372021723502L;
	
	private boolean _initialized = false;
	private Item _item;
	
	private JTextField txtIcon;
	private JTextField txtName;
	private JTextField txtShortDescription;
	private JTextField txtBlueprintAdd;
	private JComboBox<Rarity> cmbxRarity;
	private JTextArea txtDescription;
	private JList<String> lstBlueprints;
	private DefaultListModel<String> dlmBlueprints;
	private JButton btnBlueprintAdd;
	private JButton btnBlueprintRemove;

	/**
	 * Create a new item editor panel.
	 * 
	 * @param file The file to edit
	 */
	public JItemEditorPanel(File file) {
		super(file);
		
		initialize();
		loadItemFromFile();
		refreshPanel();
	}
	
	/**
	 * Insert and set all the components in the panel.
	 */
	private void initialize(){
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:min"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("0dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:min"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("0dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("10dlu"),
				RowSpec.decode("10dlu"),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("top:50dlu"),
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name:");
		add(lblName, "1, 2");
		
		txtName = new JTextField();
		txtName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				onNameFieldChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				onNameFieldChanged();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				onNameFieldChanged();
			}
		});
		add(txtName, "3, 2, fill, default");
		
		JLabel lblShortDescription = new JLabel("Sub Title:");
		add(lblShortDescription, "1, 4");
		
		txtShortDescription = new JTextField();
		txtShortDescription.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				onShortDescriptionChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				onShortDescriptionChanged();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				onShortDescriptionChanged();
			}
		});
		add(txtShortDescription, "3, 4, fill, default");
		
		JLabel lblRarity = new JLabel("Rarity:");
		add(lblRarity, "5, 4");
		
		cmbxRarity = new JComboBox<Rarity>();
		for (Rarity rarity : Rarity.values()) {
			cmbxRarity.addItem(rarity);			
		}
		cmbxRarity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onRarityChanged();
			}
		});
		add(cmbxRarity, "7, 4, fill, default");
		
		JLabel lblDescription = new JLabel("Description:");
		add(lblDescription, "1, 6");
		
		txtDescription = new JTextArea();
		txtDescription.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				onDescriptionChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				onDescriptionChanged();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				onDescriptionChanged();
			}
		});
		txtDescription.setFont(txtDescription.getFont().deriveFont(12f)); //why is the text so small ._.
		txtDescription.setLineWrap(true);
		add(txtDescription, "3, 6, 5, 2");
		
		JLabel lblIcon = new JLabel("Icon:");
		add(lblIcon, "1, 9");
		
		txtIcon = new JTextField();
		txtIcon.setEditable(false);
		add(txtIcon, "3, 9, 5, 1, fill, default");
		
		JButton btnIconBrowse = new JButton("Browse");
		btnIconBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = FileUtil.selectFile(JItemEditorPanel.this, "Select icon", _item.getInventoryIconFile());
				if(selectedFile != null) {
					_item.setInventoryIconFile(selectedFile);
				}
				
				refreshInventoryIcon();
			}
		});
		add(btnIconBrowse, "9, 9, default, center");
		
		JLabel lblBlueprints = new JLabel("Blueprints:");
		add(lblBlueprints, "1, 11");
		
		txtBlueprintAdd = new JTextField();
		txtBlueprintAdd.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				onBlueprintToAddChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				onBlueprintToAddChanged();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				onBlueprintToAddChanged();
			}
		});
		add(txtBlueprintAdd, "3, 11, fill, default");
		
		btnBlueprintAdd = new JButton("Add");
		btnBlueprintAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBlueprintFromField();
			}
		});
		add(btnBlueprintAdd, "9, 11");
		
		lstBlueprints = new JList<String>();
		lstBlueprints.setVisibleRowCount(3);
		dlmBlueprints = new DefaultListModel<String>();
		lstBlueprints.setModel(dlmBlueprints);
		lstBlueprints.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				onSelectedBlueprintChanged();
			}
		});
		add(new JScrollPane(lstBlueprints), "3, 13, 1, 3, fill, fill");
		
		btnBlueprintRemove = new JButton("Remove");
		btnBlueprintRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelectedBlueprints();
			}
		});
		add(btnBlueprintRemove, "9, 13");
		
		_initialized = true;
	}

	@Override
	public void setFile(File file) {
		super.setFile(file);
		
		if(_initialized){
			refreshPanel();
		}
	}
	
	@Override
	public void save() throws IOException{
		File file = getFile();
		
		if(file != null){
			FileReader reader = new FileReader(getFile());
			JsonElement element = new JsonParser().parse(reader);
			reader.close();
			
			JsonObject resultObject;
			JsonObject valObject = (JsonObject) JsonUtil.getGsonInstance().toJsonTree(_item);
			if(element.isJsonObject()){
				resultObject = element.getAsJsonObject();
				
				for (Map.Entry<String,JsonElement> entry : valObject.entrySet()) {
					resultObject.add(entry.getKey(), entry.getValue());
				}
			}
			else{
				resultObject = valObject;
			}

			FileWriter writer = new FileWriter(getFile());
			JsonUtil.getGsonInstance().toJson(resultObject, writer);
			writer.close();
		}
	}
	/**
	 * Load the item object from the file being editted.
	 */
	public void loadItemFromFile(){
		try {
			_item = Item.loadFromFile(getFile());
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: show error dialog and disable the panel
		}
	}
	/**
	 * Add the current blueprint name in the blueprint text field into the blueprint list.
	 */
	public void addBlueprintFromField(){
		if(txtBlueprintAdd.getText() != null && !txtBlueprintAdd.getText().trim().isEmpty()) {
			dlmBlueprints.addElement(txtBlueprintAdd.getText().trim());
			txtBlueprintAdd.setText("");
			
			refreshBlueprintAddButton();
		}
	}
	/**
	 * Remove all the selected blueprints from the list.
	 */
	public void removeSelectedBlueprints(){
		if(lstBlueprints.getSelectedValue() != null) {
			for (String blueprintToRemove : lstBlueprints.getSelectedValuesList()) {
				dlmBlueprints.removeElement(blueprintToRemove);
			}
			
			refreshBlueprintRemoveButton();
		}
	}
	
	/**
	 * Called when the name field is changed.
	 */
	private void onNameFieldChanged(){
		_item.setItemName(txtName.getText());
	}
	/**
	 * Called when the short description field is changed.
	 */
	private void onShortDescriptionChanged(){
		_item.setShortDescription(txtShortDescription.getText());
	}
	/**
	 * Called when the rarity choice is changed.
	 */
	private void onRarityChanged(){
		_item.setRarity((Rarity) cmbxRarity.getSelectedItem());
	}
	/**
	 * Called when the description field is changed.
	 */
	private void onDescriptionChanged(){
		_item.setDescription(txtDescription.getText());
	}
	/**
	 * Called when the add blueprint field is changed.
	 */
	private void onBlueprintToAddChanged(){
		refreshBlueprintAddButton();
	}
	/**
	 * Called when the selected blueprints are changed.
	 */
	private void onSelectedBlueprintChanged(){
		refreshBlueprintRemoveButton();
	}

	/**
	 * Refresh the add blueprint button.
	 */
	private void refreshBlueprintAddButton(){
		btnBlueprintAdd.setEnabled(txtBlueprintAdd.getText() != null && !txtBlueprintAdd.getText().trim().isEmpty());
	}
	/**
	 * Refresh the remove blueprint button.
	 */
	private void refreshBlueprintRemoveButton(){
		btnBlueprintRemove.setEnabled(lstBlueprints.getSelectedValue() != null);
	}
	/**
	 * Refresh the components in charge of the item editing fields.
	 */
	private void refreshItemComponents(){
		txtName.setText(_item.getItemName());
		txtShortDescription.setText(_item.getShortDescription());
		cmbxRarity.setSelectedItem(_item.getRarity());
		txtDescription.setText(_item.getDescription());
		refreshInventoryIcon();
		dlmBlueprints.clear();
		for (String blueprint : _item.getBlueprintsLearnedOnPickup()) {
			dlmBlueprints.addElement(blueprint);
		}
	}
	/**
	 * Refresh the inventory icon field.
	 */
	private void refreshInventoryIcon(){
		txtIcon.setText(_item.getInventoryIcon());
	}
	/**
	 * Refresh the entire panel.
	 */
	private void refreshPanel(){
		refreshItemComponents();
		
		refreshBlueprintAddButton();
		refreshBlueprintRemoveButton();
	}
}
