package com.oreon.kg.domain;

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
@Table(name = "weblog")
@Filter(name = "archiveFilterDef")
@Name("webLog")
@Indexed
@Cache(usage = CacheConcurrencyStrategy.NONE)
@Analyzer(definition = "entityAnalyzer")
public class WebLog extends BusinessEntity implements java.io.Serializable {
	private static final long serialVersionUID = -1347885632L;

	@Column(name = "heading", unique = false)
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String heading;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String headingDesc;

	@Lob
	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String body;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", nullable = false, updatable = true)
	@ContainedIn
	protected Employee employee;

	@Field(index = Index.TOKENIZED)
	@Analyzer(definition = "entityAnalyzer")
	protected String tag;

	protected WebLogType type;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "tagFile_name")),
			@AttributeOverride(name = "contentType", column = @Column(name = "tagFile_contentType")),
			@AttributeOverride(name = "data", column = @Column(name = "tagFile_data", length = 4194304))})
	protected FileAttachment tagFile = new FileAttachment();

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getHeading() {

		return heading;

	}

	public void setHeadingDesc(String headingDesc) {
		this.headingDesc = headingDesc;
	}

	public String getHeadingDesc() {

		return headingDesc;

	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {

		return body;

	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getEmployee() {

		return employee;

	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {

		return tag;

	}

	public void setType(WebLogType type) {
		this.type = type;
	}

	public WebLogType getType() {

		return type;

	}

	public void setTagFile(FileAttachment tagFile) {
		this.tagFile = tagFile;
	}

	public FileAttachment getTagFile() {

		return tagFile;

	}

	@Transient
	public String getDisplayName() {
		try {
			return heading;
		} catch (Exception e) {
			return " WebLog ";
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

		listSearchableFields.add("heading");

		listSearchableFields.add("headingDesc");

		listSearchableFields.add("body");

		listSearchableFields.add("tag");

		return listSearchableFields;
	}

	@Field(index = Index.TOKENIZED, name = "searchData")
	@Analyzer(definition = "entityAnalyzer")
	public String getSearchData() {
		StringBuilder builder = new StringBuilder();

		builder.append(getHeading() + " ");

		builder.append(getHeadingDesc() + " ");

		builder.append(getBody() + " ");

		builder.append(getTag() + " ");

		if (getEmployee() != null)
			builder.append("employee:" + getEmployee().getDisplayName() + " ");

		return builder.toString();
	}

	/*PROTECTED REGION ID(ClASS_EDITABLE_RGN__WebLog) ENABLED START*/
	//this is protected region

	/*PROTECTED REGION END*/

}
