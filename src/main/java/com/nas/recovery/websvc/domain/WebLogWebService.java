package com.nas.recovery.websvc.domain;

import javax.jws.WebService;
import com.oreon.kg.domain.WebLog;
import java.util.List;

@WebService
public interface WebLogWebService {

	public WebLog loadById(Long id);

	public List<WebLog> findByExample(WebLog exampleWebLog);

	public void save(WebLog webLog);

}
