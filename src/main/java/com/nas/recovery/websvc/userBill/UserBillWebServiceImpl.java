package com.nas.recovery.websvc.userBill;
import javax.jws.WebService;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.oreon.kg.domain.userBill.UserBill;

@WebService(endpointInterface = "com.nas.recovery.websvc.userBill.UserBillWebService", serviceName = "UserBillWebService")
@Name("userBillWebService")
public class UserBillWebServiceImpl implements UserBillWebService {

	@In(create = true)
	com.nas.recovery.web.action.userBill.UserBillAction userBillAction;

	public UserBill loadById(Long id) {
		return userBillAction.loadFromId(id);
	}

	public List<UserBill> findByExample(UserBill exampleUserBill) {
		return userBillAction.search(exampleUserBill);
	}

	public void save(UserBill userBill) {
		userBillAction.persist(userBill);
	}

}
