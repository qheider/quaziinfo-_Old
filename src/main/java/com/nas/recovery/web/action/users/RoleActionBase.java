package com.nas.recovery.web.action.users;

import com.oreon.kg.domain.users.Role;

import org.witchcraft.seam.action.BaseAction;

import java.util.ArrayList;
import java.util.List;

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

public abstract class RoleActionBase extends BaseAction<Role>
		implements
			java.io.Serializable {

	@In(create = true)
	@Out(required = false)
	@DataModelSelection("roleRecordList")
	private Role role;

	@DataModel
	private List<Role> roleRecordList;

	public void setRoleId(Long id) {
		if (id == 0) {
			clearInstance();
			clearLists();
			loadAssociations();
			return;
		}
		setId(id);
		if (!isPostBack())
			loadAssociations();
	}

	/** for modal dlg we need to load associaitons regardless of postback
	 * @param id
	 */
	public void setRoleIdForModalDlg(Long id) {
		setId(id);
		clearLists();
		loadAssociations();
	}

	public Long getRoleId() {
		return (Long) getId();
	}

	public Role getEntity() {
		return role;
	}

	//@Override
	public void setEntity(Role t) {
		this.role = t;
		loadAssociations();
	}

	public Role getRole() {
		return (Role) getInstance();
	}

	@Override
	protected Role createInstance() {
		return new Role();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();

	}

	public boolean isWired() {
		return true;
	}

	public Role getDefinedInstance() {
		return (Role) (isIdDefined() ? getInstance() : null);
	}

	public void setRole(Role t) {
		this.role = t;
		loadAssociations();
	}

	@Override
	public Class<Role> getEntityClass() {
		return Role.class;
	}

	public com.oreon.kg.domain.users.Role findByUnqName(String name) {
		return executeSingleResultNamedQuery("role.findByUnqName", name);
	}

	/** This function is responsible for loading associations for the given entity e.g. when viewing an order, we load the customer so
	 * that customer can be shown on the customer tab within viewOrder.xhtml
	 * @see org.witchcraft.seam.action.BaseAction#loadAssociations()
	 */
	public void loadAssociations() {

	}

	public void updateAssociations() {

	}

	public void updateComposedAssociations() {
	}

	public void clearLists() {

	}

	public com.oreon.kg.domain.users.Role findByName(String name) {

		return executeSingleResultNamedQuery("findByName", name);

	}

}
