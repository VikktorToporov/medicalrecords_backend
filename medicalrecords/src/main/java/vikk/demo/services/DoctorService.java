package vikk.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vikk.demo.data.dataTransferObjects.DoctorEdit;
import vikk.demo.data.dataTransferObjects.DoctorList;
import vikk.demo.data.dataTransferObjects.DoctorReportCount;
import vikk.demo.data.dataTransferObjects.GenericComboBox;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.repositories.DoctorRepository;
import vikk.demo.data.repositories.PatientRepository;
import vikk.demo.data.repositories.VisitRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public List<DoctorList> getDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertDbObjToDoctorList)
                .collect(Collectors.toList());
    }

    public DoctorList getDoctorById(UUID doctorId) {
        return doctorRepository.findDoctorById(doctorId)
                .stream()
                .map(this::convertDbObjToDoctorList)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Doctor " + doctorId + " not found."));
    }

    public DoctorList getDoctorByUserId(UUID userId) {
        return doctorRepository.findDoctorByUserId(userId)
                .stream()
                .map(this::convertDbObjToDoctorList)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Doctor not found."));
    }

    public List<DoctorReportCount> getReportPatientCount() {
        return doctorRepository.findAll()
                .stream()
                .map(x -> convertDbObjToReportCount(x, true))
                .collect(Collectors.toList());
    }

    public List<DoctorReportCount> getReportVisitCount() {
        return doctorRepository.findAll()
                .stream()
                .map(x -> convertDbObjToReportCount(x, false))
                .collect(Collectors.toList());
    }

    public UUID insertDoctor(Doctor doctor) {
        doctor.setPassword(BCrypt.hashpw(doctor.getPassword(), BCrypt.gensalt(12)));

        doctorRepository.save(doctor);

        return doctor.getId();
    }

    public void updateDoctor(DoctorEdit toEdit) {
        Doctor currentDoctorData = doctorRepository.findDoctorById(toEdit.getId())
                .orElseThrow(() -> new RuntimeException("Doctor " + toEdit.getId() + " not found."));

        if (toEdit.getPassword() != null) {
            toEdit.setPassword(BCrypt.hashpw(toEdit.getPassword(), BCrypt.gensalt(12)));
        } else {
            toEdit.setPassword(currentDoctorData.getPassword());
        }

        if (toEdit.getUserId() == null) {
            toEdit.setUserId(currentDoctorData.getUserId());
        }

        Doctor doctor = convertDoctorEditToDbObj(toEdit);

        doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(UUID doctorId) {
        doctorRepository.deleteById(doctorId);
    }


    private Doctor convertDoctorEditToDbObj(DoctorEdit model) {
        Doctor doctor = new Doctor();

        doctor.setId(model.getId());
        doctor.setEmail(model.getEmail());
        doctor.setPassword(model.getPassword());
        doctor.setSpecialty(model.getSpecialty());
        doctor.setGp(model.isGp());
        doctor.setFirstName(model.getFirstName());
        doctor.setMiddleName(model.getMiddleName());
        doctor.setLastName(model.getLastName());
        doctor.setUserId(model.getUserId());

        doctor.getPatients().addAll(doctorRepository.findDoctorById(model.getId())
                .orElseThrow(() -> new RuntimeException("Doctor " + model.getId() + " not found."))
                .getPatients());

        return doctor;
    }

    public DoctorList convertDbObjToDoctorList(Doctor doctor) {
        DoctorList doctorList = new DoctorList();

        doctorList.setId(doctor.getId());
        doctorList.setEmail(doctor.getEmail());
        doctorList.setFirstName(doctor.getFirstName());
        doctorList.setMiddleName(doctor.getMiddleName());
        doctorList.setLastName(doctor.getLastName());
        doctorList.setGp(doctor.isGp());
        doctorList.setSpecialty(doctor.getSpecialty());

        return doctorList;
    }

    private DoctorReportCount convertDbObjToReportCount(Doctor doctor, boolean forPatient) {
        if (doctor != null) {
            String name = doctor.getFirstName() + ' ' + doctor.getMiddleName() + ' ' + doctor.getLastName();
            GenericComboBox doctorGCB = new GenericComboBox(name, doctor.getId());
            Long count = generateCountProperty(doctor, forPatient);

            DoctorReportCount item = new DoctorReportCount(doctorGCB, count);

            return item;
        }

        return null;
    }

    private long generateCountProperty(Doctor doctor, boolean forPatient) {
        if (forPatient) {
            return patientRepository.countByDoctor(doctor);
        } else {
            return visitRepository.countByDoctor(doctor);
        }
    }
}
