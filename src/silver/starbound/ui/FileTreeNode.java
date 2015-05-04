package silver.starbound.ui;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8926362219640624886L;
	
	private File _file;

	public FileTreeNode(File file){
		_file = file;
	}
	
	public File getFile(){
		return _file;
	}
	
	public String toString(){
		return _file == null ? "" : _file.getName();
	}
}
