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
import java.io.IOException;
import java.nio.file.Files;

import org.apache.tika.Tika;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * A class representing a file with a known file.
 * 
 * @author SilverFishCat
 *
 */
public class TypedFile {
	/**
	 * A possible type of a file.
	 * 
	 * @author SilverFishCat
	 *
	 */
	public enum FileType{
		/**
		 * A json file type.
		 */
		JSON,
		/**
		 * An image file type.
		 */
		IMAGE,
		/**
		 * A text file type.
		 */
		TEXT,
		/**
		 * File type is unknown.
		 */
		UNKNOWN
	}
	/**
	 * The file.
	 */
	public File _file;
	/**
	 * The type of the file.
	 */
	private FileType _type;
	
	/**
	 * Create a new typed file.
	 * Gets the type of the file dynamically.
	 * 
	 * @param file The file to encapsulate
	 */
	public TypedFile(File file){
		_file = file;
		_type = getFileType(_file);
	}
	/**
	 * Create a new typed file.
	 * 
	 * @param file The file to encapsulate
	 * @param type The type of the file
	 */
	public TypedFile(File file, FileType type){
		_file = file;
		_type = type;
	}
	
	/**
	 * Get the underlying file.
	 * 
	 * @return The underlying file
	 */
	public File getFile(){
		return _file;
	}
	/**
	 * Get the file type.
	 * 
	 * @return The file type
	 */
	public FileType getFileType(){
		return _type;
	}
	/**
	 * Set the underlying file.
	 * 
	 * @param file The file to set
	 */
	public void setFile(File file){
		_file = file;
	}
	/**
	 * Set the file type.
	 * 
	 * @param type The new file type
	 */
	public void setFileType(FileType type){
		_type = type;
	}
	
	/**
	 * Get the type of this file.
	 * 
	 * @param file The file whose type will be found
	 * @return The type of the file, UNKNOWN if can not detect file type
	 */
	public static FileType getFileType(File file){
		FileType result = FileType.UNKNOWN;
		
		// Check if file is a json object
		if(file != null){
			try{
				new JsonParser().parse(String.join("", Files.readAllLines(file.toPath())));
				result = FileType.JSON;
			}
			catch(JsonParseException ex){
				
			} catch (IOException e) {
				
			}
			
			try{
				if(result == FileType.UNKNOWN){
					String typeMIME = new Tika().detect(file);
					
					if(typeMIME != null){
						if(typeMIME.endsWith("png"))
							result = FileType.IMAGE;
						else if(typeMIME.startsWith("text/"))
							result = FileType.TEXT;
					}
				}
			}
			catch(IOException ex){
				
			}
		}
		
		return result;
	}
}
