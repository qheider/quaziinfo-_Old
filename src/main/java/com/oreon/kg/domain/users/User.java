package com.oreon.kg.domain.users;

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
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;

import org.witchcraft.base.entity.BusinessEntity;
import org.witchcraft.model.support.audit.Auditable;
import org.witchcraft.base.entity.FileAttachment;

import org.witchcraft.utils.*;

@Entity
@Table(name = "user")
@Filter(name = "archiveFilterDef")
@Name("user")
@Indexed
@Cache(usage = CacheConcurrencyStrategy.NONE)
@Analyzer(definition = "entityAnalyzer")
public class User extends BusinessEntity implements java.io.Serializable {
	private static final long serialVersionUID = -1796332121L;

	//@Unique(entityName = "com.oreon.kg.domain.users.User", fieldName = "userName")

	@NotNull
	@Length(min = 2, max = 250)
	@Column(name = "userName", unique = true)
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String userName;

	@NotNull
	@Column(name = "password", unique = false)
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String password;

	protected Boolean enabled = true;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "users_ID"), inverseJoinColumns = @JoinColumn(name = "roles_ID"))
	private Set<Role> roles = new HashSet<Role>();

	@NotNull
	@Length(min = 2, max = 250)
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String email;

	@Column(name = "lastLogin", unique = false)
	protected Date lastLogin;

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@UserPrincipal 
	public String getUserName() {

		return userName;

	}

	public void setPassword(String password) {
		this.password = password;
	}

	@UserPassword
	public String getPassword() {

		return password;

	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@UserEnabled
	public Boolean getEnabled() {

		return enabled;

	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@UserRoles
	public Set<Role> getRoles() {
		return roles;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {

		return email;

	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastLogin() {

		return lastLogin;

	}

	@Transient
	public String getDisplayName() {
		try {
			return userName;
		} catch (Exception e) {
			return " User ";
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

		listSearchableFields.add("userName");

		listSearchableFields.add("password");

		listSearchableFields.add("email");

		return listSearchableFields;
	}

	@Field(index = Index.TOKENIZED, name = "searchData")
	@Analyzer(definition = "entityAnalyzer")
	public String getSearchData() {
		StringBuilder builder = new StringBuilder();

		builder.append(getUserName() + " ");

		builder.append(getPassword() + " ");

		builder.append(getEmail() + " ");

		return builder.toString();
	}

	/*PROTECTED REGION ID(ClASS_EDITABLE_RGN__User) ENABLED START*/
	//this is protected region
	/*PROTECTED REGION END*/

}
