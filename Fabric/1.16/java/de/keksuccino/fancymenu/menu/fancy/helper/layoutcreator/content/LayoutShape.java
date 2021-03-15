package de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.content;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutCreatorScreen;
import de.keksuccino.fancymenu.menu.fancy.item.CustomizationItemBase;
import de.keksuccino.fancymenu.menu.fancy.item.ShapeCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.gui.screens.popup.TextInputPopup;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;

public class LayoutShape extends LayoutObject {

	public LayoutShape(CustomizationItemBase object, LayoutCreatorScreen handler) {
		super(object, true, handler);
	}

	@Override
	protected void init() {
		this.stretchable = true;
		super.init();
		
		this.rightclickMenu.setAutoclose(true);
		
		AdvancedButton colorB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.shape.color"), true, (press) -> {
			this.handler.setMenusUseable(false);
			
			TextInputPopup t = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.shape.color") + ":", null, 240, (call) -> {
				if (call != null) {
					if (!call.equals("")) {
						Color c = RenderUtils.getColorFromHexString(call);
						if (c != null) {
							
							if (!this.getObject().getColorString().equalsIgnoreCase(call)) {
								this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
							}
							
							this.getObject().setColor(call);
							
						}
					} else {
						if (!this.getObject().getColorString().equalsIgnoreCase("#ffffff")) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.getObject().setColor("#ffffff");
					}
				}
				
				this.handler.setMenusUseable(true);
			});
			
			t.setText(this.getObject().getColorString());
			
			PopupHandler.displayPopup(t);
			
		});
		colorB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.shape.color.btndesc"), "%n%"));
		this.rightclickMenu.addContent(colorB);
		LayoutCreatorScreen.colorizeCreatorButton(colorB);
		
	}
	
	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection s = new PropertiesSection("customization");
		
		if (this.getObject().shape != null) {
			
			s.addEntry("action", "addshape");
			s.addEntry("shape", this.getObject().shape.name);
			s.addEntry("color", this.getObject().getColorString());
			s.addEntry("orientation", this.object.orientation);
			if (this.stretchX) {
				s.addEntry("x", "0");
				s.addEntry("width", "%guiwidth%");
			} else {
				s.addEntry("x", "" + this.object.posX);
				s.addEntry("width", "" + this.object.width);
			}
			if (this.stretchY) {
				s.addEntry("y", "0");
				s.addEntry("height", "%guiheight%");
			} else {
				s.addEntry("y", "" + this.object.posY);
				s.addEntry("height", "" + this.object.height);
			}
			
			l.add(s);
		}
		
		return l;
	}
	
	protected ShapeCustomizationItem getObject() {
		return (ShapeCustomizationItem) this.object;
	}

}