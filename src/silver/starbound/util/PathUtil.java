package silver.starbound.util;

import java.io.File;
import java.io.StringWriter;

import silver.starbound.data.Settings;
import silver.starbound.util.OSUtil.OperatingSystem;

public class PathUtil {
	private static final String STARBOUND_FOLDER_PATH		= "\\steamapps\\common\\starbound";
	private static final String MOD_FOLDER_PATH				= "\\giraffe_storage\\mods";
	private static final String DEFAULT_MODINFO_FILETYPE	= "modinfo";
	private static final String OSX_TOOL_PATH				= "osx";
	private static final String WINDOWS_TOOL_PATH			= "win";
	private static final String LINUX_TOOL_PATH				= "linux";
	private static final String BIT_64_TOOL_PATH			= "64";
	private static final String BIT_32_TOOL_PATH			= "32";
	private static final String PACKER_FILE_NAME			= "asset_packer";
	
	private static final String DEFAULT_WINDOWS_STARBOUND_PATH	= "C:\\Program Files (x86)\\Steam" + STARBOUND_FOLDER_PATH;
	private static final String DEFAULT_LINUX_STARBOUND_PATH	= "~/.steam/steam/SteamApps/common/starbound";
	private static final String DEFAULT_OSX_STARBOUND_PATH		= "~/Library/Application Support/Steam/SteamApps/";
	
	public static String getStarboundDirectory(){
		OperatingSystem os = OSUtil.getOS();
		
		String path = getAutomaticStarboundDirectory(os);
		
		if(path == null)
			path = getDefaultStarboundDirectory(os);

		return null;
	}
	public static File getModFolder(File starboundDirectory, String modName){
		if(starboundDirectory != null && modName != null)
			return new File(starboundDirectory.getAbsoluteFile() + MOD_FOLDER_PATH + "\\" + modName);
		else
			return null;
	}
	public static String getModinfoFilename(String modName){
		if(modName != null)
			return modName + "." + DEFAULT_MODINFO_FILETYPE;
		else
			return null;
	}
	public static File getToolsFolder(Settings settings){
		if(!settings.isStarboundFolderValid() || !settings.isToolSettingValid()){
			return null;
		}
		
		String toolFolder;
		switch (settings.getOperationSystem()) {
			case OSX:
				toolFolder = OSX_TOOL_PATH;
				break;
			case WINDOWS:
				toolFolder = WINDOWS_TOOL_PATH;
				break;
			case LINUX:
				toolFolder = LINUX_TOOL_PATH;
				break;
	
			default:
				return null;
		}
		
		if(settings.getOperationSystem() == OperatingSystem.WINDOWS || settings.getOperationSystem() == OperatingSystem.LINUX){
			switch (settings.getArchitecture()) {
				case BIT32:
					toolFolder += BIT_32_TOOL_PATH;
					break;
				case BIT64:
					toolFolder += BIT_64_TOOL_PATH;
					break;
		
				default:
					break;
			}
		}
		
		return new File(settings.getStarboundFolder(), toolFolder);
	}
	public static File getPacker(Settings settings){
		if(settings.isToolSettingValid()){
			File toolsFolder = getToolsFolder(settings);
			
			return new File(toolsFolder, PACKER_FILE_NAME + fileSuffix(settings));
		}
		
		return null;
	}
	public static String fileSuffix(Settings settings){
		switch (settings.getOperationSystem()) {
			case WINDOWS:
				return ".exe";
			case OSX:
				return ".sh";
	
			default:
				return "";
		}
	}
	public static String getDefaultStarboundDirectory(OperatingSystem os){
		switch (os) {
			case WINDOWS:
				return DEFAULT_WINDOWS_STARBOUND_PATH;
			case LINUX:
				return DEFAULT_LINUX_STARBOUND_PATH;
			case OSX:
				return DEFAULT_OSX_STARBOUND_PATH;
	
			default:
				return null;
		}
	}
	public static String getAutomaticStarboundDirectory(OperatingSystem os){
		switch (os) {
			case WINDOWS:{
				String steamString = getRegistrySteamDirectoryEntry();
				if(steamString != null){
					return steamString + STARBOUND_FOLDER_PATH;
				}
			}
			case LINUX:
				return null;
			case OSX:
				return null;
	
			default:
				return null;
		}
	}
	
	//--- Window Registry --- //
	private static final String STEAM_REGISTRY_KEY = "HKEY_CURRENT_USER\\Software\\Valve\\Steam"; 
	private static final String STEAM_FOLDER_REGISTRY_MEMBER = "SteamPath";
	
	private static String getRegistrySteamDirectoryEntry(){
		try{
			Process registryProcess = Runtime.getRuntime().exec("reg query \"" + STEAM_REGISTRY_KEY + "\" /v " + STEAM_FOLDER_REGISTRY_MEMBER);
			registryProcess.waitFor();
			StringWriter stringWriter = new StringWriter();
			
			int read = 0;
			while((read = registryProcess.getInputStream().read()) != -1){
				stringWriter.write(read);
			}
			
			String result = stringWriter.toString();
			result.trim();
			String[] lines = result.split("\r\n");
			String valueLine = lines[lines.length - 1];
			String[] parts = valueLine.trim().split("\\s");
			if(parts[0].equals("ERROR:"))
				return null;
			else
				return parts[parts.length - 1];
		}
		catch(Exception ex){
			
		}
		
		return null;
	}
}
