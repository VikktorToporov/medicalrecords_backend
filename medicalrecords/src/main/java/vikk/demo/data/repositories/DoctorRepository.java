package vikk.demo.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vikk.demo.data.entities.Doctor;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findDoctorById(UUID id);

    Optional<Doctor> findDoctorByUserId(UUID id);

    void deleteById(UUID id);
}
