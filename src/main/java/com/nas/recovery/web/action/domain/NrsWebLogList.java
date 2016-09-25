package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.WebLog;
import com.oreon.kg.domain.WebLogType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.jboss.seam.Component;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import org.witchcraft.base.entity.BaseQuery;
import org.witchcraft.base.entity.Range;

import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Unwrap;

@Name("nrsWebLogList")
@Scope(ScopeType.CONVERSATION)
public class NrsWebLogList extends WebLogListQueryBase implements java.io.Serializable {

    @In(create = true)
    protected EntityManager entityManager;
    EmployeeAction employeeAction=(EmployeeAction) Component.getInstance("employeeAction");
    
    List<WebLog> nrsWeblogs;

    //every time webLogList is referenced UnWrap method will be called and value of allwebLogs will be injected by @In annotation in the Calling component.
    @Unwrap
    public List<WebLog> getAllWebLogs() {
        return nrsWeblogs;
    }

    @Override
    public void validate() {
        fetchnewPersonalWeblogs();
        super.validate();
    }


    @Observer(create = false, value = "webLogCreated")
    synchronized public void fetchnewPersonalWeblogs() {
        WebLogType webLogType = null;
        javax.persistence.Query q = entityManager.createQuery("select log from WebLog log where log.type =:logTag and log.employee =:logEmp and log.tag=:tag order by log.dateCreated desc");
        List<WebLog> result = q.setParameter("logTag", webLogType.Private).setParameter("logEmp", employeeAction.getCurrentLoggedInEmployee()).setParameter("tag", "nrs").getResultList();
        nrsWeblogs = result;
      
    }
}
