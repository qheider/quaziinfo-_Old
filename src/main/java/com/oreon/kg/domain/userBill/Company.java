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

@MappedSuperclass
public class Company extends BusinessEntity {
	private static final long serialVersionUID = -545657373L;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String brand;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String companyType;

	protected Double rate;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String primaryPhone;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String email;

	@IndexedEmbedded
	@AttributeOverrides({

			@AttributeOverride(name = "unitNumber", column = @Column(name = "address_unitNumber")),

			@AttributeOverride(name = "streetNumber", column = @Column(name = "address_streetNumber")),

			@AttributeOverride(name = "streetName", column = @Column(name = "address_streetName")),

			@AttributeOverride(name = "streetDirection", column = @Column(name = "address_streetDirection")),

			@AttributeOverride(name = "streetType", column = @Column(name = "address_streetType")),

			@AttributeOverride(name = "province", column = @Column(name = "address_province")),

			@AttributeOverride(name = "postalCode", column = @Column(name = "address_postalCode")),

			@AttributeOverride(name = "city", column = @Column(name = "address_city")),

			@AttributeOverride(name = "country", column = @Column(name = "address_country")),

			@AttributeOverride(name = "displayAdr", column = @Column(name = "address_displayAdr"))

	})
	protected Address address = new Address();

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String poBox;

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrand() {

		return brand;

	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyType() {

		return companyType;

	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getRate() {

		return rate;

	}

	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public String getPrimaryPhone() {

		return primaryPhone;

	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {

		return email;

	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {

		return address;

	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getPoBox() {

		return poBox;

	}

	@Transient
	public String getDisplayName() {
		try {
			return brand;
		} catch (Exception e) {
			return " Company ";
		}
	}

	/** This method is used by hibernate full text search - override to add additional fields
	 * @see org.witchcraft.model.support.BusinessEntity#retrieveSearchableFieldsArray()
	 */
	@Override
	public List<String> listSearchableFields() {
		List<String> listSearchableFields = new ArrayList<String>();
		listSearchableFields.addAll(super.listSearchableFields());

		listSearchableFields.add("brand");

		listSearchableFields.add("companyType");

		listSearchableFields.add("primaryPhone");

		listSearchableFields.add("email");

		listSearchableFields.add("poBox");

		listSearchableFields.add("address.streetNumber");

		listSearchableFields.add("address.streetName");

		listSearchableFields.add("address.streetDirection");

		listSearchableFields.add("address.streetType");

		listSearchableFields.add("address.province");

		listSearchableFields.add("address.postalCode");

		listSearchableFields.add("address.city");

		listSearchableFields.add("address.country");

		listSearchableFields.add("address.displayAdr");

		return listSearchableFields;
	}

	@Field(index = Index.TOKENIZED, name = "searchData")
	@Analyzer(definition = "entityAnalyzer")
	public String getSearchData() {
		StringBuilder builder = new StringBuilder();

		builder.append(getBrand() + " ");

		builder.append(getCompanyType() + " ");

		builder.append(getPrimaryPhone() + " ");

		builder.append(getEmail() + " ");

		builder.append(getPoBox() + " ");

		return builder.toString();
	}

}
