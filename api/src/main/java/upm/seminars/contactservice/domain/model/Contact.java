package upm.seminars.contactservice.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Core domain entity representing a Contact in the system.
 */
@Builder
public record Contact(UUID id, String firstName, String lastName, List<String> phoneNumbers, List<String> emails,
        String address, LocalDate birthDate) {
}