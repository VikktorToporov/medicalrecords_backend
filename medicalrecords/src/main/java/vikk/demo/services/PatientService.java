package vikk.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vikk.demo.data.dataTransferObjects.*;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.entities.Patient;
import vikk.demo.data.repositories.DoctorRepository;
import vikk.demo.data.repositories.PatientRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public List<PatientList> getPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::convertDbObjToPatientList)
                .collect(Collectors.toList());
    }

    public PatientList getPatientById(UUID patientId) {
        return patientRepository.findPatientById(patientId)
                .stream()
                .map(this::convertDbObjToPatientList)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient " + patientId + " not found."));
    }

    public List<PatientList> getPatientsByDoctorId(UUID doctorId) {
        return patientRepository.findPatientsByDoctorId(doctorId)
                .stream()
                .map(this::convertDbObjToPatientList)
                .collect(Collectors.toList());
    }

    public PatientList getPatientByUserId(UUID userId) {
        return patientRepository.findPatientByUserId(userId)
                .stream()
                .map(this::convertDbObjToPatientList)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found."));
    }

    public boolean isHealthInsurancePaid(UUID patientId) {
        return patientRepository.findPatientById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient " + patientId + " not found."))
                .isHealthInsurancePaid();
    }

    public Set<PatientReportDiagnosisCount> getReportPatientsByDiagnosis() {
        List<Object[]> diagnosisCounts = patientRepository.countPatientsByDiagnosis();
        Set<PatientReportDiagnosisCount> result = new HashSet<PatientReportDiagnosisCount>();

        for (Object[] row : diagnosisCounts) {
            String diagnosis = (String) row[0];
            Long patientCount = (Long) row[1];

            PatientReportDiagnosisCount item = new PatientReportDiagnosisCount(diagnosis, patientCount);
            result.add(item);
        }

        return result;
    }

    public UUID insertPatient(PatientEdit toInsert) {
        toInsert.setPassword(BCrypt.hashpw(toInsert.getPassword(), BCrypt.gensalt(12)));

        Patient patient = convertPatientEditToDbObj(toInsert);

        patientRepository.save(patient);

        return patient.getId();
    }

    public void updatePatient(PatientEdit toEdit) {
        Patient currentPatientData = patientRepository.findPatientById(toEdit.getId())
                .orElseThrow(() -> new RuntimeException("Patient " + toEdit.getId() + " not found."));

        if (toEdit.getPassword() != null) {
            toEdit.setPassword(BCrypt.hashpw(toEdit.getPassword(), BCrypt.gensalt(12)));
        } else {
            toEdit.setPassword(currentPatientData.getPassword());
        }

        if (toEdit.getUserId() == null) {
            toEdit.setUserId(currentPatientData.getUserId());
        }

        Patient patient = convertPatientEditToDbObj(toEdit);

        patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }

    private Patient convertPatientEditToDbObj(PatientEdit model) {
        Patient patient = new Patient();

        patient.setId(model.getId());
        patient.setEmail(model.getEmail());
        patient.setPassword(model.getPassword());
        patient.setSsn(model.getSsn());
        patient.setFirstName(model.getFirstName());
        patient.setMiddleName(model.getMiddleName());
        patient.setLastName(model.getLastName());
        patient.setHealthInsurancePaid(model.isHealthInsurancePaid());
        patient.setUserId(model.getUserId());

        Doctor doctor = doctorRepository.findDoctorById(model.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor " + model.getDoctorId() + " not found!"));

        if (doctor.isGp()) {
            patient.setDoctor(doctor);
        } else {
            throw new RuntimeException("The selected doctor is not a GP!");
        }

        return patient;
    }

    public PatientList convertDbObjToPatientList(Patient patient) {
        PatientList patientList = new PatientList();

        patientList.setId(patient.getId());
        patientList.setEmail(patient.getEmail());
        patientList.setFirstName(patient.getFirstName());
        patientList.setMiddleName(patient.getMiddleName());
        patientList.setLastName(patient.getLastName());
        patientList.setSsn(patient.getSsn());
        patientList.setHealthInsurancePaid(patient.isHealthInsurancePaid());

        GenericComboBox doctorGCB = generateDoctorGCB(patient.getDoctor());
        patientList.setDoctor(doctorGCB);

        return patientList;
    }

    public GenericComboBox generateDoctorGCB(Doctor doctor) {
        if (doctor != null) {
            String name = doctor.getFirstName() + ' ' + doctor.getMiddleName() + ' ' + doctor.getLastName();

            return new GenericComboBox(name, doctor.getId());
        }

        return null;
    }
}
