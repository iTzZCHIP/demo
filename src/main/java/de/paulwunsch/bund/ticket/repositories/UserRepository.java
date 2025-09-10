package de.paulwunsch.bund.ticket.repositories;

import de.paulwunsch.bund.ticket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
