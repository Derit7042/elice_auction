package elice.eliceauction.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFormDTO {
    private String email;
    private String username;
    private String password;
}
