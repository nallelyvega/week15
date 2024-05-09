package pet.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j

public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Create pet store: {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	@PutMapping("/{storeId}")
	public PetStoreData updatePetStore(@PathVariable Long storeId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setStoreId(storeId);

		System.out.println("Updating pet store with ID: " + storeId);

		return petStoreService.savePetStore(petStoreData);
	}

	@PostMapping("/{storeId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long storeId,
			@RequestBody PetStoreEmployee employeeData) {
		log.info("Adding employee to pet store with ID {}:{}", storeId, employeeData);
		return petStoreService.saveEmployee(storeId, employeeData);
	}

	@PostMapping("/{storeId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomerToPetStore(@PathVariable Long storeId,
			@RequestBody PetStoreCustomer customerData) {
		log.info("Adding customer to pet store with ID {}:{}", storeId, customerData);
		return petStoreService.saveCustomer(storeId, customerData);
	}

	@GetMapping
	public List<PetStoreData> getAllPetStores() {
		log.info("Retrieving all pet stores");
		return petStoreService.retrieveAllPetStores();
	}

	@GetMapping("/{storeId}")
	public PetStoreData getPetStoreById(@PathVariable Long storeId) {
		log.info("Retrieving pet store with ID {}", storeId);
		return petStoreService.retrievePetStoreById(storeId);
	}

	@DeleteMapping("/{storeId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long storeId) {
		log.info("Deleting pet store with ID {}", storeId);
		petStoreService.deletePetStoreByID(storeId);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Pet store with ID " + storeId + " deleted successfully");
		return response;
	}
}
