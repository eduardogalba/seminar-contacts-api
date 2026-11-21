package upm.seminars.contactservice.infrastructure.adapters.inbound.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upm.seminars.contactservice.application.inbound.ManageContactsUseCase;
import upm.seminars.contactservice.infrastructure.adapters.inbound.rest.api.ContactsServiceApi;
import upm.seminars.contactservice.infrastructure.adapters.inbound.rest.dto.ContactRequestDto;
import upm.seminars.contactservice.infrastructure.adapters.inbound.rest.dto.ContactResponseDto;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller acting as the inbound adapter for contact operations. Implements the OpenAPI generated contract and
 * delegates business logic to the application use cases.
 */
@RestController
public class ContactsRestController implements ContactsServiceApi {

    private final ManageContactsUseCase manageContactsUseCase;
    private final ContactMapper contactMapper;

    public ContactsRestController(ManageContactsUseCase manageContactsUseCase, ContactMapper contactMapper) {
        this.manageContactsUseCase = manageContactsUseCase;
        this.contactMapper = contactMapper;
    }

    @Override
    public ResponseEntity<ContactResponseDto> createContact(ContactRequestDto contactRequestDto) {
        var contact = contactMapper.toDomain(contactRequestDto);
        var createdContact = manageContactsUseCase.createContact(contact);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdContact.id()).toUri();
        return ResponseEntity.created(location).body(contactMapper.toDto(createdContact));
    }

    @Override
    public ResponseEntity<List<ContactResponseDto>> getAllContacts() {
        var contacts = manageContactsUseCase.getAllContacts().stream().map(contactMapper::toDto).toList();
        return ResponseEntity.ok(contacts);
    }

    @Override
    public ResponseEntity<ContactResponseDto> getContactById(UUID id) {
        return manageContactsUseCase.getContactById(id).map(contactMapper::toDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ContactResponseDto> updateContact(UUID id, ContactRequestDto contactRequestDto) {
        var contact = contactMapper.toDomain(contactRequestDto);
        var updatedContact = manageContactsUseCase.updateContact(id, contact);
        return ResponseEntity.ok(contactMapper.toDto(updatedContact));
    }

    @Override
    public ResponseEntity<Void> deleteContact(UUID id) {
        manageContactsUseCase.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}