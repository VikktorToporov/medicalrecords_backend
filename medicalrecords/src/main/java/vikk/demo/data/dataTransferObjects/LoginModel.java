package vikk.demo.data.dataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {
    private String email;

    private String password;

    public String getEmail() { return this.email; }

    public String getPassword() { return this.password; }

}
