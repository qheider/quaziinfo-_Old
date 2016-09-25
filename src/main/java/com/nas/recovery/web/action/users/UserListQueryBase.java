package com.nas.recovery.web.action.users;

import com.oreon.kg.domain.users.User;

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

import com.oreon.kg.domain.users.User;

/**
 * 
 * @author WitchcraftMDA Seam Cartridge - 
 *
 */
public abstract class UserListQueryBase extends BaseQuery<User, Long> {

	private static final String EJBQL = "select user from User user";

	protected User user = new User();

	public User getUser() {
		return user;
	}

	private com.oreon.kg.domain.users.Role rolesToSearch;

	public void setRolesToSearch(com.oreon.kg.domain.users.Role roleToSearch) {
		this.rolesToSearch = roleToSearch;
	}

	public com.oreon.kg.domain.users.Role getRolesToSearch() {
		return rolesToSearch;
	}

	@Override
	protected String getql() {
		return EJBQL;
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	public String[] getEntityRestrictions() {
		return RESTRICTIONS;
	}

	private Range<Date> lastLoginRange = new Range<Date>();
	public Range<Date> getLastLoginRange() {
		return lastLoginRange;
	}
	public void setLastLogin(Range<Date> lastLoginRange) {
		this.lastLoginRange = lastLoginRange;
	}

	private static final String[] RESTRICTIONS = {
			"user.id = #{userList.user.id}",

			"lower(user.userName) like concat(lower(#{userList.user.userName}),'%')",

			"user.enabled = #{userList.user.enabled}",

			"#{userList.rolesToSearch} in elements(user.roles)",

			"lower(user.email) like concat(lower(#{userList.user.email}),'%')",

			"user.lastLogin >= #{userList.lastLoginRange.begin}",
			"user.lastLogin <= #{userList.lastLoginRange.end}",

			"user.dateCreated <= #{userList.dateCreatedRange.end}",
			"user.dateCreated >= #{userList.dateCreatedRange.begin}",};

	@Observer("archivedUser")
	public void onArchive() {
		refresh();
	}

	/** create comma delimited row 
	 * @param builder
	 */
	//@Override
	public void createCsvString(StringBuilder builder, User e) {

		builder.append("\""
				+ (e.getUserName() != null
						? e.getUserName().replace(",", "")
						: "") + "\",");

		builder.append("\"" + (e.getEnabled() != null ? e.getEnabled() : "")
				+ "\",");

		builder.append("\"" + (e.getRoles() != null ? e.getRoles() : "")
				+ "\",");

		builder.append("\""
				+ (e.getEmail() != null ? e.getEmail().replace(",", "") : "")
				+ "\",");

		builder.append("\""
				+ (e.getLastLogin() != null ? e.getLastLogin() : "") + "\",");

		builder.append("\r\n");
	}

	/** create the headings 
	 * @param builder
	 */
	//@Override
	public void createCSvTitles(StringBuilder builder) {

		builder.append("UserName" + ",");

		builder.append("Enabled" + ",");

		builder.append("Roles" + ",");

		builder.append("Email" + ",");

		builder.append("LastLogin" + ",");

		builder.append("\r\n");
	}
}
