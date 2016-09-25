package com.oreon.kg.domain.userBill;

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
@Table(name = "userbill")
@Filter(name = "archiveFilterDef")
@Name("userBill")
@Indexed
@Cache(usage = CacheConcurrencyStrategy.NONE)
@Analyzer(definition = "entityAnalyzer")
public class UserBill extends BusinessEntity implements java.io.Serializable {
	private static final long serialVersionUID = -1434786996L;

	protected Double minPayment;

	protected Date minPaymentDueDate;

	protected Double currentBalance;

	protected Double amountPaid;

	protected Double previousPayment;

	protected Date statementDate;

	protected Date previousStatementDate;
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String accountNumber;
	
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String billNumber;

	@Length(max = 5000)
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String comment;

	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "billingCompany_id", nullable = true, updatable = true)
	@ContainedIn
	protected BillingCompany billingCompany;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = false, updatable = true)
	@ContainedIn
	protected com.oreon.kg.domain.Employee employee;

	public void setMinPayment(Double minPayment) {
		this.minPayment = minPayment;
	}

	public Double getMinPayment() {

		return minPayment;

	}

	public void setMinPaymentDueDate(Date minPaymentDueDate) {
		this.minPaymentDueDate = minPaymentDueDate;
	}

	public Date getMinPaymentDueDate() {

		return minPaymentDueDate;

	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Double getCurrentBalance() {

		return currentBalance;

	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getAmountPaid() {

		return amountPaid;

	}

	public void setPreviousPayment(Double previousPayment) {
		this.previousPayment = previousPayment;
	}

	public Double getPreviousPayment() {

		return previousPayment;

	}

	public void setStatementDate(Date statementDate) {
		this.statementDate = statementDate;
	}

	public Date getStatementDate() {

		return statementDate;

	}

	public void setPreviousStatementDate(Date previousStatementDate) {
		this.previousStatementDate = previousStatementDate;
	}

	public Date getPreviousStatementDate() {

		return previousStatementDate;

	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {

		return comment;

	}

	public void setBillingCompany(BillingCompany billingCompany) {
		this.billingCompany = billingCompany;
	}

	public BillingCompany getBillingCompany() {

		return billingCompany;

	}

	public void setEmployee(com.oreon.kg.domain.Employee employee) {
		this.employee = employee;
	}

	public com.oreon.kg.domain.Employee getEmployee() {

		return employee;

	}

	@Transient
	public String getDisplayName() {
		try {
			return minPayment + "";
		} catch (Exception e) {
			return " UserBill ";
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

		listSearchableFields.add("comment");

		return listSearchableFields;
	}

	@Field(index = Index.TOKENIZED, name = "searchData")
	@Analyzer(definition = "entityAnalyzer")
	public String getSearchData() {
		StringBuilder builder = new StringBuilder();

		builder.append(getComment() + " ");

		if (getBillingCompany() != null)
			builder.append("billingCompany:"
					+ getBillingCompany().getDisplayName() + " ");

		if (getEmployee() != null)
			builder.append("employee:" + getEmployee().getDisplayName() + " ");

		return builder.toString();
	}

	/*PROTECTED REGION ID(ClASS_EDITABLE_RGN__UserBill) ENABLED START*/
	//this is protected region
	/*PROTECTED REGION END*/

}
