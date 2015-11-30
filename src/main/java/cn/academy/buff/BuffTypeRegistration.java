package cn.academy.buff;

import cn.lambdalib.annoreg.base.RegistrationFieldSimple;
import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.RegistryTypeDecl;
@RegistryTypeDecl
public class BuffTypeRegistration extends RegistrationFieldSimple<RegBuffType,BuffType> {

	public BuffTypeRegistration() {
		super(RegBuffType.class, "BuffType");
		this.setLoadStage(LoadStage.INIT);
	}

	@Override
	protected void register(BuffType value, RegBuffType anno, String field) throws Exception {
		if(value.id == null)
			return;
		value.registBuffType();
	}

}
