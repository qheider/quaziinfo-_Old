package com.nas.recovery.websvc.domain;
import javax.jws.WebService;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.oreon.kg.domain.WebLog;

@WebService(endpointInterface = "com.nas.recovery.websvc.domain.WebLogWebService", serviceName = "WebLogWebService")
@Name("webLogWebService")
public class WebLogWebServiceImpl implements WebLogWebService {

	@In(create = true)
	com.nas.recovery.web.action.domain.WebLogAction webLogAction;

	public WebLog loadById(Long id) {
		return webLogAction.loadFromId(id);
	}

	public List<WebLog> findByExample(WebLog exampleWebLog) {
		return webLogAction.search(exampleWebLog);
	}

	public void save(WebLog webLog) {
		webLogAction.persist(webLog);
	}

}
