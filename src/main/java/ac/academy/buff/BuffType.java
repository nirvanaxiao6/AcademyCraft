package ac.academy.buff;

import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;

public abstract class BuffType {
    public final boolean isBadEffect;
    public final int intervalTick;

	private CombineType durationCombineType = CombineType.Max;
	private CombineType levelCombineType = CombineType.Max;
	
    public final String id;
    private static HashBiMap<String,BuffType> allBuffMap = HashBiMap.create();
    
    public BuffType(String id,int intervalTick,boolean isBadEffect){
    	this.id = id;
    	this.intervalTick=intervalTick;
    	this.isBadEffect = isBadEffect;
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
		return this.durationCombineType;
	}
    
    public static void registBuffType(BuffType type){
    	allBuffMap.put(type.id, type);
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
    
	public void performEffectOnTick(EntityLivingBase entity,int duration, int level){}
	
	public void performEffectOnCombine(EntityLivingBase entity,int duration, int level){}
}
