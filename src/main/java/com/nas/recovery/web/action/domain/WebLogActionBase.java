package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.WebLog;

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

public abstract class WebLogActionBase extends BaseAction<WebLog>
		implements
			java.io.Serializable {

	@In(create = true)
	@Out(required = false)
	@DataModelSelection("webLogRecordList")
	private WebLog webLog;

	@In(create = true, value = "employeeAction")
	com.nas.recovery.web.action.domain.EmployeeAction employeeAction;

	@DataModel
	private List<WebLog> webLogRecordList;

	public void setWebLogId(Long id) {
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
	public void setWebLogIdForModalDlg(Long id) {
		setId(id);
		clearLists();
		loadAssociations();
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

	public Long getWebLogId() {
		return (Long) getId();
	}

	public WebLog getEntity() {
		return webLog;
	}

	//@Override
	public void setEntity(WebLog t) {
		this.webLog = t;
		loadAssociations();
	}

	public WebLog getWebLog() {
		return (WebLog) getInstance();
	}

	@Override
	protected WebLog createInstance() {
		return new WebLog();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();

		com.oreon.kg.domain.Employee employee = employeeAction
				.getDefinedInstance();
		if (employee != null && isNew()) {
			getInstance().setEmployee(employee);
		}

	}

	public boolean isWired() {
		return true;
	}

	public WebLog getDefinedInstance() {
		return (WebLog) (isIdDefined() ? getInstance() : null);
	}

	public void setWebLog(WebLog t) {
		this.webLog = t;
		loadAssociations();
	}

	@Override
	public Class<WebLog> getEntityClass() {
		return WebLog.class;
	}

	public String downloadTagFile(Long id) {
		if (id == null || id == 0)
			id = currentEntityId;
		setId(id);
		downloadAttachment(getInstance().getTagFile());
		return "success";
	}

	public void tagFileUploadListener(UploadEvent event) throws Exception {
		UploadItem uploadItem = event.getUploadItem();
		if (getInstance().getTagFile() == null)
			getInstance().setTagFile(new FileAttachment());
		getInstance().getTagFile().setName(uploadItem.getFileName());
		getInstance().getTagFile().setContentType(uploadItem.getContentType());
		getInstance().getTagFile().setData(
				FileUtils.readFileToByteArray(uploadItem.getFile()));
	}

	/** This function adds associated entities to an example criterion
	 * @see org.witchcraft.model.support.dao.BaseAction#createExampleCriteria(java.lang.Object)
	 */
	@Override
	public void addAssociations(Criteria criteria) {

		if (webLog.getEmployee() != null) {
			criteria = criteria.add(Restrictions.eq("employee.id", webLog
					.getEmployee().getId()));
		}

	}

	/** This function is responsible for loading associations for the given entity e.g. when viewing an order, we load the customer so
	 * that customer can be shown on the customer tab within viewOrder.xhtml
	 * @see org.witchcraft.seam.action.BaseAction#loadAssociations()
	 */
	public void loadAssociations() {

		if (webLog.getEmployee() != null) {
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
