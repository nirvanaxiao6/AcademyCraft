package cn.academy.medicine.item;

import cn.academy.buff.Buff;
import cn.academy.buff.BuffType;
import cn.academy.core.AcademyCraft;
import cn.academy.core.item.ACItem;
import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TestPill extends ACItem {

	public TestPill() {
		super("BanLanGen");
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		stack.stackSize--;
		if(!world.isRemote){
			new Buff(BuffType.get("allergic"),1000).addToEntity(player, player);
		}
		return stack;
	}
}
