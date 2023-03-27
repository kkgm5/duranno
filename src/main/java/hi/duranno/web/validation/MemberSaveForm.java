package hi.duranno.web.validation;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberSaveForm {
    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String pwCheck;
    @NotEmpty
    private String ordinalCode;
}
