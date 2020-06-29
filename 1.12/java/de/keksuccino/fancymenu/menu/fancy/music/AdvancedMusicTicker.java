package de.keksuccino.fancymenu.menu.fancy.music;

import java.lang.reflect.Field;

import de.keksuccino.fancymenu.FancyMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class AdvancedMusicTicker extends MusicTicker {

	public AdvancedMusicTicker(Minecraft client) {
		super(client);
	}
	
	@Override
	public void playMusic(MusicType type) {
		if ((type != null) && (type == MusicType.MENU) && !FancyMenu.config.getOrDefault("playmenumusic", true)) {
			return;
		}
		super.playMusic(type);
	}
	
	public void stop() {
		if (this.getCurrentMusic() != null) {
	         Minecraft.getMinecraft().getSoundHandler().stopSound(this.getCurrentMusic());
	         this.setCurrentMusic(null);
	         this.setTimeUntilNext(0);
	      }
	}
	
	protected ISound getCurrentMusic() {
		try {
			Field f = ReflectionHelper.findField(MusicTicker.class, "currentMusic", "field_147678_c");
			return (ISound) f.get(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void setCurrentMusic(ISound sound) {
		try {
			Field f = ReflectionHelper.findField(MusicTicker.class, "currentMusic", "field_147678_c");
			f.set(this, sound);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void setTimeUntilNext(int time) {
		try {
			Field f = ReflectionHelper.findField(MusicTicker.class, "timeUntilNextMusic", "field_147676_d");
			f.set(this, time);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}