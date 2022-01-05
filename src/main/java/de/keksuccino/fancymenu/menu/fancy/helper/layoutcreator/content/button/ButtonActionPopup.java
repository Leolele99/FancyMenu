package de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.content.button;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancymenu.api.buttonaction.ButtonActionContainer;
import de.keksuccino.fancymenu.api.buttonaction.ButtonActionRegistry;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.fancymenu.menu.fancy.helper.DynamicValueTextfield;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.UIBase;
import de.keksuccino.fancymenu.menu.fancy.helper.ui.popup.FMPopup;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.gui.content.HorizontalSwitcher;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.input.KeyboardHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ButtonActionPopup extends FMPopup {
	
	protected Consumer<String> contentCallback;
	protected Consumer<String> typeCallback;
	protected AdvancedTextField textField;
	protected AdvancedButton doneButton;
	protected int width = 250;
	
	protected HorizontalSwitcher actionSwitcher;
	
	public ButtonActionPopup(Consumer<String> contentCallback, Consumer<String> typeCallback, String selectedType) {
		super(240);
		
		this.textField = new DynamicValueTextfield(MinecraftClient.getInstance().textRenderer, 0, 0, 200, 20, true, null);
		this.textField.setFocusUnlocked(true);
		this.textField.setFocused(false);
		this.textField.setMaxLength(1000);

		List<String> actions = new ArrayList<>();
		actions.add("openlink");
		actions.add("sendmessage");
		actions.add("quitgame");
		actions.add("joinserver");
		actions.add("loadworld");
		actions.add("opencustomgui");
		actions.add("opengui");
		actions.add("openfile");
		actions.add("movefile");
		actions.add("copyfile");
		actions.add("deletefile");
		actions.add("renamefile");
		actions.add("downloadfile");
		actions.add("unpackzip");
		actions.add("reloadmenu");
		actions.add("mutebackgroundsounds");
		actions.add("runscript");
		actions.add("runcmd");
		actions.add("closegui");
		actions.add("copytoclipboard");

		/** CUSTOM ACTIONS **/
		for (ButtonActionContainer c : ButtonActionRegistry.getActions()) {
			actions.add(c.getAction());
		}

		if (!actions.contains(selectedType)) {
			selectedType = null;
		}

		this.actionSwitcher = new HorizontalSwitcher(120, true, actions.toArray(new String[0]));
		this.actionSwitcher.setButtonColor(UIBase.getButtonIdleColor(), UIBase.getButtonHoverColor(), UIBase.getButtonBorderIdleColor(), UIBase.getButtonBorderHoverColor(), 1);
		this.actionSwitcher.setValueBackgroundColor(UIBase.getButtonIdleColor());
		
		if (selectedType != null) {
			this.actionSwitcher.setSelectedValue(selectedType);
		}
		
		this.doneButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.done"), true, (press) -> {
			this.setDisplayed(false);
			if (this.contentCallback != null) {
				this.contentCallback.accept(this.textField.getText());
			}
			if (this.typeCallback != null) {
				this.typeCallback.accept(this.actionSwitcher.getSelectedValue());
			}
		});
		this.addButton(this.doneButton);

		this.contentCallback = contentCallback;
		this.typeCallback = typeCallback;
		
		KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
		KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, Screen renderIn) {
		super.render(matrix, mouseX, mouseY, renderIn);

		if (this.isDisplayed()) {
			String action = this.actionSwitcher.getSelectedValue();
			ButtonActionContainer customAction = ButtonActionRegistry.getActionByName(action);

			//Draw popup title
			drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, new LiteralText("§l" + Locals.localize("helper.creator.custombutton.config")), renderIn.width / 2, (renderIn.height / 2) - 50 - 40, -1);

			//Draw action type name
			drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, new LiteralText(Locals.localize("helper.creator.custombutton.config.actiontype")), renderIn.width / 2, (renderIn.height / 2) - 60, -1);

			this.actionSwitcher.render(matrix, (renderIn.width / 2) - (this.actionSwitcher.getTotalWidth() / 2), (renderIn.height / 2) - 45);

			//Set and draw action description
			String actionDesc;
			if (customAction != null) {
				actionDesc = customAction.getActionDescription();
			} else {
				actionDesc = Locals.localize("helper.creator.custombutton.config.actiontype." + this.actionSwitcher.getSelectedValue() + ".desc");
			}
			drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, new LiteralText(actionDesc), renderIn.width / 2, (renderIn.height / 2) - 20, Color.WHITE.getRGB());

			if (action.equals("sendmessage") || action.equals("openlink") || (action.equals("joinserver") || (action.equals("loadworld") || action.equals("openfile") || action.equals("opencustomgui") || action.equals("opengui") || action.equals("movefile") || action.equals("copyfile") || action.equals("deletefile") || action.equals("renamefile") || action.equals("runscript") || action.equals("downloadfile") || action.equals("unpackzip") || action.equals("mutebackgroundsounds") || action.equals("runcmd") || action.equals("copytoclipboard") || ((customAction != null) && customAction.hasValue())))) {
				//Set and draw value description
				String valueDesc;
				if (customAction != null) {
					valueDesc = customAction.getValueDescription();
				} else {
					valueDesc = Locals.localize("helper.creator.custombutton.config.actiontype." + this.actionSwitcher.getSelectedValue() + ".desc.value");
				}
				drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, new LiteralText(Locals.localize("helper.creator.custombutton.config.actionvalue", valueDesc)), renderIn.width / 2, (renderIn.height / 2) + 15, Color.WHITE.getRGB());

				this.textField.setX((renderIn.width / 2) - (this.textField.getWidth() / 2));
				this.textField.setY((renderIn.height / 2) + 30);
				this.textField.renderButton(matrix, mouseX, mouseY, MinecraftClient.getInstance().getTickDelta());

				//Set and draw value example
				String valueExample;
				if (customAction != null) {
					valueExample = customAction.getValueExample();
				} else {
					valueExample = Locals.localize("helper.creator.custombutton.config.actiontype." + this.actionSwitcher.getSelectedValue() + ".desc.value.example");
				}
				drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, new LiteralText(Locals.localize("helper.creator.custombutton.config.actionvalue.example", valueExample)), renderIn.width / 2, (renderIn.height / 2) + 56, Color.WHITE.getRGB());
			}

			this.doneButton.setX((renderIn.width / 2) - (this.doneButton.getWidth() / 2));
			this.doneButton.setY(((renderIn.height / 2) + 50) + 30);

			this.renderButtons(matrix, mouseX, mouseY);
		}

	}
	
	public void setText(String text) {
		this.textField.setText("");
		this.textField.write(text);
	}
	
	public String getInput() {
		return this.textField.getText();
	}
	
	public void onEnterPressed(KeyboardData d) {
		if ((d.keycode == 257) && this.isDisplayed()) {
			this.setDisplayed(false);
			if (this.contentCallback != null) {
				this.contentCallback.accept(this.textField.getText());
			}
			if (this.typeCallback != null) {
				this.typeCallback.accept(this.actionSwitcher.getSelectedValue());
			}
		}
	}
	
	public void onEscapePressed(KeyboardData d) {
		if ((d.keycode == 256) && this.isDisplayed()) {
			this.setDisplayed(false);
			if (this.contentCallback != null) {
				this.contentCallback.accept(null);
			}
			if (this.typeCallback != null) {
				this.typeCallback.accept(null);
			}
		}
	}

}
