package info.quazi.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.witchcraft.seam.action.BaseAction;

import com.nas.recovery.web.action.domain.EmployeeAction;
import com.nas.recovery.web.action.users.UserAction;
import com.oreon.kg.domain.users.User;


@Path("/hello")
@Scope(ScopeType.APPLICATION)
@Name("quaziHello")
public class HelloWorldService extends BaseAction<User> {
	@In(create=true)
	protected FullTextEntityManager entityManager;
	@In(create=true)
	protected UserAction userAction;
	
	@GET
	@Path("/{param}")
	@Produces("text/plain")
	public Response getMsg(@PathParam("param") String msg) {
		
User usr= new User();
usr=userAction.findByUnqUserName(msg);

		
		String output = "Jersey say : " + msg;
		String output1 = "Jersey say : " + usr.getUserName();
		return Response.status(200).entity(output).build();

	}

}