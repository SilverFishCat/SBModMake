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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import silver.starbound.data.Mod;
import silver.starbound.data.Settings;
import silver.starbound.data.TypedFile;
import silver.starbound.data.TypedFile.FileType;
import silver.starbound.ui.util.FileUtil;
import silver.starbound.util.PathUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.border.TitledBorder;
import javax.swing.JSplitPane;

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
	private JLabel txtFileName;
	private TypedFile selectedFile; 
	private JLabel txtFileType;
	
	private Mod _mod;
	private JMenuItem mntmPack;
	private JMenuItem mntmFileOpenJson;
	private JMenuItem mntmFileEdit;
	private JPanel pnlFileDetail;

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
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane);
		
		pnlFileDetail = new JPanel();
		pnlFileDetail.setPreferredSize(new Dimension(400, 400));
		splitPane.setRightComponent(pnlFileDetail);
		pnlFileDetail.setBorder(new TitledBorder(null, "File", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		
		JPanel pnlFiles = new JPanel();
		pnlFiles.setPreferredSize(new Dimension(200, 400));
		splitPane.setLeftComponent(pnlFiles);
		pnlFiles.setLayout(new BoxLayout(pnlFiles, BoxLayout.X_AXIS));
		
		JScrollPane scrlFiles = new JScrollPane();
		pnlFiles.add(scrlFiles);
		scrlFiles.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		treeFiles = new JTree();
		treeFiles.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent selectionEvent) {
				onTreeSelectionChanged();
			}
		});
		treeFiles.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrlFiles.setViewportView(treeFiles);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMod = new JMenu("Mod");
		menuBar.add(mnMod);
		
		mntmPack = new JMenuItem("Pack");
		mntmPack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				packMod();
			}
		});
		mnMod.add(mntmPack);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				final String ERROR_MESSAGE = "Can't save mod";
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
		mnMod.add(mntmSave);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
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
		mnMod.add(mntmLoad);
		
		JSeparator separator = new JSeparator();
		mnMod.add(separator);
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshFilesList();
				refreshFileDetailsPanel();
			}
		});
		mnMod.add(mntmRefresh);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmFileEdit = new JMenuItem("Edit Text");
		mntmFileEdit.addActionListener(new ActionListener() {
			@SuppressWarnings("incomplete-switch")
			public void actionPerformed(ActionEvent e) {
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
		mnFile.add(mntmFileEdit);
		
		mntmFileOpenJson = new JMenuItem("Open JSON");
		mntmFileOpenJson.addActionListener(new ActionListener() {
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
		mnFile.add(mntmFileOpenJson);
		
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
		
		boolean editorAvailable = false;
		if(selectedFile.getFileType() != null && fileValid){
			switch (selectedFile.getFileType()) {
				case IMAGE:
					mntmFileEdit.setText("Edit Image");
					editorAvailable = Settings.getCurrentSettings().isImageEditorValid();
					break;
				case JSON:
				case TEXT:
					mntmFileEdit.setText("Edit Text");
					editorAvailable = Settings.getCurrentSettings().isTextEditorValid();
					break;
				default:
					break;
			}
			mntmFileEdit.setEnabled( selectedFile.getFileType() != FileType.UNKNOWN && editorAvailable);
			mntmFileOpenJson.setEnabled(selectedFile.getFileType() == FileType.JSON);
		}
		else{
			mntmFileEdit.setEnabled(false);
			mntmFileOpenJson.setEnabled(false);
		}
		
		synchronized (pnlFileDetail.getTreeLock()) {
			setEnabledRecursively(pnlFileDetail, fileValid);
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
	}
	/**
	 * Refresh the settings of the pack button.
	 */
	private void refreshPackButton(){
		if(Settings.getCurrentSettings().isStarboundFolderValid()){
			
		}
		else{
			mntmPack.setEnabled(false);
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
