package ac.academy.buff;

import cn.lambdalib.util.datapart.PlayerData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.Constants.NBT;

public class Buff {
	private final BuffType type;
	private EntityLivingBase entity;
	
	private int duration;
	private int level;
	private boolean isDurationForever;
	
	private Buff(BuffType type,int level,int durationTick,boolean isForever){
		this.type = type;
		this.level = level;
		this.duration = durationTick;
		this.isDurationForever = isForever;
	}
	
	public Buff(BuffType type,int level,int durationTick) {
		this(type,level,durationTick,false);
	}
	
	public Buff(BuffType type,int level) {
		this(type,level,0,true);
	}
	
	
	public BuffType getType(){
		return this.type;
	}
	
	public boolean isForever() {
		return this.isDurationForever;
	}

	public int getDuration() {
		return this.duration;
	}
	
	public void setDuration(int durationTick) {
		this.duration = durationTick;
		this.isDurationForever = false;
	}
	
	public void setDurationForever() {
		this.isDurationForever=true;
		this.duration=0;
	}
	
	public boolean onUpdate() {
        if (this.duration>0) {
            this.type.performEffectOnTick(entity, duration, level);
            this.duration--;
        }
        return this.duration>0;
    }
	
	private void combine(Buff buff){
		switch(this.type.getDrationCombineType()){
		case Max:
			if(this.isDurationForever||buff.isDurationForever){
				this.isDurationForever = true;
				break;
			}
			this.duration = Math.max(this.duration, buff.duration);
			break;
		case Sum:{
			if(this.isDurationForever||buff.isDurationForever){
				this.isDurationForever = true;
				break;
			}
			this.duration+=buff.duration;
			break;
		}
		case PlusOne:case NoChange:
			break;
		}
		
		switch(this.type.getLevelCombineType()){
		case Max:
			this.level = Math.max(this.level, buff.level);
			break;
		case NoChange:
			break;
		case PlusOne:
			this.level++;
			break;
		case Sum:
			this.level+=buff.level;
			break;
		}
		
		this.type.performEffectOnCombine(entity, duration, level);
	}
	/**
	 * unfinished
	 * @param entity
	 */
	public void addToEntity(EntityLivingBase entity){
		this.entity=entity;
		BuffDataPart data = null;//= PlayerData.get(null).getPart(BuffDataPart.class);
		if(data.activedBuff.containsKey(this.type.id)){
			data.activedBuff.get(type.id).combine(this);
		}else{
			data.activedBuff.put(type.id, this);
		}
	}
	
	public NBTTagCompound toNBTTag(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isForever", Boolean.valueOf(isDurationForever));
		nbt.setByte("level", Byte.valueOf((byte) level));
		nbt.setInteger("duration", Integer.valueOf(this.duration));
		return nbt;
	}
	
	public static Buff fromNBTTag(String tagName,NBTTagCompound nbt){
		return new Buff(BuffType.get(tagName), nbt.getByte("level"), nbt.getInteger("duration"), nbt.getBoolean("isForever"));
	}
}
