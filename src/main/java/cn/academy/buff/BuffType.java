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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;

@Registrant
public class BuffType {
    public final boolean isBadEffect;
    public final int defaultDuration;
    public boolean showInHUD = true;

	private int maxLevel = 64;

	private ICombiner combiner = new ICombiner() {
		
		@Override
		public void combine(Buff baseBuff, Buff combinedBuff) {
			baseBuff.level = Math.min(maxLevel, baseBuff.level+combinedBuff.level);
			baseBuff.duration += combinedBuff.duration;
			baseBuff.isDurationForever |= combinedBuff.isDurationForever;
		}
	};
	
	private IRemover remover= new IRemover() {
		
		@Override
		public void remove(Buff buff) {
			buff.level = 0;
		}
	};
	
    public final String id;
    private static HashBiMap<String,BuffType> allBuffMap = HashBiMap.create();
    
    public BuffType(String id, int defaultDuration, boolean isBadEffect) {
    	this.id = id;
    	this.defaultDuration = defaultDuration;
    	this.isBadEffect = isBadEffect;
    }
    
    public BuffType(String id, boolean isBadEffect) {
    	this(id,100,isBadEffect);
    }
    
    public interface ICombiner {
    	public void combine(Buff baseBuff,Buff combinedBuff);
    }
	
    public ICombiner getCombiner() {
    	return this.combiner;
    }

	void setCombiner(ICombiner combiner){
		this.combiner = combiner;
	}
	
	public interface IRemover {
		void remove(Buff buff);
	}
	
	public IRemover getRemover() {
		return this.remover;
	}
	
	void setRemover(IRemover remover){
		this.remover = remover;
	}
	
	public void registBuffType() {
    	allBuffMap.put(this.id, this);
    }
	
    public static void registBuffType(BuffType type) {
    	type.registBuffType();
    }
    
    
    public static BuffType get(String id) {
		return allBuffMap.get(id);
    }
    
    public String getName() {
    	return StatCollector.translateToLocal(getKey(id));
    }
    
    public String getKey(String id) {
		return "ac.buff."+id;
	}
	
	public void setMaxLevel(int max){
		this.maxLevel = max;
	}
	
	public int getMaxLevel(){
		return this.maxLevel;
	}
    
    @SideOnly(Side.CLIENT)
    public ResourceLocation getIcon(Buff buff) {
    	return null;
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
