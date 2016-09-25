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
@Table(name = "billingcompany")
@Filter(name = "archiveFilterDef")
@Name("billingCompany")
@Indexed
@Cache(usage = CacheConcurrencyStrategy.NONE)
@Analyzer(definition = "entityAnalyzer")
public class BillingCompany extends com.oreon.kg.domain.userBill.Company
		implements
			java.io.Serializable {
	private static final long serialVersionUID = 2077528636L;

	@NotNull
	@Length(min = 2, max = 250)
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String name;

	@OneToMany(mappedBy = "billingCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//@JoinColumn(name = "billingCompany_ID", nullable = true)
	@OrderBy("dateCreated DESC")
	@IndexedEmbedded
	private Set<UserBill> userBills = new HashSet<UserBill>();

	public void addUserBills(UserBill userBills) {
		userBills.setBillingCompany(this);
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
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String companyInfo;
	
	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String webUrl;
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {

		return name;

	}

	public void setUserBills(Set<UserBill> userBills) {
		this.userBills = userBills;
	}

	public Set<UserBill> getUserBills() {
		return userBills;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getWebUrl() {

		return webUrl;

	}

	@Transient
	public String getDisplayName() {
		try {
			return name;
		} catch (Exception e) {
			return " BillingCompany ";
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

		listSearchableFields.add("name");

		listSearchableFields.add("webUrl");

		return listSearchableFields;
	}

	@Field(index = Index.TOKENIZED, name = "searchData")
	@Analyzer(definition = "entityAnalyzer")
	public String getSearchData() {
		StringBuilder builder = new StringBuilder();

		builder.append(getName() + " ");

		builder.append(getWebUrl() + " ");

		for (BusinessEntity e : userBills) {
			builder.append(e.getDisplayName() + " ");
		}

		return builder.toString();
	}

	/*PROTECTED REGION ID(ClASS_EDITABLE_RGN__BillingCompany) ENABLED START*/
	//this is protected region
	/*PROTECTED REGION END*/

}
