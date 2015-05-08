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
			result.setEditor(new File(textEditorPath));
		else
			result.setEditor(null);
		
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

	public static void saveSettings(Settings settings){
		Preferences node = Preferences.userRoot().node(PREFERENCES_NODE_NAME);
		if(settings.getStarboundFolder() != null)
			node.put(PREFERENCES_STARBOUND_FOLDER_KEY, settings.getStarboundFolder().getAbsolutePath());

		if(settings.getEditor() != null)
			node.put(PREFERENCES_TEXT_EDIOR_KEY, settings.getEditor().getAbsolutePath());
		
		if(settings.getOperationSystem() != null)
			node.put(PREFERENCES_OPERATING_SYSTEM_KEY, settings.getOperationSystem().toString());
		
		if(settings.getArchitecture() != null)
			node.put(PREFERENCES_ARCHITECTURE_KEY, settings.getArchitecture().toString());
	}
}
