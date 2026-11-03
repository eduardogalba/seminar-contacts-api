package upm.seminars.contactservice.infrastructure.adapters.inbound.rest;

import org.springframework.stereotype.Component;
import upm.seminars.contactservice.domain.model.Contact;
import upm.seminars.contactservice.infrastructure.adapters.inbound.rest.dto.ContactRequestDto;
import upm.seminars.contactservice.infrastructure.adapters.inbound.rest.dto.ContactResponseDto;

/**
 * Mapper component for transforming between OpenAPI DTOs and Domain models.
 */
@Component
public class ContactMapper {

    /**
     * Converts a REST request DTO into a core domain Contact record.
     *
     * @param dto
     *            the inbound request DTO
     *
     * @return the domain Contact model
     */
    public Contact toDomain(ContactRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return Contact.builder().firstName(dto.getFirstName()).lastName(dto.getLastName())
                .phoneNumbers(dto.getPhoneNumbers()).emails(dto.getEmails()).address(dto.getAddress())
                .birthDate(dto.getBirthDate()).build();
    }

    /**
     * Converts a core domain Contact record into a REST response DTO.
     *
     * @param contact
     *            the domain Contact model
     *
     * @return the outbound response DTO
     */
    public ContactResponseDto toDto(Contact contact) {
        if (contact == null) {
            return null;
        }

        ContactResponseDto dto = new ContactResponseDto();
        dto.setFirstName(contact.firstName());
        dto.setLastName(contact.lastName());
        dto.setPhoneNumbers(contact.phoneNumbers());
        dto.setEmails(contact.emails());
        dto.setAddress(contact.address());
        dto.setBirthDate(contact.birthDate());
        dto.id(contact.id());

        return dto;
    }
}