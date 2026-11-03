package upm.seminars.contactservice.domain.outbound;

import upm.seminars.contactservice.domain.model.Contact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for Contact persistence operations. Implemented by infrastructure adapters (e.g., JPA, MongoDB,
 * InMemory).
 */
public interface ContactRepositoryPort {

    /**
     * Saves a contact to the underlying storage.
     *
     * @param contact
     *            the contact to save
     *
     * @return the saved contact
     */
    Contact save(Contact contact);

    /**
     * Retrieves all contacts from storage.
     *
     * @return a list of contacts
     */
    List<Contact> findAll();

    /**
     * Finds a contact by its unique UUID.
     *
     * @param id
     *            the UUID to search for
     *
     * @return an Optional with the contact if found
     */
    Optional<Contact> findById(UUID id);

    /**
     * Deletes a contact from storage by its UUID.
     *
     * @param id
     *            the UUID of the contact to delete
     */
    void deleteById(UUID id);
}