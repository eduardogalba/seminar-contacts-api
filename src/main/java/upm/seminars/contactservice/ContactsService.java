package upm.seminars.contactservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Contacts Service API application. Bootstraps the Spring Boot context.
 */
@SpringBootApplication
public class ContactsService {

    /**
     * Application main method.
     *
     * @param args
     *            command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ContactsService.class, args);
    }
}