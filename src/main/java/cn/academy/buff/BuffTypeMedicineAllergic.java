package cn.academy.buff;

import cn.academy.core.AcademyCraft;
import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

@Registrant
public class BuffTypeMedicineAllergic extends BuffType {
	DamageSource dmgsrc = (new DamageSource("medicineAllergic")).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();
	ResourceLocation[] icons = {
			tex(1),tex(2),tex(3),tex(4),tex(5)
	};
	
	private static ResourceLocation tex(int i) {
		return new ResourceLocation("academy:textures/guis/buff_icons/medicine_allergic/" + "level" + i + ".png");
	}
	
	public BuffTypeMedicineAllergic() {
		super("medicineAllergic", true);
		this.setLevelCombineType(CombineType.PlusOne);
		this.setLevelRemoveType(RemoveType.RemoveAll);
		this.registBuffType();
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
			new Buff(ModuleBuffType.overwhelm,10*60*20).addToEntity(buff.getOrigin(), entity);
			entity.attackEntityFrom(dmgsrc, 7);
			break;
		}
		if(level>=5){
			int i = entity.worldObj.rand.nextInt(10);
			if(i<level)
				entity.attackEntityFrom(dmgsrc, Float.MAX_VALUE);
			else
				entity.attackEntityFrom(dmgsrc, 7);
			AcademyCraft.log.info(i);
		}
	}
	
	@Override
	public ResourceLocation getIcon(Buff buff) {
		int level = Math.min(5, buff.getLevel());
		return icons[level-1];
	}
	
	@Override
	public void debug(Buff buff) {
		String funcName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if(!funcName.equals("tick")||buff.getDuration()%10==0)
			AcademyCraft.log.info("\n" + funcName + " : " +
				"\n	level : " + buff.getLevel() +
				"\n	duration : " + buff.getDuration() +
				"\n	origin : " + buff.getOrigin()
				);
	}
}
