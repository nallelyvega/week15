package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Employee;

@Data
@NoArgsConstructor
public class PetStoreEmployee {
	private Long employeeId;
	private String employeeName;
	private String employeeRole;
	private String employeePhone;

	public PetStoreEmployee(Employee employee) {
		employeeId = employee.getEmployeeId();
		employeeName = employee.getEmployeeName();
		employeeRole = employee.getEmployeeRole();
		employeePhone = employee.getEmployeePhone();
	}
}
