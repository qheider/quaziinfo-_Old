package info.quazi.rest.entity;

import org.jboss.seam.annotations.Name;

import lombok.Data;

@Name("userEntity")
@Data
public class UserEntity {
	protected String userName;
	protected String password;
	protected String email;

}
