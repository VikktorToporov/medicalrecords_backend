package vikk.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vikk.demo.data.dataTransferObjects.LoginModel;
import vikk.demo.data.dataTransferObjects.PatientList;
import vikk.demo.data.dataTransferObjects.RegisterModel;
import vikk.demo.data.dataTransferObjects.SessionItem;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.entities.Patient;
import vikk.demo.data.entities.User;
import vikk.demo.data.repositories.DoctorRepository;
import vikk.demo.data.repositories.PatientRepository;
import vikk.demo.data.repositories.UserRepository;
import vikk.demo.services.SessionService;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/Users")
public class UserController {
    private SessionService sessionService;
    private UserRepository userRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterModel model) {
        // Check if the username is already taken
        if (userRepository.existsByEmail(model.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail is already taken!");
        }

        // Hash the password before saving it to the database
        String encodedPassword = passwordEncoder.encode(model.getPassword());

        // Create a new user entity
        User user = new User();
        user.setEmail(model.getEmail());
        user.setPassword(encodedPassword);
        user.setIsDoctor(model.isRoleDoctor());

        try {
            // Save the user to the database
            userRepository.save(user);

            UUID userId = userRepository.findByEmail(model.getEmail()).getId();

            if (model.isRoleDoctor()) {
                Doctor doctor = new Doctor();
                doctor.setEmail(model.getEmail());
                doctor.setPassword(encodedPassword);
                doctor.setUserId(userId);

                doctorRepository.save(doctor);
            } else if (!model.isRoleDoctor()) {
                Patient patient = new Patient();
                patient.setEmail(model.getEmail());
                patient.setPassword(encodedPassword);
                patient.setUserId(userId);

                Doctor selectedDoctor = doctorRepository.findDoctorById(model.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor " + model.getDoctorId() + " not found!"));
                patient.setDoctor(selectedDoctor);

                patientRepository.save(patient);
            }

            return new ResponseEntity(true, HttpStatus.OK);
        } catch (Exception e) {
            // Handle database-related errors, such as unique constraint violations
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @PostMapping(value="/Login")
    public ResponseEntity<?> loginUser(@RequestBody LoginModel model, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(model.getEmail(), model.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            User user = (User)authentication.getPrincipal();

            UUID sessionId = sessionService.insertSession(user.getId(), user.isDoctor());

            SessionItem sessionItem = new SessionItem(sessionId, user.getId(), user.isDoctor());
            // Return a successful response
            return ResponseEntity.ok(sessionItem);
        } else {
            // Authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PutMapping(value="/Logout")
    public ResponseEntity<?> logout(@RequestHeader HttpHeaders headers) {
        if (sessionService.removeSession(headers)) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.FORBIDDEN);
        }
    }
}
