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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import silver.starbound.data.Mod;
import silver.starbound.data.Settings;
import silver.starbound.ui.util.FileUtil;
import silver.starbound.util.PathUtil;

import com.google.gson.JsonIOException;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * A mod creation dialog.
 * 
 * @author SilverFishCat
 *
 */
public class ModCreationDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1892045175812091947L;
	
	/**
	 * A dialog result listener for the mod creation dialog.
	 * 
	 * @author SilverFishCat
	 *
	 */
	public static interface DialogResultListener{
		public void onModCreated(Mod mod);
		
	}
	
	private Mod _mod;
	private DialogResultListener _listener;
	
	private JTextField txtModName;
	private JTextField txtModFolder;
	private JCheckBox chckbxModFolderAutomatic;
	private JButton btnSelectModFolder;
	private JTextField txtModinfoFilename;
	private JCheckBox chckbxModinfoFolderAutomatic;
	private JPanel pnlDetails;
	private JPanel pnlButtons;
	private JButton btnCreate;
	private JButton btnCancel;

	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 * 
	 * @param listener The listener that will the receive the result of the edit.
	 */
	public ModCreationDialog(DialogResultListener listener) {
		this(new Mod(), listener);
	}
	
	/**
	 * Create a mod creation dialog, editing an existing mod object.
	 * 
	 * @param mod The mod to edit
	 * @param listener
	 */
	public ModCreationDialog(Mod mod, DialogResultListener listener){
		initialize();
		
		if(mod == null)
			mod = new Mod();
		_mod = mod;
		
		if(listener == null)
			throw new NullPointerException("Listener can not be null");
		_listener = listener;
		
		chckbxModFolderAutomatic.setSelected(true);
		chckbxModinfoFolderAutomatic.setSelected(true);
		refreshDialog();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Create mod");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnlDetails = new JPanel();
		getContentPane().add(pnlDetails);
		pnlDetails.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("center:default"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
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
		
		JLabel lblModName = new JLabel("Mod name:");
		pnlDetails.add(lblModName, "2, 2, fill, center");
		
		txtModName = new JTextField();
		pnlDetails.add(txtModName, "4, 2, fill, center");
		txtModName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				onModNameChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				onModNameChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				onModNameChanged();
			}
		});
		txtModName.setColumns(10);
		
		JLabel lblFolder = new JLabel("Folder:");
		pnlDetails.add(lblFolder, "2, 4, left, center");
		
		txtModFolder = new JTextField();
		pnlDetails.add(txtModFolder, "4, 4, fill, center");
		txtModFolder.setEditable(false);
		txtModFolder.setColumns(30);
		
		btnSelectModFolder = new JButton("Browse");
		pnlDetails.add(btnSelectModFolder, "6, 4, fill, center");
		
		chckbxModFolderAutomatic = new JCheckBox("Automatic");
		pnlDetails.add(chckbxModFolderAutomatic, "4, 6, fill, center");
		
		JLabel lblModinfoFilename = new JLabel("Modinfo filename:");
		pnlDetails.add(lblModinfoFilename, "2, 8, left, center");
		
		txtModinfoFilename = new JTextField();
		pnlDetails.add(txtModinfoFilename, "4, 8, fill, center");
		txtModinfoFilename.getDocument().addDocumentListener(new DocumentListener() {			
			@Override
			public void removeUpdate(DocumentEvent e) {
				onModinfoFilenameChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				onModinfoFilenameChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				onModinfoFilenameChanged();
			}
		});
		txtModinfoFilename.setColumns(10);
		
		chckbxModinfoFolderAutomatic = new JCheckBox("Automatic");
		pnlDetails.add(chckbxModinfoFolderAutomatic, "4, 10, fill, center");
		
		pnlButtons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pnlButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModCreationDialog.this.setVisible(false);
			}
		});
		pnlButtons.add(btnCancel);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tryToCreateModAndClose();
			}
		});
		pnlButtons.add(btnCreate);
		getRootPane().setDefaultButton(btnCreate);
		chckbxModinfoFolderAutomatic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean isAutomatic = chckbxModinfoFolderAutomatic.isSelected();
				onModinfoFilenameAutomaticChanged(isAutomatic);
			}
		});
		chckbxModFolderAutomatic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent checkBoxEvent) {
				boolean isSelected = chckbxModFolderAutomatic.isSelected();
				onModFolderAutomaticChanged(isSelected);
			}
		});
		btnSelectModFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				File modFolder = FileUtil.selectDirectory(ModCreationDialog.this);
				if(modFolder != null){
					setModFolder(modFolder);
				}
			}
		});
		
		pack();
	}
	
	/**
	 * Called when the checkbox status of automatic mod folder changes.
	 * 
	 * @param isAutomatic The new value of the checkbox
	 */
	private void onModFolderAutomaticChanged(boolean isAutomatic){
		btnSelectModFolder.setEnabled(!isAutomatic);
		
		if(isAutomatic){
			setAutomaticFolder();
		}
	}
	/**
	 * Called when the checkbox status of automatic modinfo filename changes.
	 * 
	 * @param isAutomatic The new value of the checkbox
	 */
	private void onModinfoFilenameAutomaticChanged(boolean isAutomatic){
		txtModinfoFilename.setEditable(!isAutomatic);
		
		if(isAutomatic){
			setAutomaticModinfoFilename();
		}
	}
	/**
	 * Called when the mod name changes.
	 */
	private void onModNameChanged(){
		_mod.setName(txtModName.getText());
		
		if(chckbxModFolderAutomatic.isSelected())
			setAutomaticFolder();
		
		if(chckbxModinfoFolderAutomatic.isSelected())
			setAutomaticModinfoFilename();
		
		refreshDialogButtons();
	}
	/**
	 * Called when the modinfo file name changes.
	 */
	private void onModinfoFilenameChanged(){
		_mod.setModInfoFilename(txtModinfoFilename.getText());
		
		refreshDialogButtons();
	}

	/**
	 * Set the mod directory automatically.
	 */
	private void setAutomaticFolder(){
		setModFolder(PathUtil.getModFolder(Settings.getCurrentSettings(), _mod.getName()));
	}
	/**
	 * Set the file name of the modinfo file automatically.
	 */
	private void setAutomaticModinfoFilename(){
		setModinfoFile(PathUtil.getModinfoFilename(_mod.getName()));
	}
	/**
	 * Set the mod directory.
	 * @param directory The new mod directory
	 */
	private void setModFolder(File directory){
		_mod.setFolder(directory);
		if(_mod.getFolder() != null)
			txtModFolder.setText(_mod.getFolder().getAbsolutePath());
		else
			txtModFolder.setText("");
		
		refreshDialogButtons();
	}
	/**
	 * Set the modinfo file name.
	 * @param filename The new modinfo filename
	 */
	private void setModinfoFile(String filename){
		_mod.setModInfoFilename(filename);
		if(_mod.getName() != null)
			txtModinfoFilename.setText(filename);
		else
			txtModinfoFilename.setText("");
		
		refreshDialogButtons();
	}
	
	/**
	 * Refresh the mod fields.
	 */
	private void refreshFields(){
		if(_mod.getName() != null){
			txtModName.setText(_mod.getName());
		}
		if(_mod.getFolder() != null){
			txtModFolder.setText(_mod.getFolder().getAbsolutePath());
	}
		if(_mod.getModInfoFilename() != null){
			txtModinfoFilename.setText(_mod.getModInfoFilename());
		}
	}
	/**
	 * Refresh the state of the dialog main buttons.
	 */
	private void refreshDialogButtons(){
		btnCreate.setEnabled(_mod.isReadyToBuild());
	}
	/**
	 * Call all refresh methods of this dialog.
	 */
	private void refreshDialog(){
		refreshFields();
		refreshDialogButtons();
	}
	
	/**
	 * Try to create the mod and close the dialog if successfull.
	 */
	private void tryToCreateModAndClose(){
		if(createMod()){
			setVisible(false);
			_listener.onModCreated(_mod);
		}
	}
	/**
	 * Create the mod structure.
	 * 
	 * @return True if successfull in creating the mod
	 */
	private boolean createMod(){
		String errorTitle = "Can't create mod";
		try {
			_mod.buildModStructure();
			return true;
		} catch (JsonIOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error writing JSON", errorTitle, JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error building mod structure", errorTitle, JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}
