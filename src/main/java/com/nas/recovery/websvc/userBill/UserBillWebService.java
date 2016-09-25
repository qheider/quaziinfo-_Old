package com.nas.recovery.websvc.userBill;

import javax.jws.WebService;
import com.oreon.kg.domain.userBill.UserBill;
import java.util.List;

@WebService
public interface UserBillWebService {

	public UserBill loadById(Long id);

	public List<UserBill> findByExample(UserBill exampleUserBill);

	public void save(UserBill userBill);

}
