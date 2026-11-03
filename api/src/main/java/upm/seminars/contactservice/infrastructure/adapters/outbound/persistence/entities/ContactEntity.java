package upm.seminars.contactservice.infrastructure.adapters.outbound.persistence.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity representing the contacts table in PostgreSQL.
 */
@Entity
@Table(name = "CONTACT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CONTACT_UUID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "CONTACT_FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "CONTACT_LAST_NAME")
    private String lastName;

    @ElementCollection
    @CollectionTable(name = "CONTACT_PHONE_NUMBER", joinColumns = @JoinColumn(name = "CONTACT_UUID"))
    @Column(name = "PHONE_NUMBER", nullable = false)
    @Builder.Default
    private List<String> phoneNumbers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "CONTACT_EMAIL", joinColumns = @JoinColumn(name = "CONTACT_UUID"))
    @Column(name = "EMAIL", nullable = false)
    @Builder.Default
    private List<String> emails = new ArrayList<>();

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;
}