package com.nas.recovery.web.action.userBill;

import com.oreon.kg.domain.userBill.BillingCompany;

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

import com.oreon.kg.domain.userBill.UserBill;

public abstract class BillingCompanyActionBase
		extends
			com.nas.recovery.web.action.userBill.CompanyAction<BillingCompany>
		implements
			java.io.Serializable {

	@In(create = true)
	@Out(required = false)
	@DataModelSelection("billingCompanyRecordList")
	private BillingCompany billingCompany;

	@In(create = true, value = "userBillAction")
	com.nas.recovery.web.action.userBill.UserBillAction userBillsAction;

	@DataModel
	private List<BillingCompany> billingCompanyRecordList;

	public void setBillingCompanyId(Long id) {
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
	public void setBillingCompanyIdForModalDlg(Long id) {
		setId(id);
		clearLists();
		loadAssociations();
	}

	public Long getBillingCompanyId() {
		return (Long) getId();
	}

	public BillingCompany getEntity() {
		return billingCompany;
	}

	//@Override
	public void setEntity(BillingCompany t) {
		this.billingCompany = t;
		loadAssociations();
	}

	public BillingCompany getBillingCompany() {
		return (BillingCompany) getInstance();
	}

	@Override
	protected BillingCompany createInstance() {
		return new BillingCompany();
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

	public BillingCompany getDefinedInstance() {
		return (BillingCompany) (isIdDefined() ? getInstance() : null);
	}

	public void setBillingCompany(BillingCompany t) {
		this.billingCompany = t;
		loadAssociations();
	}

	@Override
	public Class<BillingCompany> getEntityClass() {
		return BillingCompany.class;
	}

	/** This function is responsible for loading associations for the given entity e.g. when viewing an order, we load the customer so
	 * that customer can be shown on the customer tab within viewOrder.xhtml
	 * @see org.witchcraft.seam.action.BaseAction#loadAssociations()
	 */
	public void loadAssociations() {

		initListUserBills();

	}

	public void updateAssociations() {

		com.oreon.kg.domain.userBill.UserBill userBills = (com.oreon.kg.domain.userBill.UserBill) org.jboss.seam.Component
				.getInstance("userBill");
		userBills.setBillingCompany(billingCompany);
		events.raiseTransactionSuccessEvent("archivedUserBill");

	}

	protected List<com.oreon.kg.domain.userBill.UserBill> listUserBills = new ArrayList<com.oreon.kg.domain.userBill.UserBill>();

	void initListUserBills() {

		if (listUserBills.isEmpty())
			listUserBills.addAll(getInstance().getUserBills());

	}

	public List<com.oreon.kg.domain.userBill.UserBill> getListUserBills() {

		prePopulateListUserBills();
		return listUserBills;
	}

	public void prePopulateListUserBills() {
	}

	public void setListUserBills(
			List<com.oreon.kg.domain.userBill.UserBill> listUserBills) {
		this.listUserBills = listUserBills;
	}

	public void deleteUserBills(int index) {
		listUserBills.remove(index);
	}

	@Begin(join = true)
	public void addUserBills() {
		initListUserBills();
		UserBill userBills = new UserBill();

		userBills.setBillingCompany(getInstance());

		getListUserBills().add(userBills);
	}

	public void updateComposedAssociations() {

		if (listUserBills != null) {
			getInstance().getUserBills().clear();
			getInstance().getUserBills().addAll(listUserBills);
		}
	}

	public void clearLists() {
		listUserBills.clear();

	}

}
