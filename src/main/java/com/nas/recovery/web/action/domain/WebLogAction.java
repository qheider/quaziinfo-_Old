package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.Employee;
import com.oreon.kg.domain.WebLog;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.jpa.FullTextEntityManager;

import org.apache.commons.lang.StringUtils;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Scope;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;

import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.Observer;

import org.witchcraft.base.entity.FileAttachment;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.annotations.RaiseEvent;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

@Scope(ScopeType.CONVERSATION)
@Name("webLogAction")  
public class WebLogAction extends WebLogActionBase implements java.io.Serializable {


    @In(create = true)protected List<WebLog> webLogList;
    @In(create = true)protected List<WebLog> personalWebLogList;
    @In(create = true)protected List<WebLog> nrsWebLogList;
    
    @DataModel(value = "webLogsByEmployee")
    List<WebLog> webLogsByEmployee;

    @Factory("webLogsByEmployee")
    public void initializeLegalProcess() {
   webLogsByEmployee = webLogList;
   }
    
    
    
    @DataModel(value = "personalwebLogsByEmployee")
    List<WebLog> personalwebLogsByEmployee;

    @Factory("personalwebLogsByEmployee")
    public void initializePersonalwebLogsByEmployee() {
    	personalwebLogsByEmployee = personalWebLogList;
   }
    
    @DataModel(value = "nrswebLogsByEmployee")
    List<WebLog> nrswebLogsByEmployee;

    @Factory("nrswebLogsByEmployee")
    public void initializeNrswebLogsByEmployee() {
    	nrswebLogsByEmployee = nrsWebLogList;
   }

    @Override
    @RaiseEvent("webLogCreated")
    public String save() {
    	String hd=this.getWebLog().getBody();
        updateComposedAssociations();

        Employee newEmp = new Employee();
        newEmp = employeeAction.getCurrentLoggedInEmployee();
        this.getWebLog().setEmployee(newEmp);
        if(hd.length()>230){
        	
        	hd=hd.substring(0, 230);
        }
        this.getInstance().setHeadingDesc(hd);
        return super.doSave();
    }



}
