package hi.duranno.web;

import hi.duranno.domain.*;
import hi.duranno.domain.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {
    public static final String ASSIGNMENT_NAME = "assignment_";
    public static final String TEXTBOOK_NAME = "textbook_";
    private final AssignmentService assignmentService;

    private final TextbookService textbookService;
    private final OrdinalService ordinalService;
    private final MemberService memberService;
    private final BoardService boardService;

    private static final LocalDate DISTRIBUTION_DATE = LocalDate.of(2023, 3, 6);

    @PostConstruct
    public void init() {

        setOrdinal();

        for (int i = 0; i < 4; i++) {
            setAssignment(12 + i + "th");
        }
        for (int i = 0; i < 4; i++) {
            setTextbook(12 + i + "th");
        }
        //관리자, 기수별 유저 아이디 셋팅
        setAdminId();
        setUserId();
        //공지사항
        setBoard();

    }

    private void setOrdinal() {
        for (int i = 0; i < 4; i++) {
            Ordinal ordinal = new Ordinal();
            ordinal.setOrdinal(12 + i + "th");
            ordinalService.save(ordinal);
        }
    }

    private void setAssignment(String ordinal) {
        Path fp = Paths.get(getFullPath(ASSIGNMENT_NAME, ordinal));
        List<String> fileList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(fp)) {
            String str;
            while (true) {
                str = br.readLine();
                if (str == null)
                    break;
                Assignment assignment = new Assignment();
                assignment.setTitle(str);
                assignment.setFileName(str + ".mp3");

                String formatDate = DISTRIBUTION_DATE.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

                Ordinal findOrdinal = ordinalService.findByOrdinal(ordinal);
                assignment.setOrdinal(findOrdinal);
                assignment.setLocalDate(formatDate);
                assignmentService.save(assignment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFullPath(String name, String ordinal) {
        return name + ordinal + ".txt";
    }

    private void setTextbook(String ordinal) {
        Path fp = Paths.get(getFullPath(TEXTBOOK_NAME, ordinal));
        List<String> fileList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(fp)) {
            String str;
            while (true) {
                str = br.readLine();
                if (str == null)
                    break;
                Textbook textbook = new Textbook();
                textbook.setTitle(str);
                textbook.setFileName(str + ".hwp");

                String formatDate = DISTRIBUTION_DATE.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

                Ordinal findOrdinal = ordinalService.findByOrdinal(ordinal);;
                textbook.setOrdinal(findOrdinal);
                textbook.setLocalDate(formatDate);
                textbookService.save(textbook);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //관리자 아이디 저장
    private void setAdminId() {
        Member member1 = new Member();
        member1.setEmail("admin@gmail.com");
        member1.setPassword("1234");

        Ordinal ordinal = ordinalService.findByOrdinal("13th");

        member1.setOrdinal(ordinal);
        member1.setRoleType(RoleType.ADMIN);

        memberService.join(member1);
    }

    //기수별 아이디 저장
    private void setUserId() {
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();

        Ordinal ordinal1 = ordinalService.findByOrdinal("12th");
        Ordinal ordinal2 = ordinalService.findByOrdinal("13th");
        Ordinal ordinal3 = ordinalService.findByOrdinal("14th");
        Ordinal ordinal4 = ordinalService.findByOrdinal("15th");

        member1.setEmail("user001@naver.com");
        member1.setPassword("1234");

        member2.setEmail("user044@gmail.com");
        member2.setPassword("1234");

        member3.setEmail("user086@daum.net");
        member3.setPassword("1234");

        member4.setEmail("user117@hotmail.com");
        member4.setPassword("1234");

        member1.setOrdinal(ordinal1);
        member2.setOrdinal(ordinal2);
        member3.setOrdinal(ordinal3);
        member4.setOrdinal(ordinal4);

        member1.setRoleType(RoleType.USER);
        member2.setRoleType(RoleType.USER);
        member3.setRoleType(RoleType.USER);
        member4.setRoleType(RoleType.USER);

        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);
        memberService.join(member4);
    }

    private void setBoard() {
        String formatDate1 = DISTRIBUTION_DATE.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        Board board1 = new Board();

        board1.setTitle("두란노 홈페이지가 오픈했습니다.");
        board1.setContent("두란노 홈페이지가 오픈했습니다.");
        board1.setLocalDate(formatDate1);

        boardService.save(board1);

        String formatDate2 = DISTRIBUTION_DATE.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        Board board2 = new Board();

        board2.setTitle("휴강 공지입니다.");
        board2.setContent("휴강 공지입니다.");
        board2.setLocalDate(formatDate2);

        boardService.save(board2);
    }
}
