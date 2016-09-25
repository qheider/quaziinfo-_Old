package com.nas.recovery.websvc.users;
import javax.jws.WebService;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.oreon.kg.domain.users.Role;

@WebService(endpointInterface = "com.nas.recovery.websvc.users.RoleWebService", serviceName = "RoleWebService")
@Name("roleWebService")
public class RoleWebServiceImpl implements RoleWebService {

	@In(create = true)
	com.nas.recovery.web.action.users.RoleAction roleAction;

	public Role loadById(Long id) {
		return roleAction.loadFromId(id);
	}

	public List<Role> findByExample(Role exampleRole) {
		return roleAction.search(exampleRole);
	}

	public void save(Role role) {
		roleAction.persist(role);
	}

	public com.oreon.kg.domain.users.Role findByName(String name) {
		return roleAction.findByName(name);
	}

}
