package vikk.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vikk.demo.data.dataTransferObjects.GenericComboBox;
import vikk.demo.data.dataTransferObjects.VisitEdit;
import vikk.demo.data.dataTransferObjects.VisitList;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.entities.Patient;
import vikk.demo.data.entities.Visit;
import vikk.demo.data.repositories.DoctorRepository;
import vikk.demo.data.repositories.PatientRepository;
import vikk.demo.data.repositories.VisitRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public List<VisitList> getVisits() {
        return visitRepository.findAll().stream()
                .map(this::convertDbObjToVisitList)
                .collect(Collectors.toList());
    }

    public VisitList getVisitById(UUID visitId) {
        Visit visit = visitRepository.findVisitById(visitId);
        VisitList visitList = convertDbObjToVisitList(visit);

        return visitList;
    }

    public List<VisitList> getVisitsByPatientId(UUID patientId) {
        List<Visit> query = visitRepository.findVisitsByPatientId(patientId);

        return query.stream()
                .map(this::convertDbObjToVisitList)
                .collect(Collectors.toList());
    }

    public List<VisitList> getVisitsByDoctorId(UUID doctorId) {
        List<Visit> query = visitRepository.findVisitsByDoctorId(doctorId);

        return query.stream()
                .map(this::convertDbObjToVisitList)
                .collect(Collectors.toList());
    }

    public UUID insertVisit(VisitEdit toEdit) {
        Visit visit = convertVisitEditToDbObj(toEdit);

        visitRepository.save(visit);

        return visit.getId();
    }

    public void updateVisit(VisitEdit toEdit) {
        Visit Visit = convertVisitEditToDbObj(toEdit);

        visitRepository.save(Visit);
    }

    @Transactional
    public void deleteVisit(UUID visitId) {
        Visit visit = visitRepository.findVisitById(visitId);

        visit.setDoctor(null);
        visit.setPatient(null);

        visitRepository.delete(visit);
    }

    private Visit convertVisitEditToDbObj(VisitEdit model) {
        Visit visit = new Visit();

        visit.setId(model.getId());
        visit.setDate(model.getDate());
        visit.setDiagnosis(model.getDiagnosis());
        visit.setSickLeaveDays(model.getSickLeaveDays());
        visit.setTreatment(model.getTreatment());
        visit.setSickLeaveStartDate(model.getSickLeaveStartDate());

        Doctor doctor = doctorRepository.findDoctorById(model.getDoctor()).orElseThrow(() -> new RuntimeException("Doctor " + model.getDoctor() + "not found!"));
        visit.setDoctor(doctor);

        Patient patient = patientRepository.findPatientById(model.getPatient()).orElseThrow(() -> new RuntimeException("Patient " + model.getPatient() + "not found!"));;
        visit.setPatient(patient);

        return visit;
    }

    public VisitList convertDbObjToVisitList(Visit visit) {
        VisitList visitList = new VisitList();

        visitList.setId(visit.getId());
        visitList.setDate(visit.getDate());
        visitList.setDiagnosis(visit.getDiagnosis());
        visitList.setTreatment(visit.getTreatment());
        visitList.setSickLeaveDays(visit.getSickLeaveDays());
        visitList.setSickLeaveStartDate(visit.getSickLeaveStartDate());

        GenericComboBox doctorGCB = generateDoctorGCB(visit.getDoctor());
        visitList.setDoctor(doctorGCB);

        GenericComboBox patientGCB = generatePatientGCB(visit.getPatient());
        visitList.setPatient(patientGCB);

        return visitList;
    }

    public GenericComboBox generateDoctorGCB(Doctor doctor) {
        if (doctor != null) {
            String name = doctor.getFirstName() + ' ' + doctor.getMiddleName() + ' ' + doctor.getLastName();

            return new GenericComboBox(name, doctor.getId());
        }

        return null;
    }

    public GenericComboBox generatePatientGCB(Patient patient) {
        if (patient != null) {
            String name = patient.getFirstName() + ' ' + patient.getMiddleName() + ' ' + patient.getLastName();

            return new GenericComboBox(name, patient.getId());
        }

        return null;
    }
}
