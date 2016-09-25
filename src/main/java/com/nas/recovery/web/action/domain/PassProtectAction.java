package com.nas.recovery.web.action.domain;

import com.oreon.kg.domain.Employee;
import com.oreon.kg.domain.PassProtect;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.query.dsl.QueryBuilder;

import org.apache.commons.lang.StringUtils;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Scope;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;

import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.Observer;

import org.witchcraft.base.entity.FileAttachment;

import org.apache.commons.io.FileUtils;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

//@Scope(ScopeType.CONVERSATION)
@Name("passProtectAction")
public class PassProtectAction extends PassProtectActionBase implements java.io.Serializable {

    @In(create = true)
    EmployeeAction employeeAction;
    private String searchString;
    @DataModel(value = "searchResult", scope = ScopeType.PAGE)
    private List<PassProtect> searchResult;

    @Override
    public String save() {
        updateComposedAssociations();

        Employee newEmp = new Employee();
        newEmp = employeeAction.getCurrentLoggedInEmployee();
        this.getPassProtect().setEmployee(newEmp);
        return super.save();
    }

    public void doSearch1() {
        String query = "select p from PassProtect p where p.companyName like ?1 and p.employee=?2";
        searchResult = (this.executeQuery(query, searchString, employeeAction.getCurrentLoggedInEmployee()));

    }
    
    public void doSearch() {
    	final QueryBuilder qb = entityManager.getSearchFactory() .buildQueryBuilder().forEntity(PassProtect.class).get();
    	org.apache.lucene.search.Query lucenQuery = qb.keyword().fuzzy()
				.withThreshold(0.68f).onField("searchData")
				.matching(searchString).createQuery();
    	org.hibernate.search.jpa.FullTextQuery fullTextQuery = entityManager
				.createFullTextQuery(lucenQuery, PassProtect.class);
    	setSearchResult(fullTextQuery.getResultList());
    }
   

    /**
     * @return the searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString the searchString to set
     */
    public void setSearchString(String searchString) {
        this.searchString = "%"+searchString+"%";
    }

    /**
     * @return the searchResult
     */
    public List<PassProtect> getSearchResult() {
        return searchResult;
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(List<PassProtect> searchResult) {
        this.searchResult = searchResult;
    }
}
