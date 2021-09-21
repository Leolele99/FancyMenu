package de.keksuccino.fancymenu.menu.fancy.helper;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancymenu.menu.fancy.helper.ui.FMContextMenu;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.UIBase;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class DynamicValueTextfield extends AdvancedTextField {

	private AdvancedImageButton variableButton;
	private FMContextMenu variableMenu;
	
	private static final ResourceLocation VARIABLES_BUTTON_RESOURCE = new ResourceLocation("keksuccino", "add_btn.png");

	//TODO übernehmen
	public DynamicValueTextfield(FontRenderer fontrenderer, int x, int y, int width, int height, boolean handleTextField, CharacterFilter filter) {
		super(fontrenderer, x, y, width, height, handleTextField, filter);
		
		variableMenu = new FMContextMenu();
		variableMenu.setAutoclose(true);

		variableButton = new AdvancedImageButton(0, 0, height, height, VARIABLES_BUTTON_RESOURCE, true, (press) -> {
			UIBase.openScaledContextMenuAtMouse(variableMenu);
		});
		variableButton.ignoreBlockedInput = true;
		variableButton.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.desc"), "%n%"));
		UIBase.colorizeButton(variableButton);
		variableMenu.setParentButton(variableButton);

		/** PLAYER CATEGORY **/
		FMContextMenu playerMenu = new FMContextMenu();
		playerMenu.setAutoclose(true);
		variableMenu.addChild(playerMenu);

		AdvancedButton playerCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.categories.player"), true, (press) -> {
			playerMenu.setParentButton((AdvancedButton) press);
			playerMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(playerCategoryButton);
		variableMenu.addContent(playerCategoryButton);
		
		AdvancedButton playerName = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.playername"), true, (press) -> {
			this.writeText("%playername%");
		});
		playerName.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.playername.desc"), "%n%"));
		UIBase.colorizeButton(playerName);
		playerMenu.addContent(playerName);
		
		AdvancedButton playerUUID = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.playeruuid"), true, (press) -> {
			this.writeText("%playeruuid%");
		});
		playerUUID.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.playeruuid.desc"), "%n%"));
		UIBase.colorizeButton(playerUUID);
		playerMenu.addContent(playerUUID);

		/** CLIENT CATEGORY **/
		FMContextMenu clientMenu = new FMContextMenu();
		clientMenu.setAutoclose(true);
		variableMenu.addChild(clientMenu);

		AdvancedButton clientCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.categories.client"), true, (press) -> {
			clientMenu.setParentButton((AdvancedButton) press);
			clientMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(clientCategoryButton);
		variableMenu.addContent(clientCategoryButton);

		AdvancedButton mcVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.mcversion"), true, (press) -> {
			this.writeText("%mcversion%");
		});
		mcVersion.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.mcversion.desc"), "%n%"));
		UIBase.colorizeButton(mcVersion);
		clientMenu.addContent(mcVersion);
		
		AdvancedButton forgeVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.forgeversion"), true, (press) -> {
			this.writeText("%version:forge%");
		});
		forgeVersion.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.forgeversion.desc"), "%n%"));
		UIBase.colorizeButton(forgeVersion);
		clientMenu.addContent(forgeVersion);
		
		AdvancedButton modVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.modversion"), true, (press) -> {
			this.writeText("%version:<modid>%");
		});
		modVersion.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.modversion.desc"), "%n%"));
		UIBase.colorizeButton(modVersion);
		clientMenu.addContent(modVersion);
		
		AdvancedButton totalMods = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.totalmods"), true, (press) -> {
			this.writeText("%totalmods%");
		});
		totalMods.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.totalmods.desc"), "%n%"));
		UIBase.colorizeButton(totalMods);
		clientMenu.addContent(totalMods);
		
		AdvancedButton loadedMods = new AdvancedButton(0, 0, 0, 0, Locals.localize("helper.ui.dynamicvariabletextfield.variables.loadedmods"), true, (press) -> {
			this.writeText("%loadedmods%");
		});
		loadedMods.setDescription(StringUtils.splitLines(Locals.localize("helper.ui.dynamicvariabletextfield.variables.loadedmods.desc"), "%n%"));
		UIBase.colorizeButton(loadedMods);
		clientMenu.addContent(loadedMods);

		clientMenu.addSeparator();

		AdvancedButton percentRam = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.percentram"), true, (press) -> {
			this.writeText("%percentram%");
		});
		UIBase.colorizeButton(percentRam);
		clientMenu.addContent(percentRam);

		AdvancedButton usedRam = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.usedram"), true, (press) -> {
			this.writeText("%usedram%");
		});
		UIBase.colorizeButton(usedRam);
		clientMenu.addContent(usedRam);

		AdvancedButton maxRam = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.maxram"), true, (press) -> {
			this.writeText("%maxram%");
		});
		UIBase.colorizeButton(maxRam);
		clientMenu.addContent(maxRam);

		/** SERVER CATEGORY **/
		FMContextMenu serverMenu = new FMContextMenu();
		serverMenu.setAutoclose(true);
		variableMenu.addChild(serverMenu);

		AdvancedButton serverCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.categories.server"), true, (press) -> {
			serverMenu.setParentButton((AdvancedButton) press);
			serverMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(serverCategoryButton);
		variableMenu.addContent(serverCategoryButton);

		AdvancedButton serverMOTD = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.servermotd"), true, (press) -> {
			this.writeText("%servermotd:<serverIP>%");
		});
		serverMOTD.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.servermotd.desc"), "%n%"));
		UIBase.colorizeButton(serverMOTD);
		serverMenu.addContent(serverMOTD);

		AdvancedButton serverPing = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverping"), true, (press) -> {
			this.writeText("%serverping:<serverIP>%");
		});
		serverPing.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverping.desc"), "%n%"));
		UIBase.colorizeButton(serverPing);
		serverMenu.addContent(serverPing);

		AdvancedButton serverPlayerCount = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverplayercount"), true, (press) -> {
			this.writeText("%serverplayercount:<serverIP>%");
		});
		serverPlayerCount.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverplayercount.desc"), "%n%"));
		UIBase.colorizeButton(serverPlayerCount);
		serverMenu.addContent(serverPlayerCount);

		AdvancedButton serverStatus = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverstatus"), true, (press) -> {
			this.writeText("%serverstatus:<serverIP>%");
		});
		serverStatus.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverstatus.desc"), "%n%"));
		UIBase.colorizeButton(serverStatus);
		serverMenu.addContent(serverStatus);

		AdvancedButton serverVersion = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverversion"), true, (press) -> {
			this.writeText("%serverversion:<serverIP>%");
		});
		serverVersion.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.serverversion.desc"), "%n%"));
		UIBase.colorizeButton(serverVersion);
		serverMenu.addContent(serverVersion);

		/** REAL TIME CATEGORY **/
		FMContextMenu realtimeMenu = new FMContextMenu();
		realtimeMenu.setAutoclose(true);
		variableMenu.addChild(realtimeMenu);

		AdvancedButton realtimeCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.categories.realtime"), true, (press) -> {
			realtimeMenu.setParentButton((AdvancedButton) press);
			realtimeMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(realtimeCategoryButton);
		variableMenu.addContent(realtimeCategoryButton);

		AdvancedButton realtimeYear = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.realtimeyear"), true, (press) -> {
			this.writeText("%realtimeyear%");
		});
		UIBase.colorizeButton(realtimeYear);
		realtimeMenu.addContent(realtimeYear);

		AdvancedButton realtimeMonth = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.realtimemonth"), true, (press) -> {
			this.writeText("%realtimemonth%");
		});
		UIBase.colorizeButton(realtimeMonth);
		realtimeMenu.addContent(realtimeMonth);

		AdvancedButton realtimeDay = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.realtimeday"), true, (press) -> {
			this.writeText("%realtimeday%");
		});
		UIBase.colorizeButton(realtimeDay);
		realtimeMenu.addContent(realtimeDay);

		AdvancedButton realtimeHour = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.realtimehour"), true, (press) -> {
			this.writeText("%realtimehour%");
		});
		UIBase.colorizeButton(realtimeHour);
		realtimeMenu.addContent(realtimeHour);

		AdvancedButton realtimeMinute = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.realtimeminute"), true, (press) -> {
			this.writeText("%realtimeminute%");
		});
		UIBase.colorizeButton(realtimeMinute);
		realtimeMenu.addContent(realtimeMinute);

		AdvancedButton realtimeSecond = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.realtimesecond"), true, (press) -> {
			this.writeText("%realtimesecond%");
		});
		UIBase.colorizeButton(realtimeSecond);
		realtimeMenu.addContent(realtimeSecond);

		/** OTHER CATEGORY **/
		FMContextMenu otherMenu = new FMContextMenu();
		otherMenu.setAutoclose(true);
		variableMenu.addChild(otherMenu);

		AdvancedButton otherCategoryButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.categories.other"), true, (press) -> {
			otherMenu.setParentButton((AdvancedButton) press);
			otherMenu.openMenuAt(0, press.y);
		});
		UIBase.colorizeButton(otherCategoryButton);
		variableMenu.addContent(otherCategoryButton);

		AdvancedButton localizedText = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.local"), true, (press) -> {
			this.writeText("%local:<localization.key>%");
		});
		localizedText.setDescription(StringUtils.splitLines(Locals.localize("fancymenu.helper.ui.dynamicvariabletextfield.variables.local.desc"), "%n%"));
		UIBase.colorizeButton(localizedText);
		otherMenu.addContent(localizedText);
		
	}
	
	@Override
	public void renderWidget(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		if (this.variableButton != null) {
			
			this.variableButton.setWidth(this.height);
			this.variableButton.setHeight(this.height);
			
			super.renderWidget(matrix, mouseX, mouseY, partialTicks);
			
			this.variableButton.setX(this.x + this.width + 5);
			this.variableButton.setY(this.y);
			this.variableButton.render(matrix, mouseX, mouseY, partialTicks);
			
			float scale = UIBase.getUIScale();
			
			MouseInput.setRenderScale(scale);
			
			matrix.push();
			
			matrix.scale(scale, scale, scale);
			
			if (this.variableMenu != null) {
				this.variableMenu.render(matrix, MouseInput.getMouseX(), MouseInput.getMouseY());
			}
			
			matrix.pop();
			
			MouseInput.resetRenderScale();
			
		}
	}

}