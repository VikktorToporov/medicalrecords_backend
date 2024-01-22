package vikk.demo.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Doctor extends BaseAccount {
    @Nullable
    private String specialty;

    private boolean gp;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany
    private Set<Role> authorities = new HashSet<>();
}
