package upm.seminars.contactservice.application.services;

import org.springframework.stereotype.Service;
import upm.seminars.contactservice.application.inbound.ManageContactsUseCase;
import upm.seminars.contactservice.domain.model.Contact;
import upm.seminars.contactservice.domain.outbound.ContactRepositoryPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Core application service implementing the contact management use cases. Orchestrates domain logic and delegates data
 * persistence to outbound ports.
 */
@Service
public class ManageContactsService implements ManageContactsUseCase {

    private final ContactRepositoryPort contactRepositoryPort;

    public ManageContactsService(ContactRepositoryPort contactRepositoryPort) {
        this.contactRepositoryPort = contactRepositoryPort;
    }

    @Override
    public Contact createContact(Contact contact) {
        // Aquí irían validaciones de negocio previas a la persistencia, si las hubiera.
        // Como el ID lo generará la base de datos o el adaptador, lo pasamos tal cual.
        return contactRepositoryPort.save(contact);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepositoryPort.findAll();
    }

    @Override
    public Optional<Contact> getContactById(UUID id) {
        return contactRepositoryPort.findById(id);
    }

    @Override
    public Contact updateContact(UUID id, Contact contact) {
        // Verificamos si existe antes de actualizar
        return contactRepositoryPort.findById(id).map(existingContact -> {
            Contact updatedContact = Contact.builder().id(existingContact.id()) // Mantenemos el ID original
                    .firstName(contact.firstName()).lastName(contact.lastName()).phoneNumbers(contact.phoneNumbers())
                    .emails(contact.emails()).address(contact.address()).birthDate(contact.birthDate()).build();
            return contactRepositoryPort.save(updatedContact);
        }).orElseThrow(() -> new IllegalArgumentException("Contact not found with ID: " + id));
        // Nota: Idealmente lanzaríamos una excepción de dominio personalizada aquí.
    }

    @Override
    public void deleteContact(UUID id) {
        contactRepositoryPort.deleteById(id);
    }
}