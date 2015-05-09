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

package silver.json.swing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A json tree model.
 * Shows the structure of a json element.
 * 
 * @author SilverFishCat
 *
 */
public class JsonTreeModel implements TreeModel {
	private JsonElement _element;
	private JsonTreeNode _root;
	private Set<TreeModelListener> _listeners;
	
	/**
	 * Create a new model from an element.
	 * 
	 * @param element The root element of this model.
	 */
	public JsonTreeModel(JsonElement element){
		_listeners = new HashSet<>();
		setElement(element);
	}

	/**
	 * Set the root element to use to build the model.
	 * 
	 * @param element The new root element
	 */
	public void setElement(JsonElement element){
		_element = element;
		buildTree();
		
		// TODO?: optimize
		for (TreeModelListener treeModelListener : _listeners) {
			treeModelListener.treeNodesChanged(new TreeModelEvent(this, new TreePath(_root)));
		}
	}
	@Override
	public Object getRoot() {
		return _root;
	}
	/**
	 * Get the root element of the model.
	 * 
	 * @return The root element of the model
	 */
	public JsonElement getElement(){
		return _element;
	}
	
	/**
	 * Build the underline tree in the model.
	 */
	private void buildTree(){
		_root = buildJsonTreeNode(_element);
	}
	/**
	 * Build a JsonTreeNode tree starting from the given element
	 * @param element The element to build the tree from
	 * @return The root node of the built tree
	 */
	private JsonTreeNode buildJsonTreeNode(JsonElement element) {
		return buildJsonTreeNode(element, null);
	}
	/**
	 * Build a JsonTreeNode tree starting from the given element, giving the root the
	 * given name.
	 * 
	 * @param element The root element of the new tree
	 * @param name The name to give the root element
	 * @return The root node of the built tree
	 */
	private JsonTreeNode buildJsonTreeNode(JsonElement element, String name) {
		List<JsonTreeNode> children = new ArrayList<JsonTreeNode>();
		
		if(element.isJsonArray()){
			JsonArray array = element.getAsJsonArray();
			
			for (JsonElement jsonElement : array) {
				children.add(buildJsonTreeNode(jsonElement));
			}
		}
		else if(element.isJsonObject()){
			JsonObject object = element.getAsJsonObject();
			
			for (Entry<String, JsonElement> entry : object.entrySet()) {
				children.add(buildJsonTreeNode(entry.getValue(), entry.getKey()));
			}
		}
		
		return new JsonTreeNode(element, children, name);
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		_listeners.add(l);
	}
	@Override
	public Object getChild(Object parent, int index) {
		JsonTreeNode node = (JsonTreeNode) parent;
		if(!node.isLeaf()){
			return node.getChildren().get(index);
		}
		else{
			return null;
		}
	}
	@Override
	public int getChildCount(Object parent) {
		JsonTreeNode node = (JsonTreeNode) parent;
		if(!node.isLeaf()){
			return node.getChildren().size();
		}
		else{
			return 0;
		}
	}
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO: check if either objects are in the tree
		
		if(parent == null || child == null || !(parent instanceof JsonTreeNode) || !(child instanceof JsonTreeNode)){
			return -1;
		}
		
		JsonTreeNode parentNode = (JsonTreeNode) parent;
		
		if(parentNode.isLeaf())
			return -1;
		
		return parentNode.getChildren().indexOf(child);
	}
	@Override
	public boolean isLeaf(Object node) {
		return ((JsonTreeNode)node).isLeaf();
	}
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		_listeners.remove(l);
	}
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		if(path.getLastPathComponent() != newValue){
			// TODO: insert additional logic
			
			for (TreeModelListener treeModelListener : _listeners) {
				treeModelListener.treeNodesChanged(new TreeModelEvent(this, path));
			}
		}
	}
}
