package org.json.swing;

import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONTreeModel implements TreeModel {
	private JSONObject _root;
	private Set<TreeModelListener> _listeners;

	public void setRoot(JSONObject root){
		_root = root;
		
		// TODO?: optimize
		for (TreeModelListener treeModelListener : _listeners) {
			treeModelListener.treeNodesChanged(new TreeModelEvent(root, new TreePath(root)));
		}
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		_listeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(parent instanceof JSONArray){
			JSONArray array = (JSONArray) parent;
			return array.get(index);
		}
		else if(parent instanceof JSONObject){
			JSONObject object = (JSONObject) parent;
			return object.get(object.names().getString(index));
		}
		else{
			return null;
		}
	}

	@Override
	public int getChildCount(Object parent) {
		if(parent instanceof JSONArray){
			JSONArray array = (JSONArray) parent;
			return array.length();
		}
		else if(parent instanceof JSONObject){
			JSONObject object = (JSONObject) parent;
			return object.length();
		}
		else{
			return 0;
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO: check if either objects are in the tree
		
		if(parent != null || child == null){
			return -1;
		}
		
		if(parent instanceof JSONArray){
			JSONArray array = (JSONArray) parent;
			
			for (int i = 0; i < array.length(); i++) {
				if(array.get(i) == child)
					return i;
			}
			
			return -1;
		}
		else if(parent instanceof JSONObject){
			JSONObject object = (JSONObject) parent;
			JSONArray names = object.names();
			
			for (int i = 0; i < names.length(); i++) {
				if(object.get(names.getString(i)) == child)
					return i;
			}
			
			return -1;
		}
		else{
			return -1;
		}
	}

	@Override
	public Object getRoot() {
		return _root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node instanceof JSONArray || node instanceof JSONObject;
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
