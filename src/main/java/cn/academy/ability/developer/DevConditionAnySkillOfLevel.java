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
package cn.academy.ability.developer;

import cn.academy.ability.api.Skill;
import cn.academy.ability.api.data.AbilityData;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeAthFolD
 */
public class DevConditionAnySkillOfLevel implements IDevCondition {
	
	int level;

	public DevConditionAnySkillOfLevel(int _level) {
		level = _level;
	}
	
	@Override
	public boolean accepts(AbilityData data, Developer developer, Skill skill) {
		if(data.getCategory() == null)
			return false;
		for(Skill s : data.getCategory().getSkillsOfLevel(level)) {
			if(data.isSkillLearned(s))
				return true;
		}
		return false;
	}

	// TODO
	@Override
	public ResourceLocation getIcon() {
		return null;
	}

	@Override
	public String getHintText() {
		return null;
	}

}
