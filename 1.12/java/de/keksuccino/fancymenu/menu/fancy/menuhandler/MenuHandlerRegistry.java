package de.keksuccino.fancymenu.menu.fancy.menuhandler;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

public class MenuHandlerRegistry {
	
	private static Map<String, MenuHandlerBase> handlers = new HashMap<String, MenuHandlerBase>();
	private static MenuHandlerBase lastActiveHandler;
	
	public static void registerHandler(MenuHandlerBase handler) {
		if (!handlers.containsKey(handler.getMenuIdentifier())) {
			handlers.put(handler.getMenuIdentifier(), handler);
			MinecraftForge.EVENT_BUS.register(handler);
		}
	}
	
	public static boolean isHandlerRegistered(String menuIdentifier) {
		return handlers.containsKey(menuIdentifier);
	}
	
	public static MenuHandlerBase getLastActiveHandler() {
		return lastActiveHandler;
	}
	
	protected static void setActiveHandler(String menuIdentifier) {
		if (isHandlerRegistered(menuIdentifier)) {
			lastActiveHandler = handlers.get(menuIdentifier);
		}
	}
}
