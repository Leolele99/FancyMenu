package de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.content;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutCreatorScreen;
import de.keksuccino.fancymenu.menu.fancy.item.PlayerEntityCustomizationItem;
import de.keksuccino.fancymenu.menu.fancy.item.PlayerEntityCustomizationItem.MenuPlayerEntity;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.ContextMenu;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.gui.screens.popup.TextInputPopup;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.konkrete.web.WebUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LayoutPlayerEntity extends LayoutObject {
	
	public String skinUrl;
	public String skinPath;
	public String capeUrl;
	public String capePath;
	
	public boolean isCLientPlayerName = false;
	
	public LayoutPlayerEntity(PlayerEntityCustomizationItem parent, LayoutCreatorScreen handler) {
		super(parent, true, handler);
		this.setScale(parent.scale);
	}
	
	@Override
	protected void init() {
		super.init();
		
		this.rightclickMenu.setAutoclose(true);
		
		AdvancedButton scaleB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.setscale"), true, (press) -> {
			this.handler.setMenusUseable(false);
			
			TextInputPopup t = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.playerentity.setscale") + ":", CharacterFilter.getIntegerCharacterFiler(), 240, (call) -> {
				if (call != null) {
					if (!call.equals("")) {
						if (MathUtils.isInteger(call)) {
							int i = Integer.parseInt(call);
							
							if (i != this.getObject().scale) {
								this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
							}
							
							this.getObject().scale = i;
						}
					} else {
						if (30 != this.getObject().scale) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.getObject().scale = 30;
					}
				}
				
				this.handler.setMenusUseable(true);
			});
			
			t.setText("" + this.getObject().scale);
			
			PopupHandler.displayPopup(t);
			
		});
		scaleB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.setscale.btndesc"), "%n%"));
		this.rightclickMenu.addContent(scaleB);
		LayoutCreatorScreen.colorizeCreatorButton(scaleB);
		
		ContextMenu playernamePopup = new ContextMenu(110, 16, -1);
		this.rightclickMenu.addChild(playernamePopup);
		
		AdvancedButton playernameB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.playername"), true, (press) -> {
			this.rightclickMenu.closeChilds();
			playernamePopup.openMenuAt(0, press.y);
		});
		playernameB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.playername.btndesc"), "%n%"));
		this.rightclickMenu.addContent(playernameB);
		LayoutCreatorScreen.colorizeCreatorButton(playernameB);
		
		AdvancedButton autoPlayernameB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.playername.auto"), true, (press) -> {
			if (!this.isCLientPlayerName) {
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			}
			this.isCLientPlayerName = true;
			this.getObject().playerName = "%playername%";
			this.reloadEntity();
			this.rightclickMenu.closeMenu();
		});
		autoPlayernameB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.playername.auto.btndesc"), "%n%"));
		playernamePopup.addContent(autoPlayernameB);
		LayoutCreatorScreen.colorizeCreatorButton(autoPlayernameB);
		
		AdvancedButton setPlayernameB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.playername.setname"), true, (press) -> {
			this.handler.setMenusUseable(false);
			
			TextInputPopup t = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.playerentity.playername.setname") + ":", null, 240, (call) -> {
				if (call != null) {
					if (!call.equals("")) {
						if ((call != this.getObject().playerName) || (this.isCLientPlayerName != false)) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.isCLientPlayerName = false;
						this.getObject().playerName = call;
						this.reloadEntity();
					} else {
						if (this.getObject().playerName != null) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.isCLientPlayerName = false;
						this.getObject().playerName = null;
						this.reloadEntity();
					}
				}
				
				this.handler.setMenusUseable(true);
			});
			
			if (this.getObject().playerName != null) {
				t.setText(this.getObject().playerName);
			}
			
			PopupHandler.displayPopup(t);
			
		});
		setPlayernameB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.playername.setname.btndesc"), "%n%"));
		playernamePopup.addContent(setPlayernameB);
		LayoutCreatorScreen.colorizeCreatorButton(setPlayernameB);
		
		ContextMenu skinPopup = new ContextMenu(110, 16, -1);
		this.rightclickMenu.addChild(skinPopup);
		
		AdvancedButton skinB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.skin"), true, (press) -> {
			this.rightclickMenu.closeChilds();
			skinPopup.openMenuAt(0, press.y);
		});
		skinB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.skin.btndesc"), "%n%"));
		this.rightclickMenu.addContent(skinB);
		LayoutCreatorScreen.colorizeCreatorButton(skinB);
		
		AdvancedButton localSkinB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.texture.local"), true, (press) -> {
			this.handler.setMenusUseable(false);
			ChooseFilePopup t = new ChooseFilePopup((call) -> {
				if (call != null) {
					if (!call.equals("")) {
						File home = new File("");
						call = call.replace("\\", "/");
						File f = new File(call);
						String filename = CharacterFilter.getBasicFilenameCharacterFilter().filterForAllowedChars(f.getName());
						if (f.exists() && f.isFile() && f.getName().endsWith(".png")) {
							if (filename.equals(f.getName())) {
								if (call.startsWith(home.getAbsolutePath())) {
									call = call.replace(home.getAbsolutePath(), "");
									if (call.startsWith("\\") || call.startsWith("/")) {
										call = call.substring(1);
									}
								}
								if ((this.skinPath == null) || !this.skinPath.equals(call)) {
									this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
								}
								
								this.skinPath = call;
								this.reloadEntity();
								
								this.handler.setMenusUseable(true);
							} else {
								this.handler.displayNotification(300, Locals.localize("helper.creator.textures.invalidcharacters"), "", "", "", "", "", "");
							}
						} else {
							this.handler.displayNotification(300, "§c§l" + Locals.localize("helper.creator.items.playerentity.texture.invalidtexture.title"), "", Locals.localize("helper.creator.items.playerentity.texture.invalidtexture.desc"), "", "", "", "", "", "");
						}
					} else {
						if (this.skinPath != null) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.skinPath = null;
						this.reloadEntity();
						
						this.handler.setMenusUseable(true);
					}
				} else {
					this.handler.setMenusUseable(true);
				}
			}, "png");
			
			if (this.skinPath != null) {
				t.setText(this.skinPath);
			}
			
			PopupHandler.displayPopup(t);
			
		});
		skinPopup.addContent(localSkinB);
		LayoutCreatorScreen.colorizeCreatorButton(localSkinB);
		
		AdvancedButton urlSkinB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.texture.url"), true, (press) -> {
			this.handler.setMenusUseable(false);

			TextInputPopup t = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.playerentity.texture.url") + ":", null, 240, (call) -> {
				if (call != null) {
					if (!call.equals("")) {
						
						call = WebUtils.filterURL(call);
						
						if (WebUtils.isValidUrl(call)) {
							if (this.skinUrl != call) {
								this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
							}

							this.skinUrl = call;
							this.reloadEntity();
							
							this.handler.setMenusUseable(true);
						} else {
							this.handler.displayNotification(300, Locals.localize("helper.creator.web.invalidurl"), "", "", "", "", "", "");
						}
						
					} else {
						
						if (this.skinUrl != null) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.skinUrl = null;
						this.reloadEntity();
						
						this.handler.setMenusUseable(true);
						
					}
				}
			});
			
			if (this.skinUrl != null) {
				t.setText(this.skinUrl);
			}
			
			PopupHandler.displayPopup(t);
			
		});
		skinPopup.addContent(urlSkinB);
		LayoutCreatorScreen.colorizeCreatorButton(urlSkinB);
		
		ContextMenu capePopup = new ContextMenu(110, 16, -1);
		this.rightclickMenu.addChild(capePopup);
		
		AdvancedButton capeB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.cape"), true, (press) -> {
			this.rightclickMenu.closeChilds();
			capePopup.openMenuAt(0, press.y);
		});
		capeB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.cape.btndesc"), "%n%"));
		this.rightclickMenu.addContent(capeB);
		LayoutCreatorScreen.colorizeCreatorButton(capeB);
		
		AdvancedButton localCapeB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.texture.local"), true, (press) -> {
			this.handler.setMenusUseable(false);
			ChooseFilePopup t = new ChooseFilePopup((call) -> {
				if (call != null) {
					if (!call.equals("")) {
						File home = new File("");
						call = call.replace("\\", "/");
						File f = new File(call);
						String filename = CharacterFilter.getBasicFilenameCharacterFilter().filterForAllowedChars(f.getName());
						if (f.exists() && f.isFile() && f.getName().endsWith(".png")) {
							if (filename.equals(f.getName())) {
								if (call.startsWith(home.getAbsolutePath())) {
									call = call.replace(home.getAbsolutePath(), "");
									if (call.startsWith("\\") || call.startsWith("/")) {
										call = call.substring(1);
									}
								}
								if ((this.capePath == null) || !this.capePath.equals(call)) {
									this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
								}
								
								this.capePath = call;
								this.reloadEntity();
								
								this.handler.setMenusUseable(true);
							} else {
								this.handler.displayNotification(300, Locals.localize("helper.creator.textures.invalidcharacters"), "", "", "", "", "", "");
							}
						} else {
							this.handler.displayNotification(300, "§c§l" + Locals.localize("helper.creator.items.playerentity.texture.invalidtexture.title"), "", Locals.localize("helper.creator.items.playerentity.texture.invalidtexture.desc"), "", "", "", "", "", "");
						}
					} else {
						if (this.capePath != null) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.capePath = null;
						this.reloadEntity();
						
						this.handler.setMenusUseable(true);
					}
				} else {
					this.handler.setMenusUseable(true);
				}
			}, "png");
			
			if (this.capePath != null) {
				t.setText(this.capePath);
			}
			
			PopupHandler.displayPopup(t);
			
		});
		capePopup.addContent(localCapeB);
		LayoutCreatorScreen.colorizeCreatorButton(localCapeB);
		
		AdvancedButton urlCapeB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.texture.url"), true, (press) -> {
			this.handler.setMenusUseable(false);

			TextInputPopup t = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.playerentity.texture.url") + ":", null, 240, (call) -> {
				if (call != null) {
					if (!call.equals("")) {
						
						call = WebUtils.filterURL(call);
						
						if (WebUtils.isValidUrl(call)) {
							if (this.capeUrl != call) {
								this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
							}

							this.capeUrl = call;
							this.reloadEntity();
							
							this.handler.setMenusUseable(true);
						} else {
							this.handler.displayNotification(300, Locals.localize("helper.creator.web.invalidurl"), "", "", "", "", "", "");
						}
						
					} else {
						
						if (this.capeUrl != null) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						
						this.capeUrl = null;
						this.reloadEntity();
						
						this.handler.setMenusUseable(true);
						
					}
				}
			});
			
			if (this.capeUrl != null) {
				t.setText(this.capeUrl);
			}
			
			PopupHandler.displayPopup(t);
			
		});
		capePopup.addContent(urlCapeB);
		LayoutCreatorScreen.colorizeCreatorButton(urlCapeB);
		
		ContextMenu rotationPopup = new ContextMenu(110, 16, -1);
		this.rightclickMenu.addChild(rotationPopup);
		
		AdvancedButton rotationB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.rotation"), true, (press) -> {
			this.rightclickMenu.closeChilds();
			rotationPopup.openMenuAt(0, press.y);
		});
		rotationB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.rotation.btndesc"), "%n%"));
		this.rightclickMenu.addContent(rotationB);
		LayoutCreatorScreen.colorizeCreatorButton(rotationB);
		
		AdvancedButton autoRotationB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.rotation.auto"), true, (press) -> {
			if (!this.getObject().autoRotation) {
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			}
			this.getObject().autoRotation = true;
			this.rightclickMenu.closeMenu();
		});
		autoRotationB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.rotation.auto.btndesc"), "%n%"));
		rotationPopup.addContent(autoRotationB);
		LayoutCreatorScreen.colorizeCreatorButton(autoRotationB);
		
		AdvancedButton customRotationB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.playerentity.rotation.custom"), true, (press) -> {
			this.handler.setMenusUseable(false);
			PopupHandler.displayPopup(new PlayerEntityRotationPopup(this.handler, this));
		});
		customRotationB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.rotation.custom.btndesc"), "%n%"));
		rotationPopup.addContent(customRotationB);
		LayoutCreatorScreen.colorizeCreatorButton(customRotationB);
		
		String slimLabel = Locals.localize("helper.creator.items.playerentity.slim.off");
		if (this.getEntity().isSlimSkin()) {
			slimLabel = Locals.localize("helper.creator.items.playerentity.slim.on");
		}
		AdvancedButton slimB = new AdvancedButton(0, 0, 0, 16, slimLabel, true, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (this.getEntity().isSlimSkin()) {
				this.getEntity().setSlimSkin(false);
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.slim.off")));
			} else {
				this.getEntity().setSlimSkin(true);
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.slim.on")));
			}
		});
		slimB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.slim.btndesc"), "%n%"));
		this.rightclickMenu.addContent(slimB);
		LayoutCreatorScreen.colorizeCreatorButton(slimB);
		
		String crouchingLabel = Locals.localize("helper.creator.items.playerentity.crouching.off");
		if (this.getEntity().crouching) {
			crouchingLabel = Locals.localize("helper.creator.items.playerentity.crouching.on");
		}
		AdvancedButton crouchingB = new AdvancedButton(0, 0, 0, 16, crouchingLabel, true, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (this.getEntity().crouching) {
				this.getEntity().crouching = false;
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.crouching.off")));
			} else {
				this.getEntity().crouching = true;
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.crouching.on")));
			}
		});
		crouchingB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.crouching.btndesc"), "%n%"));
		this.rightclickMenu.addContent(crouchingB);
		LayoutCreatorScreen.colorizeCreatorButton(crouchingB);
		
		String showNameLabel = Locals.localize("helper.creator.items.playerentity.showname.off");
		if (this.getEntity().showName) {
			showNameLabel = Locals.localize("helper.creator.items.playerentity.showname.on");
		}
		AdvancedButton showNameB = new AdvancedButton(0, 0, 0, 16, showNameLabel, true, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (this.getEntity().showName) {
				this.getEntity().showName = false;
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.showname.off")));
			} else {
				this.getEntity().showName = true;
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.showname.on")));
			}
		});
		showNameB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.showname.btndesc"), "%n%"));
		this.rightclickMenu.addContent(showNameB);
		LayoutCreatorScreen.colorizeCreatorButton(showNameB);
		
		String parrotLabel = Locals.localize("helper.creator.items.playerentity.parrot.off");
		if (this.getEntity().hasParrot) {
			parrotLabel = Locals.localize("helper.creator.items.playerentity.parrot.on");
		}
		AdvancedButton parrotB = new AdvancedButton(0, 0, 0, 16, parrotLabel, true, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (this.getEntity().hasParrot) {
				this.getEntity().hasParrot = false;
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.parrot.off")));
			} else {
				this.getEntity().hasParrot = true;
				press.setMessage(new LiteralText(Locals.localize("helper.creator.items.playerentity.parrot.on")));
			}
		});
		parrotB.setDescription(StringUtils.splitLines(Locals.localize("helper.creator.items.playerentity.parrot.btndesc"), "%n%"));
		this.rightclickMenu.addContent(parrotB);
		LayoutCreatorScreen.colorizeCreatorButton(parrotB);
		
	}
	
	@Override
	protected void renderBorder(MatrixStack matrix, int mouseX, int mouseY) {
		//horizontal line top
		fill(matrix, this.getEntityPosX(), this.getEntityPosY(), this.getEntityPosX() + this.object.width, this.getEntityPosY() + 1, Color.BLUE.getRGB());
		//horizontal line bottom
		fill(matrix, this.getEntityPosX(), this.getEntityPosY() + this.object.height, this.getEntityPosX() + this.object.width + 1, this.getEntityPosY() + this.object.height + 1, Color.BLUE.getRGB());
		//vertical line left
		fill(matrix, this.getEntityPosX(), this.getEntityPosY(), this.getEntityPosX() + 1, this.getEntityPosY() + this.object.height, Color.BLUE.getRGB());
		//vertical line right
		fill(matrix, this.getEntityPosX() + this.object.width, this.getEntityPosY(), this.getEntityPosX() + this.object.width + 1, this.getEntityPosY() + this.object.height, Color.BLUE.getRGB());
	
		//Render pos and size values
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		RenderUtils.setScale(matrix, 0.5F);
		font.draw(matrix, Locals.localize("helper.creator.items.border.orientation")+ ": " + this.object.orientation, this.getEntityPosX()*2, (this.getEntityPosY()*2) - 35, Color.WHITE.getRGB());
		font.draw(matrix, Locals.localize("helper.creator.items.string.border.scale") + ": " + this.getObject().scale, this.getEntityPosX()*2, (this.getEntityPosY()*2) - 26, Color.WHITE.getRGB());
		font.draw(matrix, Locals.localize("helper.creator.items.border.posx") + ": " + this.getEntityPosX(), this.getEntityPosX()*2, (this.getEntityPosY()*2) - 17, Color.WHITE.getRGB());
		font.draw(matrix, Locals.localize("helper.creator.items.border.width") + ": " + this.object.width, this.getEntityPosX()*2, (this.getEntityPosY()*2) - 8, Color.WHITE.getRGB());
		
		font.draw(matrix, Locals.localize("helper.creator.items.border.posy") + ": " + this.getEntityPosY(), ((this.getEntityPosX() + this.object.width)*2)+3, ((this.getEntityPosY() + this.object.height)*2) - 14, Color.WHITE.getRGB());
		font.draw(matrix, Locals.localize("helper.creator.items.border.height") + ": " + this.object.height, ((this.getEntityPosX() + this.object.width)*2)+3, ((this.getEntityPosY() + this.object.height)*2) - 5, Color.WHITE.getRGB());
		RenderUtils.postScale(matrix);
	}
	
	@Override
	protected void renderHighlightBorder(MatrixStack matrix) {
		Color c = new Color(0, 200, 255, 255);
		
		//horizontal line top
		fill(matrix, this.getEntityPosX(), this.getEntityPosY(), this.getEntityPosX() + this.object.width, this.getEntityPosY() + 1, c.getRGB());
		//horizontal line bottom
		fill(matrix, this.getEntityPosX(), this.getEntityPosY() + this.object.height, this.getEntityPosX() + this.object.width + 1, this.getEntityPosY() + this.object.height + 1, c.getRGB());
		//vertical line left
		fill(matrix, this.getEntityPosX(), this.getEntityPosY(), this.getEntityPosX() + 1, this.getEntityPosY() + this.object.height, c.getRGB());
		//vertical line right
		fill(matrix, this.getEntityPosX() + this.object.width, this.getEntityPosY(), this.getEntityPosX() + this.object.width + 1, this.getEntityPosY() + this.object.height, c.getRGB());
	}
	
	public void reloadEntity() {
		PropertiesSection sec = this.getProperties().get(0);
		this.object = new PlayerEntityCustomizationItem(sec);
	}
	
	public MenuPlayerEntity getEntity() {
		return this.getObject().entity;
	}
	
	private int getEntityPosX() {
		return (int) (this.getObject().getPosX(this.handler) - ((this.getObject().entity.getWidth()*this.getObject().scale) / 2));
	}
	
	private int getEntityPosY() {
		return (int) (this.getObject().getPosY(this.handler) - (this.getObject().entity.getHeight()*this.getObject().scale));
	}
	
	public PlayerEntityCustomizationItem getObject() {
		return ((PlayerEntityCustomizationItem)this.object);
	}
	
	@Override
	public boolean isGrabberPressed() {
		return false;
	}
	
	@Override
	public int getActiveResizeGrabber() {
		return -1;
	}

	@Override
	protected void setOrientation(String pos) {
		super.setOrientation(pos);
		if (this.object.orientation.startsWith("top-")) {
			this.object.posY += this.object.height;
		}
		if (this.object.orientation.startsWith("bottom-")) {
			this.object.posY += this.object.height;
		}
		if (this.object.orientation.startsWith("mid-")) {
			this.object.posY += this.object.height;
		}
		if (this.object.orientation.endsWith("-left")) {
			this.object.posX += this.object.width;
		}
		if (this.object.orientation.endsWith("-centered")) {
			this.object.posX += this.object.width / 2;
		}
	}
	
	public void setScale(int scale) {
		if (this.getObject().scale != scale) {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
		}
		
		this.getObject().scale = scale;
		this.setWidth((int)(this.getObject().entity.getWidth()*scale));
		this.setHeight((int)(this.getObject().entity.getHeight()*scale));
	}
	
	@Override
	protected void updateHovered(int mouseX, int mouseY) {
		if ((mouseX >= this.getEntityPosX()) && (mouseX <= this.getEntityPosX() + this.object.width) && (mouseY >= this.getEntityPosY()) && mouseY <= this.getEntityPosY() + this.object.height) {
			this.hovered = true;
		} else {
			this.hovered = false;
		}
	}
	
	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		
		PropertiesSection p1 = new PropertiesSection("customization");
		p1.addEntry("action", "addentity");
		p1.addEntry("x", "" + this.object.posX);
		p1.addEntry("y", "" + this.object.posY);
		p1.addEntry("orientation", this.object.orientation);
		p1.addEntry("scale", "" + this.getObject().scale);
		if (this.getObject().playerName != null) {
			if (this.isCLientPlayerName) {
				p1.addEntry("playername", "%playername%");
			} else {
				p1.addEntry("playername", this.getObject().playerName);
			}
		}
		if (this.skinPath != null) {
			p1.addEntry("skinpath", "" + this.skinPath);
		}
		if (this.skinUrl != null) {
			p1.addEntry("skinurl", "" + this.skinUrl);
		}
		if (this.capePath != null) {
			p1.addEntry("capepath", "" + this.capePath);
		}
		if (this.capeUrl != null) {
			p1.addEntry("capeurl", "" + this.capeUrl);
		}
		if (!this.getObject().autoRotation) {
			p1.addEntry("autorotation", "false");
			p1.addEntry("bodyrotationx", "" + this.getObject().bodyRotationX);
			p1.addEntry("bodyrotationy", "" + this.getObject().bodyRotationY);
			p1.addEntry("headrotationx", "" + this.getObject().headRotationX);
			p1.addEntry("headrotationy", "" + this.getObject().headRotationY);
		}
		p1.addEntry("slim", "" + this.getEntity().isSlimSkin());
		p1.addEntry("parrot", "" + this.getEntity().hasParrot);
		p1.addEntry("crouching", "" + this.getEntity().crouching);
		p1.addEntry("showname", "" + this.getEntity().showName);
		
		l.add(p1);
		
		return l;
	}

}