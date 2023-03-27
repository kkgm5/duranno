package hi.duranno.web.session;

import hi.duranno.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SessionValue {
    private Long memberId;
    private String ordinal;
    private RoleType roleType;
}
