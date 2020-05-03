package de.keksuccino.fancymenu.menu.fancy.menuhandler.custom.videosettings;

import com.mojang.blaze3d.platform.GlStateManager;

import de.keksuccino.fancymenu.menu.fancy.menuhandler.MenuHandlerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

public class VideoSettingsList extends OptionsRowList {

	private MenuHandlerBase handler;
	
	public VideoSettingsList(Minecraft mc, int width, int height, int p_i51130_4_, int p_i51130_5_, int p_i51130_6_, MenuHandlerBase handler) {
		super(mc, width, height, p_i51130_4_, p_i51130_5_, p_i51130_6_);
		this.handler = handler;
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		int i = this.getScrollbarPosition();
		int j = i + 6;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		if (!this.handler.canRenderBackground()) {
			this.minecraft.getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
		      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		      bufferbuilder.pos((double)this.x0, (double)this.y1, 0.0D).tex((double)((float)this.x0 / 32.0F), (double)((float)(this.y1 + (int)this.getScrollAmount()) / 32.0F)).color(32, 32, 32, 255).endVertex();
		      bufferbuilder.pos((double)this.x1, (double)this.y1, 0.0D).tex((double)((float)this.x1 / 32.0F), (double)((float)(this.y1 + (int)this.getScrollAmount()) / 32.0F)).color(32, 32, 32, 255).endVertex();
		      bufferbuilder.pos((double)this.x1, (double)this.y0, 0.0D).tex((double)((float)this.x1 / 32.0F), (double)((float)(this.y0 + (int)this.getScrollAmount()) / 32.0F)).color(32, 32, 32, 255).endVertex();
		      bufferbuilder.pos((double)this.x0, (double)this.y0, 0.0D).tex((double)((float)this.x0 / 32.0F), (double)((float)(this.y0 + (int)this.getScrollAmount()) / 32.0F)).color(32, 32, 32, 255).endVertex();
		      tessellator.draw();
		}
		
		int k = this.getRowLeft();
		int l = this.y0 + 4 - (int)this.getScrollAmount();

		this.renderList(k, l, p_render_1_, p_render_2_, p_render_3_);
	      GlStateManager.disableDepthTest();
	      this.renderHoleBackground(0, this.y0, 255, 255);
	      this.renderHoleBackground(this.y1, this.height, 255, 255);
	      GlStateManager.enableBlend();
	      GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
	      GlStateManager.disableAlphaTest();
	      GlStateManager.shadeModel(7425);
	      GlStateManager.disableTexture();
	      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	      bufferbuilder.pos((double)this.x0, (double)(this.y0 + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
	      bufferbuilder.pos((double)this.x1, (double)(this.y0 + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
	      bufferbuilder.pos((double)this.x1, (double)this.y0, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	      bufferbuilder.pos((double)this.x0, (double)this.y0, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	      tessellator.draw();
	      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	      bufferbuilder.pos((double)this.x0, (double)this.y1, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	      bufferbuilder.pos((double)this.x1, (double)this.y1, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	      bufferbuilder.pos((double)this.x1, (double)(this.y1 - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
	      bufferbuilder.pos((double)this.x0, (double)(this.y1 - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
	      tessellator.draw();
	      int j1 = this.getMaxScroll();
	      if (j1 > 0) {
	         int k1 = (int)((float)((this.y1 - this.y0) * (this.y1 - this.y0)) / (float)this.getMaxPosition());
	         k1 = MathHelper.clamp(k1, 32, this.y1 - this.y0 - 8);
	         int l1 = (int)this.getScrollAmount() * (this.y1 - this.y0 - k1) / j1 + this.y0;
	         if (l1 < this.y0) {
	            l1 = this.y0;
	         }

	         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	         bufferbuilder.pos((double)i, (double)this.y1, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	         bufferbuilder.pos((double)j, (double)this.y1, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	         bufferbuilder.pos((double)j, (double)this.y0, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	         bufferbuilder.pos((double)i, (double)this.y0, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	         tessellator.draw();
	         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	         bufferbuilder.pos((double)i, (double)(l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	         bufferbuilder.pos((double)j, (double)(l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	         bufferbuilder.pos((double)j, (double)l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	         bufferbuilder.pos((double)i, (double)l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	         tessellator.draw();
	         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	         bufferbuilder.pos((double)i, (double)(l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
	         bufferbuilder.pos((double)(j - 1), (double)(l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
	         bufferbuilder.pos((double)(j - 1), (double)l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
	         bufferbuilder.pos((double)i, (double)l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
	         tessellator.draw();
	      }

	      this.renderDecorations(p_render_1_, p_render_2_);
	      GlStateManager.enableTexture();
	      GlStateManager.shadeModel(7424);
	      GlStateManager.enableAlphaTest();
	      GlStateManager.disableBlend();
	}
	
	private int getMaxScroll() {
		return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
	}

}
