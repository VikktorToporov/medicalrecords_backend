package vikk.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vikk.demo.data.dataTransferObjects.DoctorEdit;
import vikk.demo.data.dataTransferObjects.DoctorList;
import vikk.demo.data.dataTransferObjects.DoctorReportCount;
import vikk.demo.data.entities.Doctor;
import vikk.demo.services.DoctorService;
import vikk.demo.services.SessionService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/Doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<UUID> insertDoctor(@RequestBody Doctor doctor, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(doctorService.insertDoctor(doctor), HttpStatus.CREATED);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateDoctor(@RequestBody DoctorEdit doctor, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            doctorService.updateDoctor(doctor);
                    
            return new ResponseEntity(true, HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetAll")
    public ResponseEntity<List<DoctorList>> getDoctors(@RequestHeader HttpHeaders headers) {
//        TODO: add another endpoint for the doctor selector inside the signup
//        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(doctorService.getDoctors(), HttpStatus.OK);
//        }

//        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetById")
    public ResponseEntity<DoctorList> getDoctorById(@RequestParam UUID doctorId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(doctorService.getDoctorById(doctorId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetByUserId")
    public ResponseEntity<DoctorList> getDoctorByUserId(@RequestParam UUID userId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(doctorService.getDoctorByUserId(userId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetReportPatientCount")
    public ResponseEntity<List<DoctorReportCount>> getReportPatientCount(@RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(doctorService.getReportPatientCount(), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetReportVisitCount")
    public ResponseEntity<List<DoctorReportCount>> getReportVisitCount(@RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(doctorService.getReportVisitCount(), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDoctor(@RequestParam UUID doctorId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            doctorService.deleteDoctor(doctorId);
            
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }
}
