package elice.eliceauction.domain.user.entity;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
