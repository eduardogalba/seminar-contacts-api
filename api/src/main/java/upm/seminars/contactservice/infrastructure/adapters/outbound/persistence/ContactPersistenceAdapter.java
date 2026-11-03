package upm.seminars.contactservice.infrastructure.adapters.outbound.persistence;

import org.springframework.stereotype.Component;
import upm.seminars.contactservice.domain.model.Contact;
import upm.seminars.contactservice.domain.outbound.ContactRepositoryPort;
import upm.seminars.contactservice.infrastructure.adapters.outbound.persistence.repository.ContactJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound adapter implementing the ContactRepositoryPort. Translates domain persistence requests into JPA operations.
 */
@Component
public class ContactPersistenceAdapter implements ContactRepositoryPort {

    private final ContactJpaRepository contactJpaRepository;
    private final ContactEntityMapper entityMapper;

    public ContactPersistenceAdapter(ContactJpaRepository contactJpaRepository, ContactEntityMapper entityMapper) {
        this.contactJpaRepository = contactJpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Contact save(Contact contact) {
        var entity = entityMapper.toEntity(contact);
        var savedEntity = contactJpaRepository.save(entity);
        return entityMapper.toDomain(savedEntity);
    }

    @Override
    public List<Contact> findAll() {
        return contactJpaRepository.findAll().stream().map(entityMapper::toDomain).toList();
    }

    @Override
    public Optional<Contact> findById(UUID id) {
        return contactJpaRepository.findById(id).map(entityMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        contactJpaRepository.deleteById(id);
    }
}