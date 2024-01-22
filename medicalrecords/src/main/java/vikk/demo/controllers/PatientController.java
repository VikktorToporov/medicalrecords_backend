package vikk.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vikk.demo.data.dataTransferObjects.DoctorList;
import vikk.demo.data.dataTransferObjects.PatientEdit;
import vikk.demo.data.dataTransferObjects.PatientList;
import vikk.demo.data.dataTransferObjects.PatientReportDiagnosisCount;
import vikk.demo.services.PatientService;
import vikk.demo.services.SessionService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/Patients")
public class PatientController {
    private final PatientService patientService;
    private final SessionService sessionService;
    
    @PostMapping
    public ResponseEntity<UUID> insertPatient(@RequestBody PatientEdit Patient, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(patientService.insertPatient(Patient), HttpStatus.CREATED);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @PutMapping
    public ResponseEntity<Boolean> updatePatient(@RequestBody PatientEdit Patient, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            patientService.updatePatient(Patient);

            return new ResponseEntity(true, HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetAll")
    public ResponseEntity<List<PatientList>> getPatients(@RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(patientService.getPatients(), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/isHealthInsurancePaid")
    public ResponseEntity<?> isHealthInsurancePaid(@RequestParam UUID patientId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            return new ResponseEntity(patientService.isHealthInsurancePaid(patientId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetById")
    public ResponseEntity<PatientList> getPatientById(@RequestParam UUID patientId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            return new ResponseEntity(patientService.getPatientById(patientId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);

    }

    @GetMapping(value="/GetByDoctorId")
    public ResponseEntity<PatientList> getPatientsByDoctorId(@RequestParam UUID doctorId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            return new ResponseEntity(patientService.getPatientsByDoctorId(doctorId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);

    }

    @GetMapping(value="/GetByUserId")
    public ResponseEntity<PatientList> getPatientByUserId(@RequestParam UUID userId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            return new ResponseEntity(patientService.getPatientByUserId(userId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }


    @GetMapping(value="/GetReportPatientsByDiagnosis")
    public ResponseEntity<Set<PatientReportDiagnosisCount>> getReportPatientsByDiagnosis(@RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(patientService.getReportPatientsByDiagnosis(), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePatient(@RequestParam UUID patientId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            patientService.deletePatient(patientId);
            
            return new ResponseEntity(true, HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }
}
