package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.PassProtect;

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

import com.oreon.kg.domain.PassProtect;

/**
 * 
 * @author WitchcraftMDA Seam Cartridge - 
 *
 */
public abstract class PassProtectListQueryBase
		extends
			BaseQuery<PassProtect, Long> {

	private static final String EJBQL = "select passProtect from PassProtect passProtect";

	protected PassProtect passProtect = new PassProtect();

	public PassProtect getPassProtect() {
		return passProtect;
	}

	@Override
	protected String getql() {
		return EJBQL;
	}

	@Override
	public Class<PassProtect> getEntityClass() {
		return PassProtect.class;
	}

	@Override
	public String[] getEntityRestrictions() {
		return RESTRICTIONS;
	}

	private static final String[] RESTRICTIONS = {
			"passProtect.id = #{passProtectList.passProtect.id}",

			"passProtect.employee.id = #{passProtectList.passProtect.employee.id}",

			"lower(passProtect.companyName) like concat(lower(#{passProtectList.passProtect.companyName}),'%')",

			"lower(passProtect.companyUserName) like concat(lower(#{passProtectList.passProtect.companyUserName}),'%')",

			"lower(passProtect.companyPassword) like concat(lower(#{passProtectList.passProtect.companyPassword}),'%')",

			"lower(passProtect.note) like concat(lower(#{passProtectList.passProtect.note}),'%')",

			"passProtect.dateCreated <= #{passProtectList.dateCreatedRange.end}",
			"passProtect.dateCreated >= #{passProtectList.dateCreatedRange.begin}",};

	List<PassProtect> allpassProtectsByEmployee = null;
	public List<PassProtect> getPassProtectsByEmployee(
			com.oreon.kg.domain.Employee employee) {
		if (allpassProtectsByEmployee == null
				|| allpassProtectsByEmployee.isEmpty()) {
			passProtect.setEmployee(employee);
			allpassProtectsByEmployee = getResultList();
			return allpassProtectsByEmployee;
		} else {
			return allpassProtectsByEmployee;
		}
	}

	@Observer("archivedPassProtect")
	public void onArchive() {
		refresh();
	}

	/** create comma delimited row 
	 * @param builder
	 */
	//@Override
	public void createCsvString(StringBuilder builder, PassProtect e) {

		builder.append("\""
				+ (e.getEmployee() != null ? e.getEmployee().getDisplayName()
						.replace(",", "") : "") + "\",");

		builder.append("\""
				+ (e.getCompanyName() != null ? e.getCompanyName().replace(",",
						"") : "") + "\",");

		builder.append("\""
				+ (e.getCompanyUserName() != null ? e.getCompanyUserName()
						.replace(",", "") : "") + "\",");

		builder.append("\""
				+ (e.getCompanyPassword() != null ? e.getCompanyPassword()
						.replace(",", "") : "") + "\",");

		builder.append("\""
				+ (e.getNote() != null ? e.getNote().replace(",", "") : "")
				+ "\",");

		builder.append("\r\n");
	}

	/** create the headings 
	 * @param builder
	 */
	//@Override
	public void createCSvTitles(StringBuilder builder) {

		builder.append("Employee" + ",");

		builder.append("CompanyName" + ",");

		builder.append("CompanyUserName" + ",");

		builder.append("CompanyPassword" + ",");

		builder.append("Note" + ",");

		builder.append("\r\n");
	}
}
