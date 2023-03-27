package hi.duranno.domain.service;

import hi.duranno.domain.Member;
import hi.duranno.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     *
     * @return null이면 로그인 실패
     */
    public Member login(String loginEmail, String password) {
        return memberRepository.findByEmail(loginEmail)
                //.filter(m -> m.getPassword().equals(password))
                .filter(m -> passwordEncoder.matches(password, m.getPassword()))
                .orElse(null);
    }
}
