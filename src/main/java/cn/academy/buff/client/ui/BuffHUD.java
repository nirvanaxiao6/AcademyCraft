package cn.academy.buff.client.ui;

import org.lwjgl.opengl.GL11;

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

@Registrant
@RegAuxGui
public class BuffHUD extends AuxGui{
	

	@Override
	public boolean isForeground() {
		return false;
	}

	@Override
	public void draw(ScaledResolution sr) {
		final int WIDTH = sr.getScaledWidth(), HEIGHT = sr.getScaledHeight();
		int X0 = -16*sr.getScaleFactor(), Y0 = 16*sr.getScaleFactor();
		double len = 16*sr.getScaleFactor(),
		        hei = 16*sr.getScaleFactor();
		Tessellator t = Tessellator.instance;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		BuffDataPart data = BuffDataPart.get(player);
		GL11.glPushMatrix();
		for(Buff buff : data.getActivedBuff().values()){
			if(!buff.getType().showInHUD || buff.getIcon()==null)
				continue;
			RenderUtils.loadTexture(buff.getIcon());
			
			t.startDrawingQuads();
			t.addVertexWithUV(X0 + (WIDTH - len), Y0, -90, 0, 0);
			t.addVertexWithUV(X0 + (WIDTH - len), Y0 + hei, -90, 0, 1);
			t.addVertexWithUV(X0 + WIDTH, Y0 + hei, -90, 1, 1);
			t.addVertexWithUV(X0 + WIDTH, Y0, -90, 1, 0);
			t.draw();
			
			fr.drawStringWithShadow(buff.getDurationString(), (int) (X0 + (WIDTH - len)), (int) (Y0 + hei),0x00FFFFFF);
			X0-=18*sr.getScaleFactor();
		}
		GL11.glPopMatrix();
	}
}
