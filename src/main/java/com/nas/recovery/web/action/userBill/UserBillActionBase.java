package com.nas.recovery.web.action.userBill;

import com.oreon.kg.domain.userBill.UserBill;

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

public abstract class UserBillActionBase extends BaseAction<UserBill>
		implements
			java.io.Serializable {

	@In(create = true)
	@Out(required = false)
	@DataModelSelection("userBillRecordList")
	private UserBill userBill;

	@In(create = true, value = "billingCompanyAction")
	com.nas.recovery.web.action.userBill.BillingCompanyAction billingCompanyAction;

	@In(create = true, value = "employeeAction")
	com.nas.recovery.web.action.domain.EmployeeAction employeeAction;

	@DataModel
	private List<UserBill> userBillRecordList;

	public void setUserBillId(Long id) {
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
	public void setUserBillIdForModalDlg(Long id) {
		setId(id);
		clearLists();
		loadAssociations();
	}

	public void setBillingCompanyId(Long id) {

		if (id != null && id > 0)
			getInstance()
					.setBillingCompany(billingCompanyAction.loadFromId(id));

	}

	public Long getBillingCompanyId() {
		if (getInstance().getBillingCompany() != null)
			return getInstance().getBillingCompany().getId();
		return 0L;
	}

	public void setEmployeeId(Long id) {

		if (id != null && id > 0)
			getInstance().setEmployee(employeeAction.loadFromId(id));

	}

	public Long getEmployeeId() {
		if (getInstance().getEmployee() != null)
			return getInstance().getEmployee().getId();
		return 0L;
	}

	public Long getUserBillId() {
		return (Long) getId();
	}

	public UserBill getEntity() {
		return userBill;
	}

	//@Override
	public void setEntity(UserBill t) {
		this.userBill = t;
		loadAssociations();
	}

	public UserBill getUserBill() {
		return (UserBill) getInstance();
	}

	@Override
	protected UserBill createInstance() {
		return new UserBill();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();

		com.oreon.kg.domain.userBill.BillingCompany billingCompany = billingCompanyAction
				.getDefinedInstance();
		if (billingCompany != null && isNew()) {
			getInstance().setBillingCompany(billingCompany);
		}

		com.oreon.kg.domain.Employee employee = employeeAction
				.getDefinedInstance();
		if (employee != null && isNew()) {
			getInstance().setEmployee(employee);
		}

	}

	public boolean isWired() {
		return true;
	}

	public UserBill getDefinedInstance() {
		return (UserBill) (isIdDefined() ? getInstance() : null);
	}

	public void setUserBill(UserBill t) {
		this.userBill = t;
		loadAssociations();
	}

	@Override
	public Class<UserBill> getEntityClass() {
		return UserBill.class;
	}

	/** This function adds associated entities to an example criterion
	 * @see org.witchcraft.model.support.dao.BaseAction#createExampleCriteria(java.lang.Object)
	 */
	@Override
	public void addAssociations(Criteria criteria) {

		if (userBill.getBillingCompany() != null) {
			criteria = criteria.add(Restrictions.eq("billingCompany.id",
					userBill.getBillingCompany().getId()));
		}

		if (userBill.getEmployee() != null) {
			criteria = criteria.add(Restrictions.eq("employee.id", userBill
					.getEmployee().getId()));
		}

	}

	/** This function is responsible for loading associations for the given entity e.g. when viewing an order, we load the customer so
	 * that customer can be shown on the customer tab within viewOrder.xhtml
	 * @see org.witchcraft.seam.action.BaseAction#loadAssociations()
	 */
	public void loadAssociations() {

		if (userBill.getBillingCompany() != null) {
			billingCompanyAction.setInstance(getInstance().getBillingCompany());
		}

		if (userBill.getEmployee() != null) {
			employeeAction.setInstance(getInstance().getEmployee());
		}

	}

	public void updateAssociations() {

	}

	public void updateComposedAssociations() {
	}

	public void clearLists() {

	}

}
