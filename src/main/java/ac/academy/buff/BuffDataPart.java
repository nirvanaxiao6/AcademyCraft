package ac.academy.buff;

import java.util.ArrayList;

import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.util.datapart.DataPart;
import cn.lambdalib.util.datapart.RegDataPart;
import net.minecraft.nbt.NBTTagCompound;

@Registrant
@RegDataPart("Buff")
public class BuffDataPart extends DataPart {
	ArrayList<Buff> activedBuff = new ArrayList<Buff>();
	
	@Override
	public void fromNBT(NBTTagCompound tag) {
		activedBuff.clear();
		tag.func_150296_c().stream().forEach(s->{
			String key = (String)s;
			activedBuff.add(Buff.fromNBTTag(key, tag.getCompoundTag(key)));
		});
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		activedBuff.stream().forEach(buff->{
			nbt.setTag(buff.getType().id, buff.toNBTTag());
		});
		return nbt;
	}
}
