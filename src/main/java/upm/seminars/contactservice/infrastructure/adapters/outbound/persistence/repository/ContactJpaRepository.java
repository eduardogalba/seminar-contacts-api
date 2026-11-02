package upm.seminars.contactservice.infrastructure.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upm.seminars.contactservice.infrastructure.adapters.outbound.persistence.entities.ContactEntity;

import java.util.UUID;

/**
 * Spring Data JPA repository for ContactEntity operations.
 */
@Repository
public interface ContactJpaRepository extends JpaRepository<ContactEntity, UUID> {
}