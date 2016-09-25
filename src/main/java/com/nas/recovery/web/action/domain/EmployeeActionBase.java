package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.Employee;

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

import com.oreon.kg.domain.WebLog;
import com.oreon.kg.domain.PassProtect;
import com.oreon.kg.domain.userBill.UserBill;

public abstract class EmployeeActionBase
		extends
			com.nas.recovery.web.action.domain.PersonAction<Employee>
		implements
			java.io.Serializable {

	@In(create = true)
	@Out(required = false)
	@DataModelSelection("employeeRecordList")
	private Employee employee;

	@DataModel
	private List<Employee> employeeRecordList;

	public void setEmployeeId(Long id) {
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
	public void setEmployeeIdForModalDlg(Long id) {
		setId(id);
		clearLists();
		loadAssociations();
	}

	public Long getEmployeeId() {
		return (Long) getId();
	}

	public Employee getEntity() {
		return employee;
	}

	//@Override
	public void setEntity(Employee t) {
		this.employee = t;
		loadAssociations();
	}

	public Employee getEmployee() {
		return (Employee) getInstance();
	}

	@Override
	protected Employee createInstance() {
		return new Employee();
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

	public Employee getDefinedInstance() {
		return (Employee) (isIdDefined() ? getInstance() : null);
	}

	public void setEmployee(Employee t) {
		this.employee = t;
		loadAssociations();
	}

	@Override
	public Class<Employee> getEntityClass() {
		return Employee.class;
	}

	public String downloadEmpPicture(Long id) {
		if (id == null || id == 0)
			id = currentEntityId;
		setId(id);
		downloadAttachment(getInstance().getEmpPicture());
		return "success";
	}

	public void empPictureUploadListener(UploadEvent event) throws Exception {
		UploadItem uploadItem = event.getUploadItem();
		if (getInstance().getEmpPicture() == null)
			getInstance().setEmpPicture(new FileAttachment());
		getInstance().getEmpPicture().setName(uploadItem.getFileName());
		getInstance().getEmpPicture().setContentType(
				uploadItem.getContentType());
		getInstance().getEmpPicture().setData(
				FileUtils.readFileToByteArray(uploadItem.getFile()));
	}

	/** This function is responsible for loading associations for the given entity e.g. when viewing an order, we load the customer so
	 * that customer can be shown on the customer tab within viewOrder.xhtml
	 * @see org.witchcraft.seam.action.BaseAction#loadAssociations()
	 */
	public void loadAssociations() {

		initListWebLogs();

		initListPassProtects();

		initListUserBills();

	}

	public void updateAssociations() {

	}

	protected List<com.oreon.kg.domain.WebLog> listWebLogs = new ArrayList<com.oreon.kg.domain.WebLog>();

	void initListWebLogs() {

		if (listWebLogs.isEmpty())
			listWebLogs.addAll(getInstance().getWebLogs());

	}

	public List<com.oreon.kg.domain.WebLog> getListWebLogs() {

		prePopulateListWebLogs();
		return listWebLogs;
	}

	public void prePopulateListWebLogs() {
	}

	public void setListWebLogs(List<com.oreon.kg.domain.WebLog> listWebLogs) {
		this.listWebLogs = listWebLogs;
	}

	public void deleteWebLogs(int index) {
		listWebLogs.remove(index);
	}

	@Begin(join = true)
	public void addWebLogs() {
		initListWebLogs();
		WebLog webLogs = new WebLog();

		webLogs.setEmployee(getInstance());

		getListWebLogs().add(webLogs);
	}

	protected List<com.oreon.kg.domain.PassProtect> listPassProtects = new ArrayList<com.oreon.kg.domain.PassProtect>();

	void initListPassProtects() {

		if (listPassProtects.isEmpty())
			listPassProtects.addAll(getInstance().getPassProtects());

	}

	public List<com.oreon.kg.domain.PassProtect> getListPassProtects() {

		prePopulateListPassProtects();
		return listPassProtects;
	}

	public void prePopulateListPassProtects() {
	}

	public void setListPassProtects(
			List<com.oreon.kg.domain.PassProtect> listPassProtects) {
		this.listPassProtects = listPassProtects;
	}

	public void deletePassProtects(int index) {
		listPassProtects.remove(index);
	}

	@Begin(join = true)
	public void addPassProtects() {
		initListPassProtects();
		PassProtect passProtects = new PassProtect();

		passProtects.setEmployee(getInstance());

		getListPassProtects().add(passProtects);
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

		userBills.setEmployee(getInstance());

		getListUserBills().add(userBills);
	}

	public void updateComposedAssociations() {

		if (listWebLogs != null) {
			getInstance().getWebLogs().clear();
			getInstance().getWebLogs().addAll(listWebLogs);
		}

		if (listPassProtects != null) {
			getInstance().getPassProtects().clear();
			getInstance().getPassProtects().addAll(listPassProtects);
		}

		if (listUserBills != null) {
			getInstance().getUserBills().clear();
			getInstance().getUserBills().addAll(listUserBills);
		}
	}

	public void clearLists() {
		listWebLogs.clear();
		listPassProtects.clear();
		listUserBills.clear();

	}

	public String register() {

		return null;

	}

	public String login() {

		return null;

	}

	public com.oreon.kg.domain.Employee findByPhone(String phone) {

		return executeSingleResultNamedQuery("findByPhone", phone);

	}

	public String retrieveCredentials() {

		return null;

	}

	public Employee getCurrentLoggedInEmployee() {
		String query = "Select e from Employee e where e.user.userName = ?1";
		return (Employee) executeSingleResultQuery(query, Identity.instance()
				.getCredentials().getUsername());
	}

}
