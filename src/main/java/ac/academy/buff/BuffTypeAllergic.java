package ac.academy.buff;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class BuffTypeAllergic extends BuffType {

	public BuffTypeAllergic() {
		super("allergic", true);
		this.registBuffType();
	}
	
	@Override
	public void performEffectOnCombine(Buff buff, EntityLivingBase entity, int level) {
		switch(level){
		case 2:
			entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 3*20));
			entity.attackEntityFrom(DamageSource.causePlayerDamage(buff.getOrigin()), Math.min(entity.getHealth()-1, 3));
			break;
		case 3:
			entity.addPotionEffect(new PotionEffect(Potion.confusion.id, 5*20));
			entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 15*20));
			entity.attackEntityFrom(DamageSource.causePlayerDamage(buff.getOrigin()), 5);
			break;
		case 4:
			new Buff(BuffType.get("Overwhelm"),10*60*20).addToEntity(buff.getOrigin(), entity);
			entity.attackEntityFrom(DamageSource.causePlayerDamage(buff.getOrigin()), 10);
			break;
		case 5:
			entity.setDead();
			break;
		}
	}
}
