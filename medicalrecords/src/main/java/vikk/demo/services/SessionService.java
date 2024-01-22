package vikk.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vikk.demo.data.dataTransferObjects.DoctorEdit;
import vikk.demo.data.dataTransferObjects.LoginModel;
import vikk.demo.data.dataTransferObjects.PatientEdit;
import vikk.demo.data.dataTransferObjects.PatientList;
import vikk.demo.data.entities.Doctor;
import vikk.demo.data.entities.Patient;
import vikk.demo.data.entities.Session;
import vikk.demo.data.repositories.SessionRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public UUID insertSession(UUID userId, boolean isDoctor) {
        Session session = convertLoginModelToSessionDbObj(userId, isDoctor);

        sessionRepository.save(session);

        return session.getId();
    }

    public boolean checkPermissions(HttpHeaders headers, boolean checkIfDoctor) {
        if (headers.get("session-id") != null && headers.get("session-id").get(0) != null) {
            UUID sessionId = UUID.fromString(headers.get("session-id").get(0));

            Session session = sessionRepository.findSessionById(sessionId);

            return session.getUserId() != null && (!checkIfDoctor || session.isDoctor());
        }

        return false;
    }

    @Transactional
    public boolean removeSession(HttpHeaders headers) {
        if (headers.get("session-id") != null && headers.get("session-id").get(0) != null) {
            UUID sessionId = UUID.fromString(headers.get("session-id").get(0));

            sessionRepository.deleteById(sessionId);

            return true;
        }

        return false;
    }



    private Session convertLoginModelToSessionDbObj(UUID userId, boolean isDoctor) {
        Session session = new Session();

        session.setUserId(userId);
        session.setDoctor(isDoctor);

        return session;
    }
}
