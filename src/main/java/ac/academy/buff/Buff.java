package ac.academy.buff;

import cn.lambdalib.util.datapart.PlayerData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.Constants.NBT;

public class Buff {
	private final BuffType type;
	private EntityLivingBase entity;
	private EntityPlayer origin;
	
	private int duration;
	private int level;
	private boolean isDurationForever;
	
	private Buff(BuffType type, int level, int durationTick, boolean useDefaultDuration, boolean isForever) {
		this.type = type;
		this.level = level;
		
		if(useDefaultDuration)
			this.duration = this.type.defaultDuration;
		else
			this.duration = durationTick;
		
		this.isDurationForever = isForever;
	}
	
	private Buff(BuffType type, int level, int durationTick, boolean isForever) {
		this(type,level,durationTick,false,isForever);
	}
	
	public Buff(BuffType type,int level,int durationTick) {
		this(type,level,durationTick,false,false);
	}
	
	public Buff(BuffType type,int durationTick) {
		this(type,1,durationTick,false,false);
	}
	/**
	 * 
	 * @param type
	 * @param level
	 * @param isForeverOrDefault True if the buff will last forever;
	 * False if the buff use the default duration defined in {@link BuffType#defaultDuration}
	 */
	public Buff(BuffType type, int level, boolean isForeverOrDefault){
		this.type = type;
		this.level = level;
		if(isForeverOrDefault){
			this.duration = 0;
			this.isDurationForever = true;
		}else{
			this.duration = this.type.defaultDuration;
			this.isDurationForever = false;
		}
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
            this.type.performEffectOnTick(this, entity, duration, level);
            this.duration--;
        }
        return this.duration>0;
    }
	
	public void combine(Buff buff){
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
		
		this.type.performEffectOnCombine(this, entity, level);
	}
	
	public EntityPlayer getOrigin(){
		return this.origin;
	}
	/**
	 * unfinished
	 * @param entity
	 */
	public void addToEntity(EntityPlayer origin, EntityLivingBase entity){
		this.entity=entity;
		BuffDataPart data = null;//= PlayerData.get(null).getPart(BuffDataPart.class);
		data.add(this);
		this.type.performEffectOnAdded(this, entity, level);
	}
	
	public boolean removeFromEntity(){
		switch(type.getLevelRemoveType()){
		case RemoveAll:
			this.level=0;
			break;
		case RemoveOne:
			this.level--;
			this.duration=this.type.defaultDuration;
			break;
		}
		this.type.performEffectOnRemove(this, entity, level);
		return this.level<=0;
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
