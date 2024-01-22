package vikk.demo.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.entities.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findPatientById(UUID id);

    Optional<Patient> findPatientByUserId(UUID id);

    List<Patient> findPatientsByDoctorId(UUID id);

    long countByDoctor(Doctor doctor);

    @Query("SELECT v.diagnosis, COUNT(p) FROM Patient p JOIN p.visits v GROUP BY v.diagnosis")
    List<Object[]> countPatientsByDiagnosis();

    void deleteById(UUID id);
}
