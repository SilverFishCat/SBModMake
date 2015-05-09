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

package silver.starbound.ui;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A file node to be used in a DefaultTreeModel, wrapping a File object.
 * Overrides the toString function to show the file name.
 * 
 * @author SilverFishCat
 *
 */
public class FileTreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8926362219640624886L;
	
	private File _file;

	/**
	 * Create a new TreeNode from a file.
	 * 
	 * @param file The file of this node
	 */
	public FileTreeNode(File file){
		_file = file;
	}
	
	/**
	 * Get the file of this node.
	 * 
	 * @return The file of this node
	 */
	public File getFile(){
		return _file;
	}
	
	@Override
	public String toString(){
		return _file == null ? "" : _file.getName();
	}
}
