package com.nas.recovery.websvc.userBill;

import javax.jws.WebService;
import com.oreon.kg.domain.userBill.BillingCompany;
import java.util.List;

@WebService
public interface BillingCompanyWebService {

	public BillingCompany loadById(Long id);

	public List<BillingCompany> findByExample(
			BillingCompany exampleBillingCompany);

	public void save(BillingCompany billingCompany);

}
