package vikk.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vikk.demo.data.dataTransferObjects.VisitEdit;
import vikk.demo.data.dataTransferObjects.VisitList;
import vikk.demo.services.SessionService;
import vikk.demo.services.VisitService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/Visits")
public class VisitController {
    private final VisitService visitService;
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<UUID> insertVisit(@RequestBody VisitEdit visit, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(visitService.insertVisit(visit), HttpStatus.CREATED);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateVisit(@RequestBody VisitEdit visit, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            visitService.updateVisit(visit);

            return new ResponseEntity(true, HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetAll")
    public ResponseEntity<List<VisitList>> getVisits(@RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(visitService.getVisits(), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetById")
    public ResponseEntity<VisitList> getVisitById(@RequestParam UUID id, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            return new ResponseEntity(visitService.getVisitById(id), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetByPatientId")
    public ResponseEntity<List<VisitList>> getVisitsByPatientId(@RequestParam UUID patientId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, false)) {
            return new ResponseEntity(visitService.getVisitsByPatientId(patientId), HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/GetByDoctorId")
    public ResponseEntity<List<VisitList>> getVisitsByDoctorId(@RequestParam UUID doctorId, @RequestHeader HttpHeaders headers) {
       if (sessionService.checkPermissions(headers, true)) {
            return new ResponseEntity(visitService.getVisitsByDoctorId(doctorId), HttpStatus.OK);
       }

       return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteVisit(@RequestParam UUID visitId, @RequestHeader HttpHeaders headers) {
        if (sessionService.checkPermissions(headers, true)) {
            visitService.deleteVisit(visitId);

            return new ResponseEntity(true, HttpStatus.OK);
        }

        return new ResponseEntity(false, HttpStatus.FORBIDDEN);
    }
}
