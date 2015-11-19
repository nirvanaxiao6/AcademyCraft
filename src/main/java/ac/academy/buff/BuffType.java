package ac.academy.buff;

import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;

public abstract class BuffType {
    public final boolean isBadEffect;
    public final int intervalTick;
    public final String id;
    private static HashBiMap<String,BuffType> allBuffMap = HashBiMap.create();
    
    public BuffType(String id,int intervalTick,boolean isBadEffect){
    	this.id = id;
    	this.intervalTick=intervalTick;
    	this.isBadEffect = isBadEffect;
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
    
	public abstract void performEffect(EntityLivingBase entity,int level);
	
	/**
	 * Check every tick.
	 * Invoke {@link BuffType#performEffect(EntityLivingBase, int)} method when is true.
	 * Default: Invoke {@link BuffType#performEffect(EntityLivingBase, int)} method every {@link BuffType#intervalTick} ticks.
	 * @param durationTick The rest time of the buff
	 * @param level 
	 * @return The buff should perform the effect at this tick or not.
	 */
	public boolean isThisTickReady(int durationTick, int level) {
		if(durationTick%this.intervalTick==0){
			return true;
		}
		return false;
	}
}
