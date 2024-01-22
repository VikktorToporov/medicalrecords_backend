package vikk.demo.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vikk.demo.data.entities.Session;

import java.util.UUID;

public interface SessionRepository  extends JpaRepository<Session, Long> {
    Session findSessionById(UUID id);

    void deleteById(UUID id);
}
