package org.witchcraft.action.test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.security.Identity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.witchcraft.seam.action.BaseAction;
import com.oreon.kg.domain.users.Role;
import com.oreon.kg.domain.users.User;
import org.witchcraft.users.action.UserAction;

public class AuthenticatorTest extends BaseTest<User> {

	UserAction action = new UserAction();

	@BeforeClass
	public void init() {
		super.init();
	}

	@Override
	public BaseAction<User> getAction() {
		return action;
	}

	@Test(dependsOnMethods = { "testRegisterAction" })
	public void validateAuthenticationBadUser() throws Exception {
		new ComponentTest() {

			protected void testComponents() throws Exception {
				Identity.instance().getCredentials().setUsername("admin");
				Identity.instance().getCredentials().setPassword("admin");
				Identity.instance().addRole("generalUser");
				
				  assert invokeMethod( "#{authenticator.authenticate}")
				  .equals(true);
				 
			}

		}.run();
	}

	@Test
	public void testRegisterAction() throws Exception {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();

		Query query = em
				.createQuery("Select u From User u where u.userName = ?1 ");
		query.setParameter(1, "admin");
		if (!query.getResultList().isEmpty())
			return;

		new ComponentTest() {

			protected void testComponents() throws Exception {
				Identity.instance().getCredentials().setUsername("quazi");
				Identity.instance().getCredentials().setPassword("quazi");

				createUserAndRole( "admin", "admin", "admin");
				createUserAndRole( "rayad", "rayad", "support");
				createUserAndRole( "quazi", "quazi", "generalUser");
			}
			
		}.run();

		// em.getTransaction().commit();
		em.close();
	}
	
	private User createUserAndRole(String username, String password, String role) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		
		
		User admin = new User();
		admin.setUserName(username);
		admin.setPassword(password);
		
		Role adminRole = new Role();
		adminRole.setName(role);
		admin.getRoles().add(adminRole);
		admin.setEmail(username + "@gmail.com");
		admin.setEnabled(true);
		
		//setValue("#{userAction.instance}", admin);
		//invokeMethod("#{userAction.save}");
		em.persist(admin);
		em.getTransaction().commit();
		return admin;
	}
	


    

}