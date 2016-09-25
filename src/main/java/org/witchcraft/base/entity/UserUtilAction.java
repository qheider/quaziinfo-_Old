package org.witchcraft.base.entity;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import com.oreon.kg.domain.users.User;



@Name("userUtilAction")
@Scope(ScopeType.SESSION)
public class UserUtilAction implements Serializable{
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3320546173691963806L;

	private User currentUser;
	
	@In
	EntityManager entityManager;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = entityManager.merge(currentUser);	
	}
}
