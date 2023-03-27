package hi.duranno.domain.service;

import hi.duranno.domain.Member;
import hi.duranno.domain.repository.MemberRepository;
import hi.duranno.web.validation.MemberEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateEmail(member); //기수별 명단 이메일 매칭
        validateDuplicateMember(member); //중복 회원 검증

        String encodedPassword = passwordEncoder.encode(member.getPassword()); // 비밀번호 암호화
        member.setPassword(encodedPassword);

        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * 기수별 명단 이메일 매칭
     */
    private void validateEmail(Member member) {
        String[] mList = getMemberList(member.getOrdinal().getOrdinal());
        String email = member.getEmail().split("@")[0];

        Optional<String> emailCheck = Arrays.stream(mList).filter(m -> m.equals(email)).findAny();
        if (emailCheck.isEmpty()) {
            throw new IllegalStateException("접근 권한이 없는 사용자입니다.");
        }
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByEmail(member.getEmail());//email에 유니크 제약 조건 추가해야됨
        if (findMembers.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    private String[] getMemberList(String ordinal) {

        Path fp = Paths.get("member_" + ordinal + ".txt");
        List<String> mList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(fp)) {
            String str;
            while (true) {
                str = br.readLine();
                if (str == null)
                    break;
                mList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] list = mList.toArray(new String[0]);

        return list;
    }

    @Transactional
    public void update(Long id, MemberEditForm updateParam) {
        String encodedPassword = passwordEncoder.encode(updateParam.getPassword());
        updateParam.setPassword(encodedPassword);
        memberRepository.update(id, updateParam);
    }

    @Transactional
    public void update(Long id, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        memberRepository.update(id, encodedPassword);
    }
}
