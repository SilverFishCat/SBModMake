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

package silver.starbound.util;

import java.io.File;
import java.util.prefs.Preferences;

import silver.starbound.data.Settings;
import silver.starbound.util.OSUtil.Architecture;
import silver.starbound.util.OSUtil.OperatingSystem;

public class SettingsUtil {
	private static final String PREFERENCES_NODE_NAME = "SBModMake";
	private static final String PREFERENCES_STARBOUND_FOLDER_KEY = "starbound_folder";
	private static final String PREFERENCES_TEXT_EDIOR_KEY = "text_editor";
	private static final String PREFERENCES_OPERATING_SYSTEM_KEY = "operating_system";
	private static final String PREFERENCES_ARCHITECTURE_KEY = "architecture";
	
	/**
	 * Load the settings from preferences.
	 * 
	 * @return The settings in the preferences, null for every setting missing 
	 */
	public static Settings loadSettings(){
		Settings result = new Settings();
		
		Preferences node = Preferences.userRoot().node(PREFERENCES_NODE_NAME);
		String starboundPath = node.get(PREFERENCES_STARBOUND_FOLDER_KEY, null);
		if(starboundPath == null)
			starboundPath = PathUtil.getStarboundDirectory();
		if(starboundPath != null)
			result.setStarboundFolder(new File(starboundPath));
		else
			result.setStarboundFolder(null);
		
		String textEditorPath = node.get(PREFERENCES_TEXT_EDIOR_KEY, null);
		if(textEditorPath != null)
			result.setTextEditor(new File(textEditorPath));
		else
			result.setTextEditor(null);
		
		String operatingSystemString = node.get(PREFERENCES_OPERATING_SYSTEM_KEY, OSUtil.getOS().toString());
		if(operatingSystemString != null){
			try{
				OperatingSystem operationSystem = OperatingSystem.valueOf(operatingSystemString);
				result.setOperationSystem(operationSystem);
			}
			catch(IllegalArgumentException ex){
				result.setOperationSystem(null);
			}
		}
		else
			result.setOperationSystem(null);
		
		String architectureString = node.get(PREFERENCES_ARCHITECTURE_KEY, OSUtil.getOS().toString());
		if(architectureString != null){
			try{
				Architecture architecture = Architecture.valueOf(architectureString);
				result.setArchitecture(architecture);
			}
			catch(IllegalArgumentException ex){
				result.setArchitecture(null);
			}
		}
		else
			result.setArchitecture(null);
		
		return result;
	}

	/**
	 * Save the settings into the preferences.
	 * 
	 * @param settings The settings to save
	 */
	public static void saveSettings(Settings settings){
		Preferences node = Preferences.userRoot().node(PREFERENCES_NODE_NAME);
		if(settings.getStarboundFolder() != null)
			node.put(PREFERENCES_STARBOUND_FOLDER_KEY, settings.getStarboundFolder().getAbsolutePath());

		if(settings.getTextEditor() != null)
			node.put(PREFERENCES_TEXT_EDIOR_KEY, settings.getTextEditor().getAbsolutePath());
		
		if(settings.getOperationSystem() != null)
			node.put(PREFERENCES_OPERATING_SYSTEM_KEY, settings.getOperationSystem().toString());
		
		if(settings.getArchitecture() != null)
			node.put(PREFERENCES_ARCHITECTURE_KEY, settings.getArchitecture().toString());
	}
}
