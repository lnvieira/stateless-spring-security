package stateless.spring.security.service;

import java.util.ArrayList;
import java.util.List;

import stateless.spring.security.domain.Credentials;
import stateless.spring.security.domain.Employee;
import stateless.spring.security.dto.entity.employee.*;
import stateless.spring.security.repository.CredentialsRepository;
import stateless.spring.security.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stateless.spring.security.service.crypto.PasswordResolver;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Autowired
	private PasswordResolver passwordResolver;

	public ProfileDto fetchProfile(Employee employee){
		return new ProfileDto(employee);
	}

	public ProfileDto updateProfile(Employee employee, ProfileDto profileDto){
		employee.setCompetency(profileDto.getCompetency());
		employee.setName(profileDto.getName());
		employeeRepository.save(employee);
		return new ProfileDto(employee);
	}

	public EmployeeCountDto count() {
		return new EmployeeCountDto(employeeRepository.count());
	}
	
	public EmployeeList findAllEmployees() {
		Iterable<Employee> employees = employeeRepository.findAll();
		
		if(employees == null){
			return new EmployeeList();
		}
		
		List<EmployeeDto> dtos = new ArrayList<EmployeeDto>();
		for (Employee employee : employees) {
			dtos.add(new EmployeeDto(employee));
		}
		return new EmployeeList(dtos);
	}

	public EmployeeDto findById(long id) {
		Employee employee = employeeRepository.findOne(id);
		EmployeeDto employeeDto = null;
		if(employee != null){
			employeeDto = new EmployeeDto(employee);
		}
		return employeeDto;
	}

	public Employee saveEmployee(CredentialsDto credentialsDto) {
		Credentials credentials = credentialsDto.buildEmployee();
		passwordResolver.encode(credentials);
		credentialsRepository.save(credentials);
		return credentials.getEmployee();
	}

	public void updateEmployee(EmployeeDto current, EmployeeDto employee) {
		current.setName(employee.getName());
        current.setCompetency(employee.getCompetency());
        employeeRepository.save(current.buildEmployee());
	}

	public EmployeeDto enableOrDisableEmployee(EmployeeDto employeeDto, boolean isEnable) {
		employeeDto.setEnabled(isEnable);
		employeeRepository.save(employeeDto.buildEmployee());
		return employeeDto;
	}

	public void deleteUserById(long id) {
		employeeRepository.delete(id);
	}

	public void deleteAllEmployees() {
		employeeRepository.deleteAll();
	}

}
