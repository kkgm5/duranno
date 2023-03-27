package hi.duranno.web.temp;

import hi.duranno.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public void update(Long id, String mail) throws MessagingException {
        String password = getTempPassword();
        memberService.update(id, password);

        sendEmail(mail, password);
    }
    private void sendEmail(String to, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("[두란노] 임시 비밀번호 안내");
        mimeMessageHelper.setText("회원님의 임시 비밀번호는 " + "<span style=\"color:blue;\">" + password + "</span>" + "입니다.", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                javaMailSender.send(mimeMessage);
            }
        }).start();
    }

    private String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String str = "";

        int idx = 0;
        for (int i = 0; i < 7; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }

        return str;
    }
}
