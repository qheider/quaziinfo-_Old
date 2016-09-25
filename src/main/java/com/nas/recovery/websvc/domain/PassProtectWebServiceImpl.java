package com.nas.recovery.websvc.domain;
import javax.jws.WebService;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.oreon.kg.domain.PassProtect;

@WebService(endpointInterface = "com.nas.recovery.websvc.domain.PassProtectWebService", serviceName = "PassProtectWebService")
@Name("passProtectWebService")
public class PassProtectWebServiceImpl implements PassProtectWebService {

	@In(create = true)
	com.nas.recovery.web.action.domain.PassProtectAction passProtectAction;

	public PassProtect loadById(Long id) {
		return passProtectAction.loadFromId(id);
	}

	public List<PassProtect> findByExample(PassProtect examplePassProtect) {
		return passProtectAction.search(examplePassProtect);
	}

	public void save(PassProtect passProtect) {
		passProtectAction.persist(passProtect);
	}

}
