package silver.starbound.data;

import java.io.File;

import silver.starbound.util.OSUtil.Architecture;
import silver.starbound.util.OSUtil.OperatingSystem;
import silver.starbound.util.PathUtil;

public class Settings {
	private File _starboundFolder;
	private File _textEditor;
	private File _imageEditor;
	private OperatingSystem _operationSystem;
	
	private Architecture _architecture;
	
	public File getStarboundFolder(){
		return _starboundFolder;
	}
	public File getTextEditor(){
		return _textEditor;
	}
	public File getImageEditor(){
		return _imageEditor;
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
	public File getModsFolder(){
		return PathUtil.getModsFolder(this);
	}
	
	public void setStarboundFolder(File starboundFolder){
		this._starboundFolder = starboundFolder;
	}
	public void setTextEditor(File editor){
		this._textEditor = editor;
	}
	public void setImageEditor(File editor){
		this._imageEditor = editor;
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
	public boolean isTextEditorValid(){
		return _textEditor != null && _textEditor.isFile();
	}
	public boolean isImageEditorValid(){
		return _imageEditor != null && _imageEditor.isFile();
	}
	public boolean isToolSettingValid(){
		return !(getOperationSystem() == null || getOperationSystem() == OperatingSystem.UNKNOWN
			|| ((getArchitecture() == null || getArchitecture() == Architecture.UNKNOWN) && getOperationSystem() != OperatingSystem.OSX));
	}
	
	public Settings clone(){
		Settings clone = new Settings();
		
		clone.setTextEditor(getTextEditor());
		clone.setStarboundFolder(getStarboundFolder());
		clone.setArchitecture(getArchitecture());
		clone.setOperationSystem(getOperationSystem());
		
		return clone;
	}
}
