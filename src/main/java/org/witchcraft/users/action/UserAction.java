package org.witchcraft.users.action;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.witchcraft.seam.action.BaseAction;
import com.oreon.kg.domain.users.User;

@Scope(ScopeType.CONVERSATION)
@Name("witchcraftUserAction")
public class UserAction extends BaseAction<User>
		implements
			java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2104139566875503593L;

	@In(create = true)
	@Out(required = false)
	@DataModelSelection
	private User user;

	@DataModel
	private List<User> userList;

	@Factory("userList")
	@Observer("archivedUser")
	public void findRecords() {
		
	}

	public User getEntity() {
		return user;
	}

	

	public void updateAssociations() {

	}

	public List<User> getEntityList() {
		if (userList == null) {
			findRecords();
		}
		return userList;
	}

	public User findByUnqUserName(String actorId) {
		// TODO Auto-generated method stub
		return null;
	}

}
