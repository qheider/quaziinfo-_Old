package com.nas.recovery.websvc.domain;

import javax.jws.WebService;
import com.oreon.kg.domain.Employee;
import java.util.List;

@WebService
public interface EmployeeWebService {

	public Employee loadById(Long id);

	public List<Employee> findByExample(Employee exampleEmployee);

	public void save(Employee employee);

	public String register();

	public String login();

	public com.oreon.kg.domain.Employee findByPhone(String phone);

	public String retrieveCredentials();

}
