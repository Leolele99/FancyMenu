package de.keksuccino.fancymenu.keybinding;

import de.keksuccino.core.input.KeyboardHandler;
import de.keksuccino.fancymenu.menu.fancy.helper.CustomizationHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinding {

	public static KeyBinding KeyReloadMenu;
	
	public static void init() {
		KeyReloadMenu = new KeyBinding("Reload Menu", 19, "FancyMenu");
		ClientRegistry.registerKeyBinding(KeyReloadMenu);
		
		initGuiClickActions();
	}
	
	private static void initGuiClickActions() {
		//It's not possible in GUIs to check for keypresses via Keybinding.isPressed(), so I'm doing it on my own
		KeyboardHandler.addKeyPressedListener((c) -> {
			
			if (KeyReloadMenu.getKeyCode() == c.keycode) {
				CustomizationHelper.getInstance().onReloadButtonPress();
			}
			
		});
	}
}
