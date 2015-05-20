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

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Component;

import javax.swing.JButton;

import java.awt.Dialog.ModalityType;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import silver.starbound.data.Mod;
import silver.starbound.data.Settings;
import silver.starbound.data.TypedFile;
import silver.starbound.data.TypedFile.FileType;
import silver.starbound.ui.SettingsDialog.DialogResult;
import silver.starbound.util.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.FlowLayout;

/**
 * A window that displays the detail of a mod, as other components
 * to manipulate a mod.
 * 
 * @author SilverFishCat
 *
 */
public class ModWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5116457126300886427L;
	
	private JTextField txtModName;
	private JTextField txtModFolder;
	private JTextField txtModinfoFilename;
	private JButton btnModFolder;
	private JButton btnCreateModFolder;
	private JButton btnModinfoFilename;
	private JCheckBox chckbxModFolderAutomatic;
	private JCheckBox chckbxModinfoFolderAutomatic;
	private JTree treeFiles;
	private JButton btnFileEdit;
	private JButton btnPack;
	private JButton btnFilesLoad;
	private JButton btnFileOpenJson;
	private JPanel pnlFile;
	private JLabel txtFileName;
	private TypedFile selectedFile; 
	private JLabel txtFileType;
	
	private Mod _mod;

	/**
	 * Create the application.
	 */
	public ModWindow(Mod mod) {
		initialize();
		
		_mod = mod; 

		selectedFile = new TypedFile(null);

		chckbxModFolderAutomatic.setSelected(true);
		chckbxModinfoFolderAutomatic.setSelected(true);
		refreshEntireFrame();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent closeEvent) {
				SettingsUtil.saveSettings(Settings.getCurrentSettings());
				// TODO: save when exiting settings screen
			}
		});
		setTitle("ModMake");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		Component hsCntWest = Box.createHorizontalStrut(20);
		getContentPane().add(hsCntWest, BorderLayout.WEST);
		
		Component hsCntEast = Box.createHorizontalStrut(20);
		getContentPane().add(hsCntEast, BorderLayout.EAST);
		
		Box hbMainContainer = Box.createHorizontalBox();
		getContentPane().add(hbMainContainer, BorderLayout.CENTER);
		
		JPanel pnlMainContent = new JPanel();
		pnlMainContent.setAlignmentX(Component.RIGHT_ALIGNMENT);
		GridBagLayout gbl_pnlMainContent = new GridBagLayout();
		gbl_pnlMainContent.columnWidths = new int[]{0, 0, 0};
		gbl_pnlMainContent.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlMainContent.columnWeights = new double[]{0.0, 1.0, 0.0};
		gbl_pnlMainContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE, 0.0};
		pnlMainContent.setLayout(gbl_pnlMainContent);
		hbMainContainer.add(pnlMainContent);
		
		JLabel lblModName = new JLabel("Mod name:");
		GridBagConstraints gbc_lblModName = new GridBagConstraints();
		gbc_lblModName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblModName.insets = new Insets(0, 0, 5, 5);
		gbc_lblModName.gridx = 0;
		gbc_lblModName.gridy = 0;
		pnlMainContent.add(lblModName, gbc_lblModName);
		
		txtModName = new JTextField();
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
		GridBagConstraints gbc_txtModName = new GridBagConstraints();
		gbc_txtModName.insets = new Insets(0, 0, 5, 5);
		gbc_txtModName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtModName.gridx = 1;
		gbc_txtModName.gridy = 0;
		pnlMainContent.add(txtModName, gbc_txtModName);
		txtModName.setColumns(10);
		
		JLabel lblFolder = new JLabel("Folder:");
		GridBagConstraints gbc_lblFolder = new GridBagConstraints();
		gbc_lblFolder.anchor = GridBagConstraints.WEST;
		gbc_lblFolder.insets = new Insets(0, 0, 5, 5);
		gbc_lblFolder.gridx = 0;
		gbc_lblFolder.gridy = 1;
		pnlMainContent.add(lblFolder, gbc_lblFolder);
		
		txtModFolder = new JTextField();
		txtModFolder.setEditable(false);
		GridBagConstraints gbc_txtModFolder = new GridBagConstraints();
		gbc_txtModFolder.insets = new Insets(0, 0, 5, 5);
		gbc_txtModFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtModFolder.gridx = 1;
		gbc_txtModFolder.gridy = 1;
		pnlMainContent.add(txtModFolder, gbc_txtModFolder);
		txtModFolder.setColumns(30);
		
		chckbxModFolderAutomatic = new JCheckBox("Automatic");
		chckbxModFolderAutomatic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent checkBoxEvent) {
				boolean isSelected = chckbxModFolderAutomatic.isSelected();
				onModFolderAutomaticChanged(isSelected);
			}
		});
		
		btnCreateModFolder = new JButton("Create");
		btnCreateModFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				if(_mod.getFolder() == null){
					JOptionPane.showMessageDialog(ModWindow.this, "No folder path set", "Can't create folder", JOptionPane.ERROR_MESSAGE);
				}
				if(_mod.isFolderValid()){
					JOptionPane.showMessageDialog(ModWindow.this, "Folder already exists", "Can't create folder", JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(_mod.getFolder().mkdirs()){
						JOptionPane.showMessageDialog(ModWindow.this, "Folder created", "Success", JOptionPane.INFORMATION_MESSAGE);
						refreshModFolderCreateButton();
						refreshModinfoButton();
						refreshFilesLoadButton();
					}
					else{
						JOptionPane.showMessageDialog(ModWindow.this, "There was an error creating the folder", "Can't create folder", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		GridBagConstraints gbc_btnCreateModFolder = new GridBagConstraints();
		gbc_btnCreateModFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCreateModFolder.insets = new Insets(0, 0, 5, 0);
		gbc_btnCreateModFolder.gridx = 2;
		gbc_btnCreateModFolder.gridy = 1;
		pnlMainContent.add(btnCreateModFolder, gbc_btnCreateModFolder);
		GridBagConstraints gbc_chckbxModFolderAutomatic = new GridBagConstraints();
		gbc_chckbxModFolderAutomatic.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxModFolderAutomatic.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxModFolderAutomatic.gridx = 1;
		gbc_chckbxModFolderAutomatic.gridy = 2;
		pnlMainContent.add(chckbxModFolderAutomatic, gbc_chckbxModFolderAutomatic);
		
		btnModFolder = new JButton("Browse");
		btnModFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				File modFolder = selectDirectory();
				if(modFolder != null){
					setModFolder(modFolder);
				}
			}
		});
		GridBagConstraints gbc_btnModFolder = new GridBagConstraints();
		gbc_btnModFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnModFolder.insets = new Insets(0, 0, 5, 0);
		gbc_btnModFolder.gridx = 2;
		gbc_btnModFolder.gridy = 2;
		pnlMainContent.add(btnModFolder, gbc_btnModFolder);
		
		JLabel lblModinfoFilename = new JLabel("Modinfo filename:");
		GridBagConstraints gbc_lblModinfoFilename = new GridBagConstraints();
		gbc_lblModinfoFilename.anchor = GridBagConstraints.WEST;
		gbc_lblModinfoFilename.insets = new Insets(0, 0, 5, 5);
		gbc_lblModinfoFilename.gridx = 0;
		gbc_lblModinfoFilename.gridy = 3;
		pnlMainContent.add(lblModinfoFilename, gbc_lblModinfoFilename);
		
		txtModinfoFilename = new JTextField();
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
		GridBagConstraints gbc_txtModinfoFilename = new GridBagConstraints();
		gbc_txtModinfoFilename.insets = new Insets(0, 0, 5, 5);
		gbc_txtModinfoFilename.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtModinfoFilename.gridx = 1;
		gbc_txtModinfoFilename.gridy = 3;
		pnlMainContent.add(txtModinfoFilename, gbc_txtModinfoFilename);
		
		chckbxModinfoFolderAutomatic = new JCheckBox("Automatic");
		chckbxModinfoFolderAutomatic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean isAutomatic = chckbxModinfoFolderAutomatic.isSelected();
				onModinfoFilenameAutomaticChanged(isAutomatic);
			}
		});
		
		btnModinfoFilename = new JButton("Create");
		btnModinfoFilename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				File modinfoFile = _mod.getModinfoFile();
				if(!modinfoFile.exists()){
					createModInfoFile(modinfoFile);
				}
				else{
					editTextFile(modinfoFile);
				}
				
				refreshModinfoButton();
			}
		});
		GridBagConstraints gbc_btnModinfoFilename = new GridBagConstraints();
		gbc_btnModinfoFilename.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnModinfoFilename.insets = new Insets(0, 0, 5, 0);
		gbc_btnModinfoFilename.gridx = 2;
		gbc_btnModinfoFilename.gridy = 3;
		pnlMainContent.add(btnModinfoFilename, gbc_btnModinfoFilename);
		GridBagConstraints gbc_chckbxModinfoFolderAutomatic = new GridBagConstraints();
		gbc_chckbxModinfoFolderAutomatic.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxModinfoFolderAutomatic.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxModinfoFolderAutomatic.gridx = 1;
		gbc_chckbxModinfoFolderAutomatic.gridy = 4;
		pnlMainContent.add(chckbxModinfoFolderAutomatic, gbc_chckbxModinfoFolderAutomatic);
		gbc_txtModinfoFilename.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel lblFiles = new JLabel("Files:");
		GridBagConstraints gbc_lblFiles = new GridBagConstraints();
		gbc_lblFiles.gridwidth = 3;
		gbc_lblFiles.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFiles.insets = new Insets(0, 0, 5, 0);
		gbc_lblFiles.gridx = 0;
		gbc_lblFiles.gridy = 5;
		pnlMainContent.add(lblFiles, gbc_lblFiles);
		
		JScrollPane scrlFiles = new JScrollPane();
		scrlFiles.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrlFiles = new GridBagConstraints();
		gbc_scrlFiles.insets = new Insets(0, 0, 0, 5);
		gbc_scrlFiles.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrlFiles.gridwidth = 2;
		gbc_scrlFiles.gridx = 0;
		gbc_scrlFiles.gridy = 6;
		scrlFiles.setPreferredSize(new Dimension(100, 150));
		pnlMainContent.add(scrlFiles, gbc_scrlFiles);
		
		treeFiles = new JTree();
		treeFiles.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent selectionEvent) {
				onTreeSelectionChanged();
			}
		});
		treeFiles.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrlFiles.setViewportView(treeFiles);
		
		JPanel pnlFilesButtons = new JPanel();
		GridBagConstraints gbc_pnlFilesButtons = new GridBagConstraints();
		gbc_pnlFilesButtons.fill = GridBagConstraints.BOTH;
		gbc_pnlFilesButtons.gridx = 2;
		gbc_pnlFilesButtons.gridy = 6;
		pnlMainContent.add(pnlFilesButtons, gbc_pnlFilesButtons);
		pnlFilesButtons.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("23px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		btnFilesLoad = new JButton("Load");
		pnlFilesButtons.add(btnFilesLoad, "1, 1");
		btnFilesLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				refreshFilesList();
				refreshFileDetailsPanel();
			}
		});
		
		pnlFile = new JPanel();
		pnlFile.setBorder(new TitledBorder(null, "File", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		GridBagConstraints gbc_pnlFile = new GridBagConstraints();
		gbc_pnlFile.gridy = 7;
		gbc_pnlFile.gridx = 0;
		gbc_pnlFile.gridwidth = 3;
		gbc_pnlFile.fill = GridBagConstraints.HORIZONTAL;
		pnlMainContent.add(pnlFile, gbc_pnlFile);		
		pnlFile.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlFileDetail = new JPanel();
		pnlFile.add(pnlFileDetail, BorderLayout.CENTER);
		pnlFileDetail.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("0dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("0dlu:grow(3)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("0dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("0dlu:grow(3)"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblFileName = new JLabel("Name:");
		pnlFileDetail.add(lblFileName, "2, 2, left, default");
		
		txtFileName = new JLabel("N/A");
		pnlFileDetail.add(txtFileName, "4, 2");
		
		JLabel lblFileType = new JLabel("Type:");
		pnlFileDetail.add(lblFileType, "6, 2");
		
		txtFileType = new JLabel("N/A");
		pnlFileDetail.add(txtFileType, "8, 2");
		
		JPanel pnlFileButtons = new JPanel();
		FlowLayout fl_pnlFileButtons = (FlowLayout) pnlFileButtons.getLayout();
		fl_pnlFileButtons.setAlignment(FlowLayout.RIGHT);
		pnlFile.add(pnlFileButtons, BorderLayout.SOUTH);
		
		btnFileEdit = new JButton("Edit Text");
		pnlFileButtons.add(btnFileEdit);
		btnFileEdit.addActionListener(new ActionListener() {
			@SuppressWarnings("incomplete-switch")
			public void actionPerformed(ActionEvent arg0) {
				if(selectedFile.getFile() != null)
					switch(selectedFile.getFileType()){
						case JSON:
						case TEXT:{
							editTextFile(selectedFile.getFile());
						}
						break;
						case IMAGE:{
							editImageFile(selectedFile.getFile());
						}
						break;
					}
			}
		});
		
		btnFileOpenJson = new JButton("Open JSON");
		btnFileOpenJson.addActionListener(new ActionListener() {
			private static final String ERROR_TITLE = "Can not open json object dialog";
			
			public void actionPerformed(ActionEvent e) {
				if(selectedFile.getFileType() == FileType.JSON){
					try{
						JsonElement jsonFile = new JsonParser().parse(new FileReader(selectedFile.getFile())); 
						showJsonObjectDialog(jsonFile, selectedFile.getFile().getName());
					}
					catch(Exception exception){
						exception.printStackTrace();
						JOptionPane.showMessageDialog(ModWindow.this, exception.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlFileButtons.add(btnFileOpenJson);
		
		Component hsMid = Box.createHorizontalStrut(5);
		hbMainContainer.add(hsMid);
		
		JPanel pnlButton = new JPanel();
		hbMainContainer.add(pnlButton);
		pnlButton.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		btnPack = new JButton("Pack");
		btnPack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				packMod();
			}
		});
		pnlButton.add(btnPack, "1, 1");
		btnPack.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				final String ERROR_MESSAGE = "Can't save _mod";
				File targetDirectory;
				if(_mod.isFolderValid())
					 targetDirectory = _mod.getFolder();
				else
					targetDirectory = Settings.getCurrentSettings().getModsFolder();
				
				File file = selectFile("Select save file", new File(targetDirectory, _mod.getDefaultModSaveFileName()));
				if(file != null){
					try {
						_mod.saveToFile(file);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(ModWindow.this, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(ModWindow.this, "Error in file:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlButton.add(btnSave, "1, 3");
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				final String ERROR_MESSAGE = "Can't load mod";
				File file = selectFile("Select save file", new File(Settings.getCurrentSettings().getModsFolder(), "mod.save"));
				if(file != null){
					try {
						_mod = Mod.loadFromFile(file);
						
						refreshEntireFrame();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(ModWindow.this, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(ModWindow.this, "Error in file:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (JsonParseException e){
						e.printStackTrace();
						JOptionPane.showMessageDialog(ModWindow.this, "Error in json object:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlButton.add(btnLoad, "1, 5");
		
		JButton btnSettings = new JButton("Settings");
		pnlButton.add(btnSettings, "1, 7");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsDialog dialog = new SettingsDialog(Settings.getCurrentSettings());
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setVisible(true);
				
				if(dialog.getDialogResult() == DialogResult.OK){
					Settings.setCurrentSettings(dialog.getSettings());
					refreshEntireFrame();
				}
			}
		});
		btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Component hsCntNorth = Box.createVerticalStrut(20);
		getContentPane().add(hsCntNorth, BorderLayout.NORTH);
		
		Component hsCntSouth = Box.createVerticalStrut(20);
		getContentPane().add(hsCntSouth, BorderLayout.SOUTH);
		
		pack();
	}

	/**
	 * Called when the checkbox status of automatic mod folder changes.
	 * 
	 * @param isAutomatic The new value of the checkbox
	 */
	private void onModFolderAutomaticChanged(boolean isAutomatic){
		btnModFolder.setEnabled(!isAutomatic);
		
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
	}
	/**
	 * Called when the modinfo file name changes.
	 */
	private void onModinfoFilenameChanged(){
		_mod.setModInfo(txtModinfoFilename.getText());
		refreshModinfoButton();
	}
	/**
	 * Called when the selection in the file tree changes.
	 */
	private void onTreeSelectionChanged(){
		setSelectedFile(getSelectedFileFromFileTree());
	}

	/**
	 * Set the _mod directory automatically.
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
		
		refreshModFolderCreateButton();
		refreshModinfoButton();
		refreshFilesLoadButton();
	}
	/**
	 * Set the modinfo file name.
	 * @param filename The new modinfo filename
	 */
	private void setModinfoFile(String filename){
		_mod.setModInfo(filename);
		if(_mod.getName() != null)
			txtModinfoFilename.setText(filename);
		else
			txtModinfoFilename.setText("");
	}
	/**
	 * Set the currently selected project file.
	 * @param file The file selected
	 */
	private void setSelectedFile(File file){		
		selectedFile = new TypedFile(file);
		
		if(file != null){
			txtFileName.setText(selectedFile.getFile().getName());
			
			switch (selectedFile.getFileType()) {
				case JSON:{
					txtFileType.setText("JSON");
				}
				break;
				case IMAGE:{
					txtFileType.setText("Image");
				}
				break;
				case TEXT:{
					txtFileType.setText("Text");
				}
	
				default:{
					txtFileType.setText("N/A");
				}
				break;
			}
		}
		else{
			txtFileName.setText("N/A");
			txtFileType.setText("N/A");
		}
	
		refreshFileDetailsPanel();
	}
	
	/**
	 * Refresh the settings of the mod folder create button.
	 */
	private void refreshModFolderCreateButton(){
		if(_mod.getFolder() != null && (!_mod.getFolder().exists())){
			btnCreateModFolder.setEnabled(true);
		}
		else{
			btnCreateModFolder.setEnabled(false);
		}
	}
	/**
	 * Refresh the settings of the modinfo button.
	 */
	private void refreshModinfoButton(){
		// Mod folder is valid
		if(!_mod.isFolderValid()){
			btnModinfoFilename.setEnabled(false);
		}
		// Mod name is valid
		else if(!_mod.isNameValid()){
			btnModinfoFilename.setEnabled(false);
		}
		// Modinfo filename
		else if(!_mod.isModInfoValid()){
			btnModinfoFilename.setEnabled(false);
		}
		else{
			File ModinfoFile = _mod.getModinfoFile();
			
			if(ModinfoFile.isDirectory()){
				btnModinfoFilename.setEnabled(false);
			}
			else{
				btnModinfoFilename.setEnabled(true);
				
				if(ModinfoFile.exists()){
					btnModinfoFilename.setText("Edit");
					
					if(!Settings.getCurrentSettings().isTextEditorValid()){
						btnModinfoFilename.setEnabled(false);
					}
				}
				else{
					btnModinfoFilename.setText("Create");
				}
			}
		}
	}
	/**
	 * Refresh the files list with the files in the mod folder.
	 */
	private void refreshFilesList(){
		if(_mod.isFolderValid()){
			DefaultMutableTreeNode root = new FileTreeNode(_mod.getFolder());
			buildTree(root, _mod.getFolder());
			treeFiles.setModel(new DefaultTreeModel(root));
		}
		else{
			resetFilesList();
		}
	}
	/**
	 * Reset the files list to show no files.
	 */
	private void resetFilesList(){
		treeFiles.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("N/A")));
	}
	/**
	 * Refresh the files details panel with the file's details.
	 */
	private void refreshFileDetailsPanel(){		
		boolean fileValid = selectedFile.getFile() != null && selectedFile.getFile().isFile();
		
		synchronized (pnlFile.getTreeLock()) {
			setEnabledRecursively(pnlFile, fileValid);
		}
		
		boolean editorAvailable = false;
		if(selectedFile.getFileType() != null){
			switch (selectedFile.getFileType()) {
				case IMAGE:
					btnFileEdit.setText("Edit Image");
					editorAvailable = Settings.getCurrentSettings().isImageEditorValid();
					break;
				case JSON:
				case TEXT:
					btnFileEdit.setText("Edit Text");
					editorAvailable = Settings.getCurrentSettings().isTextEditorValid();
					break;
				default:
					break;
			}
			btnFileEdit.setEnabled(selectedFile.getFileType() != FileType.UNKNOWN && editorAvailable);
			btnFileOpenJson.setEnabled(selectedFile.getFileType() == FileType.JSON);
		}
		else{
			btnFileEdit.setEnabled(false);
			btnFileOpenJson.setEnabled(false);
		}
	}
	/**
	 * Set the enabled state of a component and of all its children.
	 * 
	 * @param component The root component to set the state of
	 * @param enabled The new enabled state
	 */
	private static void setEnabledRecursively(JComponent component, boolean enabled){
		component.setEnabled(enabled);
		
		for (Component child : component.getComponents()) {
			if(child instanceof JComponent)
				setEnabledRecursively((JComponent) child, enabled);
		}
	}
	/**
	 * Refresh the settings of the file list refresh button.
	 */
	private void refreshFilesLoadButton(){
		btnFilesLoad.setEnabled(_mod.isFolderValid() && !_mod.getFolder().equals(Settings.getCurrentSettings().getModsFolder()));
	}
	/**
	 * Refresh the settings of the pack button.
	 */
	private void refreshPackButton(){
		if(Settings.getCurrentSettings().isStarboundFolderValid()){
			
		}
		else{
			btnPack.setEnabled(false);
		}
	}
	/**
	 * Refresh the entire frame.
	 */
	private void refreshEntireFrame(){
		refreshModFolderCreateButton();
		refreshModinfoButton();
		refreshFilesList();
		refreshFileDetailsPanel();
		refreshFilesLoadButton();
		refreshPackButton();
		
		refreshFields();	
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
		if(_mod.getModInfo() != null){
			txtModinfoFilename.setText(_mod.getModInfo());
		}
	}
	
	/**
	 * Select a directory.
	 * Opens a dialog to allow the user to select a directory.
	 * 
	 * @return A directory selected by the user, null if user cancelled
	 */
	private File selectDirectory(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Select folder");
		int dialogResult = fc.showOpenDialog(this);
		
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
	private File selectFile(String title){
		return selectFile(title, null);
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
	private File selectFile(String title, File defaultFile){
		return selectFile(title, null, defaultFile);
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
	private File selectFile(String title, File directory, File defaultFile){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle(title);
		if(defaultFile != null){
			fc.setSelectedFile(defaultFile);
		}
		else if(directory != null)
			fc.setCurrentDirectory(directory);
		int dialogResult = fc.showOpenDialog(this);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
	/**
	 * Create the modinfo file.
	 * 
	 * @param file The possibly non-existant modinfo file to create
	 * @return True if file created, false otherwise
	 */
	private boolean createModInfoFile(File file){
		String errorTitle = "Can't create modinfo file";
		boolean created = createFile(file);
		
		if(created){
			if(file.canWrite()){
				FileWriter writer;
				try {
					writer = new FileWriter(file);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
					return false;
				}
				
				try {
					writer.write(
							"{"										+ "\r\n" +
							"\t\"name\" : \"" + _mod.getName() + "\","	+ "\r\n" +
							"\t\"requires\" : [],"					+ "\r\n" +
							"\t\"includes\" : []"						+ "\r\n" +
						"}"											+ "\r\n"
					);
					
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
					return false;
				}
				finally{
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(this, "Can't create file", errorTitle, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else{
			//JOptionPane.showMessageDialog(this, "Can't create file", "Can't create modinfo file", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	/**
	 * Create a file.
	 * 
	 * @param file The file to create
	 * @return True if file create, false otherwise
	 */
	private boolean createFile(File file){
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Can't create file", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	/**
	 * Open file for editing, using the text editor set.
	 * 
	 * @param textFile The file to edit
	 */
	private void editTextFile(File textFile){
		File editor = Settings.getCurrentSettings().getTextEditor();
		
		try {
			Runtime.getRuntime().exec(String.join(" ", editor.getAbsolutePath(), textFile.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error in edit", "Can't open editor", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Open file for editing, using the image editor set.
	 * 
	 * @param imageFile The file to edit
	 */
	private void editImageFile(File imageFile){
		File editor = Settings.getCurrentSettings().getImageEditor();
		
		try {
			Runtime.getRuntime().exec(String.join(" ", editor.getAbsolutePath(), imageFile.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error in edit", "Can't open editor", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Open a dialog showing the given json element.
	 * 
	 * @param json The json element to display
	 * @param title The title of the dialog
	 */
	private void showJsonObjectDialog(JsonElement json, String title) {
		JsonViewerDialog dialog = new JsonViewerDialog(json, title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
	/**
	 * Builds the file tree from the given directory.
	 * 
	 * @param currentNode The current node on which the tree is being built
	 * @param directory The directory of the current node.
	 */
	private void buildTree(DefaultMutableTreeNode currentNode, File directory){
		for (File file : directory.listFiles()) {
			DefaultMutableTreeNode fileNode = new FileTreeNode(file);
			currentNode.add(fileNode);
			if(file.isDirectory())
				buildTree(fileNode, file);
		}
	}
	/**
	 * Get the file selected in the tree.
	 * 
	 * @return The selected file in the tree
	 */
	private File getSelectedFileFromFileTree(){
		TreePath selectionPath = treeFiles.getSelectionPath();
		if(selectionPath != null){
			Object endNode = selectionPath.getLastPathComponent();
			if(endNode instanceof FileTreeNode){
				File file = ((FileTreeNode) endNode).getFile();
				return file;
			}
		}
		return null;
	}
	/**
	 * Pack the mod using the starbound asset packet tool.
	 */
	private void packMod(){
		final String ERROR_MESSAGE = "Error packing mods";
		File packer = PathUtil.getPacker(Settings.getCurrentSettings());
		
		if(packer == null || !packer.exists()){
			JOptionPane.showMessageDialog(this, "Asset packer not found", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!_mod.isNameValid()){
			JOptionPane.showMessageDialog(this, "Mod name invalid", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!_mod.isFolderValid()){
			JOptionPane.showMessageDialog(this, "Mod folder invalid", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File outputFolder = selectFile("Select output file");
		
		if(outputFolder == null)
			return;
		
		try {
			Runtime.getRuntime().exec(packer.getAbsolutePath() + " \"" + _mod.getName() + "\" \"" + _mod.getFolder() + "\" \"" + outputFolder.getAbsolutePath() + "\"");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
