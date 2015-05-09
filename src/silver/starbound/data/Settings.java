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

/**
 * An object containing all the user settings for the ModMake application.
 * All unset settings can be null or for enums, UNKNOWN.
 * 
 * @author SilverFishCat
 *
 */
public class Settings {
	private File _starboundFolder;
	private File _textEditor;
	private File _imageEditor;
	private OperatingSystem _operationSystem;
	
	private Architecture _architecture;
	
	/**
	 * Get the starbound directory.
	 * 
	 * @return The starbound directory
	 */
	public File getStarboundFolder(){
		return _starboundFolder;
	}
	/**
	 * Get the text editor.
	 * 
	 * @return The text editor
	 */
	public File getTextEditor(){
		return _textEditor;
	}
	/**
	 * Get the image editor.
	 * 
	 * @return The image editor
	 */
	public File getImageEditor(){
		return _imageEditor;
	}
	/**
	 * Get the selected operating system.
	 * 
	 * @return The selected operating system
	 */
	public OperatingSystem getOperationSystem() {
		return _operationSystem;
	}
	/**
	 * Get the operating system architecture.
	 * 
	 * @return The operating system architecture
	 */
	public Architecture getArchitecture() {
		return _architecture;
	}
	/**
	 * Get the starbound tool directory.
	 * 
	 * @return The starbound tool directory
	 */
	public File getToolFolder() {
		return PathUtil.getToolsFolder(this);
	}
	/**
	 * Get the mods directory.
	 * 
	 * @return The mods directory
	 */
	public File getModsFolder(){
		return PathUtil.getModsFolder(this);
	}
	
	/**
	 * Set the starbound directory.
	 * 
	 * @param starboundFolder The starbound directory
	 */
	public void setStarboundFolder(File starboundFolder){
		this._starboundFolder = starboundFolder;
	}
	/**
	 * Set the text editor.
	 * 
	 * @param editor The text editor
	 */
	public void setTextEditor(File editor){
		this._textEditor = editor;
	}
	/**
	 * Set the image editor.
	 * 
	 * @param editor The image editor
	 */
	public void setImageEditor(File editor){
		this._imageEditor = editor;
	}
	/**
	 * Set the operating system.
	 * 
	 * @param operationSystem The operating system
	 */
	public void setOperationSystem(OperatingSystem operationSystem) {
		this._operationSystem = operationSystem;
	}
	/**
	 * Set the operating system architecture.
	 * 
	 * @param architecture The operating system architecture
	 */
	public void setArchitecture(Architecture architecture) {
		this._architecture = architecture;
	}
	
	/**
	 * Check whether the arhcitecture is relevant for the given os.
	 * 
	 * @return True if the arhcitecture is relevant for the given os
	 */
	public boolean isArchitectureSpecific(){
		return _operationSystem != null &&
				(_operationSystem == OperatingSystem.LINUX || _operationSystem == OperatingSystem.WINDOWS);
	}
	
	/**
	 * Check if the starbound directory is a valid directory.
	 * 
	 * @return True if the starbound directory is valid
	 */
	public boolean isStarboundFolderValid(){
		return _starboundFolder != null && _starboundFolder.isDirectory();
	}
	/**
	 * Check if the text editor is a valid file.
	 * 
	 * @return True if the text editor is valid
	 */
	public boolean isTextEditorValid(){
		return _textEditor != null && _textEditor.isFile();
	}
	/**
	 * Check if the image editor is a valid file.
	 * 
	 * @return True if the image editor is valid
	 */
	public boolean isImageEditorValid(){
		return _imageEditor != null && _imageEditor.isFile();
	}
	/**
	 * Check if the tool directory can be retreived from the given settings.
	 * 
	 * @return True if the tool directory can be retreived
	 */
	public boolean isToolSettingValid(){
		return !(getOperationSystem() == null || getOperationSystem() == OperatingSystem.UNKNOWN
			|| ((getArchitecture() == null || getArchitecture() == Architecture.UNKNOWN) && getOperationSystem() != OperatingSystem.OSX));
	}
	
	/**
	 * Makes a shallow copy of this settings object.
	 */
	public Settings clone(){
		Settings clone = new Settings();
		
		clone.setTextEditor(getTextEditor());
		clone.setStarboundFolder(getStarboundFolder());
		clone.setArchitecture(getArchitecture());
		clone.setOperationSystem(getOperationSystem());
		
		return clone;
	}
}
