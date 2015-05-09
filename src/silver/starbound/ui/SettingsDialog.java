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
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import silver.starbound.data.Settings;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import silver.starbound.util.PathUtil;
import silver.starbound.util.OSUtil.Architecture;
import silver.starbound.util.OSUtil.OperatingSystem;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * A dialog for displaying and editing settings.
 * 
 * @author SilverFishCat
 *
 */
public class SettingsDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 900094647442468743L;
	
	/**
	 * Possible results of a dialog.
	 * 
	 * @author SilverFishCat
	 *
	 */
	public enum DialogResult{
		OK,
		CANCEL
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField txtStrbndFolder;
	private JTextField txtTextEditor;
	private JTextField txtImageEditor;
	
	private Settings currentSettings;
	private DialogResult result = DialogResult.CANCEL;
	private JComboBox<OperatingSystem> cmbBoxOperatingSystem;
	private JComboBox<Architecture> cmbBoxArchitecture;
	private JTextField txtToolFolder;
	
	/**
	 * Get the settings container in this dialog.
	 * 
	 * @return The settings container in this dialog
	 */
	public Settings getSettings(){
		return currentSettings;
	}
	/**
	 * Get the result of the dialog.
	 * 
	 * @return OK if user wishes to use the new settints, CANCEL otherwise
	 */
	public DialogResult getDialogResult(){
		return result;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param lastSetting The settings set before starting the dialog
	 */
	public SettingsDialog(Settings lastSetting) {
		setResizable(false);
		if(lastSetting != null){
			currentSettings = lastSetting.clone();
		}
		else
			currentSettings = new Settings();
		
		setBounds(100, 100, 350, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JLabel lblStrbndFolder = new JLabel("Starbound folder:");
		GridBagConstraints gbc_lblStrbndFolder = new GridBagConstraints();
		gbc_lblStrbndFolder.insets = new Insets(0, 0, 5, 5);
		gbc_lblStrbndFolder.anchor = GridBagConstraints.WEST;
		gbc_lblStrbndFolder.gridx = 0;
		gbc_lblStrbndFolder.gridy = 0;
		contentPanel.add(lblStrbndFolder, gbc_lblStrbndFolder);
		
		txtStrbndFolder = new JTextField();
		txtStrbndFolder.setText((String) null);
		txtStrbndFolder.setEditable(false);
		txtStrbndFolder.setColumns(20);
		GridBagConstraints gbc_txtStrbndFolder = new GridBagConstraints();
		gbc_txtStrbndFolder.gridwidth = 2;
		gbc_txtStrbndFolder.insets = new Insets(0, 0, 5, 5);
		gbc_txtStrbndFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStrbndFolder.gridx = 1;
		gbc_txtStrbndFolder.gridy = 0;
		contentPanel.add(txtStrbndFolder, gbc_txtStrbndFolder);
		
		JButton btnStrbndFolder = new JButton("Browse");
		btnStrbndFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File modFolder = selectDirectory();
				if(modFolder != null){
					setStarboundFolder(modFolder);
				}
			}
		});
		GridBagConstraints gbc_btnStrbndFolder = new GridBagConstraints();
		gbc_btnStrbndFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStrbndFolder.insets = new Insets(0, 0, 5, 0);
		gbc_btnStrbndFolder.gridx = 3;
		gbc_btnStrbndFolder.gridy = 0;
		contentPanel.add(btnStrbndFolder, gbc_btnStrbndFolder);
		
		JButton btnStrbndFolderFind = new JButton("Find");
		btnStrbndFolderFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final String ERROR_MESSAGE = "Can not find starbound folder";
				String starboundFolderPath = PathUtil.getStarboundDirectory();
				if(starboundFolderPath != null){
					File starboundFolder = new File(starboundFolderPath);
					if(starboundFolder.isDirectory())
						currentSettings.setStarboundFolder(starboundFolder);
					else
						JOptionPane.showMessageDialog(contentPanel, "Found path, but path is invalid", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(contentPanel, "Can't find starbound folder", ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnStrbndFolderFind = new GridBagConstraints();
		gbc_btnStrbndFolderFind.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStrbndFolderFind.insets = new Insets(0, 0, 5, 0);
		gbc_btnStrbndFolderFind.gridx = 3;
		gbc_btnStrbndFolderFind.gridy = 1;
		contentPanel.add(btnStrbndFolderFind, gbc_btnStrbndFolderFind);
		
		JLabel lblTextEditor = new JLabel("Text editor:");
		GridBagConstraints gbc_lblTextEditor = new GridBagConstraints();
		gbc_lblTextEditor.anchor = GridBagConstraints.WEST;
		gbc_lblTextEditor.insets = new Insets(0, 0, 5, 5);
		gbc_lblTextEditor.gridx = 0;
		gbc_lblTextEditor.gridy = 2;
		contentPanel.add(lblTextEditor, gbc_lblTextEditor);
		
		txtTextEditor = new JTextField();
		txtTextEditor.setText((String) null);
		txtTextEditor.setEditable(false);
		txtTextEditor.setColumns(20);
		GridBagConstraints gbc_txtTextEditor = new GridBagConstraints();
		gbc_txtTextEditor.gridwidth = 2;
		gbc_txtTextEditor.insets = new Insets(0, 0, 5, 5);
		gbc_txtTextEditor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTextEditor.gridx = 1;
		gbc_txtTextEditor.gridy = 2;
		contentPanel.add(txtTextEditor, gbc_txtTextEditor);
		
		JButton btnTextEditor = new JButton("Browse");
		btnTextEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File modFolder = selectFile();
				if(modFolder != null){
					setTextEditor(modFolder);
				}
			}
		});
		GridBagConstraints gbc_btnTextEditor = new GridBagConstraints();
		gbc_btnTextEditor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTextEditor.insets = new Insets(0, 0, 5, 0);
		gbc_btnTextEditor.gridx = 3;
		gbc_btnTextEditor.gridy = 2;
		contentPanel.add(btnTextEditor, gbc_btnTextEditor);
		
		JLabel lblImageEditor = new JLabel("Image editor:");
		GridBagConstraints gbc_lblImageEditor = new GridBagConstraints();
		gbc_lblImageEditor.anchor = GridBagConstraints.WEST;
		gbc_lblImageEditor.insets = new Insets(0, 0, 5, 5);
		gbc_lblImageEditor.gridx = 0;
		gbc_lblImageEditor.gridy = 3;
		contentPanel.add(lblImageEditor, gbc_lblImageEditor);
		
		txtImageEditor = new JTextField();
		txtImageEditor.setText((String) null);
		txtImageEditor.setEditable(false);
		txtImageEditor.setColumns(20);
		GridBagConstraints gbc_txtImageEditor = new GridBagConstraints();
		gbc_txtImageEditor.gridwidth = 2;
		gbc_txtImageEditor.insets = new Insets(0, 0, 5, 5);
		gbc_txtImageEditor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtImageEditor.gridx = 1;
		gbc_txtImageEditor.gridy = 3;
		contentPanel.add(txtImageEditor, gbc_txtImageEditor);
		
		JButton btnImageEditor = new JButton("Browse");
		btnImageEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File modFolder = selectFile();
				if(modFolder != null){
					setImageEditor(modFolder);
				}
			}
		});
		GridBagConstraints gbc_btnImageEditor = new GridBagConstraints();
		gbc_btnImageEditor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnImageEditor.insets = new Insets(0, 0, 5, 0);
		gbc_btnImageEditor.gridx = 3;
		gbc_btnImageEditor.gridy = 3;
		contentPanel.add(btnImageEditor, gbc_btnImageEditor);
		
		JLabel lblOperatingSystem = new JLabel("Operating system:");
		GridBagConstraints gbc_lblOperatingSystem = new GridBagConstraints();
		gbc_lblOperatingSystem.anchor = GridBagConstraints.EAST;
		gbc_lblOperatingSystem.insets = new Insets(0, 0, 5, 5);
		gbc_lblOperatingSystem.gridx = 0;
		gbc_lblOperatingSystem.gridy = 4;
		contentPanel.add(lblOperatingSystem, gbc_lblOperatingSystem);
		
		cmbBoxOperatingSystem = new JComboBox<OperatingSystem>();
		cmbBoxOperatingSystem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent comboboxChanged) {
				setOperatingSystem(cmbBoxOperatingSystem.getItemAt(cmbBoxOperatingSystem.getSelectedIndex()));
			}
		});
		List<OperatingSystem> osList = new ArrayList<OperatingSystem>();
		for (OperatingSystem operatingSystem : OperatingSystem.values()) {
			osList.add(operatingSystem);
		}
		osList.remove(OperatingSystem.UNKNOWN);
		cmbBoxOperatingSystem.setModel(new DefaultComboBoxModel<OperatingSystem>(osList.toArray(OperatingSystem.values())));
		GridBagConstraints gbc_cmbBoxOperatingSystem = new GridBagConstraints();
		gbc_cmbBoxOperatingSystem.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxOperatingSystem.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxOperatingSystem.gridx = 1;
		gbc_cmbBoxOperatingSystem.gridy = 4;
		contentPanel.add(cmbBoxOperatingSystem, gbc_cmbBoxOperatingSystem);
		
		JLabel lblArchitecture = new JLabel("Architecture:");
		GridBagConstraints gbc_lblArchitecture = new GridBagConstraints();
		gbc_lblArchitecture.anchor = GridBagConstraints.WEST;
		gbc_lblArchitecture.insets = new Insets(0, 0, 5, 5);
		gbc_lblArchitecture.gridx = 0;
		gbc_lblArchitecture.gridy = 5;
		contentPanel.add(lblArchitecture, gbc_lblArchitecture);
		
		cmbBoxArchitecture = new JComboBox<Architecture>();
		cmbBoxArchitecture.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setArchitecture(cmbBoxArchitecture.getItemAt(cmbBoxArchitecture.getSelectedIndex()));
			}
		});
		List<Architecture> archList = new ArrayList<Architecture>();
		for (Architecture architecture : Architecture.values()) {
			archList.add(architecture);
		}
		archList.remove(Architecture.UNKNOWN);
		cmbBoxArchitecture.setModel(new DefaultComboBoxModel<Architecture>(archList.toArray(Architecture.values())));
		GridBagConstraints gbc_cmbBoxArchitecture = new GridBagConstraints();
		gbc_cmbBoxArchitecture.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxArchitecture.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxArchitecture.gridx = 1;
		gbc_cmbBoxArchitecture.gridy = 5;
		contentPanel.add(cmbBoxArchitecture, gbc_cmbBoxArchitecture);
		
		JLabel lblToolFolder = new JLabel("Tool folder:");
		GridBagConstraints gbc_lblToolFolder = new GridBagConstraints();
		gbc_lblToolFolder.anchor = GridBagConstraints.WEST;
		gbc_lblToolFolder.insets = new Insets(0, 0, 0, 5);
		gbc_lblToolFolder.gridx = 0;
		gbc_lblToolFolder.gridy = 6;
		contentPanel.add(lblToolFolder, gbc_lblToolFolder);
		
		txtToolFolder = new JTextField();
		txtToolFolder.setEditable(false);
		GridBagConstraints gbc_txtToolFolder = new GridBagConstraints();
		gbc_txtToolFolder.gridwidth = 2;
		gbc_txtToolFolder.insets = new Insets(0, 0, 0, 5);
		gbc_txtToolFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtToolFolder.gridx = 1;
		gbc_txtToolFolder.gridy = 6;
		contentPanel.add(txtToolFolder, gbc_txtToolFolder);
		txtToolFolder.setColumns(20);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = DialogResult.OK;
				setVisible(false);
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = DialogResult.CANCEL;
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setStarboundFolder(currentSettings.getStarboundFolder());
		setTextEditor(currentSettings.getTextEditor());
		cmbBoxOperatingSystem.setSelectedItem(currentSettings.getOperationSystem());
		cmbBoxArchitecture.setSelectedItem(currentSettings.getArchitecture());
		
		pack();
		
		refreshForm();
	}
	
	/**
	 * Set the starbound directory.
	 * 
	 * @param directory The starbound directory
	 */
	private void setStarboundFolder(File directory){
		currentSettings.setStarboundFolder(directory);
		
		if(currentSettings.getStarboundFolder() != null)
			txtStrbndFolder.setText(currentSettings.getStarboundFolder().getAbsolutePath());
		else
			txtStrbndFolder.setText("");
		
		refreshToolPath();
	}
	/**
	 * Set the text editor.
	 * 
	 * @param editor The text editor
	 */
	private void setTextEditor(File editor){
		currentSettings.setTextEditor(editor);
		
		if(currentSettings.getTextEditor() != null)
			txtTextEditor.setText(currentSettings.getTextEditor().getAbsolutePath());
		else
			txtTextEditor.setText("");
	}
	/**
	 * Set the image editor.
	 * 
	 * @param editor The image editor
	 */
	private void setImageEditor(File editor){
		currentSettings.setImageEditor(editor);
		
		if(currentSettings.getImageEditor() != null)
			txtImageEditor.setText(currentSettings.getImageEditor().getAbsolutePath());
		else
			txtImageEditor.setText("");
	}
	/**
	 * Set the preferred operating system to get the tools for.
	 * 
	 * @param operatingSystem The operating system selected
	 */
	private void setOperatingSystem(OperatingSystem operatingSystem){
		currentSettings.setOperationSystem(operatingSystem);
		
		refreshArchitectureComboBox();
		refreshToolPath();
	}
	/**
	 * Set the preferred architecture.
	 * 
	 * @param architecture The preferred architecture
	 */
	private void setArchitecture(Architecture architecture){
		currentSettings.setArchitecture(architecture);
		
		refreshToolPath();
	}
	
	/**
	 * Refresh the settings of the architecture combo box.
	 */
	private void refreshArchitectureComboBox(){
		cmbBoxArchitecture.setEnabled(currentSettings.isArchitectureSpecific());
	}
	/**
	 * Refresh the settings generated tool directory path.
	 */
	private void refreshToolPath(){
		if(currentSettings.isToolSettingValid())
			txtToolFolder.setText(currentSettings.getToolFolder().getAbsolutePath());
		else
			txtToolFolder.setText("");
	}
	/**
	 * Refresh the settings of the entire form.
	 */
	private void refreshForm(){
		refreshArchitectureComboBox();
		refreshToolPath();
	}
	
	/**
	 * Select a directory.
	 * 
	 * @return A directory selected by the user
	 */
	private File selectDirectory(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Select folder");
		int dialogResult = fc.showOpenDialog(contentPanel);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
	/**
	 * Select a file
	 * 
	 * @return A file selected by the user
	 */
	private File selectFile(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogTitle("Select file");
		int dialogResult = fc.showOpenDialog(contentPanel);
		
		if(dialogResult == JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		else{
			return null;
		}
	}
}
