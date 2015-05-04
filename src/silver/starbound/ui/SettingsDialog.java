package silver.starbound.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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

import silver.starbound.util.OSUtil.Architecture;
import silver.starbound.util.OSUtil.OperatingSystem;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class SettingsDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 900094647442468743L;
	
	public enum DialogResult{
		OK,
		CANCEL
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField txtStrbndFolder;
	private JTextField txtEditor;
	
	private Settings currentSettings;
	private DialogResult result = DialogResult.CANCEL;
	private JComboBox<OperatingSystem> cmbBoxOperatingSystem;
	private JComboBox<Architecture> cmbBoxArchitecture;
	private JTextField txtToolFolder;
	
	public Settings getSettings(){
		return currentSettings;
	}
	public DialogResult getDialogResult(){
		return result;
	}

	/**
	 * Create the dialog.
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
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_btnStrbndFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnStrbndFolder.gridx = 3;
		gbc_btnStrbndFolder.gridy = 0;
		contentPanel.add(btnStrbndFolder, gbc_btnStrbndFolder);
		
		JLabel lblEditor = new JLabel("Text editor:");
		GridBagConstraints gbc_lblEditor = new GridBagConstraints();
		gbc_lblEditor.anchor = GridBagConstraints.WEST;
		gbc_lblEditor.insets = new Insets(0, 0, 5, 5);
		gbc_lblEditor.gridx = 0;
		gbc_lblEditor.gridy = 1;
		contentPanel.add(lblEditor, gbc_lblEditor);
		
		txtEditor = new JTextField();
		txtEditor.setText((String) null);
		txtEditor.setEditable(false);
		txtEditor.setColumns(20);
		GridBagConstraints gbc_txtEditor = new GridBagConstraints();
		gbc_txtEditor.gridwidth = 2;
		gbc_txtEditor.insets = new Insets(0, 0, 5, 5);
		gbc_txtEditor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEditor.gridx = 1;
		gbc_txtEditor.gridy = 1;
		contentPanel.add(txtEditor, gbc_txtEditor);
		
		JButton btnEditor = new JButton("Browse");
		btnEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File modFolder = selectFile();
				if(modFolder != null){
					setTextEditor(modFolder);
				}
			}
		});
		GridBagConstraints gbc_btnEditor = new GridBagConstraints();
		gbc_btnEditor.insets = new Insets(0, 0, 5, 5);
		gbc_btnEditor.gridx = 3;
		gbc_btnEditor.gridy = 1;
		contentPanel.add(btnEditor, gbc_btnEditor);
		
		JLabel lblOperatingSystem = new JLabel("Operating system:");
		GridBagConstraints gbc_lblOperatingSystem = new GridBagConstraints();
		gbc_lblOperatingSystem.anchor = GridBagConstraints.EAST;
		gbc_lblOperatingSystem.insets = new Insets(0, 0, 5, 5);
		gbc_lblOperatingSystem.gridx = 0;
		gbc_lblOperatingSystem.gridy = 2;
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
		gbc_cmbBoxOperatingSystem.gridy = 2;
		contentPanel.add(cmbBoxOperatingSystem, gbc_cmbBoxOperatingSystem);
		
		JLabel lblArchitecture = new JLabel("Architecture:");
		GridBagConstraints gbc_lblArchitecture = new GridBagConstraints();
		gbc_lblArchitecture.anchor = GridBagConstraints.WEST;
		gbc_lblArchitecture.insets = new Insets(0, 0, 5, 5);
		gbc_lblArchitecture.gridx = 0;
		gbc_lblArchitecture.gridy = 3;
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
		gbc_cmbBoxArchitecture.gridy = 3;
		contentPanel.add(cmbBoxArchitecture, gbc_cmbBoxArchitecture);
		
		JLabel lblToolFolder = new JLabel("Tool folder:");
		GridBagConstraints gbc_lblToolFolder = new GridBagConstraints();
		gbc_lblToolFolder.anchor = GridBagConstraints.WEST;
		gbc_lblToolFolder.insets = new Insets(0, 0, 5, 5);
		gbc_lblToolFolder.gridx = 0;
		gbc_lblToolFolder.gridy = 4;
		contentPanel.add(lblToolFolder, gbc_lblToolFolder);
		
		txtToolFolder = new JTextField();
		txtToolFolder.setEditable(false);
		GridBagConstraints gbc_txtToolFolder = new GridBagConstraints();
		gbc_txtToolFolder.gridwidth = 2;
		gbc_txtToolFolder.insets = new Insets(0, 0, 5, 5);
		gbc_txtToolFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtToolFolder.gridx = 1;
		gbc_txtToolFolder.gridy = 4;
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
		setTextEditor(currentSettings.getEditor());
		cmbBoxOperatingSystem.setSelectedItem(currentSettings.getOperationSystem());
		cmbBoxArchitecture.setSelectedItem(currentSettings.getArchitecture());
		
		pack();
		
		refreshForm();
	}
	
	private void setStarboundFolder(File folder){
		currentSettings.setStarboundFolder(folder);
		
		if(currentSettings.getStarboundFolder() != null)
			txtStrbndFolder.setText(currentSettings.getStarboundFolder().getAbsolutePath());
		else
			txtStrbndFolder.setText("");
		
		refreshToolPath();
	}
	private void setTextEditor(File editor){
		currentSettings.setEditor(editor);
		
		if(currentSettings.getEditor() != null)
			txtEditor.setText(currentSettings.getEditor().getAbsolutePath());
		else
			txtEditor.setText("");
	}
	private void setOperatingSystem(OperatingSystem operatingSystem){
		currentSettings.setOperationSystem(operatingSystem);
		
		refreshArchitectureComboBox();
		refreshToolPath();
	}
	private void setArchitecture(Architecture architecture){
		currentSettings.setArchitecture(architecture);
		
		refreshToolPath();
	}
	
	private void refreshArchitectureComboBox(){
		cmbBoxArchitecture.setEnabled(currentSettings.isArchitectureSpecific());
	}
	private void refreshToolPath(){
		if(currentSettings.isToolSettingValid())
			txtToolFolder.setText(currentSettings.getToolFolder().getAbsolutePath());
		else
			txtToolFolder.setText("");
	}
	private void refreshForm(){
		refreshArchitectureComboBox();
		refreshToolPath();
	}

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
