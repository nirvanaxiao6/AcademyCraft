package cn.academy.buff;

import java.util.UUID;

import cn.lambdalib.util.datapart.EntityData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class Buff {
	private EntityLivingBase thisEntity;
	private final BuffType type;
	
	private UUID originUUID;
	
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
		this.level = Math.min(level, type.getMaxLevel());
		if(isForeverOrDefault){
			this.duration = 0;
			this.isDurationForever = true;
		}else{
			this.duration = this.type.defaultDuration;
			this.isDurationForever = false;
		}
	}
	
	public World getWorld(){
		return thisEntity.worldObj;
	}
	
	public Entity getOrigin(){
		if(originUUID != null){
			for(Object o : getWorld().getLoadedEntityList()){
				Entity e = (Entity) o;
				if(e.getUniqueID().equals(originUUID)) return e;
			}
		}
		return null;
	}
	
	public BuffType getType(){
		return this.type;
	}
	
	public int getLevel(){
		return this.level;
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
	
	@SideOnly(Side.CLIENT)
	public  ResourceLocation getIcon(){
		return this.type.getIcon(this);
	}

    
    @SideOnly(Side.CLIENT)
    public String getDurationString()
    {
        if (this.isForever())
        {
            return "**:**";
        }
        else
        {
            int i = this.getDuration();
            return StringUtils.ticksToElapsedTime(i);
        }
    }
	
	public boolean onUpdate(EntityLivingBase entity) {
        if (this.duration>0) {
            this.type.performEffectOnTick(this, entity, duration, level);
            this.duration--;
        }
        return this.duration>0;
    }
	
	public Buff combine(EntityLivingBase entity, Buff buff){
		this.originUUID = buff.originUUID;
		switch(this.type.getDrationCombineType()){
		case Max:
			if(this.isDurationForever||buff.isDurationForever){
				this.isDurationForever = true;
				break;
			}
			this.duration = Math.max(this.duration, buff.duration);
			break;
		case Sum:
			if(this.isDurationForever||buff.isDurationForever){
				this.isDurationForever = true;
				break;
			}
			this.duration+=buff.duration;
			break;
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
		if(this.level > this.type.getMaxLevel())
			this.level = this.type.getMaxLevel();
		this.type.performEffectOnCombine(this, entity, level);
		return this;
	}
	
	/**
	 * Add this buff to a Entity.
	 * @param origin
	 * @param entity
	 */
	public void addToEntity(Entity origin, EntityLivingBase entity) {
		if(origin == null)
			this.originUUID = null;
		else
			this.originUUID = origin.getUniqueID();
		thisEntity = entity;
		BuffDataPart data = EntityData.get(entity).getPart(BuffDataPart.class);
		data.add(this);
		this.type.performEffectOnAdded(this, entity, level);
	}
	
	void removeFromEntity(EntityLivingBase entity, BuffType type) {
		BuffDataPart data = EntityData.get(entity).getPart(BuffDataPart.class);
		Buff buff = data.activedBuff.get(type.id);
		if(buff==null)
			return;
		
		type.performEffectOnRemove(buff, entity, buff.level);
		
		switch(type.getLevelRemoveType()){
		case RemoveAll:
			buff.level=0;
			break;
		case RemoveOne:
			buff.level--;
			buff.duration=buff.type.defaultDuration;
			break;
		}
		if(buff.level<=0){
			data.remove(buff);
		}
	}
	
	public static void clearFromEntity(EntityLivingBase entity, BuffType type,int level) {
		BuffDataPart data = EntityData.get(entity).getPart(BuffDataPart.class);
		Buff buff = data.activedBuff.get(type.id);
		if(buff==null)
			return;
		
		type.performEffectOnClear(buff, entity, buff.level);
		buff.level -= level;
		if(buff.level<=0){
			data.remove(buff);
		}
	}
	
	public NBTTagCompound toNBTTag(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isForever", Boolean.valueOf(isDurationForever));
		nbt.setByte("level", Byte.valueOf((byte) level));
		nbt.setInteger("duration", Integer.valueOf(this.duration));
		nbt.setString("originUUID", this.originUUID == null ? "null" : this.originUUID.toString());
		return nbt;
	}
	
	public static Buff fromNBTTag(EntityLivingBase entity, String tagName,NBTTagCompound nbt){
		Buff buff = new Buff(BuffType.get(tagName), nbt.getByte("level"), nbt.getInteger("duration"), nbt.getBoolean("isForever"));
		buff.thisEntity = entity;
		String s = nbt.getString("originUUID");
		buff.originUUID = s.equals("null") ? null : UUID.fromString(s);
		return buff;
	}
}