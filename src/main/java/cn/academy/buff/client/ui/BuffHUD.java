package cn.academy.buff.client.ui;

import cn.academy.buff.BuffDataPart;
import cn.lambdalib.util.client.auxgui.AuxGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public class BuffHUD extends AuxGui{

	@Override
	public boolean isForeground() {
		return false;
	}

	@Override
	public void draw(ScaledResolution sr) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		BuffDataPart data = null;//= PlayerData.get(null).getPart(BuffDataPart.class);
		
		
		
		
	}

}
