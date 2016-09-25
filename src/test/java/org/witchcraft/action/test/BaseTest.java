package  org.witchcraft.action.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.seam.annotations.In;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.security.Identity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.witchcraft.base.entity.BusinessEntity;
import org.witchcraft.seam.action.BaseAction;

public abstract class BaseTest<T extends BusinessEntity> extends SeamTest{
	
	private static final String NOMBRE_PERSISTENCE_UNIT = "appEntityManager";
	private EntityManagerFactory emf;
	
	@In(create=true)
	protected EntityManager em;

	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	
	public void init() {
		emf = Persistence.createEntityManagerFactory(NOMBRE_PERSISTENCE_UNIT);
		em = getEntityManagerFactory().createEntityManager();
		//em.getTransaction().begin();
		//getAction().setEntityManager(Search.getFullTextEntityManager(em));
	}
	
	@BeforeMethod
	public void beginTx() {
		try {
			em.getTransaction().begin();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		super.begin();
	}

	@AfterMethod
	public void endTx() {
		try {
			if(em.getTransaction() != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	abstract public BaseAction<T> getAction();

	@AfterClass
	public void destroy() {
	//	em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
	public void login(final String username, final String pwd){
		try {
			new ComponentTest() {
				protected void testComponents() throws Exception {
					Identity.instance().getCredentials().setUsername(username);
					Identity.instance().getCredentials().setPassword(pwd);
					
					  assert invokeMethod( "#{authenticator.authenticate}")
					  .equals(true);
				}

			}.run();
		} catch (Exception e) {
	
		}
	}

}
