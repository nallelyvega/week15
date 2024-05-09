package pet.store.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

	@Autowired
	private PetStoreDao petStoreDao;

//lists all pet stores
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		return petStores.stream().map(this::convertToPetStoreData).collect(Collectors.toList());
	}

	private PetStoreData convertToPetStoreData(PetStore petStore) {
		PetStoreData petStoreData = new PetStoreData();
		petStoreData.setStoreId(petStore.getStoreId());
		petStoreData.setStoreName(petStore.getStoreName());
		petStoreData.setStoreAddress(petStore.getStoreAddress());
		petStoreData.setStoreCity(petStore.getStoreCity());
		petStoreData.setStorePhone(petStore.getStorePhone());
		petStoreData.setStoreState(petStore.getStoreState());
		petStoreData.setStoreZip(petStore.getStoreZip());
		return petStoreData;
	}

	public PetStoreData savePetStore(PetStoreData petStoreData) {
		PetStore petStore = findOrCreatePetStore(petStoreData.getStoreId());

		copyPetStoreFields(petStore, petStoreData);

		PetStore savedPetStore = petStoreDao.save(petStore);
		return new PetStoreData(savedPetStore);
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		if (petStoreId == null) {
			return new PetStore();
		} else {
			return findPetStoreById(petStoreId);

		}
	}

	private PetStore findPetStoreById(Long storeId) {
		return petStoreDao.findById(storeId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID " + storeId + " not found"));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setStoreId(petStoreData.getStoreId());
		petStore.setStoreName(petStoreData.getStoreName());
		petStore.setStoreAddress(petStoreData.getStoreAddress());
		petStore.setStoreCity(petStoreData.getStoreCity());
		petStore.setStorePhone(petStoreData.getStorePhone());
		petStore.setStoreState(petStoreData.getStoreState());
		petStore.setStoreZip(petStoreData.getStoreZip());
	}

	@Autowired
	EmployeeDao employeeDao;

	@Transactional(readOnly = false)

	public Employee findEmployeeById(Long StoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(NoSuchElementException::new);
		if (!employee.getPetStore().getStoreId().equals(StoreId)) {
			throw new IllegalArgumentException(
					"Employee with ID " + employeeId + " does not belong to the pet store with ID " + StoreId);
		}
		return employee;
	}

	public Employee findOrCreateEmployee(Long StoreId, Long employeeId) {
		if (employeeId == null) {
			return new Employee();
		} else {
			return findEmployeeById(StoreId, employeeId);
		}
	}

	public void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeName(petStoreEmployee.getEmployeeName());
		employee.setEmployeeRole(petStoreEmployee.getEmployeeRole());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
	}

	public PetStoreEmployee saveEmployee(Long StoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findOrCreatePetStore(StoreId);
		Employee employee = findOrCreateEmployee(StoreId, petStoreEmployee.getEmployeeId());
		copyEmployeeFields(employee, petStoreEmployee);

		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);

		Employee savedEmployee = employeeDao.save(employee);

		return convertToPetStoreEmployee(savedEmployee);
	}

	public PetStoreEmployee convertToPetStoreEmployee(Employee employee) {
		PetStoreEmployee petStoreEmployee = new PetStoreEmployee();
		petStoreEmployee.setEmployeeId(employee.getEmployeeId());
		petStoreEmployee.setEmployeeName(employee.getEmployeeName());
		petStoreEmployee.setEmployeeRole(employee.getEmployeeRole());
		petStoreEmployee.setEmployeePhone(employee.getEmployeePhone());
		return petStoreEmployee;
	}

	@Autowired
	CustomerDao customerDao;

	@Transactional(readOnly = false)

	public Customer findCustomerById(Long StoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(NoSuchElementException::new);

		boolean found = false;
		for (PetStore petStore : customer.getPetStores()) {
			if (petStore.getStoreId() == StoreId) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException(
					"Customer with Id = " + customerId + " not a member of this petStore Id = " + StoreId);
		}
		return customer;

	}

	public Customer findOrCreateCustomer(Long StoreId, Long customerId) {
		if (customerId == null) {
			return new Customer();
		} else {
			return findCustomerById(StoreId, customerId);
		}
	}

	public void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerName(petStoreCustomer.getCustomerName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		customer.setCustomerAddress(petStoreCustomer.getCustomerAddress());
	}

	public PetStoreCustomer saveCustomer(Long storeId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findOrCreatePetStore(storeId);
		Customer customer = findOrCreateCustomer(storeId, petStoreCustomer.getCustomerId());
		copyCustomerFields(customer, petStoreCustomer);
		petStore.getCustomers().add(customer);
		customer.getPetStores().add(petStore);

		Customer savedCustomer = customerDao.save(customer);

		return convertToPetStoreCustomer(savedCustomer);

		// return new PetStoreCustomer(savedCustomer);
	}

	public PetStoreCustomer convertToPetStoreCustomer(Customer customer) {
		PetStoreCustomer petStoreCustomer = new PetStoreCustomer();
		petStoreCustomer.setCustomerId(customer.getCustomerId());
		petStoreCustomer.setCustomerName(customer.getCustomerName());
		petStoreCustomer.setCustomerEmail(customer.getCustomerEmail());
		petStoreCustomer.setCustomerAddress(customer.getCustomerAddress());
		return petStoreCustomer;
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long storeId) {

		return new PetStoreData(findPetStoreById(storeId));
	}

	@Transactional
	public void deletePetStoreByID(Long storeId) {
		PetStore petStore = findPetStoreById(storeId);
		petStoreDao.delete(petStore);
	}
}
