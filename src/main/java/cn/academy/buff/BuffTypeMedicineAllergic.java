package cn.academy.buff;

import cn.academy.core.AcademyCraft;
import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

@Registrant
@RegBuffType
public class BuffTypeMedicineAllergic extends BuffType {
	DamageSource dmgsrc = DamageSource.generic;

	public BuffTypeMedicineAllergic() {
		super("medicineAllergic", true);
		this.setLevelCombineType(CombineType.PlusOne);
		this.setLevelRemoveType(RemoveType.RemoveAll);
	}
	
	@Override
	public void performEffectOnCombine(Buff buff, EntityLivingBase entity, int level) {
		switch(level){
		case 2:
			entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 3*20));
			entity.attackEntityFrom(dmgsrc, Math.min(entity.getHealth()-1, 3));
			break;
		case 3:
			entity.addPotionEffect(new PotionEffect(Potion.confusion.id, 15*20));
			entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 15*20));
			entity.attackEntityFrom(dmgsrc, 5);
			break;
		case 4:
			new Buff(BuffType.get("overwhelm"),10*60*20).addToEntity((EntityLivingBase) entity.worldObj.getEntityByID(buff.getOrigin()), entity);
			entity.attackEntityFrom(dmgsrc, 10);
			break;
		}
		if(level>=5){
			entity.attackEntityFrom(dmgsrc, Float.MAX_VALUE);
		}
	}
	
	@Override
	public void debug(Buff buff) {
		String funcName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if(!funcName.equals("tick")||buff.getDuration()%10==0)
			AcademyCraft.log.info("\n" + funcName + " : " +
				"\n	level : " + buff.getLevel() +
				"\n	duration : " + buff.getDuration() +
				"\n	originID : " + buff.getOrigin()
				);
	}
}
