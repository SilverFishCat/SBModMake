package silver.starbound.data;

import java.io.File;

public class Mod {
	private String _name;
	private File _folder;
	private String _modInfo;
	
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
}
