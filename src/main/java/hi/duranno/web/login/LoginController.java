package hi.duranno.web.login;

import hi.duranno.domain.service.LoginService;
import hi.duranno.domain.Member;
import hi.duranno.web.SessionConst;
import hi.duranno.web.session.SessionValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "/login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request, @RequestParam(required = false) String redirectURL) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginEmail(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        SessionValue sessionValue = new SessionValue(loginMember.getId(), loginMember.getOrdinal().getOrdinal(), loginMember.getRoleType());

        HttpSession session = request.getSession();

        session.setAttribute(SessionConst.LOGIN_MEMBER, sessionValue);

        if (redirectURL != null) {
            return "redirect:" + redirectURL;
        }
        //로그인 성공 처리 TODO
        return "redirect:/archive";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
