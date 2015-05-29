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
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class JItemEditorPanel extends JEditorPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8384357372021723502L;
	
	private boolean _initialized = false;
	private Item _item;
	private File _inventoryIconFileUI;
	
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

	public JItemEditorPanel(File file) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		super(file);
		_inventoryIconFileUI = null;
		
		initialize();
		loadItemFromFile();
		refreshPanel();
	}
	
	public void initialize(){
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
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
		add(txtBlueprintAdd, "3, 11, fill, default");
		
		btnBlueprintAdd = new JButton("Add");
		btnBlueprintAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBlueprintFromField();
			}
		});
		add(btnBlueprintAdd, "9, 11");
		
		lstBlueprints = new JList<String>();
		dlmBlueprints = new DefaultListModel<String>();
		lstBlueprints.setModel(dlmBlueprints);
		lstBlueprints.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
			}
		});
		add(lstBlueprints, "3, 13, 1, 5, fill, fill");
		
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
			
			if(element.isJsonObject()){
				JsonObject object = element.getAsJsonObject();
				JsonObject valObject = (JsonObject) JsonUtil.getGsonInstance().toJsonTree(_item);
				
				for (Map.Entry<String,JsonElement> entry : valObject.entrySet()) {
					object.add(entry.getKey(), entry.getValue());
				}

				JsonWriter writer = new JsonWriter(new FileWriter(getFile()));
				JsonUtil.getGsonInstance().toJson(object, writer);
				writer.close();
			}
			
		}
	}
	public void loadItemFromFile(){
		try {
			_item = Item.loadFromFile(getFile());
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: show error dialog and disable the panel
		}
	}
	public void addBlueprintFromField(){
		if(txtBlueprintAdd.getText() != null && !txtBlueprintAdd.getText().trim().isEmpty()) {
			dlmBlueprints.addElement(txtBlueprintAdd.getText().trim());
			txtBlueprintAdd.setText("");
			
			refreshBlueprintAddButton();
		}
	}
	public void removeSelectedBlueprints(){
		if(lstBlueprints.getSelectedValue() != null) {
			for (String blueprintToRemove : lstBlueprints.getSelectedValuesList()) {
				dlmBlueprints.removeElement(blueprintToRemove);
			}
			
			refreshBlueprintRemoveButton();
		}
	}
	
	public void onNameFieldChanged(){
		_item.setItemName(txtName.getText());
	}
	public void onShortDescriptionChanged(){
		_item.setShortDescription(txtShortDescription.getText());
	}
	public void onRarityChanged(){
		_item.setRarity((Rarity) cmbxRarity.getSelectedItem());
	}
	public void onDescriptionChanged(){
		_item.setDescription(txtDescription.getText());
	}
	public void onInventoryItemChanged(){
		_item.setInventoryIconFile(_inventoryIconFileUI);
	}
	public void onBlueprintToAddChanged(){
		refreshBlueprintAddButton();
	}
	public void onSelectedBlueprintChanged(){
		refreshBlueprintRemoveButton();
	}
	public void onBlueprintsChanged(){
		List<String> blueprints = new ArrayList<String>();
		for (int i = 0; i < dlmBlueprints.getSize(); i++) {
			blueprints.add(dlmBlueprints.getElementAt(i));
		}
		_item.setBlueprintsLearnedOnPickup(blueprints);
	}

	public void refreshBlueprintAddButton(){
		btnBlueprintAdd.setEnabled(txtBlueprintAdd.getText() != null && !txtBlueprintAdd.getText().trim().isEmpty());
	}
	public void refreshBlueprintRemoveButton(){
		btnBlueprintRemove.setEnabled(lstBlueprints.getSelectedValue() != null);
	}
	public void refreshItemComponents(){
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
	public void refreshInventoryIcon(){
		txtIcon.setText(_item.getInventoryIcon());
	}
	public void refreshPanel(){
		refreshItemComponents();
		
		refreshBlueprintAddButton();
		refreshBlueprintRemoveButton();
	}
}
