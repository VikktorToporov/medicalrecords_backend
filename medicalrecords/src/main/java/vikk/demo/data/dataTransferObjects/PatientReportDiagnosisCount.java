package vikk.demo.data.dataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientReportDiagnosisCount implements Serializable {
    private String diagnosis;

    private long count;
}