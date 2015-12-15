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
	
	protected int duration;
	protected final int lastDuration;
	protected int level;
	protected boolean isDurationForever;
	
	private Buff(BuffType type, int level, int durationTick, int lastDuration, boolean isForever) {
		this.type = type;
		this.level = level;
		
		this.duration = durationTick;
		this.lastDuration = lastDuration;
		
		this.isDurationForever = isForever;
	}
	
	public Buff(BuffType type,int level,int durationTick) {
		this(type,level,durationTick,durationTick,false);
	}
	
	public Buff(BuffType type,int durationTick) {
		this(type,1,durationTick);
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
	
	public EntityLivingBase getThisEntity(){
		return thisEntity;
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
		this.type.getCombiner().combine(this, buff);
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
		BuffDataPart data = BuffDataPart.get(thisEntity);
		data.add(this);
		this.type.performEffectOnAdded(this, thisEntity, level);
	}
	
	boolean onRemoveFromEntity(EntityLivingBase entity) {
		type.performEffectOnRemove(this, entity, this.level);
		
		return type.getRemover().remove(this);
	}
	
	public NBTTagCompound toNBTTag(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isForever", Boolean.valueOf(isDurationForever));
		nbt.setByte("level", Byte.valueOf((byte) level));
		nbt.setInteger("duration", Integer.valueOf(this.duration));
		nbt.setInteger("lastDuration", Integer.valueOf(this.lastDuration));
		nbt.setString("originUUID", this.originUUID == null ? "null" : this.originUUID.toString());
		return nbt;
	}
	
	public static Buff fromNBTTag(EntityLivingBase entity, String tagName,NBTTagCompound nbt){
		Buff buff = new Buff(
				BuffType.get(tagName), 
				nbt.getByte("level"), 
				nbt.getInteger("duration"), 
				nbt.getInteger("lastDuration"), 
				nbt.getBoolean("isForever")
				);
		buff.thisEntity = entity;
		String s = nbt.getString("originUUID");
		buff.originUUID = s.equals("null") ? null : UUID.fromString(s);
		return buff;
	}
}
