package silver.starbound.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.tika.Tika;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class TypedFile {
	public enum FileType{
		JSON,
		IMAGE,
		TEXT,
		UNKNOWN
	}
	public File _file;
	private FileType _type;
	
	public TypedFile(File file){
		_file = file;
		_type = getFileType(_file);
	}
	public TypedFile(File file, FileType type){
		_file = file;
		_type = type;
	}
	
	public File getFile(){
		return _file;
	}
	public FileType getFileType(){
		return _type;
	}
	public void setFile(File file){
		_file = file;
	}
	public void setFileType(FileType type){
		_type = type;
	}
	
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
				if(result == null){
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
