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
import java.util.List;

import com.google.gson.JsonElement;

/**
 * A node in a json tree.
 * 
 * @author SilverFishCat
 *
 */
public class JsonTreeNode {
	private JsonElement _value;
	private String _elementName;
	private List<JsonTreeNode> _children;
	
	/**
	 * Create a new json tree node.
	 * 
	 * @param value The json element of this node
	 */
	public JsonTreeNode(JsonElement value){
		this(value, (String)null);
	}
	/**
	 * Create a new json tree node.
	 * 
	 * @param value The json element of this node
	 * @param elementName The name of the element
	 */
	public JsonTreeNode(JsonElement value, String elementName){
		this(value, null, elementName);
	}
	/**
	 * Create a new json tree node.
	 * 
	 * @param value The json element of this node
	 * @param children The children nodes of the new node
	 */
	public JsonTreeNode(JsonElement value, List<JsonTreeNode> children){
		this(value, children, null);
	}
	/**
	 * Create a new json tree node.
	 * 
	 * @param value The json element of this node
	 * @param children The children nodes of the new node
	 * @param elementName The name of the element
	 */
	public JsonTreeNode(JsonElement value, List<JsonTreeNode> children, String elementName){
		this._value = value;
		
		if(elementName == null)
			this._elementName = null;
		else
			this._elementName = elementName;
		
		if(children == null)
			this._children = new ArrayList<JsonTreeNode>();
		else
			this._children = new ArrayList<>(children);
	}
	
	/**
	 * Get the json element of this node.
	 * 
	 * @return The json element of this node
	 */
	public JsonElement getValue(){
		return _value;
	}
	/**
	 * Get all the child nodes of this node.
	 * 
	 * @return This node's children nodes
	 */
	public List<JsonTreeNode> getChildren(){
		return new ArrayList<>(_children);
	}
	
	/**
	 * Check if the node is a leaf node.
	 * 
	 * @return True if a json primitive or null, false otherwise
	 */
	public boolean isLeaf(){
		return (!_value.isJsonArray()) && (!_value.isJsonObject());
	}
	
	@Override
	public String toString() {
		String name = _elementName;
		String valueString;
		if(_value.isJsonPrimitive()){
			valueString = _value.toString();
		}
		else if(_value.isJsonNull()){
			valueString = "null";
		}
		else{
			valueString = "";
		}
		
		if(name == null || name.equals("")){
			name = "";
		}
		else if(valueString == null || valueString.equals("")){
			valueString = "";
		}
		else{
			return name + " : " + valueString;
		}
		
		return name + valueString;
	}
}
