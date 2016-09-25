package com.nas.recovery.web.action.domain;

import com.nas.recovery.web.action.users.RoleAction;
import com.oreon.kg.domain.Employee;
import com.oreon.kg.domain.users.Role;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

@Scope(ScopeType.CONVERSATION)
@Name("employeeAction")
public class EmployeeAction extends EmployeeActionBase implements java.io.Serializable {
//    EmployeeAction employeeAction=(EmployeeAction) Component.getInstance("employeeAction");
@In
RoleAction roleAction=(RoleAction) Component.getInstance("roleAction");
    public void paint(OutputStream stream, Object object) throws IOException {
//        stream.write(employeeAction.getInstance().getEmpPicture().getData());
    }
    
    String realId;
    
  public String getRealId() {
		return realId;
	}



	public void setRealId(String realId) {

	
		this.realId = realId;
	}

public void doTest(){
	
}

@Override
    public String saveWithoutConversation() {
       Role newRole = null;
       newRole=roleAction.findByName("generalUser");
       if(newRole==null ){
    	   newRole=new Role();
    	   RoleAction roleAction=(RoleAction) Component.getInstance("roleAction");
    	   newRole.setName("generalUser");
    	  newRole= roleAction.persist(newRole);
       }
        this.getEmployee().getUser().getRoles().add(newRole);
        return super.saveWithoutConversation();
    }
  
  
  
	public void findEmployeeId(Long id) {
		
	
//		String realId=id.substring(10);
		Long currentId=1L;
		
		if (currentId == 0) {
			clearInstance();
			clearLists();
			loadAssociations();
			return;
		}
		this.setEmployeeId(id);
		if (!isPostBack())
			loadAssociations();
	}

}
