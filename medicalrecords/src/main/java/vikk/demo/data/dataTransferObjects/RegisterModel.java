package vikk.demo.data.dataTransferObjects;

import com.sun.istack.Nullable;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterModel {
    private boolean roleDoctor;

    private String email;

    private String password;

    @Nullable
    private UUID doctorId;
}
