package hi.duranno.web.validation;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class TempForm {
    @Email
    private String email;
}
