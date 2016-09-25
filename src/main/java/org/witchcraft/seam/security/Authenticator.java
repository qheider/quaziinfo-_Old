package org.witchcraft.seam.security;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.permission.RuleBasedPermissionResolver;
import org.witchcraft.base.entity.UserUtilAction;
import com.oreon.kg.domain.users.Role;
import com.oreon.kg.domain.users.User;


@Name("authenticator")
public class Authenticator {
	
	@Logger Log log;

	@In
	EntityManager entityManager;

	@In
	Credentials credentials;
	
	@In(required=false)
	Actor actor;

	@In
	Identity identity;

	public boolean authenticate() {

		try {

			User user = (User) entityManager
					.createQuery(
					"from User where username = :username and password = :password")
					.setParameter("username", credentials.getUsername())
					.setParameter("password", credentials.getPassword())
					.getSingleResult();

			if (user.getRoles() != null) {
				Set<Role> roles = user.getRoles();
				for (Role role : roles) {
					identity.addRole(role.getName());
				}
			}else{
				log.warn("no role found for user " + user.getUserName());
			}
			updateActor(user);
			UserUtilAction userUtilAction = (UserUtilAction)Component.getInstance("userUtilAction");
			
			RuleBasedPermissionResolver resolver = RuleBasedPermissionResolver.instance();
			if(resolver != null) {
				resolver.getSecurityContext().insert(user);
			}

			userUtilAction.setCurrentUser(user);
			/*
			user.setLastLogin(new Date());
			UserAction userAction = (UserAction) Component.getInstance("userAction");
			userAction.setInstance(user);
			userAction.save();*/
			
			return true;
		}

		catch (NoResultException ex) {

			return false;

		}

	}
	
	private void updateActor(User user) {
		if(actor == null)
			return;
		actor.setId(user.getUserName());
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			actor.getGroupActorIds().add( role.getName() );
		}
	}

}