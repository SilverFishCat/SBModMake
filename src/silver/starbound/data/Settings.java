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
