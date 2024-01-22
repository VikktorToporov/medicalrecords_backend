package vikk.demo.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.entities.Visit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {
    Visit findVisitById(UUID id);

    List<Visit> findVisitsByPatientId(UUID id);

    List<Visit> findVisitsByDoctorId(UUID id);

    long countByDoctor(Doctor doctor);

    @Query("DELETE FROM Visit v WHERE v.id = :visitId")
    void deleteById(UUID visitId);

    void delete(Visit visit);
}