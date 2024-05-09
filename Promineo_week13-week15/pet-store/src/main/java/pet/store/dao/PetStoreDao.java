package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import pet.store.entity.PetStore;

@Service
public interface PetStoreDao extends JpaRepository<PetStore, Long> {
}