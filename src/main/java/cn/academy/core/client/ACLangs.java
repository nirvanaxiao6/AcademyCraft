/**
 * 
 */
package cn.academy.core.client;

import net.minecraft.util.StatCollector;

/**
 * Generic language translations
 * @author WeathFolD
 */
public class ACLangs {

	public static String presetPrefix() {
		return local("ac.preset");
	}
	
	public static String notLearned() {
		return local("ac.notlearned");
	}
	
	public static String devSyncRate() {
		return local("ac.syncrate");
	}
	
	public static String learnAbility() {
		return local("ac.learnability");
	}
	
	public static String upgradeLevel() {
		return local("ac.upgradelv");
	}
	
	public static String machineStat() {
		return local("ac.machinestat");
	}
	
	public static String curEnergy() {
		return local("ac.curenergy");
	}
	
	public static String presetSettings() {
		return local("ac.presset");
	}
	
	public static String pgLearn() {
		return local("page.adlearning");
	}
	
	public static String pgSkills() {
		return local("page.adskills");
	}
	
	public static String actionConfirm() {
		return local("page.adconfirm");
	}
	
	public static String confirm() {
		return local("ac.btnconfirm");
	}
	
	public static String cancel() {
		return local("ac.btncancel");
	}
	
	public static String upgradeTo() {
		return local("ac.upgradeto");
	}
	
	public static String confirmHead() {
		return local("ac.confirm.head");
	}
	
	public static String confirmTail() {
		return local("ac.confirm.tail");
	}
	
	public static String learnSkill() {
		return local("ac.learnskill");
	}
	
	public static String upgradeSkill() {
		return local("ac.upgradeskill");
	}
	
	public static String stimProg() {
		return local("ac.stimprog");
	}
	
	public static String curAction() {
		return local("ac.curaction");
	}
	
	public static String attemptes() {
		return local("ac.attempts");
	}
	
	public static String fails() {
		return local("ac.fails");
	}
	
	public static String aborted() {
		return local("ac.aborted");
	}
	
	public static String successful() {
		return local("ac.successful");
	}
	
	public static String expConsumption() {
		return local("ac.expconsumption");
	}
	
	public static String fullyLearned() {
		return local("ac.fullylearned");
	}
	
	public static String holoView() {
		return local("adev.holoview");
	}
	
	public static String ad_UserInfo() {
		return local("adev.userinfo");
	}
	
	public static String chooseSkill() {
		return local("prs.chooseskl");
	}
	
	private static String local(String str) {
		return StatCollector.translateToLocal(str);
	}

}
