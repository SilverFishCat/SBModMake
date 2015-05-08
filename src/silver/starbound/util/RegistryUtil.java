package silver.starbound.util;

import java.io.StringWriter;

public class RegistryUtil {
	private static final String STARBOUND_REGISTRY_KEY = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\Steam App 211820"; 
	private static final String STARBOUND_FOLDER_REGISTRY_MEMBER = "InstallLocation";
	private static final String STEAM_REGISTRY_KEY = "HKEY_CURRENT_USER\\Software\\Valve\\Steam"; 
	private static final String STEAM_FOLDER_REGISTRY_MEMBER = "SteamPath";
	
	public static String getRegistryStarboundDirectoryEntry(){
		return getValueFromRegistry(STARBOUND_REGISTRY_KEY, STARBOUND_FOLDER_REGISTRY_MEMBER);
	}
	public static String getRegistrySteamDirectoryEntry(){
		return getValueFromRegistry(STEAM_REGISTRY_KEY, STEAM_FOLDER_REGISTRY_MEMBER);
	}
	public static String getValueFromRegistry(String key, String parameter){
		try{
			Process registryProcess = Runtime.getRuntime().exec("reg query \"" + key + "\" /v " + parameter);
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
			// TODO handle exception
		}
		
		return null;
	}
}
