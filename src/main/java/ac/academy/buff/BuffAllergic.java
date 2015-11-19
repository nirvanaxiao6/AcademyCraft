package ac.academy.buff;

import net.minecraft.entity.EntityLivingBase;

public class BuffAllergic extends BuffType {

	public BuffAllergic() {
		super("allergic", 1, true);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int level) {
		switch(level){
		case 0:
			break;
		}
	}
	
}
