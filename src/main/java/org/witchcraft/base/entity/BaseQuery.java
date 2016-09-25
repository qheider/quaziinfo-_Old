package org.witchcraft.base.entity;

import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;
import org.jboss.seam.persistence.PersistenceProvider;
import org.witchcraft.exceptions.ContractViolationException;



/**
 * @author User
 *
 * @param <E>
 * @param <PK>
 */
@SuppressWarnings("serial")
public abstract class BaseQuery<E extends BusinessEntity, PK extends Serializable>
		extends EntityQuery<E> {

	private static final String SEARCH_DATA = "searchData";

	private Class<E> entityClass = null;

	protected E instance;

	@Logger
	protected Log log;

	private Range<java.util.Date> dateCreatedRange = new Range<Date>();

	private List<E> entityList;

	@RequestParameter
	protected String searchText;

	public static final int DEFAULT_PAGES_FOR_PAGINATION = 25;

	@In
	// @PersistenceContext(type=EXTENDED)
	protected FullTextEntityManager entityManager;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public abstract String[] getEntityRestrictions();

	public BaseQuery() {
		setEjbql(getql());
		setRestrictionExpressionStrings(Arrays.asList(getEntityRestrictions()));
		setMaxResults(DEFAULT_PAGES_FOR_PAGINATION);
	}

	protected String getql() {
		String simpleEntityName = getSimpleEntityName().toLowerCase();
		return "SELECT " + simpleEntityName + " FROM  " + getEntityName() + " "
				+ simpleEntityName;
	}

	protected String textToSearch;

	public String getTextToSearch() {
		return textToSearch;
	}

	public void setTextToSearch(String textToSearch) {
		this.textToSearch = textToSearch;
	}

	/**
	 * Get the class of the entity being managed. <br />
	 * If not explicitly specified, the generic type of implementation is used.
	 */
	@SuppressWarnings("unchecked")
	public Class<E> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();

			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
			} else {
				type = getClass().getSuperclass().getGenericSuperclass();
				if (type instanceof ParameterizedType) {
					ParameterizedType paramType = (ParameterizedType) type;
					entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
				} else {
					throw new IllegalArgumentException(
							"Could not guess entity class by reflection");
				}
			}
		}
		return entityClass;
	}

	public E getInstance() {
		if (instance == null) {
			setInstance(createInstance());
		}
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}

	/**
	 * Create a new instance of the entity. <br />
	 * Utility method called by {@link #getInstance()} to create a new instance
	 * of the entity.
	 */
	protected E createInstance() {
		if (getEntityClass() != null) {
			try {
				return getEntityClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	/**
	 * The simple name of the entity
	 */
	protected String getSimpleEntityName() {
		String name = getEntityName();
		if (name != null) {
			return name.lastIndexOf(".") > 0
					&& name.lastIndexOf(".") < name.length() ? name.substring(
					name.lastIndexOf(".") + 1, name.length()) : name;
		} else {
			return null;
		}
	}

	protected String getEntityName() {
		try {
			return PersistenceProvider.instance().getName(getInstance(),
					getEntityManager());
		} catch (IllegalArgumentException e) {
			// Handle that the passed object may not be an entity
			return null;
		}
	}

	public E getObjectByID(PK id) {
		return (E) getEntityManager().find(getEntityClass(), id);
	}

	/**
	 * Get records by a named query. Can also optionally get all records
	 * starting at a start position and the count of records to return
	 * 
	 * @param name
	 * @param parameters
	 * @param rowStartIndexAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<E> findRecordsByName(final String name,
			final Map parameters, final int... rowStartIndexAndCount) {
		Query query = getEntityManager().createNamedQuery(name);
		if (parameters != null) {
			for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, Object> entry = it.next();
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		setupQueryForStartIndexAndCount(query, rowStartIndexAndCount);
		return query.getResultList();
	}

	/**
	 * Find a single record by a named query
	 * 
	 * @param name
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E findRecordByName(String name, Map parameters) {
		Query query = getEntityManager().createNamedQuery(name);
		if (parameters != null) {
			for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, Object> entry = it.next();
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return (E) query.getSingleResult();

	}

	/**
	 * Helper method to setup a query with a start position and the count of
	 * records to return.
	 * 
	 * @param query
	 * @param rowStartIndexAndCount
	 */
	private void setupQueryForStartIndexAndCount(Query query,
			final int... rowStartIndexAndCount) {
		if (rowStartIndexAndCount != null && rowStartIndexAndCount.length > 0) {
			int rowStartIdx = Math.max(0, rowStartIndexAndCount[0]);
			if (rowStartIdx > 0) {
				query.setFirstResult(rowStartIdx);
			}
			if (rowStartIndexAndCount.length > 1) {
				int rowCount = Math.max(0, rowStartIndexAndCount[1]);
				if (rowCount > 0) {
					query.setMaxResults(rowCount);
				}
			}
		}
	}

	public Range<java.util.Date> getDateCreatedRange() {
		return dateCreatedRange;
	}

	public void setDateCreatedRange(Range<java.util.Date> dateCreatedRange) {
		this.dateCreatedRange = dateCreatedRange;
	}

	@SuppressWarnings("unchecked")
	@Begin(join = true)
	public String textSearch() {


		QueryParser parser = new QueryParser(Version.LUCENE_30, SEARCH_DATA ,
			 entityManager.getSearchFactory()
						.getAnalyzer("entityAnalyzer"));

		org.apache.lucene.search.Query query = null;

		try {
			if (searchText == null)
				searchText = textToSearch;
			if (searchText == null)
				searchText = "";
			query = parser.parse(searchText);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		QueryScorer scorer = new QueryScorer(query, SEARCH_DATA);
		// Highlight using a CSS style
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
				"<span style='background-color:yellow;'>", "</span>");
		Highlighter highlighter = new Highlighter(formatter, scorer); 
		highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, 100));

		FullTextQuery ftq = entityManager.createFullTextQuery(query,
				getEntityClass());

		List<E> result = ftq.getResultList();
		
		for (E e : result) {
			try {
				String fragment = highlighter.getBestFragment(entityManager
						.getSearchFactory().getAnalyzer("entityAnalyzer"),
						SEARCH_DATA, e.getSearchData() );
				
				e.setHiglightedFragment(fragment);
			} catch (Exception ex) {
				throw new ContractViolationException(ex.getMessage());
			}
		}

		setEntityList(result);
		return "textSearch";
	}

	// @Override
	public void setEntityList(List<E> list) {
		this.entityList = list;
	}

	public List<E> getEntityList() {
		return entityList;
	}

	/**
	 * do autocomplete based on lucene/hibernate text search
	 * 
	 * @param suggest
	 * @return
	 */
	public List<E> autocomplete(Object suggest) {

		String input = (String) suggest;

		ArrayList<E> result = new ArrayList<E>();
		// departmentListQuery.getDepartment().setName(input);

		Iterator<E> iterator = getResultList().iterator();

		while (iterator.hasNext()) {
			E elem = iterator.next();
			String elemProperty = elem.getDisplayName();
			if ((elemProperty != null && elemProperty.toLowerCase().indexOf(
					input.toLowerCase()) == 0)
					|| "".equals(input)) {
				result.add(elem);
			}
		}

		return result;
	}

	/**
	 * Do autocomplete based on database fields
	 * 
	 * @param suggest
	 * @return
	 */
	public List<E> autocompletedb(Object suggest) {
		String input = (String) suggest;
		setupForAutoComplete(input);
		super.setRestrictionLogicOperator("or");
		return getResultList();
	}

	/**
	 * This method should be overridden by derived classes to provide fileds
	 * that will be used for autocomplete
	 * 
	 * @param input
	 */
	protected void setupForAutoComplete(String input) {

	}

	/**
	 * set the query to be usable by hibernate second level cache
	 */
	@SuppressWarnings("unchecked")
	protected void setQueryCacheable() {
		Map region = new TreeMap();
		region.put("name=org.hibernate.cacheable", "value=true");
		this.setHints(region);
	}

	protected void downloadAttachment(byte[] bytes) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		response.setContentType("text/plain");

		response.addHeader("Content-disposition", "attachment; filename=\""
				+ "export.csv" + "\"");

		try {
			OutputStream os = response.getOutputStream();
			os.write(bytes);
			os.flush();
			os.close();
			context.responseComplete();
		} catch (Exception e) {
			log.error("\nFailure : " + e.toString() + "\n");
		}
	}

	public void exportToCsv() {
		int originalMaxResults = getMaxResults();
		setMaxResults(50000);
		
		List<E> results = getResultList();

		StringBuilder builder = new StringBuilder();
		createCSvTitles(builder);

		for (E e : results) {
			createCsvString(builder, e);
		}

		setMaxResults(originalMaxResults);
		downloadAttachment(builder.toString().getBytes());
	}
	
	/** create comma delimited row 
	 * @param builder
	 */
	public void createCsvString(StringBuilder builder, E e){
	}
	

	
	/** create the headings 
	 * @param builder
	 */
	public void createCSvTitles(StringBuilder builder ){
	
	}
	
	
	protected boolean isPostBack() {
		ResponseStateManager rtm = FacesContext.getCurrentInstance()
				.getRenderKit().getResponseStateManager();
		return rtm.isPostback(FacesContext.getCurrentInstance());
	}
	
	/** Creates a string for export to csv, if null, blank string is returned
	 * @param e
	 * @return
	 */
	protected String getFieldForCSV(String e) {
		return (e != null ? e.replace("," , "") : "");
	}
	
	
	public static void main(String[] args) {
		
	}
	
}