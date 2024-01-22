package vikk.demo.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Patient extends BaseAccount {
    @Nullable
    private String ssn;

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Doctor doctor;

    private boolean healthInsurancePaid;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Visit> visits = new HashSet<>();

    @ManyToMany
    private Set<Role> authorities = new HashSet<>();
}
