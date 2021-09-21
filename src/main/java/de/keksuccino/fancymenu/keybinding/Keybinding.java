package de.keksuccino.fancymenu.keybinding;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.menu.fancy.helper.CustomizationHelper;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import de.keksuccino.konkrete.input.KeyboardHandler;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

public class Keybinding {

	public static KeyMapping KeyReloadMenu;
	public static KeyMapping KeyToggleHelper;
	
	public static void init() {
		KeyReloadMenu = new KeyMapping("Reload Menu | CTRL + ALT + ", 82, "FancyMenu");
		ClientRegistry.registerKeyBinding(KeyReloadMenu);

		KeyToggleHelper = new KeyMapping("Toggle Customization Overlay | CTRL + ALT + ", 67, "FancyMenu");
		ClientRegistry.registerKeyBinding(KeyToggleHelper);
		
		initGuiClickActions();
	}
	
	private static void initGuiClickActions() {
		
		//It's not possible in GUIs to check for keypresses via Keybinding.isPressed(), so I'm doing it on my own
		KeyboardHandler.addKeyPressedListener((c) -> {
			if ((KeyReloadMenu.getKey().getValue() == c.keycode) && KeyboardHandler.isCtrlPressed() && KeyboardHandler.isAltPressed()) {
				CustomizationHelper.reloadSystemAndMenu();
			}

			if ((KeyToggleHelper.getKey().getValue() == c.keycode) && KeyboardHandler.isCtrlPressed() && KeyboardHandler.isAltPressed()) {
				try {
					if (FancyMenu.config.getOrDefault("showcustomizationbuttons", true)) {
						FancyMenu.config.setValue("showcustomizationbuttons", false);
					} else {
						FancyMenu.config.setValue("showcustomizationbuttons", true);
					}
					FancyMenu.config.syncConfig();
					CustomizationHelper.updateUI();
				} catch (InvalidValueException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
