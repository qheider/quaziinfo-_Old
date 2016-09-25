package org.witchcraft.seam.action;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.witchcraft.base.entity.BusinessEntity;
import org.witchcraft.base.entity.EntityComment;
import org.witchcraft.base.entity.EntityTemplate;
import org.witchcraft.base.entity.FileAttachment;
import org.witchcraft.model.support.audit.AuditLog;
import org.witchcraft.model.support.audit.Auditable;
import org.witchcraft.model.support.audit.EntityAuditLogInterceptor;

/**
 * The base action class - contains common persistence related methods, also
 * contains comment related functionality
 * 
 * @author jsingh
 * 
 * @param <T>
 */
public abstract class BaseAction<T extends BusinessEntity> extends
		EntityHome<T> {

	@Logger
	protected Log log;

	@In(create=true)
	// @PersistenceContext(type = PersistenceContextType.EXTENDED)
	protected FullTextEntityManager entityManager;

	@In(create = true)
	protected EntityAuditLogInterceptor entityAuditLogInterceptor;

	@In
	protected Events events;
	
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";

	@RequestParameter
	private String queryString;

	private String templateName;

	@RequestParameter
	private Long idToArchive;

	@In(create = true)
	private Renderer renderer;

	@RequestParameter
	protected Long currentEntityId;

	private List<AuditLog> auditLog;

	@In
	protected Map<String, UIComponent> uiComponent;

	private List<EntityComment> comments;
	private String currentCommentText;

	private boolean templateMode;

	@In
	protected StatusMessages statusMessages;

	private EntityTemplate<T> entityTemplate = new EntityTemplate<T>();

	private String selectedTab;

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public EntityTemplate<T> getEntityTemplate() {
		return entityTemplate;
	}

	public void setEntityTemplate(EntityTemplate<T> entityTemplate) {
		this.entityTemplate = entityTemplate;
	}

	public boolean isTemplateMode() {
		return templateMode;
	}

	public void setTemplateMode(boolean templateMode) {
		this.templateMode = templateMode;
	}

	@Begin
	public String createTemplate() {
		setTemplateMode(true);
		return "edit";
	}

	public String getCurrentCommentText() {
		return currentCommentText;
	}

	public void setCurrentCommentText(String currentCommentText) {
		this.currentCommentText = currentCommentText;
	}

	@Begin(join = true)
	public String select(T t) {
		// setEntity(entityManager.merge(t));
		log
				.info("User selected #{t.getClass().getName()}: #{t.displayName} #{t.id} ");
		updateAssociations();
		log.info("returnring: " + "view" + getClassName(t));
		return "view";
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@End
	@Restrict
	public String archive(T t) {
		if (t == null)
			t = getInstance();
		t.setArchived(true);
		entityManager.merge(t);
		addInfoMessage("Successfully archived  " + t.getDisplayName());
		log
				.info("User archived #{t.getClass().getName()}: #{t.displayName} #{t.id} ");
		events.raiseTransactionSuccessEvent("archived" + getClassName(t));
		events.raiseTransactionSuccessEvent("resetList");
		Events.instance().raiseEvent(EventTypes.ARCHIVE.name(),
				EventTypes.ARCHIVE, t);
		return "archived";
	}

	protected void addInfoMessage(String message, Object... params) {
		statusMessages.add(message, params);
	}

	protected void addErrorMessage(String message, Object... params) {
		statusMessages.add(Severity.ERROR, message, params);
	}

	public String saveTemplate() {
		EntityTemplate<T> template = entityTemplate;

		template.setTemplateName(templateName);
		template.setEntity(getInstance());
		updateComposedAssociations();
		template.setEntityName(getClassName(getInstance()));
		// template.setTemplateName(getEn);
		entityManager.persist(template);

		// TODO: replace with statusmessages seam class
		addInfoMessage("Successfully saved template: "
				+ template.getTemplateName());
		return "save";
	}

	public void loadFromTemplate(String templateName) {
		setInstance((T) getEntityTemplate().getEntity());
	}

	public void loadFromTemplate() {
		loadFromTemplate(entityTemplate.getId());
	}

	// @Transactional
	// @Begin(join = true)
	public void loadFromTemplate(Long id) {
		entityTemplate = entityManager.find(EntityTemplate.class, id);
		@SuppressWarnings("unused")
		T t = (T) entityTemplate.getEntity();
		Session session = (Session) entityManager.getDelegate();
		// session.lock(t, LockMode.UPGRADE);
		instance = t;

		loadAssociations();

	}

	public List<EntityTemplate<T>> getTemplateList() {
		return executeNamedQuery("entityTemplates.templatesForEntity",
				getClassName(getInstance()), Identity.instance()
						.getCredentials().getUsername());
	}

	public EntityTemplate getCurrentTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The current template name
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return templateName;
	}

	@Transactional
	public T persist(T e) {
		if (e.getId() != null && e.getId() > 0) {

			if (e instanceof Auditable) {
				T prevEntity = loadFromId(e.getId());
				Events.instance().raiseEvent(EventTypes.UPDATE.name(),
						EventTypes.UPDATE, prevEntity);
			}
			entityManager.merge(e);
		} else { // new object
			entityManager.persist(e);
			Events.instance().raiseEvent(EventTypes.CREATE.name(),
					EventTypes.CREATE, e);
		}
		return e;
	}

	@Transactional
	public String doSave() {
		try {

			updateComposedAssociations();

			if (isManaged())
				update();
			else
				persist();


			updateAssociations();
                        clearInstance();

		} catch (Exception e) {
			addErrorMessage("Error Saving record: " + e.getMessage());
			log.error("error saving ", e);
			return "error";
		}
		return "save";

	}

	public String save() {
		return doSave();
	}
 @RaiseEvent("webLogCreated")
	@End(beforeRedirect=true)
	public String saveWithoutConversation() {
		String result = save();
		Conversation.instance().end();
		clearInstance();
		return result;
	}

	public String savehome() {
		Conversation.instance().begin();
		persist();
		Conversation.instance().endAndRedirect();
		return null;
	}

	@SuppressWarnings("unchecked")
	public T loadFromId(Long entityId) {

		log.info("Loading " + entityId);
		if (entityId != null && entityId > 0) {

			try {
				T t = (T) getEntityManager().find(getEntityClass(), entityId);
				return t;

			} catch (NoResultException noResultException) {
				addErrorMessage("Invalid Id: {0} ", entityId);
			}
		} else {
			// loadAssociations();
		}

		return null;
	}

	public void load(Long entityId) {
		// if (entityId == null || entityId == 0)
		// entityId = currentEntityId;
		if (entityId == null || entityId == 0) {
			log.info("Empty id " + entityId);
			return;
		}
		log.info("loading id: " + entityId);
		setId(entityId);
		loadInstance();

		// setInstance(loadFromId(entityId));
		// return "edit";
	}

	public void loadAssociations() {
	};

	public String edit() {
		// load(id);
		return "edit";
	}

	public String view() {
		// load(getId());
		return "view";
	}

	@Restrict
	public void archive() {
		load((Long) getId());
		archive(getInstance());
	}

	@Restrict
	public void archiveById() {
		T t = loadFromId(idToArchive);
		archive(t);
	}

	@SuppressWarnings("unchecked")
	public Long getRecordCount() {
		try {
			String queryString = "select count(c) from " + getClassName()
					+ " c ";
			return (Long) getEntityManager().createQuery(queryString)
					.getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@End
	public String cancel() {
		Conversation.instance().end();
		clearInstance();
		clearLists();
		return "cancel";
	}

	protected void clearLists() {
		
	}

	@SuppressWarnings("unchecked")
	public List<T> search(T  t) {
		setInstance(t);
		Criteria criteria = createExampleCriteria();
		return criteria.list();
	}

	public List<AuditLog> getAuditLog() {
		if (auditLog == null) {
			EntityAuditLogInterceptor entityAuditLogInterceptor = (EntityAuditLogInterceptor) Component
					.getInstance("entityAuditLogInterceptor");
			auditLog = entityAuditLogInterceptor
					.getAuditLogsForEntityAndId(getInstance().getClass()
							.getCanonicalName(), (Long) getId());
		}
		return auditLog;
	}

	public List<AuditLog> getAuditLogForCurrentEntity() {
		// if(auditLog == null || auditLog.isEmpty()){
		EntityAuditLogInterceptor entityAuditLogInterceptor = (EntityAuditLogInterceptor) Component
				.getInstance("entityAuditLogInterceptor");
		auditLog = entityAuditLogInterceptor.getAuditLogsForEntityAndId(
				getInstance().getClass().getCanonicalName(), getInstance()
						.getId());
		// }
		return auditLog;
	}

	public void setAuditLog(List<AuditLog> auditLog) {
		this.auditLog = auditLog;
	}

	public Criteria createExampleCriteria() {
		Session session = (Session) entityManager.getDelegate();

		Example example = Example.create(getInstance()).enableLike(
				MatchMode.START).ignoreCase().excludeZeroes();

		Criteria criteria = session.createCriteria(getInstance().getClass())
				.add(example);
		/*
		 * for (String exclude : excludedProperties) {
		 * example.excludeProperty(exclude); }
		 */
		addAssociations(criteria);

		criteria.addOrder(getOrder());

		return criteria;
	}

	public Order getOrder() {
		return Order.asc("id");
	}

	/**
	 * This method should be overloaded by actions to add associations e.g. an
	 * order would need the associated customer
	 * 
	 * @param criteria
	 */
	public void addAssociations(Criteria criteria) {
	}

	public void updateAssociations() {
	}

	/**
	 * This method should be overridden by classes that need to update composed
	 * associations once this
	 */
	public void updateComposedAssociations() {
	}

	@SuppressWarnings("unchecked")
	public <S> List<S> executeQuery(String queryString, Object... params) {
		Query query = entityManager.createQuery(queryString);
		setQueryParams(query, params);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <S> S executeSingleResultQuery(String queryString, Object... params) {
		Query query = entityManager.createQuery(queryString);
		setQueryParams(query, params);
		return (S) executeSingleResultQuery(query);
	}

	private <S> S executeSingleResultQuery(Query query) {
		try {
			return (S) query.getSingleResult();
		} catch (NoResultException nre) {
			log.info("No " + "record " + " found !");
			return null;
		}
	}

	@End
	public String returnToListingView() {
		String retVal = Redirect.instance().getViewId() == null ? StringUtils
				.uncapitalize(getClassName()) : Redirect.instance().getViewId();
		return retVal;
	}

	
	/**
	 * To create a full text index for the given entity
	 * 
	 * @return
	 */
	public String reIndex() {
		final List<T> entries = entityManager.createQuery(
				"select d from " + getClassName(getInstance()) + "  d")
				.getResultList();
		for (T t : entries)
			entityManager.index(t);
		return null;
	}

	@SuppressWarnings("unchecked")
	public <S> List<S> executeNamedQuery(String queryString, Object... params) {
		Query query = entityManager.createNamedQuery(queryString);
		setQueryParams(query, params);
		// setEntityList(query.getResultList());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <S> S executeSingleResultNamedQuery(String queryString,
			Object... params) {
		Query query = entityManager.createNamedQuery(queryString);
		setQueryParams(query, params);
		return (S) executeSingleResultQuery(query);
	}

	@SuppressWarnings("unchecked")
	public <S> S executeSingleResultNativeQuery(String queryString,
			Object... params) {
		Query query = entityManager.createNativeQuery(queryString);
		setQueryParams(query, params);
		return (S) query.getSingleResult();

	}

	@SuppressWarnings("unchecked")
	public <S> List<S> executeNativeQuery(String queryString, Object... params) {
		Query query = entityManager.createNativeQuery(queryString);
		setQueryParams(query, params);
		return query.getResultList();
	}

	private void setQueryParams(Query query, Object... params) {
		int counter = 1;
		for (Object param : params) {
			query.setParameter(counter++, param);
		}
	}

	@End
	public void reset() {
	}

	@Destroy
	public void destroy() {

	}

	protected String getClassName(T t) {
		String name = t.getClass().getSimpleName();
		if (name.indexOf("$$") > 0)
			name = name.substring(0, name.indexOf("$$"));
		return name;
	}

	protected String getClassName() {
		return getClassName(getInstance());
	}

	// //////////////// Comments
	// /////////////////////////////////////////////////////////

	public void saveComment() {
		EntityComment currentComment = new EntityComment();
		currentComment.setText(currentCommentText);
		currentComment.setEntityId(getInstance().getId());
		currentComment.setEntityName(getClassName(getInstance()));
		getEntityManager().persist(currentComment);
		currentCommentText = new String();
		events.raiseTransactionSuccessEvent("entityCommentsUpdated",
				getClassName(getInstance()));
	}

	public List<EntityComment> getComments() {
		// if (comments == null || comments.isEmpty()) {
		loadComments();
		// }
		return comments;
	}

	@Observer("entityCommentsUpdated")
	public void loadComments() {
		comments = executeNamedQuery("commentsForRecord",
				getInstance().getId(), getClassName(getInstance()));
	}

	public FullTextEntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(FullTextEntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// /////////////////// Delete Modal Dialog
	// ////////////////////////////////////////

	private boolean deleteDialogRendered = false;

	public boolean isDeleteDialogRendered() {
		return deleteDialogRendered;
	}

	public void setDeleteDialogRendered(boolean deleteDialogRendered) {
		this.deleteDialogRendered = deleteDialogRendered;
	}

	protected void downloadAttachment(FileAttachment fileAttachment) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		response.setContentType(fileAttachment.getContentType());

		response.addHeader("Content-disposition", "attachment; filename=\""
				+ fileAttachment.getName() + "\"");

		try {
			OutputStream os = response.getOutputStream();
			os.write(fileAttachment.getData());
			os.flush();
			os.close();
			context.responseComplete();
		} catch (Exception e) {
			log.error("\nFailure : " + e.toString() + "\n");
		}
	}

	public void sendMail(String template) {
		try {
			renderer.render(template);
			// facesMessages.add("Email sent successfully");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("Email sending failed: {0}", e.getMessage());
		}
	}

	protected boolean isDifferentFromCurrent(Long id) {
		if (id == null || getInstance() == null
				|| getInstance().getId() == null)
			return true;
		return id != getInstance().getId().longValue();
	}

	protected boolean isNew() {
		boolean isNew = getInstance().getId() == null;
		return isNew;
		
	}

	protected boolean isPostBack() {
		ResponseStateManager rtm = FacesContext.getCurrentInstance()
				.getRenderKit().getResponseStateManager();
		return rtm.isPostback(FacesContext.getCurrentInstance());
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
