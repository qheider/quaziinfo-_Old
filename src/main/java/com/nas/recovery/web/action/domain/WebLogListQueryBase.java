package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.WebLog;

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

import com.oreon.kg.domain.WebLog;

/**
 * 
 * @author WitchcraftMDA Seam Cartridge - 
 *
 */
public abstract class WebLogListQueryBase extends BaseQuery<WebLog, Long> {

	private static final String EJBQL = "select webLog from WebLog webLog";

	protected WebLog webLog = new WebLog();

	public WebLog getWebLog() {
		return webLog;
	}

	@Override
	protected String getql() {
		return EJBQL;
	}

	@Override
	public Class<WebLog> getEntityClass() {
		return WebLog.class;
	}

	@Override
	public String[] getEntityRestrictions() {
		return RESTRICTIONS;
	}

	private static final String[] RESTRICTIONS = {
			"webLog.id = #{webLogList.webLog.id}",

			"lower(webLog.heading) like concat(lower(#{webLogList.webLog.heading}),'%')",

			"lower(webLog.headingDesc) like concat(lower(#{webLogList.webLog.headingDesc}),'%')",

			"lower(webLog.body) like concat(lower(#{webLogList.webLog.body}),'%')",

			"webLog.employee.id = #{webLogList.webLog.employee.id}",

			"lower(webLog.tag) like concat(lower(#{webLogList.webLog.tag}),'%')",

			"webLog.type = #{webLogList.webLog.type}",

			"webLog.dateCreated <= #{webLogList.dateCreatedRange.end}",
			"webLog.dateCreated >= #{webLogList.dateCreatedRange.begin}",};

	List<WebLog> allwebLogsByEmployee = null;
	public List<WebLog> getWebLogsByEmployee(
			com.oreon.kg.domain.Employee employee) {
		if (allwebLogsByEmployee == null || allwebLogsByEmployee.isEmpty()) {
			webLog.setEmployee(employee);
			allwebLogsByEmployee = getResultList();
			return allwebLogsByEmployee;
		} else {
			return allwebLogsByEmployee;
		}
	}

	@Observer("archivedWebLog")
	public void onArchive() {
		refresh();
	}

	/** create comma delimited row 
	 * @param builder
	 */
	//@Override
	public void createCsvString(StringBuilder builder, WebLog e) {

		builder.append("\""
				+ (e.getHeading() != null
						? e.getHeading().replace(",", "")
						: "") + "\",");

		builder.append("\""
				+ (e.getHeadingDesc() != null ? e.getHeadingDesc().replace(",",
						"") : "") + "\",");

		builder.append("\""
				+ (e.getBody() != null ? e.getBody().replace(",", "") : "")
				+ "\",");

		builder.append("\""
				+ (e.getEmployee() != null ? e.getEmployee().getDisplayName()
						.replace(",", "") : "") + "\",");

		builder.append("\""
				+ (e.getTag() != null ? e.getTag().replace(",", "") : "")
				+ "\",");

		builder.append("\"" + (e.getType() != null ? e.getType() : "") + "\",");

		builder.append("\r\n");
	}

	/** create the headings 
	 * @param builder
	 */
	//@Override
	public void createCSvTitles(StringBuilder builder) {

		builder.append("Heading" + ",");

		builder.append("HeadingDesc" + ",");

		builder.append("Body" + ",");

		builder.append("Employee" + ",");

		builder.append("Tag" + ",");

		builder.append("Type" + ",");

		builder.append("\r\n");
	}
}
