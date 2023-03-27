package hi.duranno.web.archive;

import hi.duranno.domain.*;
import hi.duranno.web.argumentresolver.Login;
import hi.duranno.web.session.SessionValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchiveController {

    @GetMapping
    public String home(@Login SessionValue sessionValue, Model model) {
        String roleType = "user";

        if (sessionValue.getRoleType() == RoleType.ADMIN) {
            roleType = "admin";
        }
        model.addAttribute("roleType", roleType);

        return "archive/home";
    }

    @GetMapping("/select1")
    public String select1(@Login SessionValue sessionValue) {
        if (sessionValue.getRoleType() != RoleType.ADMIN) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }
        return "archive/select1";
    }

    @GetMapping("/select2")
    public String select2(@Login SessionValue sessionValue) {
        if (sessionValue.getRoleType() != RoleType.ADMIN) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }
        return "archive/select2";
    }

}
