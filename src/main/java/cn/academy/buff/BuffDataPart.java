package cn.academy.buff;

import java.util.HashMap;

import cn.academy.core.AcademyCraft;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.util.datapart.DataPart;
import cn.lambdalib.util.datapart.EntityData;
import cn.lambdalib.util.datapart.RegDataPart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

@Registrant
@RegDataPart("AC_Buff")
public class BuffDataPart extends DataPart<EntityLivingBase> {
	HashMap<String, Buff> activedBuff = new HashMap<String, Buff>();
	
	public static BuffDataPart get(EntityLivingBase entity){
		return EntityData.get(entity).getPart(BuffDataPart.class);
	}
	
	@Override
	public void fromNBT(NBTTagCompound tag) {
		activedBuff.clear();
		for(Object s:tag.func_150296_c()){
			String key = (String)s;
			activedBuff.put(key, Buff.fromNBTTag(key, tag.getCompoundTag(key)));
		}
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		for(Buff buff:activedBuff.values()){
			nbt.setTag(buff.getType().id, buff.toNBTTag());
		}
		return nbt;
	}
	
	@Override
	public void tick() {
		for(Buff buff:activedBuff.values()){
			
			if(AcademyCraft.DEBUG_MODE){
				buff.getType().debug(buff);
			}
			
			if(!buff.onUpdate(getEntity())){
				remove(buff);
			}
		}
	}
	
	void add(Buff buff) {
		
		if(AcademyCraft.DEBUG_MODE){
			buff.getType().debug(buff);
		}
		
		if(this.activedBuff.containsKey(buff.getType().id)){
			this.activedBuff.get(buff.getType().id).combine(buff);
		}else{
			this.activedBuff.put(buff.getType().id, buff);
		}
	}

	void remove(Buff buff) {
		
		if(AcademyCraft.DEBUG_MODE){
			buff.getType().debug(buff);
		}
		
		this.activedBuff.remove(buff.getType().id);
	}
}
