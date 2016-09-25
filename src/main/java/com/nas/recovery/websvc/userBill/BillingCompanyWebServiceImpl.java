package com.nas.recovery.websvc.userBill;
import javax.jws.WebService;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.oreon.kg.domain.userBill.BillingCompany;

@WebService(endpointInterface = "com.nas.recovery.websvc.userBill.BillingCompanyWebService", serviceName = "BillingCompanyWebService")
@Name("billingCompanyWebService")
public class BillingCompanyWebServiceImpl implements BillingCompanyWebService {

	@In(create = true)
	com.nas.recovery.web.action.userBill.BillingCompanyAction billingCompanyAction;

	public BillingCompany loadById(Long id) {
		return billingCompanyAction.loadFromId(id);
	}

	public List<BillingCompany> findByExample(
			BillingCompany exampleBillingCompany) {
		return billingCompanyAction.search(exampleBillingCompany);
	}

	public void save(BillingCompany billingCompany) {
		billingCompanyAction.persist(billingCompany);
	}

}
