package com.nas.recovery.websvc.users;
import javax.jws.WebService;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.oreon.kg.domain.users.User;

@WebService(endpointInterface = "com.nas.recovery.websvc.users.UserWebService", serviceName = "UserWebService")
@Name("userWebService")
public class UserWebServiceImpl implements UserWebService {

	@In(create = true)
	com.nas.recovery.web.action.users.UserAction userAction;

	public User loadById(Long id) {
		return userAction.loadFromId(id);
	}

	public List<User> findByExample(User exampleUser) {
		return userAction.search(exampleUser);
	}

	public void save(User user) {
		userAction.persist(user);
	}

}
