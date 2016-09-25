package com.nas.recovery.web.action.userBill;

import com.oreon.kg.domain.userBill.UserBill;

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

import com.oreon.kg.domain.userBill.UserBill;

/**
 * 
 * @author WitchcraftMDA Seam Cartridge - 
 *
 */
public abstract class UserBillListQueryBase extends BaseQuery<UserBill, Long> {

	private static final String EJBQL = "select userBill from UserBill userBill";

	protected UserBill userBill = new UserBill();

	public UserBill getUserBill() {
		return userBill;
	}

	@Override
	protected String getql() {
		return EJBQL;
	}

	@Override
	public Class<UserBill> getEntityClass() {
		return UserBill.class;
	}

	@Override
	public String[] getEntityRestrictions() {
		return RESTRICTIONS;
	}

	private Range<Double> minPaymentRange = new Range<Double>();
	public Range<Double> getMinPaymentRange() {
		return minPaymentRange;
	}
	public void setMinPayment(Range<Double> minPaymentRange) {
		this.minPaymentRange = minPaymentRange;
	}

	private Range<Date> minPaymentDueDateRange = new Range<Date>();
	public Range<Date> getMinPaymentDueDateRange() {
		return minPaymentDueDateRange;
	}
	public void setMinPaymentDueDate(Range<Date> minPaymentDueDateRange) {
		this.minPaymentDueDateRange = minPaymentDueDateRange;
	}

	private Range<Double> currentBalanceRange = new Range<Double>();
	public Range<Double> getCurrentBalanceRange() {
		return currentBalanceRange;
	}
	public void setCurrentBalance(Range<Double> currentBalanceRange) {
		this.currentBalanceRange = currentBalanceRange;
	}

	private Range<Double> amountPaidRange = new Range<Double>();
	public Range<Double> getAmountPaidRange() {
		return amountPaidRange;
	}
	public void setAmountPaid(Range<Double> amountPaidRange) {
		this.amountPaidRange = amountPaidRange;
	}

	private Range<Double> previousPaymentRange = new Range<Double>();
	public Range<Double> getPreviousPaymentRange() {
		return previousPaymentRange;
	}
	public void setPreviousPayment(Range<Double> previousPaymentRange) {
		this.previousPaymentRange = previousPaymentRange;
	}

	private Range<Date> statementDateRange = new Range<Date>();
	public Range<Date> getStatementDateRange() {
		return statementDateRange;
	}
	public void setStatementDate(Range<Date> statementDateRange) {
		this.statementDateRange = statementDateRange;
	}

	private Range<Date> previousStatementDateRange = new Range<Date>();
	public Range<Date> getPreviousStatementDateRange() {
		return previousStatementDateRange;
	}
	public void setPreviousStatementDate(Range<Date> previousStatementDateRange) {
		this.previousStatementDateRange = previousStatementDateRange;
	}

	private static final String[] RESTRICTIONS = {
			"userBill.id = #{userBillList.userBill.id}",

			"userBill.minPayment >= #{userBillList.minPaymentRange.begin}",
			"userBill.minPayment <= #{userBillList.minPaymentRange.end}",

			"userBill.minPaymentDueDate >= #{userBillList.minPaymentDueDateRange.begin}",
			"userBill.minPaymentDueDate <= #{userBillList.minPaymentDueDateRange.end}",

			"userBill.currentBalance >= #{userBillList.currentBalanceRange.begin}",
			"userBill.currentBalance <= #{userBillList.currentBalanceRange.end}",

			"userBill.amountPaid >= #{userBillList.amountPaidRange.begin}",
			"userBill.amountPaid <= #{userBillList.amountPaidRange.end}",

			"userBill.previousPayment >= #{userBillList.previousPaymentRange.begin}",
			"userBill.previousPayment <= #{userBillList.previousPaymentRange.end}",

			"userBill.statementDate >= #{userBillList.statementDateRange.begin}",
			"userBill.statementDate <= #{userBillList.statementDateRange.end}",

			"userBill.previousStatementDate >= #{userBillList.previousStatementDateRange.begin}",
			"userBill.previousStatementDate <= #{userBillList.previousStatementDateRange.end}",

			"lower(userBill.comment) like concat(lower(#{userBillList.userBill.comment}),'%')",

			"userBill.billingCompany.id = #{userBillList.userBill.billingCompany.id}",

			"userBill.employee.id = #{userBillList.userBill.employee.id}",

			"userBill.dateCreated <= #{userBillList.dateCreatedRange.end}",
			"userBill.dateCreated >= #{userBillList.dateCreatedRange.begin}",};

	List<UserBill> alluserBillsByBillingCompany = null;
	public List<UserBill> getUserBillsByBillingCompany(
			com.oreon.kg.domain.userBill.BillingCompany billingCompany) {
		if (alluserBillsByBillingCompany == null
				|| alluserBillsByBillingCompany.isEmpty()) {
			userBill.setBillingCompany(billingCompany);
			alluserBillsByBillingCompany = getResultList();
			return alluserBillsByBillingCompany;
		} else {
			return alluserBillsByBillingCompany;
		}
	}

	List<UserBill> alluserBillsByEmployee = null;
	public List<UserBill> getUserBillsByEmployee(
			com.oreon.kg.domain.Employee employee) {
		if (alluserBillsByEmployee == null || alluserBillsByEmployee.isEmpty()) {
			userBill.setEmployee(employee);
			alluserBillsByEmployee = getResultList();
			return alluserBillsByEmployee;
		} else {
			return alluserBillsByEmployee;
		}
	}

	@Observer("archivedUserBill")
	public void onArchive() {
		refresh();
	}

	/** create comma delimited row 
	 * @param builder
	 */
	//@Override
	public void createCsvString(StringBuilder builder, UserBill e) {

		builder.append("\""
				+ (e.getMinPayment() != null ? e.getMinPayment() : "") + "\",");

		builder.append("\""
				+ (e.getMinPaymentDueDate() != null
						? e.getMinPaymentDueDate()
						: "") + "\",");

		builder.append("\""
				+ (e.getCurrentBalance() != null ? e.getCurrentBalance() : "")
				+ "\",");

		builder.append("\""
				+ (e.getAmountPaid() != null ? e.getAmountPaid() : "") + "\",");

		builder.append("\""
				+ (e.getPreviousPayment() != null ? e.getPreviousPayment() : "")
				+ "\",");

		builder.append("\""
				+ (e.getStatementDate() != null ? e.getStatementDate() : "")
				+ "\",");

		builder.append("\""
				+ (e.getPreviousStatementDate() != null ? e
						.getPreviousStatementDate() : "") + "\",");

		builder.append("\""
				+ (e.getComment() != null
						? e.getComment().replace(",", "")
						: "") + "\",");

		builder.append("\""
				+ (e.getBillingCompany() != null ? e.getBillingCompany()
						.getDisplayName().replace(",", "") : "") + "\",");

		builder.append("\""
				+ (e.getEmployee() != null ? e.getEmployee().getDisplayName()
						.replace(",", "") : "") + "\",");

		builder.append("\r\n");
	}

	/** create the headings 
	 * @param builder
	 */
	//@Override
	public void createCSvTitles(StringBuilder builder) {

		builder.append("MinPayment" + ",");

		builder.append("MinPaymentDueDate" + ",");

		builder.append("CurrentBalance" + ",");

		builder.append("AmountPaid" + ",");

		builder.append("PreviousPayment" + ",");

		builder.append("StatementDate" + ",");

		builder.append("PreviousStatementDate" + ",");

		builder.append("Comment" + ",");

		builder.append("BillingCompany" + ",");

		builder.append("Employee" + ",");

		builder.append("\r\n");
	}
}
