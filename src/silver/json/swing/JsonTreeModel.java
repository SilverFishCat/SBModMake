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

public class JsonTreeModel implements TreeModel {
	private JsonElement _element;
	private JsonTreeNode _root;
	private Set<TreeModelListener> _listeners;
	
	public JsonTreeModel(JsonElement element){
		_listeners = new HashSet<>();
		setElement(element);
	}

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
	public JsonElement getElement(){
		return _element;
	}
	
	private void buildTree(){
		_root = buildJsonTreeNode(_element);
	}
	private JsonTreeNode buildJsonTreeNode(JsonElement element) {
		return buildJsonTreeNode(element, null);
	}
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
