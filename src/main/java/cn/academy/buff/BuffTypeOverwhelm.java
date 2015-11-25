package cn.academy.buff;

import cn.academy.ability.api.data.CPData;
import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@Registrant
@RegBuffType
public class BuffTypeOverwhelm extends BuffType {

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
			CPData.get(player).perform(0, 0);
		}
	}
}
