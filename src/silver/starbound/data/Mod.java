package silver.starbound.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

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
	public JSONObject getJSON(){
		JSONObject result = new JSONObject();
		
		result.put(JSON_NAME_KEY, getName());
		result.put(JSON_FOLDER_PATH_KEY, getFolder().getAbsolutePath());
		result.put(JSON_MOD_INFO_KEY, getModInfo());
		
		return result;
	}
	public static Mod parseJSON(String jsonString) throws IllegalArgumentException, JSONException{
		return parseJSON(new JSONObject(jsonString));
	}
	public static Mod parseJSON(JSONObject jsonObject) throws IllegalArgumentException, JSONException{
		String name = null;
		File folder = null;
		String modinfo = null;
		
		if(jsonObject.has(JSON_NAME_KEY)){
			name = jsonObject.getString(JSON_NAME_KEY);
		}
		if(jsonObject.has(JSON_FOLDER_PATH_KEY)){
			String folderPath = jsonObject.getString(JSON_FOLDER_PATH_KEY);
			folder = new File(folderPath);
		}
		if(jsonObject.has(JSON_MOD_INFO_KEY)){
			modinfo = jsonObject.getString(JSON_MOD_INFO_KEY);
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
	public static Mod loadFromFile(File file) throws IllegalArgumentException, IOException, JSONException{
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
