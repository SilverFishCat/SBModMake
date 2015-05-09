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

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Renders JTree cells that contain JsonTreeNodes.
 * The renderer subclasses DefaultTreeCellRenderer to maintain the basic looks
 * of a default JTree.
 * 
 * @author SilverFishCat
 *
 */
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
	
	/**
	 * Creates a new JsonTreeCellRenderer.
	 */
	public JsonTreeCellRenderer() {
		_objectIcon = new ImageIcon("img\\jsonobjecticon.png");
		_arrayIcon = new ImageIcon("img\\jsonarrayicon.png");
		_stringIcon = new ImageIcon("img\\jsonstringicon.png");
		_numberIcon = new ImageIcon("img\\jsonnumbericon.png");
		_booleanIcon = new ImageIcon("img\\jsonbooleanicon.png");
		_nullIcon = new ImageIcon("img\\jsonnullicon.png");
		_unknownIcon = new ImageIcon("img\\jsonunknownicon.png");
	}

	/**
	 * Override the default icon used for a JsonObject.
	 * 
	 * @param icon The icon to use
	 */
	public void setObjectIcon(Icon icon){
		this._objectIcon = icon;
	}
	/**
	 * Override the default icon used for a JsonArray.
	 * 
	 * @param icon The icon to use
	 */
	public void setArrayIcon(Icon icon){
		this._arrayIcon = icon;
	}
	/**
	 * Override the default icon used for a JsonPrimitive string.
	 * 
	 * @param icon The icon to use
	 */
	public void setStringIcon(Icon icon){
		this._stringIcon = icon;
	}
	/**
	 * Override the default icon used for a JsonPrimitive number.
	 * 
	 * @param icon The icon to use
	 */
	public void setNumberIcon(Icon icon){
		this._numberIcon = icon;
	}
	/**
	 * Override the default icon used for a JsonPrimitive boolean.
	 * 
	 * @param icon The icon to use
	 */
	public void setBooleanIcon(Icon icon){
		this._booleanIcon = icon;
	}
	/**
	 * Override the default icon used for a JsonNull.
	 * 
	 * @param icon The icon to use
	 */
	public void setNullIcon(Icon icon){
		this._nullIcon = icon;
	}
	/**
	 * Override the default icon used for an unknown object in the tree.
	 * 
	 * @param icon The icon to use
	 */
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
	
	/**
	 * Get the item for the given JsonElement.
	 * @param element The element to get the icon for
	 * @return The icon of the given element
	 */
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
