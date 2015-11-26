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
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote){
			new Buff(BuffType.get("medicineAllergic"),30).addToEntity(player, player);
		}
		return super.onItemRightClick(stack, world, player);
	}
}
//ItemStack stack, World world, EntityPlayer player