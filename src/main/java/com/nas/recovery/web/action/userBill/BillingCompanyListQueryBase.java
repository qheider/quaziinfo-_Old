package com.nas.recovery.web.action.userBill;

import com.oreon.kg.domain.userBill.BillingCompany;

import org.witchcraft.seam.action.BaseAction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import org.witchcraft.base.entity.BaseQuery;
import org.witchcraft.base.entity.Range;

import org.jboss.seam.annotations.Observer;

import com.oreon.kg.domain.userBill.BillingCompany;

/**
 * 
 * @author WitchcraftMDA Seam Cartridge - 
 *
 */
public abstract class BillingCompanyListQueryBase
		extends
			BaseQuery<BillingCompany, Long> {

	private static final String EJBQL = "select billingCompany from BillingCompany billingCompany";

	protected BillingCompany billingCompany = new BillingCompany();

	public BillingCompany getBillingCompany() {
		return billingCompany;
	}

	@Override
	protected String getql() {
		return EJBQL;
	}

	@Override
	public Class<BillingCompany> getEntityClass() {
		return BillingCompany.class;
	}

	@Override
	public String[] getEntityRestrictions() {
		return RESTRICTIONS;
	}

	private Range<Double> rateRange = new Range<Double>();
	public Range<Double> getRateRange() {
		return rateRange;
	}
	public void setRate(Range<Double> rateRange) {
		this.rateRange = rateRange;
	}

	private Range<Integer> address_unitNumberRange = new Range<Integer>();
	public Range<Integer> getAddress_unitNumberRange() {
		return address_unitNumberRange;
	}
	public void setAddress_unitNumber(Range<Integer> address_unitNumberRange) {
		this.address_unitNumberRange = address_unitNumberRange;
	}

	private static final String[] RESTRICTIONS = {
			"billingCompany.id = #{billingCompanyList.billingCompany.id}",

			"lower(billingCompany.brand) like concat(lower(#{billingCompanyList.billingCompany.brand}),'%')",

			"lower(billingCompany.companyType) like concat(lower(#{billingCompanyList.billingCompany.companyType}),'%')",

			"billingCompany.rate >= #{billingCompanyList.rateRange.begin}",
			"billingCompany.rate <= #{billingCompanyList.rateRange.end}",

			"lower(billingCompany.primaryPhone) like concat(lower(#{billingCompanyList.billingCompany.primaryPhone}),'%')",

			"lower(billingCompany.email) like concat(lower(#{billingCompanyList.billingCompany.email}),'%')",

			"billingCompany.address.unitNumber >= #{billingCompanyList.address_unitNumberRange.begin}",
			"billingCompany.address.unitNumber <= #{billingCompanyList.address_unitNumberRange.end}",

			"lower(billingCompany.address.streetNumber) like concat(lower(#{billingCompanyList.billingCompany.address.streetNumber}),'%')",

			"lower(billingCompany.address.streetName) like concat(lower(#{billingCompanyList.billingCompany.address.streetName}),'%')",

			"lower(billingCompany.address.streetDirection) like concat(lower(#{billingCompanyList.billingCompany.address.streetDirection}),'%')",

			"lower(billingCompany.address.streetType) like concat(lower(#{billingCompanyList.billingCompany.address.streetType}),'%')",

			"lower(billingCompany.address.province) like concat(lower(#{billingCompanyList.billingCompany.address.province}),'%')",

			"lower(billingCompany.address.postalCode) like concat(lower(#{billingCompanyList.billingCompany.address.postalCode}),'%')",

			"lower(billingCompany.address.city) like concat(lower(#{billingCompanyList.billingCompany.address.city}),'%')",

			"lower(billingCompany.address.country) like concat(lower(#{billingCompanyList.billingCompany.address.country}),'%')",

			"lower(billingCompany.poBox) like concat(lower(#{billingCompanyList.billingCompany.poBox}),'%')",

			"lower(billingCompany.name) like concat(lower(#{billingCompanyList.billingCompany.name}),'%')",

			"lower(billingCompany.webUrl) like concat(lower(#{billingCompanyList.billingCompany.webUrl}),'%')",

			"billingCompany.dateCreated <= #{billingCompanyList.dateCreatedRange.end}",
			"billingCompany.dateCreated >= #{billingCompanyList.dateCreatedRange.begin}",};

	@Observer("archivedBillingCompany")
	public void onArchive() {
		refresh();
	}

	/** create comma delimited row 
	 * @param builder
	 */
	//@Override
	public void createCsvString(StringBuilder builder, BillingCompany e) {

		builder.append("\""
				+ (e.getName() != null ? e.getName().replace(",", "") : "")
				+ "\",");

		builder.append("\""
				+ (e.getWebUrl() != null ? e.getWebUrl().replace(",", "") : "")
				+ "\",");

		builder.append("\r\n");
	}

	/** create the headings 
	 * @param builder
	 */
	//@Override
	public void createCSvTitles(StringBuilder builder) {

		builder.append("Name" + ",");

		builder.append("WebUrl" + ",");

		builder.append("\r\n");
	}
}
