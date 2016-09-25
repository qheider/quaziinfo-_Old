package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.WebLog;
import com.oreon.kg.domain.WebLogType;

import java.util.ArrayList;
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

@Name("webLogList")
@Scope(ScopeType.APPLICATION)
public class WebLogListQuery extends WebLogListQueryBase implements java.io.Serializable {

    @In(create = true)
    protected EntityManager entityManager;
    List<WebLog> allWeblogs;
//    String frontPage;
//    

	






	List<WebLog>frontPages=new ArrayList<WebLog>();

    //every time webLogList is referenced UnWrap method will be called and value of allwebLogs will be injected by @In annotation in the Calling component.
    @Unwrap
    public List<WebLog> getAllWebLogs() {
        return allWeblogs;
    }

    @Override
    public void validate() {
        fetchnewWeblogs();
        super.validate();
    }

//@Create
//public void onCreate(){
//fetchnewWeblogs();
//}
    @Observer(create = false, value = "webLogCreated")
    synchronized public void fetchnewWeblogs() {
        WebLogType webLogType = null;
        javax.persistence.Query q = entityManager.createQuery("select log from WebLog log where log.type =:logTag order by log.dateCreated desc");
        List<WebLog> result = q.setParameter("logTag", webLogType.Public).getResultList();
        allWeblogs = result;
    }

    		
    		
}
