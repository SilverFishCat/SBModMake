package silver.starbound.data;

import java.io.File;

import silver.starbound.util.OSUtil.Architecture;
import silver.starbound.util.OSUtil.OperatingSystem;
import silver.starbound.util.PathUtil;

public class Settings {
	private File _starboundFolder;
	private File _editor;
	private OperatingSystem _operationSystem;
	
	private Architecture _architecture;
	
	public File getStarboundFolder(){
		return _starboundFolder;
	}
	public File getEditor(){
		return _editor;
	}
	public OperatingSystem getOperationSystem() {
		return _operationSystem;
	}
	public Architecture getArchitecture() {
		return _architecture;
	}
	public File getToolFolder() {
		return PathUtil.getToolsFolder(this);
	}
	
	public void setStarboundFolder(File starboundFolder){
		this._starboundFolder = starboundFolder;
	}
	public void setEditor(File editor){
		this._editor = editor;
	}
	public void setOperationSystem(OperatingSystem operationSystem) {
		this._operationSystem = operationSystem;
	}
	public void setArchitecture(Architecture architecture) {
		this._architecture = architecture;
	}
	
	public boolean isArchitectureSpecific(){
		return _operationSystem != null &&
				(_operationSystem == OperatingSystem.LINUX || _operationSystem == OperatingSystem.WINDOWS);
	}
	
	public boolean isStarboundFolderValid(){
		return _starboundFolder != null && _starboundFolder.isDirectory();
	}
	public boolean isEditorValid(){
		return _editor != null && _editor.isFile();
	}
	public boolean isToolSettingValid(){
		return !(getOperationSystem() == null || getOperationSystem() == OperatingSystem.UNKNOWN
			|| ((getArchitecture() == null || getArchitecture() == Architecture.UNKNOWN) && getOperationSystem() != OperatingSystem.OSX));
	}
	
	public Settings clone(){
		Settings clone = new Settings();
		
		clone.setEditor(getEditor());
		clone.setStarboundFolder(getStarboundFolder());
		clone.setArchitecture(getArchitecture());
		clone.setOperationSystem(getOperationSystem());
		
		return clone;
	}
}
