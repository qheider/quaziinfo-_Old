package com.oreon.kg.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;

import javax.persistence.*;
import org.hibernate.validator.*;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Cascade;

import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.witchcraft.model.support.audit.*;
import org.hibernate.annotations.Filter;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import org.jboss.seam.annotations.Name;

import org.witchcraft.base.entity.BusinessEntity;
import org.witchcraft.model.support.audit.Auditable;
import org.witchcraft.base.entity.FileAttachment;

import org.witchcraft.utils.*;

@Entity
@Table(name = "employee")
@Filter(name = "archiveFilterDef")
@Name("employee")
@Indexed
@Cache(usage = CacheConcurrencyStrategy.NONE)
@Analyzer(definition = "entityAnalyzer")
public class Employee extends com.oreon.kg.domain.Person
		implements
			java.io.Serializable {
	private static final long serialVersionUID = 2046415010L;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String employeeNumber;

	protected EmployeeType employeeType;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JoinColumn(name = "employee_ID", nullable = true)
	@OrderBy("dateCreated DESC")
	@IndexedEmbedded
	private Set<WebLog> webLogs = new HashSet<WebLog>();

	public void addWebLogs(WebLog webLogs) {
		webLogs.setEmployee(this);
		this.webLogs.add(webLogs);
	}

	@Transient
	public List<com.oreon.kg.domain.WebLog> getListWebLogs() {
		return new ArrayList<com.oreon.kg.domain.WebLog>(webLogs);
	}

	//JSF Friendly function to get count of collections
	public int getWebLogsCount() {
		return webLogs.size();
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "empPicture_name")),
			@AttributeOverride(name = "contentType", column = @Column(name = "empPicture_contentType")),
			@AttributeOverride(name = "data", column = @Column(name = "empPicture_data", length = 4194304))})
	protected FileAttachment empPicture = new FileAttachment();

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JoinColumn(name = "employee_ID", nullable = true)
	@OrderBy("dateCreated DESC")
	@IndexedEmbedded
	private Set<PassProtect> passProtects = new HashSet<PassProtect>();

	public void addPassProtects(PassProtect passProtects) {
		passProtects.setEmployee(this);
		this.passProtects.add(passProtects);
	}

	@Transient
	public List<com.oreon.kg.domain.PassProtect> getListPassProtects() {
		return new ArrayList<com.oreon.kg.domain.PassProtect>(passProtects);
	}

	//JSF Friendly function to get count of collections
	public int getPassProtectsCount() {
		return passProtects.size();
	}

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JoinColumn(name = "employee_ID", nullable = true)
	@OrderBy("dateCreated DESC")
	@IndexedEmbedded
	private Set<com.oreon.kg.domain.userBill.UserBill> userBills = new HashSet<com.oreon.kg.domain.userBill.UserBill>();

	public void addUserBills(com.oreon.kg.domain.userBill.UserBill userBills) {
		userBills.setEmployee(this);
		this.userBills.add(userBills);
	}

	@Transient
	public List<com.oreon.kg.domain.userBill.UserBill> getListUserBills() {
		return new ArrayList<com.oreon.kg.domain.userBill.UserBill>(userBills);
	}

	//JSF Friendly function to get count of collections
	public int getUserBillsCount() {
		return userBills.size();
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmployeeNumber() {

		return employeeNumber;

	}

	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}

	public EmployeeType getEmployeeType() {

		return employeeType;

	}

	public void setWebLogs(Set<WebLog> webLogs) {
		this.webLogs = webLogs;
	}

	public Set<WebLog> getWebLogs() {
		return webLogs;
	}

	public void setEmpPicture(FileAttachment empPicture) {
		this.empPicture = empPicture;
	}

	public FileAttachment getEmpPicture() {

		return empPicture;

	}

	public void setPassProtects(Set<PassProtect> passProtects) {
		this.passProtects = passProtects;
	}

	public Set<PassProtect> getPassProtects() {
		return passProtects;
	}

	public void setUserBills(
			Set<com.oreon.kg.domain.userBill.UserBill> userBills) {
		this.userBills = userBills;
	}

	public Set<com.oreon.kg.domain.userBill.UserBill> getUserBills() {
		return userBills;
	}

	@Transient
	public String getDisplayName() {
		try {
			return super.getDisplayName();
		} catch (Exception e) {
			return " Employee ";
		}
	}

	//Empty setter , needed for richfaces autocomplete to work 
	public void setDisplayName(String name) {
	}

	/** This method is used by hibernate full text search - override to add additional fields
	 * @see org.witchcraft.model.support.BusinessEntity#retrieveSearchableFieldsArray()
	 */
	@Override
	public List<String> listSearchableFields() {
		List<String> listSearchableFields = new ArrayList<String>();
		listSearchableFields.addAll(super.listSearchableFields());

		listSearchableFields.add("employeeNumber");

		listSearchableFields.add("webLogs.heading");

		listSearchableFields.add("webLogs.headingDesc");

		listSearchableFields.add("webLogs.body");

		listSearchableFields.add("webLogs.tag");

		listSearchableFields.add("passProtects.companyName");

		listSearchableFields.add("passProtects.companyUserName");

		listSearchableFields.add("passProtects.companyPassword");

		listSearchableFields.add("passProtects.note");

		listSearchableFields.add("userBills.comment");

		return listSearchableFields;
	}

	@Field(index = Index.TOKENIZED, name = "searchData")
	@Analyzer(definition = "entityAnalyzer")
	public String getSearchData() {
		StringBuilder builder = new StringBuilder();

		builder.append(getEmployeeNumber() + " ");

		for (BusinessEntity e : webLogs) {
			builder.append(e.getDisplayName() + " ");
		}

		for (BusinessEntity e : passProtects) {
			builder.append(e.getDisplayName() + " ");
		}

		for (BusinessEntity e : userBills) {
			builder.append(e.getDisplayName() + " ");
		}

		return builder.toString();
	}

	/*PROTECTED REGION ID(ClASS_EDITABLE_RGN__Employee) ENABLED START*/
	//this is protected region
	/*PROTECTED REGION END*/

}
