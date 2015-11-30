package cn.academy.buff;

import cn.academy.ability.api.data.CPData;
import cn.academy.core.AcademyCraft;
import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@Registrant
public class BuffTypeOverwhelm extends BuffType {
	ResourceLocation icon = new ResourceLocation("academy:textures/guis/buff_icons/overwhelm/overwhelm.png");

	public BuffTypeOverwhelm() {
		super("overwhelm",true);
		this.registBuffType();
	}
	
	@Override
	public void performEffectOnAdded(Buff buff, EntityLivingBase entity, int level) {
		if(entity != null && entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entity;
			CPData.get(player).perform(80, 0);
		}
	}
	
	@Override
	public void performEffectOnTick(Buff buff, EntityLivingBase entity, int duration, int level) {
		if(entity != null && entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entity;
			CPData cp = CPData.get(player);
			cp.perform(Math.max(0, 80-cp.getOverload()), 0);
		}
	}
	
	@Override
	public ResourceLocation getIcon(Buff buff) {
		return icon;
	}
}
