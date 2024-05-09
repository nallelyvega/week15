package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;

@Data
@NoArgsConstructor
public class PetStoreCustomer {
	private Long customerId;
	private Long storeId;
	private String customerName;
	private String customerEmail;
	private String customerAddress;

	public PetStoreCustomer(Customer customer) {
		customerId = customer.getCustomerId();
		storeId = customer.getStoreId();
		customerName = customer.getCustomerName();
		customerEmail = customer.getCustomerEmail();
		customerAddress = customer.getCustomerAddress();
	}
}