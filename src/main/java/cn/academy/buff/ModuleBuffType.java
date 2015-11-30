package cn.academy.buff;

import cn.lambdalib.annoreg.core.Registrant;
import net.minecraft.util.ResourceLocation;

@Registrant
public class ModuleBuffType {
	@RegBuffType
	public static BuffType
	medicineAllergic = new BuffTypeMedicineAllergic(),
	overwhelm = new BuffTypeOverwhelm();
}
