package de.keksuccino.fancymenu.menu.fancy.menuhandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import de.keksuccino.core.input.MouseInput;
import de.keksuccino.core.math.MathUtils;
import de.keksuccino.core.properties.PropertiesSection;
import de.keksuccino.core.properties.PropertiesSet;
import de.keksuccino.core.rendering.animation.IAnimationRenderer;
import de.keksuccino.core.resources.ExternalTextureHandler;
import de.keksuccino.core.resources.ExternalTextureResourceLocation;
import de.keksuccino.core.sound.SoundHandler;
import de.keksuccino.fancymenu.menu.animation.AdvancedAnimation;
import de.keksuccino.fancymenu.menu.animation.AnimationHandler;
import de.keksuccino.fancymenu.menu.button.ButtonCache;
import de.keksuccino.fancymenu.menu.button.ButtonCachedEvent;
import de.keksuccino.fancymenu.menu.button.ButtonData;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomization;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomizationProperties;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutCreatorScreen;
import de.keksuccino.fancymenu.menu.fancy.item.AnimationCustomizationItem;
import de.keksuccino.fancymenu.menu.fancy.item.ButtonCustomizationItem;
import de.keksuccino.fancymenu.menu.fancy.item.CustomizationItemBase;
import de.keksuccino.fancymenu.menu.fancy.item.StringCustomizationItem;
import de.keksuccino.fancymenu.menu.fancy.item.TextureCustomizationItem;
import de.keksuccino.fancymenu.menu.fancy.item.VanillaButtonCustomizationItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;


public class MenuHandlerBase {
	
	protected List<CustomizationItemBase> frontRenderItems = new ArrayList<CustomizationItemBase>();
	protected List<CustomizationItemBase> backgroundRenderItems = new ArrayList<CustomizationItemBase>();
	
	protected Map<String, Boolean> audio = new HashMap<String, Boolean>();
	protected List<String> oldAudio = new ArrayList<String>();
	protected IAnimationRenderer backgroundAnimation = null;
	protected IAnimationRenderer lastBackgroundAnimation = null;
	protected List<IAnimationRenderer> backgroundAnimations = new ArrayList<IAnimationRenderer>();
	protected int backgroundAnimationId = 0;
	protected ExternalTextureResourceLocation backgroundTexture = null;
	private String identifier;
	private boolean backgroundDrawable;
	
	/**
	 * @param identifier Has to be the valid and full class name of the GUI screen.
	 */
	public MenuHandlerBase(@Nonnull String identifier) {
		this.identifier = identifier;
	}
	
	public String getMenuIdentifier() {
		return this.identifier;
	}
	
	@SubscribeEvent
	public void onInitPost(ButtonCachedEvent e) {
		if (!this.shouldCustomize(e.getGui())) {
			return;
		}
		if (!AnimationHandler.isReady()) {
			return;
		}
		if (LayoutCreatorScreen.isActive) {
			return;
		}
		
		List<GuiButton> buttons = e.getButtonList();
		List<PropertiesSet> props = MenuCustomizationProperties.getPropertiesWithIdentifier(this.getMenuIdentifier());
		
		audio.clear();
		frontRenderItems.clear();
		backgroundRenderItems.clear();
		this.backgroundAnimations.clear();
		if ((this.backgroundAnimation != null) && (this.backgroundAnimation instanceof AdvancedAnimation)) {
			((AdvancedAnimation)this.backgroundAnimation).stopAudio();
		}
		this.backgroundAnimation = null;
		this.backgroundDrawable = false;
		
		boolean backgroundTextureSet = false;

		for (PropertiesSet s : props) {
			List<PropertiesSection> metas = s.getPropertiesOfType("customization-meta");
			if (metas.isEmpty()) {
				metas = s.getPropertiesOfType("type-meta");
			}
			if (metas.isEmpty()) {
				continue;
			}
			String renderOrder = metas.get(0).getEntryValue("renderorder");
			for (PropertiesSection sec : s.getPropertiesOfType("customization")) {
				String action = sec.getEntryValue("action");
				if (action != null) {
					String identifier = sec.getEntryValue("identifier");
					GuiButton b = null;
					if (identifier != null) {
						b = getButton(identifier, buttons);
					}

					if (action.equalsIgnoreCase("texturizebackground")) {
						String value = sec.getEntryValue("path");
						if (value != null) {
							File f = new File(value.replace("\\", "/"));
							if (f.exists() && f.isFile() && (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".png"))) {
								if ((this.backgroundTexture == null) || !this.backgroundTexture.getPath().equals(value)) {
									this.backgroundTexture = ExternalTextureHandler.getResource(value);
								}
								backgroundTextureSet = true;
							}
						}
					}
					
					if (action.equalsIgnoreCase("animatebackground")) {
						String value = sec.getEntryValue("name");
						String random = sec.getEntryValue("random");
						boolean ran = false;
						if ((random != null) && random.equalsIgnoreCase("true")) {
							ran = true;
						}
						if (value != null) {
							if (value.contains(",")) {
								for (String s2 : value.split("[,]")) {
									int i = 0;
									for (char c : s2.toCharArray()) {
										if (c != " ".charAt(0)) {
											break;
										}
										i++;
									}
									if (i > s2.length()) {
										continue;
									}
									String temp = new StringBuilder(s2.substring(i)).reverse().toString();
									int i2 = 0;
									for (char c : temp.toCharArray()) {
										if (c != " ".charAt(0)) {
											break;
										}
										i2++;
									}
									String name = new StringBuilder(temp.substring(i2)).reverse().toString();
									if (AnimationHandler.animationExists(name)) {
										this.backgroundAnimations.add(AnimationHandler.getAnimation(name));
									}
								}
							} else {
								if (AnimationHandler.animationExists(value)) {
									this.backgroundAnimations.add(AnimationHandler.getAnimation(value));
								}
							}
							
							if (!this.backgroundAnimations.isEmpty()) {
								if (ran) {
									if ((MenuHandlerRegistry.getLastActiveHandler() == null) || (MenuHandlerRegistry.getLastActiveHandler() != this)) {
										this.backgroundAnimationId = MathUtils.getRandomNumberInRange(0, this.backgroundAnimations.size()-1);
									}
									this.backgroundAnimation = this.backgroundAnimations.get(this.backgroundAnimationId);
								} else {
									if ((this.lastBackgroundAnimation != null) && this.backgroundAnimations.contains(this.lastBackgroundAnimation)) {
										this.backgroundAnimation = this.lastBackgroundAnimation;
									} else {
										this.backgroundAnimationId = 0;
										this.backgroundAnimation = this.backgroundAnimations.get(0);
									}
									this.lastBackgroundAnimation = this.backgroundAnimation;
								}
							}
						}
					}
					
					if (action.equalsIgnoreCase("hidebutton")) {
						if (b != null) {
							b.visible = false;
						}
					}
					
					if (action.equalsIgnoreCase("renamebutton")) {
						String value = sec.getEntryValue("value");
						if ((value != null) && (b != null)) {
							b.displayString = value;
						}
					}
					
					if (action.equalsIgnoreCase("resizebutton")) {
						String width = sec.getEntryValue("width");
						String height = sec.getEntryValue("height");
						if ((width != null) && (height != null) && MathUtils.isInteger(width) && MathUtils.isInteger(height) && (b != null)) {
							int w = Integer.parseInt(width);
							int h = Integer.parseInt(height);
							b.setWidth(w);
							b.height = h;
						}
					}
					
					if (action.equalsIgnoreCase("movebutton")) {
						String posX = sec.getEntryValue("x");
						String posY = sec.getEntryValue("y");
						String orientation = sec.getEntryValue("orientation");
						if ((orientation != null) && (posX != null) && (posY != null) && MathUtils.isInteger(posX) && MathUtils.isInteger(posY) && (b != null)) {
							int x = Integer.parseInt(posX);
							int y = Integer.parseInt(posY);
							int w = e.getGui().width;
							int h = e.getGui().height;

							//TODO Remove deprecated "original" orientation
							if (orientation.equalsIgnoreCase("original")) {
								b.x = b.x + x;
								b.y = b.y + y;
							}
							//-----------------------------
							if (orientation.equalsIgnoreCase("top-left")) {
								b.x = x;
								b.y = y;
							}
							
							if (orientation.equalsIgnoreCase("mid-left")) {
								b.x = x;
								b.y = (h / 2) + y;
							}
							
							if (orientation.equalsIgnoreCase("bottom-left")) {
								b.x = x;
								b.y = h + y;
							}
							//----------------------------
							if (orientation.equalsIgnoreCase("top-centered")) {
								b.x = (w / 2) + x;
								b.y = y;
							}
							
							if (orientation.equalsIgnoreCase("mid-centered")) {
								b.x = (w / 2) + x;
								b.y = (h / 2) + y;
							}
							
							if (orientation.equalsIgnoreCase("bottom-centered")) {
								b.x = (w / 2) + x;
								b.y = h + y;
							}
							//-----------------------------
							if (orientation.equalsIgnoreCase("top-right")) {
								b.x = w + x;
								b.y = y;
							}
							
							if (orientation.equalsIgnoreCase("mid-right")) {
								b.x = w + x;
								b.y = (h / 2) + y;
							}
							
							if (orientation.equalsIgnoreCase("bottom-right")) {
								b.x = w + x;
								b.y = h + y;
							}
						}
					}
					
					if (action.equalsIgnoreCase("setbuttontexture")) {
						if (b != null) {
							String backNormal = sec.getEntryValue("backgroundnormal");
							String backHover = sec.getEntryValue("backgroundhovered");
							if ((backNormal != null) && (backHover != null)) {
								File f = new File(backNormal.replace("\\", "/"));
								File f2 = new File(backHover.replace("\\", "/"));
								if (f.isFile() && f.exists() && f2.isFile() && f2.exists()) {
									b.visible = false;
									frontRenderItems.add(new VanillaButtonCustomizationItem(sec, b));
								}
							}
						}
					}
					
					if (action.equalsIgnoreCase("clickbutton")) {
						if (b != null) {
							String clicks = sec.getEntryValue("clicks");
							if ((clicks != null) && (MathUtils.isInteger(clicks))) {
								for (int i = 0; i < Integer.parseInt(clicks); i++) {
									b.mousePressed(Minecraft.getMinecraft(), MouseInput.getMouseX(), MouseInput.getMouseY());
									try {
										Method m = ReflectionHelper.findMethod(GuiScreen.class, "actionPerformed", "func_146284_a", GuiButton.class);
										m.invoke(Minecraft.getMinecraft().currentScreen, b);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						}
					}
					
					if (action.equalsIgnoreCase("addtext")) {
						if ((renderOrder != null) && renderOrder.equalsIgnoreCase("background")) {
							backgroundRenderItems.add(new StringCustomizationItem(sec));
						} else {
							frontRenderItems.add(new StringCustomizationItem(sec));
						}
					}
					
					if (action.equalsIgnoreCase("addtexture")) {
						if ((renderOrder != null) && renderOrder.equalsIgnoreCase("background")) {
							backgroundRenderItems.add(new TextureCustomizationItem(sec));
						} else {
							frontRenderItems.add(new TextureCustomizationItem(sec));
						}
					}
					
					if (action.equalsIgnoreCase("addanimation")) {
						if ((renderOrder != null) && renderOrder.equalsIgnoreCase("background")) {
							backgroundRenderItems.add(new AnimationCustomizationItem(sec));
						} else {
							frontRenderItems.add(new AnimationCustomizationItem(sec));
						}
					}
					
					if (action.equalsIgnoreCase("addbutton")) {
						if ((renderOrder != null) && renderOrder.equalsIgnoreCase("background")) {
							backgroundRenderItems.add(new ButtonCustomizationItem(sec));
						} else {
							frontRenderItems.add(new ButtonCustomizationItem(sec));
						}
					}
					
					if (action.equalsIgnoreCase("addaudio")) {
						String path = sec.getEntryValue("path");
						String loopString = sec.getEntryValue("loop");
						boolean loop = false; 
						if ((loopString != null) && loopString.equalsIgnoreCase("true")) {
							loop = true;
						}
						if (path != null) {
							File f = new File(path);
							if (f.isFile() && f.exists() && f.getName().endsWith(".wav")) {
								try {
									String name = path + Files.size(f.toPath());
									MenuCustomization.registerSound(name, path);
									this.audio.put(name, loop);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
					
				}
			}
		}
		
		MenuHandlerRegistry.setActiveHandler(this.getMenuIdentifier());
		
		for (String s : this.oldAudio) {
			if (!this.audio.containsKey(s)) {
				SoundHandler.stopSound(s);
			}
		}
		
		this.oldAudio.clear();
		
		for (Map.Entry<String, Boolean> m : this.audio.entrySet()) {
			SoundHandler.playSound(m.getKey());
			if (m.getValue()) {
				SoundHandler.setLooped(m.getKey(), true);
			}
			this.oldAudio.add(m.getKey());
		}
		
		if (!backgroundTextureSet) {
			this.backgroundTexture = null;
		}
	}

	@SubscribeEvent
	public void onRenderPost(GuiScreenEvent.DrawScreenEvent.Post e) {
		if (!this.shouldCustomize(e.getGui())) {
			return;
		}
		
		if (!this.backgroundDrawable) {
			//Rendering all items which SHOULD be rendered in the background IF it's not possible to render them in the background (In this case, they will be forced to render in the foreground)
			for (CustomizationItemBase i : this.backgroundRenderItems) {
				try {
					i.render(e.getGui());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		//Rendering all items which should be rendered in the foreground
		for (CustomizationItemBase i : this.frontRenderItems) {
			try {
				i.render(e.getGui());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent
	public void drawToBackground(GuiScreenEvent.BackgroundDrawnEvent e) {
		if (this.shouldCustomize(e.getGui())) {
			//Rendering the background animation to the menu
			if (this.canRenderBackground()) {
				if ((this.backgroundAnimation != null) && this.backgroundAnimation.isReady()) {
					boolean b = this.backgroundAnimation.isStretchedToStreensize();
					this.backgroundAnimation.setStretchImageToScreensize(true);
					this.backgroundAnimation.render();
					this.backgroundAnimation.setStretchImageToScreensize(b);
				} else if (this.backgroundTexture != null) {
					GlStateManager.enableBlend();
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.backgroundTexture.getResourceLocation());
					GuiScreen.drawModalRectWithCustomSizedTexture(0, 0, 1.0F, 1.0F, e.getGui().width, e.getGui().height, e.getGui().width, e.getGui().height);
					GlStateManager.disableBlend();
				}
			}

			//Rendering all items which should be rendered in the background
			for (CustomizationItemBase i : this.backgroundRenderItems) {
				try {
					i.render(e.getGui());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			this.backgroundDrawable = true;
		}
	}
	
	private static GuiButton getButton(String identifier, List<GuiButton> buttons) {
		if (identifier.startsWith("%id=")) { //%id=1%
			String p = identifier.split("[=]")[1].replace("%", "");
			if (!MathUtils.isInteger(p)) {
				return null;
			}
			int id = Integer.parseInt(p);
			
			ButtonData b = ButtonCache.getButtonForId(id);
			if (b != null) {
				return b.getButton();
			}
		} else {
			ButtonData b = null;
			if (I18n.hasKey(identifier)) {
				b = ButtonCache.getButtonForKey(identifier);
			} else {
				b = ButtonCache.getButtonForName(identifier);
			}
			if (b != null) {
				return b.getButton();
			}
		}
		return null;
	}
	
	protected boolean shouldCustomize(GuiScreen menu) {
		if (getMenuIdentifier() != null) {
			if (!this.getMenuIdentifier().equals(menu.getClass().getName())) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canRenderBackground() {
		return ((this.backgroundAnimation != null) || (this.backgroundTexture != null));
	}
	
	public boolean setBackgroundAnimation(int id) {
		if (id < this.backgroundAnimations.size()) {
			this.backgroundAnimationId = id;
			this.backgroundAnimation = this.backgroundAnimations.get(id);
			this.lastBackgroundAnimation = this.backgroundAnimation;
			return true;
		}
		return false;
	}
	
	public int getCurrentBackgroundAnimationId() {
		return this.backgroundAnimationId;
	}
	
	public List<IAnimationRenderer> backgroundAnimations() {
		return this.backgroundAnimations;
	}

}
