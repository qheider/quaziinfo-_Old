package com.nas.recovery.web.action.users;

import com.oreon.kg.domain.users.Role;

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

import com.oreon.kg.domain.users.Role;

/**
 * 
 * @author WitchcraftMDA Seam Cartridge - 
 *
 */
public abstract class RoleListQueryBase extends BaseQuery<Role, Long> {

	private static final String EJBQL = "select role from Role role";

	protected Role role = new Role();

	public Role getRole() {
		return role;
	}

	private com.oreon.kg.domain.users.User usersToSearch;

	public void setUsersToSearch(com.oreon.kg.domain.users.User userToSearch) {
		this.usersToSearch = userToSearch;
	}

	public com.oreon.kg.domain.users.User getUsersToSearch() {
		return usersToSearch;
	}

	@Override
	protected String getql() {
		return EJBQL;
	}

	@Override
	public Class<Role> getEntityClass() {
		return Role.class;
	}

	@Override
	public String[] getEntityRestrictions() {
		return RESTRICTIONS;
	}

	private static final String[] RESTRICTIONS = {
			"role.id = #{roleList.role.id}",

			"lower(role.name) like concat(lower(#{roleList.role.name}),'%')",

			"#{roleList.usersToSearch} in elements(role.users)",

			"role.dateCreated <= #{roleList.dateCreatedRange.end}",
			"role.dateCreated >= #{roleList.dateCreatedRange.begin}",};

	@Observer("archivedRole")
	public void onArchive() {
		refresh();
	}

	/** create comma delimited row 
	 * @param builder
	 */
	//@Override
	public void createCsvString(StringBuilder builder, Role e) {

		builder.append("\""
				+ (e.getName() != null ? e.getName().replace(",", "") : "")
				+ "\",");

		builder.append("\"" + (e.getUsers() != null ? e.getUsers() : "")
				+ "\",");

		builder.append("\r\n");
	}

	/** create the headings 
	 * @param builder
	 */
	//@Override
	public void createCSvTitles(StringBuilder builder) {

		builder.append("Name" + ",");

		builder.append("Users" + ",");

		builder.append("\r\n");
	}
}
