package silver.json.swing;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class JsonTreeCellRenderer extends DefaultTreeCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3129721733001074936L;
	
	private Icon _objectIcon;
	private Icon _arrayIcon;
	private Icon _stringIcon;
	private Icon _numberIcon;
	private Icon _booleanIcon;
	private Icon _nullIcon;
	private Icon _unknownIcon;
	
	public JsonTreeCellRenderer() {
		_objectIcon = new ImageIcon("img\\jsonobjecticon.png");
		_arrayIcon = new ImageIcon("img\\jsonarrayicon.png");
		_stringIcon = new ImageIcon("img\\jsonstringicon.png");
		_numberIcon = new ImageIcon("img\\jsonnumbericon.png");
		_booleanIcon = new ImageIcon("img\\jsonbooleanicon.png");
		_nullIcon = new ImageIcon("img\\jsonnullicon.png");
		_unknownIcon = new ImageIcon("img\\jsonunknownicon.png");
	}

	public void setObjectIcon(Icon icon){
		this._objectIcon = icon;
	}
	public void setArrayIcon(Icon icon){
		this._arrayIcon = icon;
	}
	public void setStringIcon(Icon icon){
		this._stringIcon = icon;
	}
	public void setNumberIcon(Icon icon){
		this._numberIcon = icon;
	}
	public void setBooleanIcon(Icon icon){
		this._booleanIcon = icon;
	}
	public void setNullIcon(Icon icon){
		this._nullIcon = icon;
	}
	public void setUnknownIcon(Icon icon){
		this._unknownIcon = icon;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component result = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		JsonTreeNode node = (JsonTreeNode) value;
		((JLabel) result).setIcon(getIconForJsonElement(node.getValue()));
		
		return result;
	}
	
	private Icon getIconForJsonElement(JsonElement element){
		try{
			if(element.isJsonObject())
				return _objectIcon;
			else if(element.isJsonArray())
				return _arrayIcon;
			else if(element.isJsonNull())
				return _nullIcon;
			else if(element.isJsonPrimitive()){
				JsonPrimitive primitive = element.getAsJsonPrimitive();
				if(primitive.isString())
					return _stringIcon;
				else if(primitive.isNumber())
					return _numberIcon;
				else if(primitive.isBoolean())
					return _booleanIcon;
			}
		}
		catch (Exception ex){
		}
		
		return _unknownIcon;
	}
}
