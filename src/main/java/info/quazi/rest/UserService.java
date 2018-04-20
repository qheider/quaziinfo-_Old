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

import info.quazi.rest.entity.UserEntity;


@Path("/userService")
@Scope(ScopeType.APPLICATION)
@Name("userService")
public class UserService extends BaseAction<User> {
	@In(create=true)
	protected FullTextEntityManager entityManager;
	@In(create=true)
	protected UserAction userAction;
	
	@GET
	@Path("user/{param}")
	@Produces("application/json")
	public UserEntity getMsg(@PathParam("param") String msg) {
		
User usr= new User();
usr=userAction.findByUnqUserName(msg);
UserEntity usrE=new UserEntity();
usrE.setUserName(usr.getDisplayName());
usrE.setPassword(usr.getPassword());
usrE.setEmail(usr.getEmail());


return usrE;		


	}

}