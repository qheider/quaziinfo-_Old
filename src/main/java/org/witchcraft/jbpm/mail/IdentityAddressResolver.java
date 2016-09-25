package org.witchcraft.jbpm.mail;

import org.jboss.seam.Component;
import com.oreon.kg.domain.users.User;

import org.witchcraft.users.action.UserAction;




public class IdentityAddressResolver extends org.jbpm.identity.mail.IdentityAddressResolver{

	/**
	 * 
	 */
	private static final long serialVersionUID = -947350246139489225L;
	
	

	@Override
	public Object resolveAddress(String actorId) {
		UserAction userAction = (UserAction) Component.getInstance("userAction");
		User user = userAction.findByUnqUserName(actorId);
		return user.getEmail();
	}

}
