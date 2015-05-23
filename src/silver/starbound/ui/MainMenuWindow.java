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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import silver.starbound.data.Mod;
import silver.starbound.data.Settings;
import silver.starbound.ui.util.FileUtil;
import silver.starbound.util.SettingsUtil;

import com.google.gson.JsonParseException;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;

/**
 * The main menu window for the applicaiton.
 * 
 * @author SilverFishCat
 *
 */
public class MainMenuWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2752516398621133183L;

	/**
	 * Create the dialog.
	 */
	public MainMenuWindow() {
		setMinimumSize(new Dimension(190, 0));
		setResizable(false);
		setTitle("ModMake");
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.GLUE_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.GLUE_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent closeEvent) {
				SettingsUtil.saveSettings(Settings.getCurrentSettings());
			}
		});
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnCreateMod = new JButton("Create Mod");
		btnCreateMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ModCreationDialog(new ModCreationDialog.DialogResultListener() {
					@Override
					public void onModCreated(Mod mod) {
						openMod(mod);
					}
				}).setVisible(true);;
			}
		});
		getContentPane().add(btnCreateMod, "2, 2");
		
		JButton btnLoadMod = new JButton("Load Mod");
		btnLoadMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				final String ERROR_MESSAGE = "Can't load mod";
				File file = FileUtil.selectFile(MainMenuWindow.this,"Select save file", new File(Settings.getCurrentSettings().getModsFolder(), "mod.save"));
				if(file != null){
					try {
						Mod mod = Mod.loadFromFile(file);
						
						openMod(mod);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(MainMenuWindow.this, e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(MainMenuWindow.this, "Error in file:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					} catch (JsonParseException e){
						e.printStackTrace();
						JOptionPane.showMessageDialog(MainMenuWindow.this, "Error in json object:\n " + e.getMessage(), ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		getContentPane().add(btnLoadMod, "2, 4");
		
		JButton btnSetttings = new JButton("Setttings");
		btnSetttings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsDialog dialog = new SettingsDialog(Settings.getCurrentSettings(), new SettingsDialog.DialogResultListener() {
					@Override
					public void onSettingsCreated(Settings settings) {
						Settings.setCurrentSettings(settings);
					}
				});
				dialog.setVisible(true);
			}
		});
		getContentPane().add(btnSetttings, "2, 6");
		
		pack();
	}

	public void openMod(Mod mod){
		ModWindow window = new ModWindow(mod);
		window.setLocationRelativeTo(MainMenuWindow.this);
		window.setVisible(true);
	}
}
