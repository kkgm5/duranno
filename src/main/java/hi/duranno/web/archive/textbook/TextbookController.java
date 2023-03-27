package hi.duranno.web.archive.textbook;

import hi.duranno.domain.Ordinal;
import hi.duranno.domain.RoleType;
import hi.duranno.domain.Textbook;
import hi.duranno.domain.service.OrdinalService;
import hi.duranno.domain.service.TextbookService;
import hi.duranno.web.archive.FileStore2;
import hi.duranno.web.archive.Pagination;
import hi.duranno.web.archive.UploadForm;
import hi.duranno.web.argumentresolver.Login;
import hi.duranno.web.session.SessionValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/textbook")
public class TextbookController {
    private final TextbookService textbookService;
    private final OrdinalService ordinalService;
    private final FileStore2 fileStore;

    private static final String[] ORDINAL_LIST = {"12", "13", "14", "15"};

    @GetMapping
    public String textbook(@Login SessionValue sessionInfo,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String ordinal,
                           Model model) {

        if (sessionInfo.getRoleType() == RoleType.ADMIN) {
            if (ordinalCheck(ordinal)) {
                throw new NoSuchElementException("존재하지 않는 기수입니다.");
            }
            getPagination(ordinal + "th", page, model);
            model.addAttribute("roleType", "admin");
        } else {
            getPagination(sessionInfo.getOrdinal(), page, model);
            model.addAttribute("roleType", "user");
        }

        return "archive/textbook/textbook";
    }

    private static boolean ordinalCheck(String ordinal) {
        return Arrays.stream(ORDINAL_LIST).filter(o -> o.equals(ordinal)).findAny().isEmpty();
    }

    private void getPagination(String ordinal, int page, Model model) {
        Ordinal findOrdinal = ordinalService.findByOrdinal(ordinal);

        int totalListCnt = textbookService.allTextbookCnt(findOrdinal);

        Pagination pagination = new Pagination(totalListCnt, page);

        int startIndex = pagination.getStartIndex();
        int pageSize = pagination.getPageSize();

        List<Textbook> batchList = textbookService.search(startIndex, pageSize, findOrdinal);

        int remainder = totalListCnt % 10;

        if (remainder == 0) {
            remainder = pagination.getPageSize();
        }
        if (remainder != 0) {
            if (page == pagination.getTotalPageCnt()) {
                remainder = pagination.getPageSize();
            }
        }
        model.addAttribute("ordinal", ordinal.split("th")[0]);
        model.addAttribute("batchList", batchList);
        model.addAttribute("pagination", pagination);

        //교재 번호 관련 변수
        model.addAttribute("remainder", remainder);
        model.addAttribute("totalPageCnt", pagination.getTotalPageCnt());
        model.addAttribute("currentPage", page);
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("uploadForm") UploadForm form,
                          @Login SessionValue sessionValue,
                          @RequestParam(required = false) String ordinal) {

        if (sessionValue.getRoleType() == RoleType.ADMIN) {
            if (ordinalCheck(ordinal)) {
                return "redirect:/assignment?ordinal=" + ordinal;
            }
        }
        return "archive/textbook/add";
    }

    @PostMapping("/add")
    public String add(@Validated @ModelAttribute("uploadForm") UploadForm form,
                      BindingResult bindingResult,
                      @RequestParam String ordinal) throws IOException {

        if (bindingResult.hasErrors()) {
            return "archive/textbook/add";
        }

        //파일 업로드
        String originalFileName = fileStore.storeFile(form.getAttachFile(), ordinal);

        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        Ordinal findOrdinal = ordinalService.findByOrdinal(ordinal + "th");

        //DB에 교재 저장
        Textbook textbook = new Textbook();
        textbook.setTitle(form.getTitle());
        textbook.setFileName(originalFileName);
        textbook.setLocalDate(formatDate);
        textbook.setOrdinal(findOrdinal);

        textbookService.save(textbook);

        return "redirect:/textbook?ordinal=" + ordinal;
    }

    @GetMapping("/{textbookId}")
    public String view(@Login SessionValue sessionValue,
                       @PathVariable Long textbookId,
                       Model model, @RequestParam String ordinal) {

        //일반 사용자(USER) 진입 금지
        if (sessionValue.getRoleType() == RoleType.USER) {
            return "redirect:/textbook";
        }
        //관리자의 경우, 쿼리 파라미터 null인 경우 기수별 과제 파일 화면으로 redirect
        if (sessionValue.getRoleType() == RoleType.ADMIN && ordinal == null) {
            return "redirect:/archive/select2";
        }

        String roleType = "admin";
        Textbook findTextbook = textbookService.findById(textbookId);

        //교재 Id가 존재하지 않을 경우 기수별 교재 파일 화면으로 redirect
        if (findTextbook == null) {
            return "redirect:/textbook?ordinal=" + ordinal;
        }

        String cmp = findTextbook.getOrdinal().getOrdinal().split("th")[0];

        //교재의 기수가 맞지 않을 경우 기수별 교재 파일 화면으로 redirect
        if (!cmp.equals(ordinal)) {
            return "redirect:/textbook?ordinal=" + ordinal;
        }

        model.addAttribute("textbook", findTextbook);
        model.addAttribute("ordinal", ordinal);

        if (sessionValue.getRoleType() == RoleType.USER) {
            roleType = "user";
        }

        model.addAttribute("roleType", roleType);

        return "archive/textbook/view";
    }

    //관리자만 진입
    @GetMapping("/{textbookId}/edit")
    public String editForm(@PathVariable Long textbookId,
                           @ModelAttribute("uploadForm") UploadForm uploadForm,
                           @RequestParam(required = false) String ordinal,
                           BindingResult bindingResult, Model model) {
        Textbook findTextbook = textbookService.findById(textbookId);

        //교재 Id가 존재하지 않을 경우 기수별 교재 파일 화면으로 redirect
        if (findTextbook == null) {
            return "redirect:/textbook?ordinal=" + ordinal;
        }

        String cmp = findTextbook.getOrdinal().getOrdinal().split("th")[0];

        //교재의 기수가 맞지 않을 경우 기수별 교재 파일 화면으로 redirect
        if (!cmp.equals(ordinal)) {
            return "redirect:/textbook?ordinal=" + ordinal;
        }

        return "archive/textbook/edit";
    }

    @PostMapping("/{textbookId}/edit")
    public String edit(@PathVariable Long textbookId,
                       @Validated @ModelAttribute("uploadForm") UploadForm uploadForm,
                       BindingResult bindingResult, @RequestParam String ordinal, Model model) throws IOException {

        Textbook findTextbook = textbookService.findById(textbookId);

        //기존 파일 삭제
        fileStore.deleteFile(findTextbook.getFileName(), ordinal);

        //새로운 파일 업로드 및 기존 교재 수정
        String originalFileName = fileStore.storeFile(uploadForm.getAttachFile(), ordinal);
        Textbook updateParam = new Textbook();

        updateParam.setFileName(originalFileName);
        updateParam.setTitle(uploadForm.getTitle());

        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        updateParam.setLocalDate(formatDate);
        textbookService.update(textbookId, updateParam);

        return "redirect:/textbook?ordinal=" + ordinal;
    }

    @GetMapping("/{textbookId}/remove")
    public String remove(@PathVariable Long textbookId, @RequestParam String ordinal) {

        Textbook findTextbook = textbookService.findById(textbookId);

        //교재 Id가 존재하지 않을 경우 기수별 교재 파일 화면으로 redirect
        if (findTextbook == null) {
            return "redirect:/textbook?ordinal=" + ordinal;
        }

        String cmp = findTextbook.getOrdinal().getOrdinal().split("th")[0];

        //교재의 기수가 맞지 않을 경우 기수별 교재 파일 화면으로 redirect
        if (!cmp.equals(ordinal)) {
            return "redirect:/textbook?ordinal=" + ordinal;
        }

        //파일 삭제
        fileStore.deleteFile(findTextbook.getFileName(), ordinal);
        textbookService.remove(findTextbook);

        return "redirect:/textbook?ordinal=" + ordinal;
    }

}
