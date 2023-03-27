package hi.duranno.web.member;

import hi.duranno.domain.Member;
import hi.duranno.domain.Ordinal;
import hi.duranno.domain.RoleType;
import hi.duranno.domain.service.MemberService;
import hi.duranno.domain.service.OrdinalService;
import hi.duranno.web.argumentresolver.Login;
import hi.duranno.web.session.SessionValue;
import hi.duranno.web.temp.SendEmailService;
import hi.duranno.web.validation.MemberEditForm;
import hi.duranno.web.validation.MemberSaveForm;
import hi.duranno.web.validation.OrdinalCode;
import hi.duranno.web.validation.TempForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final OrdinalService ordinalService;
    private final SendEmailService sendEmailService;
    private static final List<OrdinalCode> ordinalCodes;

    @Value("${spring.mail.username}")
    private String from;

    static {
        ordinalCodes = new ArrayList<>();
        ordinalCodes.add(new OrdinalCode("12th", "12기"));
        ordinalCodes.add(new OrdinalCode("13th", "13기"));
        ordinalCodes.add(new OrdinalCode("14th", "14기"));
        ordinalCodes.add(new OrdinalCode("15th", "15기"));
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") MemberSaveForm memberSaveForm, Model model) {
        model.addAttribute("ordinalCodes", ordinalCodes);
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute("member") MemberSaveForm memberSaveForm, BindingResult bindingResult, Model model) {

        if (!memberSaveForm.getPassword().equals(memberSaveForm.getPwCheck())) {
            bindingResult.reject("globalError", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("ordinalCodes", ordinalCodes);
            return "members/addMemberForm";
        }

        Member member = new Member();
        member.setEmail(memberSaveForm.getEmail());
        member.setPassword(memberSaveForm.getPassword());

        Ordinal ordinal = ordinalService.findByOrdinal(memberSaveForm.getOrdinalCode());

        member.setOrdinal(ordinal);

        //관리자와 일반사용자
        if (memberSaveForm.getEmail().split("@")[0].equals("holon2018")) {
            member.setRoleType(RoleType.ADMIN);
        } else {
            member.setRoleType(RoleType.USER);
        }

        Long savedId = memberService.join(member); //회원가입

        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String memberEditForm(@ModelAttribute("memberEditForm") MemberEditForm memberEditForm) {


        return "members/memberEditForm";
    }

    @PostMapping("/edit")
    public String memberEdit(@Validated @ModelAttribute("memberEditForm") MemberEditForm memberEditForm,
                             BindingResult bindingResult, @Login SessionValue sessionValue) {

        if (!memberEditForm.getPassword().equals(memberEditForm.getPwCheck())) {
            bindingResult.reject("globalError", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "members/memberEditForm";
        }


        memberService.update(sessionValue.getMemberId(), memberEditForm); //비밀번호 변경

        return "redirect:/";
    }

    @GetMapping("/temp")
    public String tempForm(@ModelAttribute("tempForm") TempForm tempForm) {
        return "members/temp";
    }

    @PostMapping("/temp")
    public String temp(@ModelAttribute TempForm tempForm, BindingResult bindingResult, Model model) throws MessagingException {
        Optional<Member> findEmail = memberService.findByEmail(tempForm.getEmail());

        if (findEmail.isEmpty()) {
            bindingResult.reject("globalError", "등록되지 않은 이메일입니다.");
            return "members/temp";
        }

        sendEmailService.update(findEmail.get().getId(), tempForm.getEmail()); //임시 비밀번호 설정 및 메일 발송

        model.addAttribute("alert", "이메일로 임시 비밀번호가 발급되었습니다.");

        return "members/temp";
    }
}
