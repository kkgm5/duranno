package hi.duranno.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty
    private String loginEmail;
    @NotEmpty
    private String password;
}
