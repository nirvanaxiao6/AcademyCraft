/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under  
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.academy.ability.api.ctrl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.academy.ability.api.Controllable;
import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.liutils.util.client.ClientUtils;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * Class that handles cooldown. Currently, the cooldown in SERVER will be completely ignored, counting only
 * 	client cooldown on SkillInstance startup. But you can still visit the method to avoid side dependency.
 * @author WeAthFolD
 */
@Registrant
@RegEventHandler(Bus.FML)
public class Cooldown {
	
	/**
	 * The current cooldown data map. Direct manipulation should be avoided, this
	 *  is opened just for visit of reading (like UI drawings)
	 */
	public static final Map<Controllable, CooldownData> cooldown = new HashMap();
	
    public static void setCooldown(Controllable c, int cd) {
    	if(isInCooldown(c)) {
    		CooldownData data = cooldown.get(c);
    		if(data.max < cd) data.max = cd;
    		data.current = Math.max(cd, data.current);
    	} else {
    		cooldown.put(c, new CooldownData(cd));
    	}
    }
    
    public static boolean isInCooldown(Controllable c) {
    	return cooldown.containsKey(c);
    }
    
    public static CooldownData getCooldownData(Controllable c) {
    	return cooldown.get(c);
    }
    
    private static void updateCooldown() {
    	Iterator< Entry<Controllable, CooldownData> > iter = cooldown.entrySet().iterator();
    	
    	while(iter.hasNext()) {
    		Entry< Controllable, CooldownData > entry = iter.next();
    		CooldownData data = entry.getValue();
    		if(data.current <= 0) {
    			iter.remove();
    		} else {
    			data.current -= 1;
    		}
    	}
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(ClientTickEvent event) {
    	if(event.phase == Phase.END && ClientUtils.isPlayerPlaying()) {
			updateCooldown();
		}
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
	public void playerDeath(LivingDeathEvent event) {
    	if(event.entityLiving.equals(Minecraft.getMinecraft().thePlayer))
    		cooldown.clear();
    }
    
    @SubscribeEvent
    public void onDisconnect(ClientDisconnectionFromServerEvent event) {
    	cooldown.clear();
    }
    
    public static class CooldownData {
    	private int current;
    	private int max;
    	
    	public CooldownData(int time) {
    		current = max = time;
    	}
    	
    	/**
    	 * @return How many ticks until the cooldown is end
    	 */
    	public int getTickLeft() {
    		return current;
    	}
    	
    	/**
    	 * @return the cooldown time specified at the start of the cooldown progress.
    	 */
    	public int getMaxTick() {
    		return max;
    	}
    }

}
