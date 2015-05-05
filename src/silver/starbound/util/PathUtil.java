package silver.starbound.util;

import java.io.File;
import java.io.StringWriter;

import silver.starbound.data.Settings;
import silver.starbound.util.OSUtil.OperatingSystem;

public class PathUtil {
	private static String STARBOUND_FOLDER_PATH		= "\\steamapps\\common\\starbound";
	private static String MOD_FOLDER_PATH			= "\\giraffe_storage\\mods";
	private static String DEFAULT_MODINFO_FILETYPE	= "modinfo";
	private static String OSX_TOOL_PATH				= "osx";
	private static String WINDOWS_TOOL_PATH			= "win";
	private static String LINUX_TOOL_PATH			= "linux";
	private static String BIT_64_TOOL_PATH			= "64";
	private static String BIT_32_TOOL_PATH			= "32";
	private static String PACKER_FILE_NAME			= "asset_packer";
	
	public static String getStarboundDirectory(){
		OperatingSystem os = OSUtil.getOS();
		if(os == OperatingSystem.WINDOWS){
			String steamString = getRegistrySteamDirectoryEntry();
			if(steamString != null){
				return steamString + STARBOUND_FOLDER_PATH;
			}
		}
		return null;
	}
	public static File getModsFolder(Settings settings){
		return getModFolder(settings, "");
	}
	public static File getModFolder(Settings settings, String modName){
		File starboundDirectory = settings.getStarboundFolder();
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
	
	private static String getRegistrySteamDirectoryEntry(){
		try{
			Process registryProcess = Runtime.getRuntime().exec("reg query \"HKEY_CURRENT_USER\\Software\\Valve\\Steam\" /v SteamPath");
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
