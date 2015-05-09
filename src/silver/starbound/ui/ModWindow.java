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

import java.awt.EventQueue;

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

public class ModWindow {
	private JFrame frmModmake;
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
	
	private Mod mod;
	private Settings settings;

	private TypedFile selectedFile; 
	private JLabel txtFileType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModWindow window = new ModWindow();
					window.frmModmake.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ModWindow() {
		initialize();
		
		mod = new Mod();
		mod.setName("");
		mod.setFolder(null);
		
		settings = SettingsUtil.loadSettings();

		selectedFile = new TypedFile(null);

		chckbxModFolderAutomatic.setSelected(true);
		chckbxModinfoFolderAutomatic.setSelected(true);
		refreshEntireFrame();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmModmake = new JFrame();
		frmModmake.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent closeEvent) {
				SettingsUtil.saveSettings(settings);
			}
		});
		frmModmake.setTitle("ModMake");
		frmModmake.setBounds(100, 100, 548, 240);
		frmModmake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmModmake.getContentPane().setLayout(new BorderLayout(0, 0));
		
		Component hsCntWest = Box.createHorizontalStrut(20);
		frmModmake.getContentPane().add(hsCntWest, BorderLayout.WEST);
		
		Component hsCntEast = Box.createHorizontalStrut(20);
		frmModmake.getContentPane().add(hsCntEast, BorderLayout.EAST);
		
		Box hbMainContainer = Box.createHorizontalBox();
		frmModmake.getContentPane().add(hbMainContainer, BorderLayout.CENTER);
		
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
				if(mod.getFolder() == null){
					JOptionPane.showMessageDialog(frmModmake, "No folder path set", "Can't create folder", JOptionPane.ERROR_MESSAGE);
				}
				if(mod.isFolderValid()){
					JOptionPane.showMessageDialog(frmModmake, "Folder already exists", "Can't create folder", JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(mod.getFolder().mkdirs()){
						JOptionPane.showMessageDialog(frmModmake, "Folder created", "Success", JOptionPane.INFORMATION_MESSAGE);
						refreshModFolderCreateButton();
						refreshModinfoButton();
						refreshFilesLoadButton();
					}
					else{
						JOptionPane.showMessageDialog(frmModmake, "There was an error creating the folder", "Can't create folder", JOptionPane.ERROR_MESSAGE);
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
				File modinfoFile = mod.getModinfoFile();
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
						JOptionPane.showMessageDialog(frmModmake, exception.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
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
				final String ERROR_MESSAGE = "Can't save mod";
				File targetDirectory;
				if(mod.isFolderValid())
					 targetDirectory = mod.getFolder();
				else
					targetDirectory = settings.getModsFolder();
				
				File file = selectFile("Select save file", new File(targetDirectory, mod.getDefaultModSaveFileName()));
				if(file != null){
					try {
						mod.saveToFile(file);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frmModmake, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frmModmake, "Error in file:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlButton.add(btnSave, "1, 3");
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent pressEvent) {
				final String ERROR_MESSAGE = "Can't load mod";
				File file = selectFile("Select save file", new File(settings.getModsFolder(), "mod.save"));
				if(file != null){
					try {
						mod = Mod.loadFromFile(file);
						
						refreshEntireFrame();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frmModmake, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frmModmake, "Error in file:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (JsonParseException e){
						e.printStackTrace();
						JOptionPane.showMessageDialog(frmModmake, "Error in json object:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		pnlButton.add(btnLoad, "1, 5");
		
		JButton btnSettings = new JButton("Settings");
		pnlButton.add(btnSettings, "1, 7");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsDialog dialog = new SettingsDialog(settings);
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setVisible(true);
				
				if(dialog.getDialogResult() == DialogResult.OK){
					settings = dialog.getSettings();
					refreshEntireFrame();
				}
			}
		});
		btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Component hsCntNorth = Box.createVerticalStrut(20);
		frmModmake.getContentPane().add(hsCntNorth, BorderLayout.NORTH);
		
		Component hsCntSouth = Box.createVerticalStrut(20);
		frmModmake.getContentPane().add(hsCntSouth, BorderLayout.SOUTH);
		
		frmModmake.pack();
	}

	private void onModFolderAutomaticChanged(boolean isAutomatic){
		btnModFolder.setEnabled(!isAutomatic);
		
		if(isAutomatic){
			setAutomaticFolderName();
		}
	}
	private void onModinfoFilenameAutomaticChanged(boolean isAutomatic){
		txtModinfoFilename.setEditable(!isAutomatic);
		
		if(isAutomatic){
			setAutomaticModinfoFilename();
		}
	}
	private void onModNameChanged(){
		mod.setName(txtModName.getText());
		
		if(chckbxModFolderAutomatic.isSelected())
			setAutomaticFolderName();
		
		if(chckbxModinfoFolderAutomatic.isSelected())
			setAutomaticModinfoFilename();
	}
	private void onModinfoFilenameChanged(){
		mod.setModInfo(txtModinfoFilename.getText());
		refreshModinfoButton();
	}
	private void onTreeSelectionChanged(){
		setSelectedFile(getSelectedFileFromFileTree());
	}

	private void setAutomaticFolderName(){
		setModFolder(PathUtil.getModFolder(settings, mod.getName()));
	}
	private void setAutomaticModinfoFilename(){
		setModinfoFile(PathUtil.getModinfoFilename(mod.getName()));
	}
	private void setModFolder(File folder){
		mod.setFolder(folder);
		if(mod.getFolder() != null)
			txtModFolder.setText(mod.getFolder().getAbsolutePath());
		else
			txtModFolder.setText("");
		
		refreshModFolderCreateButton();
		refreshModinfoButton();
		refreshFilesLoadButton();
	}
	private void setModinfoFile(String filename){
		mod.setModInfo(filename);
		if(mod.getName() != null)
			txtModinfoFilename.setText(filename);
		else
			txtModinfoFilename.setText("");
	}
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
	
	private void refreshModFolderCreateButton(){
		if(mod.getFolder() != null && (!mod.getFolder().exists())){
			btnCreateModFolder.setEnabled(true);
		}
		else{
			btnCreateModFolder.setEnabled(false);
		}
	}
	private void refreshModinfoButton(){
		// Mod folder is valid
		if(!mod.isFolderValid()){
			btnModinfoFilename.setEnabled(false);
		}
		// Mod name is valid
		else if(!mod.isNameValid()){
			btnModinfoFilename.setEnabled(false);
		}
		// Modinfo filename
		else if(!mod.isModInfoValid()){
			btnModinfoFilename.setEnabled(false);
		}
		else{
			File ModinfoFile = mod.getModinfoFile();
			
			if(ModinfoFile.isDirectory()){
				btnModinfoFilename.setEnabled(false);
			}
			else{
				btnModinfoFilename.setEnabled(true);
				
				if(ModinfoFile.exists()){
					btnModinfoFilename.setText("Edit");
					
					if(!settings.isTextEditorValid()){
						btnModinfoFilename.setEnabled(false);
					}
				}
				else{
					btnModinfoFilename.setText("Create");
				}
			}
		}
	}
	private void refreshFilesList(){
		if(mod.isFolderValid()){
			DefaultMutableTreeNode root = new FileTreeNode(mod.getFolder());
			buildTree(root, mod.getFolder());
			treeFiles.setModel(new DefaultTreeModel(root));
		}
		else{
			resetFilesList();
		}
	}
	private void resetFilesList(){
		treeFiles.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("N/A")));
	}
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
					editorAvailable = settings.isImageEditorValid();
					break;
				case JSON:
				case TEXT:
					btnFileEdit.setText("Edit Text");
					editorAvailable = settings.isTextEditorValid();
					break;
				default:
					break;
			}
			btnFileEdit.setEnabled(selectedFile.getFileType() != FileType.UNKNOWN && editorAvailable);
		}
		else{
			btnFileEdit.setEnabled(false);
		}
	}
	private void setEnabledRecursively(JComponent component, boolean enabled){
		component.setEnabled(enabled);
		
		for (Component child : component.getComponents()) {
			if(child instanceof JComponent)
				setEnabledRecursively((JComponent) child, enabled);
		}
	}
	private void refreshFilesLoadButton(){
		btnFilesLoad.setEnabled(mod.isFolderValid() && !mod.getFolder().equals(settings.getModsFolder()));
	}
	private void refreshPackButton(){
		if(settings.isStarboundFolderValid()){
			
		}
		else{
			btnPack.setEnabled(false);
		}
	}
	private void refreshEntireFrame(){
		refreshModFolderCreateButton();
		refreshModinfoButton();
		refreshFilesList();
		refreshFileDetailsPanel();
		refreshFilesLoadButton();
		refreshPackButton();
		
		refreshFields();	
	}
	private void refreshFields(){
		if(mod.getName() != null){
			txtModName.setText(mod.getName());
		}
		if(mod.getFolder() != null){
			txtModFolder.setText(mod.getFolder().getAbsolutePath());
	}
		if(mod.getModInfo() != null){
			txtModinfoFilename.setText(mod.getModInfo());
		}
	}
	
	private File selectDirectory(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Select folder");
		int dialogResult = fc.showOpenDialog(frmModmake);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
	private File selectFile(String title){
		return selectFile(title, null);
	}
	/*private File selectFileFromDirectory(String title, File directory){
		return selectFile(title, directory, null);
	}*/
	private File selectFile(String title, File defaultFile){
		return selectFile(title, null, defaultFile);
	}
	private File selectFile(String title, File directory, File defaultFile){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle(title);
		if(defaultFile != null){
			fc.setSelectedFile(defaultFile);
		}
		else if(directory != null)
			fc.setCurrentDirectory(directory);
		int dialogResult = fc.showOpenDialog(frmModmake);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
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
					JOptionPane.showMessageDialog(frmModmake, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
					return false;
				}
				
				try {
					writer.write(
							"{"										+ "\r\n" +
							"\t\"name\" : \"" + mod.getName() + "\","	+ "\r\n" +
							"\t\"requires\" : [],"					+ "\r\n" +
							"\t\"includes\" : []"						+ "\r\n" +
						"}"											+ "\r\n"
					);
					
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frmModmake, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(frmModmake, "Can't create file", errorTitle, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		else{
			//JOptionPane.showMessageDialog(frmModmake, "Can't create file", "Can't create modinfo file", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	private boolean createFile(File file){
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frmModmake, e.getMessage(), "Can't create file", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	private void editTextFile(File textFile){
		File editor = settings.getTextEditor();
		
		try {
			Runtime.getRuntime().exec(String.join(" ", editor.getAbsolutePath(), textFile.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frmModmake, "Error in edit", "Can't open editor", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void editImageFile(File imageFile){
		File editor = settings.getImageEditor();
		
		try {
			Runtime.getRuntime().exec(String.join(" ", editor.getAbsolutePath(), imageFile.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frmModmake, "Error in edit", "Can't open editor", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void showJsonObjectDialog(JsonElement json, String title) {
		JsonViewerDialog dialog = new JsonViewerDialog(json, title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
	private void buildTree(DefaultMutableTreeNode currentNode, File directory){
		for (File file : directory.listFiles()) {
			DefaultMutableTreeNode fileNode = new FileTreeNode(file);
			currentNode.add(fileNode);
			if(file.isDirectory())
				buildTree(fileNode, file);
		}
	}
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
	private void packMod(){
		final String ERROR_MESSAGE = "Error packing mods";
		File packer = PathUtil.getPacker(settings);
		
		if(packer == null || !packer.exists()){
			JOptionPane.showMessageDialog(frmModmake, "Asset packer not found", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!mod.isNameValid()){
			JOptionPane.showMessageDialog(frmModmake, "Mod name invalid", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!mod.isFolderValid()){
			JOptionPane.showMessageDialog(frmModmake, "Mod folder invalid", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File outputFolder = selectFile("Select output file");
		
		if(outputFolder == null)
			return;
		
		try {
			Runtime.getRuntime().exec(packer.getAbsolutePath() + " \"" + mod.getName() + "\" \"" + mod.getFolder() + "\" \"" + outputFolder.getAbsolutePath() + "\"");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frmModmake, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
