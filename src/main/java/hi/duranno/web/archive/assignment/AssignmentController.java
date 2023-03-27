package hi.duranno.web.archive.assignment;

import hi.duranno.domain.Assignment;
import hi.duranno.domain.Ordinal;
import hi.duranno.domain.RoleType;
import hi.duranno.domain.service.AssignmentService;
import hi.duranno.domain.service.OrdinalService;
import hi.duranno.web.archive.FileStore1;
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
@RequestMapping("/assignment")
public class AssignmentController {

    private final FileStore1 fileStore;
    private final OrdinalService ordinalService;
    private final AssignmentService assignmentService;

    private static final String[] ORDINAL_LIST = {"12", "13", "14", "15"};


    @GetMapping
    public String assignment(@Login SessionValue sessionValue,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(required = false) String ordinal,
                             Model model) {

        if (sessionValue.getRoleType() == RoleType.ADMIN) {
            if (ordinalCheck(ordinal)) {
                throw new NoSuchElementException("존재하지 않는 기수입니다.");
            }
            getPagination(ordinal + "th", page, model);
            model.addAttribute("roleType", "admin");
        } else {
            getPagination(sessionValue.getOrdinal(), page, model);
            model.addAttribute("roleType", "user");
        }

        return "archive/assignment/assignment";
    }

    private static boolean ordinalCheck(String ordinal) {
        return Arrays.stream(ORDINAL_LIST).filter(o -> o.equals(ordinal)).findAny().isEmpty();
    }

    private void getPagination(String ordinal, int page, Model model) {
        Ordinal findOrdinal = ordinalService.findByOrdinal(ordinal);

        int totalListCnt = assignmentService.allAssignmentCnt(findOrdinal);

        Pagination pagination = new Pagination(totalListCnt, page);

        int startIndex = pagination.getStartIndex();
        int pageSize = pagination.getPageSize();

        List<Assignment> batchList = assignmentService.search(startIndex, pageSize, findOrdinal);

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

        //과제 번호 관련 변수
        model.addAttribute("remainder", remainder);
        model.addAttribute("totalPageCnt", pagination.getTotalPageCnt());
        model.addAttribute("currentPage", page);
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("uploadForm") UploadForm form, @RequestParam String ordinal) {
        return "archive/assignment/add";
    }

    @PostMapping("/add")
    public String add(@Validated @ModelAttribute("uploadForm") UploadForm form,
                      BindingResult bindingResult,
                      @RequestParam String ordinal) throws IOException {

        if (bindingResult.hasErrors()) {
            return "archive/assignment/add";
        }

        //파일 업로드
        String originalFileName = fileStore.storeFile(form.getAttachFile(), ordinal);

        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        Ordinal findOrdinal = ordinalService.findByOrdinal(ordinal + "th");

        //DB에 과제 저장
        Assignment assignment = new Assignment();
        assignment.setTitle(form.getTitle());
        assignment.setFileName(originalFileName);
        assignment.setLocalDate(formatDate);
        assignment.setOrdinal(findOrdinal);

        assignmentService.save(assignment);

        return "redirect:/assignment?ordinal=" + ordinal;
    }

    @GetMapping("/{assignmentId}")
    public String view(@Login SessionValue sessionValue,
                       @PathVariable Long assignmentId,
                       Model model, @RequestParam(required = false) String ordinal) {
        //일반 사용자(USER) 진입 금지
        if (sessionValue.getRoleType() == RoleType.USER) {
            return "redirect:/assignment";
        }
        //관리자의 경우, 쿼리 파라미터 null인 경우 기수별 과제 파일 화면으로 redirect
        if (sessionValue.getRoleType() == RoleType.ADMIN && ordinal == null) {
            return "redirect:/archive/select1";
        }

        String roleType = "admin";
        Assignment findAssignment = assignmentService.findById(assignmentId);

        //과제 Id가 존재하지 않을 경우 기수별 과제 파일 화면으로 redirect
        if (findAssignment == null) {
            return "redirect:/assignment?ordinal=" + ordinal;
        }

        String cmp = findAssignment.getOrdinal().getOrdinal().split("th")[0];

        //과제의 기수가 맞지 않을 경우 기수별 과제 파일 화면으로 redirect
        if (!cmp.equals(ordinal)) {
            return "redirect:/assignment?ordinal=" + ordinal;
        }

        model.addAttribute("assignment", findAssignment);
        model.addAttribute("ordinal", ordinal);
        model.addAttribute("roleType", roleType);

        return "archive/assignment/view";
    }

    @GetMapping("/{assignmentId}/edit")
    public String editForm(@PathVariable Long assignmentId,
                           @ModelAttribute("uploadForm") UploadForm uploadForm,
                           @RequestParam(required = false) String ordinal,
                           BindingResult bindingResult, Model model) {

        Assignment findAssignment = assignmentService.findById(assignmentId);

        //과제 Id가 존재하지 않을 경우 기수별 과제 파일 화면으로 redirect
        if (findAssignment == null) {
            return "redirect:/assignment?ordinal=" + ordinal;
        }

        String cmp = findAssignment.getOrdinal().getOrdinal().split("th")[0];

        //과제의 기수가 맞지 않을 경우 기수별 과제 파일 화면으로 redirect
        if (!cmp.equals(ordinal)) {
            return "redirect:/assignment?ordinal=" + ordinal;
        }

        return "archive/assignment/edit";
    }

    @PostMapping("/{assignmentId}/edit")
    public String edit(@PathVariable Long assignmentId,
                       @Validated @ModelAttribute("uploadForm") UploadForm uploadForm,
                       BindingResult bindingResult, @RequestParam(required = false) String ordinal,
                       Model model) throws IOException {

        Assignment findAssignment = assignmentService.findById(assignmentId);

        //기존 파일 삭제
        fileStore.deleteFile(findAssignment.getFileName(), ordinal);

        //새로운 파일 업로드 및 기존 과제 수정
        String originalFileName = fileStore.storeFile(uploadForm.getAttachFile(), ordinal);
        Assignment updateParam = new Assignment();

        updateParam.setFileName(originalFileName);
        updateParam.setTitle(uploadForm.getTitle());

        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        updateParam.setLocalDate(formatDate);
        assignmentService.update(assignmentId, updateParam);

        return "redirect:/assignment?ordinal=" + ordinal;
    }

    @GetMapping("/{assignmentId}/remove")
    public String remove(@PathVariable Long assignmentId, @RequestParam String ordinal) {

        Assignment findAssignment = assignmentService.findById(assignmentId);

        //과제 Id가 존재하지 않을 경우 기수별 과제 파일 화면으로 redirect
        if (findAssignment == null) {
            return "redirect:/assignment?ordinal=" + ordinal;
        }

        String cmp = findAssignment.getOrdinal().getOrdinal().split("th")[0];

        //과제의 기수가 맞지 않을 경우 기수별 과제 파일 화면으로 redirect
        if (!cmp.equals(ordinal)) {
            return "redirect:/assignment?ordinal=" + ordinal;
        }

        //파일 삭제
        fileStore.deleteFile(findAssignment.getFileName(), ordinal);
        assignmentService.remove(findAssignment);

        return "redirect:/assignment?ordinal=" + ordinal;
    }


}
