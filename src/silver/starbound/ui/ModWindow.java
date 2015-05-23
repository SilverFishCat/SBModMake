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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JLabel;

import java.awt.Component;

import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import silver.starbound.data.Mod;
import silver.starbound.data.Settings;
import silver.starbound.data.TypedFile;
import silver.starbound.data.TypedFile.FileType;
import silver.starbound.ui.util.FileUtil;
import silver.starbound.util.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

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
		refreshEntireFrame();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		gbl_pnlMainContent.rowHeights = new int[]{0, 0, 0};
		gbl_pnlMainContent.columnWeights = new double[]{0.0, 1.0, 0.0};
		gbl_pnlMainContent.rowWeights = new double[]{0.0, Double.MIN_VALUE, 0.0};
		pnlMainContent.setLayout(gbl_pnlMainContent);
		hbMainContainer.add(pnlMainContent);
		
		JLabel lblFiles = new JLabel("Files:");
		GridBagConstraints gbc_lblFiles = new GridBagConstraints();
		gbc_lblFiles.gridwidth = 3;
		gbc_lblFiles.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFiles.insets = new Insets(0, 0, 5, 0);
		gbc_lblFiles.gridx = 0;
		gbc_lblFiles.gridy = 0;
		pnlMainContent.add(lblFiles, gbc_lblFiles);
		
		JScrollPane scrlFiles = new JScrollPane();
		scrlFiles.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrlFiles = new GridBagConstraints();
		gbc_scrlFiles.insets = new Insets(0, 0, 5, 5);
		gbc_scrlFiles.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrlFiles.gridwidth = 2;
		gbc_scrlFiles.gridx = 0;
		gbc_scrlFiles.gridy = 1;
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
		gbc_pnlFilesButtons.insets = new Insets(0, 0, 5, 0);
		gbc_pnlFilesButtons.fill = GridBagConstraints.BOTH;
		gbc_pnlFilesButtons.gridx = 2;
		gbc_pnlFilesButtons.gridy = 1;
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
		gbc_pnlFile.gridy = 2;
		gbc_pnlFile.gridx = 0;
		gbc_pnlFile.gridwidth = 3;
		gbc_pnlFile.fill = GridBagConstraints.HORIZONTAL;
		pnlMainContent.add(pnlFile, gbc_pnlFile);		
		pnlFile.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlFileDetail = new JPanel();
		pnlFile.add(pnlFileDetail, BorderLayout.CENTER);
		pnlFileDetail.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow(3)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow(3)"),
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
				
				File file = FileUtil.selectFile(ModWindow.this, "Select save file", new File(targetDirectory, _mod.getDefaultModSaveFileName()));
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
				File file = FileUtil.selectFile(ModWindow.this, "Select save file", new File(Settings.getCurrentSettings().getModsFolder(), "mod.save"));
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
		
		Component hsCntNorth = Box.createVerticalStrut(20);
		getContentPane().add(hsCntNorth, BorderLayout.NORTH);
		
		Component hsCntSouth = Box.createVerticalStrut(20);
		getContentPane().add(hsCntSouth, BorderLayout.SOUTH);
		
		pack();
	}

	/**
	 * Called when the selection in the file tree changes.
	 */
	private void onTreeSelectionChanged(){
		setSelectedFile(getSelectedFileFromFileTree());
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
		refreshFilesList();
		refreshFileDetailsPanel();
		refreshFilesLoadButton();
		refreshPackButton();	
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
		
		File outputFolder = FileUtil.selectFile(this, "Select output file");
		
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
