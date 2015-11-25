package cn.academy.buff;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.common.collect.HashBiMap;

import cn.lambdalib.annoreg.base.RegistrationClassSimple;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.core.RegistryTypeDecl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;

@Registrant
public abstract class BuffType {
    public final boolean isBadEffect;
    public final int defaultDuration;

	private CombineType durationCombineType = CombineType.Max;
	private CombineType levelCombineType = CombineType.Max;
	
	private RemoveType levelRemoveType = RemoveType.RemoveAll;
	
    public final String id;
    private static HashBiMap<String,BuffType> allBuffMap = HashBiMap.create();
    
    public BuffType(String id, int defaultDuration, boolean isBadEffect) {
    	this.id = id;
    	this.defaultDuration = defaultDuration;
    	this.isBadEffect = isBadEffect;
    }
    
    public BuffType(String id, boolean isBadEffect) {
    	this(id,0,isBadEffect);
    }

    public static enum CombineType{
		Max,Sum,PlusOne,NoChange;
	}
	
	public void setCombineType(CombineType durationCombineType, CombineType levelCombineType) {
		this.durationCombineType = durationCombineType;
		this.levelCombineType = levelCombineType;
	}
	
	public void setDrationCombineType(CombineType durationCombineType) {
		this.durationCombineType = durationCombineType;
	}
	
	public void setLevelCombineType(CombineType levelCombineType) {
		this.levelCombineType = levelCombineType;
	}
	
	public CombineType getDrationCombineType(){
		return this.durationCombineType;
	}
	
	public CombineType getLevelCombineType(){
		return this.levelCombineType;
	}
	
	
	public static enum RemoveType{
		RemoveOne, RemoveAll;
	}
    
	public void setLevelRemoveType(RemoveType levelRemoveType){
		this.levelRemoveType = levelRemoveType;
	}
	
	public RemoveType getLevelRemoveType(){
		return this.levelRemoveType;
	}
	
	
	public void registBuffType(){
    	allBuffMap.put(this.id, this);
    }
	
    public static void registBuffType(BuffType type){
    	type.registBuffType();
    }
    
    
    public static BuffType get(String id) {
		return allBuffMap.get(id);
    }
    
    public String getName(){
    	return StatCollector.translateToLocal(getKey(id));
    }
    
    public String getKey(String id) {
		return "ac.buff."+id;
	}
    
    @SideOnly(Side.CLIENT)
    public static String getDurationString(Buff buff)
    {
        if (buff.isForever())
        {
            return "**:**";
        }
        else
        {
            int i = buff.getDuration();
            return StringUtils.ticksToElapsedTime(i);
        }
    }
    
    public void debug(Buff buff){}
    
	public void performEffectOnTick(Buff buff, EntityLivingBase entity,int duration, int level){}
	
	public void performEffectOnCombine(Buff buff, EntityLivingBase entity, int level){}
	
	public void performEffectOnAdded(Buff buff, EntityLivingBase entity, int level){}
	/**
	 * Calls on the buff removed when its duration is 0.
	 * Mostly the buff remove 1 level and invoke this method each time on remove.
	 * @param buff
	 * @param entity
	 * @param level
	 */
	public void performEffectOnRemove(Buff buff, EntityLivingBase entity, int level){}
	/**
	 * Only calls when the buff cleared by other things.
	 * @param buff
	 * @param entity
	 * @param level
	 */
	public void performEffectOnClear(Buff buff, EntityLivingBase entity, int level){}
}
