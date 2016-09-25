package org.witchcraft.base.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.Component;
import com.oreon.kg.domain.users.User;


public class EntityListener {
	
	Log log = LogFactory.getLog(EntityListener.class);
	
	@PrePersist
	public void setDatesAndUser(BusinessEntity modelBase) {
		Date now = new Date();
		if (modelBase.getDateCreated() == null) {
			modelBase.setDateCreated(now);
		}

		modelBase.setDateUpdated(now);

		try {

			UserUtilAction userUtilAction = (UserUtilAction)Component.getInstance("userUtilAction");
			User currentUser = userUtilAction.getCurrentUser();

			if (currentUser != null) {
				if (modelBase.getCreatedByUser() == null) {
					modelBase.setCreatedByUser(currentUser);
				}
			}else{
				log.warn("No creator for " + modelBase.getClass().getSimpleName() + "-> " + modelBase.getDisplayName());
			}

		} catch (IllegalStateException e) {
			if (log.isInfoEnabled())
				log.info("couldn't get component instance");
		}

	}
	
	@PreUpdate
	public void setUpdateDateAndUser(BusinessEntity modelBase){
		Date now = new Date();
		modelBase.setDateUpdated(now);
	}

}
