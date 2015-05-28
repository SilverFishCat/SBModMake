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

package silver.starbound.ui.editor;

import java.io.File;
import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ItemEditor implements FileEditor {

	/*@Override
	public JsonTreeConstraints getJsonTreeConstraints() {
		Map<String, JsonTreeConstraints.Node> childNodes = new HashMap<String, JsonTreeConstraints.Node>();
		
		childNodes.put("itemName", new Node(null, null, true, true));
		
		List<JsonElement> rarityPossibleValues = new ArrayList<JsonElement>();
		rarityPossibleValues.add(new JsonPrimitive("legendary"));
		rarityPossibleValues.add(new JsonPrimitive("rare"));
		rarityPossibleValues.add(new JsonPrimitive("uncommon"));
		rarityPossibleValues.add(new JsonPrimitive("common"));
		childNodes.put("rarity", new Node(rarityPossibleValues));
		
		childNodes.put("inventoryIcon", new Node(null, null, true, true));
		
		childNodes.put("description", new Node(null, null, true, true));
		
		childNodes.put("shortdescription", new Node(null, null, true, true));
		
		JsonTreeConstraints.Node rootNode = 
				new JsonTreeConstraints.Node(
						childNodes
				);
		
		return  new JsonTreeConstraints(rootNode);
	}*/

	@Override
	public boolean isExtensionMatchingEditor(String extension) {
		return extension.equals("item");
	}

	@Override
	public JEditorPanel getEditorPanel(File file) {
		try {
			return new JItemEditorPanel(file);
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace(); // TODO:show error message
			return null;
		}
	}

	@Override
	public String toString() {
		return "Item";
	}
}
