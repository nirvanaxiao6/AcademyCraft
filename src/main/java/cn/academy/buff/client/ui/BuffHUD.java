package cn.academy.buff.client.ui;

import org.lwjgl.opengl.GL11;

import cn.academy.ability.api.data.CPData;
import cn.academy.buff.Buff;
import cn.academy.buff.BuffDataPart;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.util.client.RenderUtils;
import cn.lambdalib.util.client.auxgui.AuxGui;
import cn.lambdalib.util.client.auxgui.AuxGuiRegistry.RegAuxGui;
import cn.lambdalib.util.datapart.EntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@Registrant
@RegAuxGui
public class BuffHUD extends AuxGui{
	ResourceLocation more = new ResourceLocation("academy:textures/guis/buff_icons/more.png");

	@Override
	public boolean isForeground() {
		return false;
	}

	@Override
	public void draw(ScaledResolution sr) {
		
		final int WIDTH = sr.getScaledWidth(), HEIGHT = sr.getScaledHeight();
		int X0 = -8*sr.getScaleFactor(),
				Y0 = 20*sr.getScaleFactor();
		int len = 10*sr.getScaleFactor(),
		        hei = 10*sr.getScaleFactor();
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		BuffDataPart data = BuffDataPart.get(player);
		Buff[] buffs = new Buff[6];
		buffs = data.getActivedBuff().values().toArray(buffs);
		int showedBuffNum = Math.min(data.getActivedBuff().values().size(), 5);
		
		if(!CPData.get(player).isActivated())
			return;

		GL11.glPushMatrix();
		for(int i = 0;i<=showedBuffNum;i++){
			Buff buff = buffs[i];
			if(buff == null || !buff.getType().showInHUD || buff.getIcon()==null)
				continue;
			drawIcon(buff.getIcon(), X0 + WIDTH, Y0, len, hei);
			fr.drawStringWithShadow(buff.getDurationString(), (int) (X0 + (WIDTH - len)), (int) (Y0 + hei),0x00FFFFFF);
			Y0+=(4*sr.getScaleFactor()+hei);
		}
		if(showedBuffNum > 5)
			drawIcon(more, X0+WIDTH, Y0, len, hei);
		GL11.glPopMatrix();
	}
	
	void drawIcon(ResourceLocation icon, int x, int y, int len, int hei) {

		RenderUtils.loadTexture(icon);

		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(x - len, y, -90, 0, 0);
		t.addVertexWithUV(x - len, y + hei, -90, 0, 1);
		t.addVertexWithUV(x, y + hei, -90, 1, 1);
		t.addVertexWithUV(x, y, -90, 1, 0);
		t.draw();
	}
}
