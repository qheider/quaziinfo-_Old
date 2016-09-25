package org.witchcraft.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Table(name = "template")
@Name("entityTemplate")
@EntityListeners({EntityTemplateListener.class})
public class EntityTemplate<T extends BusinessEntity> extends BusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5160168397259950773L;

	@NotNull
	private String entityName;
	
	@NotNull
	private String templateName;
	
	@Lob
	@Column(length=1048576)
	protected String encodedXml;
	
	@Transient
	private T entity;

	@Transient
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}



	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEncodedXml() {
		return encodedXml;
	}

	public void setEncodedXml(String encodedXml) {
		this.encodedXml = encodedXml;
	}

	@Transient
	public String getDisplayName() {
		return templateName;
	}

}
