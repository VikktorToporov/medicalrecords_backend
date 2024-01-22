package vikk.demo.data.dataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientList implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private String firstName;

    private String middleName;

    private String lastName;

    @Email
    @Column(unique = true) // Constraint so that there is only 1 user with said email
    private String email;

    private String ssn;

    private GenericComboBox doctor;

    private boolean healthInsurancePaid;

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
