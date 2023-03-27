package hi.duranno.web.validation;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberEditForm {
    @NotEmpty
    private String password;
    @NotEmpty
    private String pwCheck;
}
