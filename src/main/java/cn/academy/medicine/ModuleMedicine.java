package cn.academy.medicine;

import cn.academy.medicine.item.TestPill;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegItem;
import net.minecraft.item.Item;

@Registrant
public class ModuleMedicine {
	@RegItem
	public static Item pill = new TestPill();

}
