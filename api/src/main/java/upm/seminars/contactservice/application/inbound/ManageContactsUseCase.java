package upm.seminars.contactservice.application.inbound;

import upm.seminars.contactservice.domain.model.Contact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Inbound port (use case) for managing contacts in the system. Defines the operations that the application layer
 * exposes to external adapters.
 */
public interface ManageContactsUseCase {

    /**
     * Creates a new contact.
     *
     * @param contact
     *            the contact data to be created
     *
     * @return the created contact with assigned identifier
     */
    Contact createContact(Contact contact);

    /**
     * Retrieves all available contacts.
     *
     * @return a list of all contacts
     */
    List<Contact> getAllContacts();

    /**
     * Retrieves a contact by its unique identifier.
     *
     * @param id
     *            the UUID of the contact
     *
     * @return an Optional containing the contact if found, or empty otherwise
     */
    Optional<Contact> getContactById(UUID id);

    /**
     * Updates an existing contact.
     *
     * @param id
     *            the UUID of the contact to update
     * @param contact
     *            the updated contact data
     *
     * @return the updated contact
     */
    Contact updateContact(UUID id, Contact contact);

    /**
     * Deletes a contact by its unique identifier.
     *
     * @param id
     *            the UUID of the contact to delete
     */
    void deleteContact(UUID id);
}