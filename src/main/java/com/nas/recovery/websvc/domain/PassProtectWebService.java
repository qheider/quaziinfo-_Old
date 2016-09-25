package com.nas.recovery.websvc.domain;

import javax.jws.WebService;
import com.oreon.kg.domain.PassProtect;
import java.util.List;

@WebService
public interface PassProtectWebService {

	public PassProtect loadById(Long id);

	public List<PassProtect> findByExample(PassProtect examplePassProtect);

	public void save(PassProtect passProtect);

}
