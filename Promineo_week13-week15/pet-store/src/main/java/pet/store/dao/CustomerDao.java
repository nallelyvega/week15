package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import pet.store.entity.Customer;

@Service
public interface CustomerDao extends JpaRepository<Customer, Long> {

}
