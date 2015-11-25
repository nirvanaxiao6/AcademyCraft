package cn.academy.buff;

import cn.lambdalib.annoreg.base.RegistrationClassSimple;
import cn.lambdalib.annoreg.core.LoadStage;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.core.RegistryTypeDecl;

@RegistryTypeDecl
public class BuffTypeRegistration extends RegistrationClassSimple<RegBuffType, BuffType>{

	public BuffTypeRegistration() {
		super(RegBuffType.class, "BuffType");
		this.setLoadStage(LoadStage.INIT);
	}

	@Override
	protected void register(Class<? extends BuffType> theClass, RegBuffType anno) throws Exception {
		theClass.newInstance().registBuffType();
	}
	
}
