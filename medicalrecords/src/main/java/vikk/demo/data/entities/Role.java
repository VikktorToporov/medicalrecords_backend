package vikk.demo.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority, Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "char(36)", updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private String authority;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER)
    private Set<Doctor> doctors;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER)
    private Set<Patient> patients;

    public String getAuthority() {
        return this.authority;
    }

    public Set<Doctor> getDoctors() {
        return this.doctors;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setAuthority(final String authority) {
        this.authority = authority;
    }

    public void setDoctors(final Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void setPatients(final Set<Patient> patients) {
        this.patients = patients;
    }
}