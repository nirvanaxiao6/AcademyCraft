package cn.academy.buff;

import java.util.HashMap;
import java.util.LinkedList;

import cn.academy.core.AcademyCraft;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.util.datapart.DataPart;
import cn.lambdalib.util.datapart.EntityData;
import cn.lambdalib.util.datapart.RegDataPart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

@Registrant
@RegDataPart("AC_Buff")
public class BuffDataPart extends DataPart<EntityLivingBase> {
	HashMap<String, Buff> activedBuff;
	
	public BuffDataPart(){
		super();
		this.setTick();
		this.clearOnDeath();
		activedBuff = new HashMap<String, Buff>();
	}
	
	public HashMap<String,Buff> getActivedBuff() {
		return this.activedBuff;
	}
	
	public static BuffDataPart get(EntityLivingBase entity){
		return EntityData.get(entity).getPart(BuffDataPart.class);
	}
	
	@Override
	public void fromNBT(NBTTagCompound tag) {
		activedBuff.clear();
		for(Object s:tag.func_150296_c()){
			String key = (String)s;
			Buff buff = Buff.fromNBTTag(getEntity(), key, tag.getCompoundTag(key));
			if(buff.getType() == null)
				continue;
			activedBuff.put(key, buff);
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
	
	private short tick = 0;
	private LinkedList<Buff> removeList= new LinkedList<>();
	@Override
	public void tick() {
		tick++;
		for(Buff removedBuff : removeList){
			activedBuff.remove(removedBuff.getType().id);
		}
		
		for(Buff buff:activedBuff.values()){
			if(!buff.onUpdate(getEntity())){
				if(buff.onRemoveFromEntity(getEntity())){
					remove(buff);
				}
			}

			if(AcademyCraft.DEBUG_MODE){
				buff.getType().debug(buff);
			}
		}
		if(tick%10 == 0 && !isRemote()){
			tick = 0;
			sync();
		}
	}
	
	void add(Buff buff) {
		if(this.activedBuff.containsKey(buff.getType().id)){
			buff = this.activedBuff.get(buff.getType().id).combine(getEntity(), buff);
		}else{
			this.activedBuff.put(buff.getType().id, buff);
		}

		if(AcademyCraft.DEBUG_MODE){
			buff.getType().debug(buff);
		}
		
		if(!isRemote())
			sync();
	}

	void remove(Buff buff) {
		removeList.add(buff);
		
		if(AcademyCraft.DEBUG_MODE){
			buff.getType().debug(buff);
		}
	}
	
}
