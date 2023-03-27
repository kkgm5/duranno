package hi.duranno.web.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrdinalCode {
    private String code;
    private String displayName;
}
