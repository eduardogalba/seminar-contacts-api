package upm.seminars.contactservice.infrastructure.adapters.outbound.persistence;

import org.springframework.stereotype.Component;
import upm.seminars.contactservice.domain.model.Contact;
import upm.seminars.contactservice.infrastructure.adapters.outbound.persistence.entities.ContactEntity;

/**
 * Mapper component for transforming between Domain models and JPA Entities.
 */
@Component
public class ContactEntityMapper {

    /**
     * Converts a JPA entity to a core domain Contact record.
     *
     * @param entity
     *            the JPA entity
     *
     * @return the domain Contact model
     */
    public Contact toDomain(ContactEntity entity) {
        if (entity == null) {
            return null;
        }
        return Contact.builder().id(entity.getId()).firstName(entity.getFirstName()).lastName(entity.getLastName())
                .phoneNumbers(entity.getPhoneNumbers()).emails(entity.getEmails()).address(entity.getAddress())
                .birthDate(entity.getBirthDate()).build();
    }

    /**
     * Converts a core domain Contact record to a JPA entity.
     *
     * @param contact
     *            the domain Contact model
     *
     * @return the JPA entity
     */
    public ContactEntity toEntity(Contact contact) {
        if (contact == null) {
            return null;
        }
        return ContactEntity.builder().id(contact.id()).firstName(contact.firstName()).lastName(contact.lastName())
                .phoneNumbers(contact.phoneNumbers()).emails(contact.emails()).address(contact.address())
                .birthDate(contact.birthDate()).build();
    }
}