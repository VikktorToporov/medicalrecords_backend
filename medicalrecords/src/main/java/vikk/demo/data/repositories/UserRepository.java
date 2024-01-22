package vikk.demo.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vikk.demo.data.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
