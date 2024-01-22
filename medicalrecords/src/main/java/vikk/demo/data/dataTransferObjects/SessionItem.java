package vikk.demo.data.dataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionItem {
    private UUID sessionId;

    private UUID userId;

    private boolean isDoctor;
}
