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

package silver.starbound.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Mod {
	private static final String JSON_NAME_KEY = "name";
	private static final String JSON_FOLDER_PATH_KEY = "folder";
	private static final String JSON_MOD_INFO_KEY = "modinfo_filename";
	
	private String _name;
	private File _folder;
	private String _modInfo;
	
	public Mod(){
		_name = null;
		_folder = null;
		_modInfo = null;
	}
	public Mod(String name, File folder, String modInfo){
		_name = name;
		_folder = folder;
		_modInfo = modInfo;
	}
	
	public String getName() {
		return _name;
	}
	public File getFolder() {
		return _folder;
	}
	public String getModInfo() {
		return _modInfo;
	}
	public File getModinfoFile(){
		if(getFolder() != null && getModInfo() != null)
			return new File(getFolder(), getModInfo());
		else
			return null;
	}
	
	public void setName(String name) {
		this._name = name;
	}
	public void setFolder(File folder) {
		this._folder = folder;
	}
	public void setModInfo(String modInfo) {
		this._modInfo = modInfo;
	}
	
	public boolean isNameValid(){
		return _name != null && !_name.trim().isEmpty();
	}
	public boolean isFolderValid(){
		return getFolder() != null && getFolder().isDirectory();
	}
	public boolean isModInfoValid(){
		return isFolderValid() && _modInfo != null && !_modInfo.trim().isEmpty();
	}
	
	public String getDefaultModSaveFileName(){
		if(isNameValid())
			return _name + ".save";
		else
			return "mod.save";
	}
	public JsonObject getJSON(){
		JsonObject result = new JsonObject();
		
		result.add(JSON_NAME_KEY, new JsonPrimitive(getName()));
		result.add(JSON_FOLDER_PATH_KEY, new JsonPrimitive(getFolder().getAbsolutePath()));
		result.add(JSON_MOD_INFO_KEY, new JsonPrimitive(getModInfo()));
		
		return result;
	}
	public static Mod parseJSON(String jsonString) throws IllegalArgumentException, JsonParseException{
		return parseJSON(new JsonParser().parse(jsonString));
	}
	public static Mod parseJSON(JsonElement jsonElement) throws IllegalArgumentException, JsonParseException{
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String name = null;
		File folder = null;
		String modinfo = null;
		
		if(jsonObject.has(JSON_NAME_KEY)){
			name = jsonObject.get(JSON_NAME_KEY).getAsString();
		}
		if(jsonObject.has(JSON_FOLDER_PATH_KEY)){
			String folderPath = jsonObject.get(JSON_FOLDER_PATH_KEY).getAsString();
			folder = new File(folderPath);
		}
		if(jsonObject.has(JSON_MOD_INFO_KEY)){
			modinfo = jsonObject.get(JSON_MOD_INFO_KEY).getAsString();
		}
		
		return new Mod(name, folder, modinfo);
	}
	public void saveToFile(File file) throws IllegalArgumentException, IOException{
		boolean created = file.createNewFile();
		try{
			if(file.canWrite()){
				FileWriter writer = null; 
				try{
					writer = new FileWriter(file);
					
					writer.write(getJSON().toString());
				}
				finally{
					if(writer != null)
						writer.close();
				}
			}
			else{
				throw new IllegalArgumentException("Can not write to file");
			}
		}
		catch(Exception ex){
			if(created)
				file.delete();
			
			throw ex;
		}
	}
	public static Mod loadFromFile(File file) throws IllegalArgumentException, IOException, JsonParseException{
		if(file.canRead()){
			FileReader reader = null;
			try{
				StringBuffer jsonStringBuffer = new StringBuffer();
				char[] buffer = new char[1024];
				reader = new FileReader(file);
				int read = 0;
				while((read = reader.read(buffer)) != -1){
					jsonStringBuffer.append(String.valueOf(buffer, 0, read));
				}
				return parseJSON(jsonStringBuffer.toString());
			}
			finally{
				if(reader != null)
					reader.close();
			}
		}
		else{
			throw new IllegalArgumentException("Can not read file");
		}
	}
}
