package org.domain.quaziinfo.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Version;
import org.hibernate.validator.Length;

@Entity
public class dictionaryInfo implements Serializable
{
    // seam-gen attributes (you should probably edit these)
    private Long id;
    private Integer version;
    private String symbolicName;
    private String componentName;
    private String databaseName;
    private String description;
    private String marsDef;
    private int marsFieldLength;
    private String bwDef;
    private String rbcDef;
    private String consolidatedDef;
    private String comments;
    private boolean avtive;
    

    // add additional entity attributes

    // seam-gen attribute getters/setters with annotations (you probably should edit)

    public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMarsDef() {
		return marsDef;
	}

	public void setMarsDef(String marsDef) {
		this.marsDef = marsDef;
	}

	public int getMarsFieldLength() {
		return marsFieldLength;
	}

	public void setMarsFieldLength(int marsFieldLength) {
		this.marsFieldLength = marsFieldLength;
	}

	public String getBwDef() {
		return bwDef;
	}

	public void setBwDef(String bwDef) {
		this.bwDef = bwDef;
	}

	public String getRbcDef() {
		return rbcDef;
	}

	public void setRbcDef(String rbcDef) {
		this.rbcDef = rbcDef;
	}

	public String getConsolidatedDef() {
		return consolidatedDef;
	}

	public void setConsolidatedDef(String consolidatedDef) {
		this.consolidatedDef = consolidatedDef;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isAvtive() {
		return avtive;
	}

	public void setAvtive(boolean avtive) {
		this.avtive = avtive;
	}

	@Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

 

}
