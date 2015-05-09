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

import java.io.StringWriter;

public class RegistryUtil {
	private static final String STARBOUND_REGISTRY_KEY = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\Steam App 211820"; 
	private static final String STARBOUND_FOLDER_REGISTRY_MEMBER = "InstallLocation";
	private static final String STEAM_REGISTRY_KEY = "HKEY_CURRENT_USER\\Software\\Valve\\Steam"; 
	private static final String STEAM_FOLDER_REGISTRY_MEMBER = "SteamPath";
	
	/**
	 * Get the starbound directory from the registry.
	 * 
	 * @return The starbound directory, null if error
	 */
	public static String getRegistryStarboundDirectoryEntry(){
		return getValueFromRegistry(STARBOUND_REGISTRY_KEY, STARBOUND_FOLDER_REGISTRY_MEMBER);
	}
	/**
	 * Get the steam directory from the registry.
	 * 
	 * @return The steam directory, null if error
	 */
	public static String getRegistrySteamDirectoryEntry(){
		return getValueFromRegistry(STEAM_REGISTRY_KEY, STEAM_FOLDER_REGISTRY_MEMBER);
	}
	/**
	 * Get a value from registry.
	 * 
	 * @param key The registry key
	 * @param parameter The paramter to get
	 * @return The value of the parameter in the given key
	 */
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
