package silver.json.swing;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

public class JsonTreeNode {
	private JsonElement _value;
	private String _elementName;
	private List<JsonTreeNode> _children;
	
	public JsonTreeNode(JsonElement value){
		this(value, (String)null);
	}
	public JsonTreeNode(JsonElement value, String elementName){
		this(value, null, elementName);
	}
	public JsonTreeNode(JsonElement value, List<JsonTreeNode> children){
		this(value, children, null);
	}
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
	
	public JsonElement getValue(){
		return _value;
	}
	public List<JsonTreeNode> getChildren(){
		return new ArrayList<>(_children);
	}
	
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
			valueString = null;
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
