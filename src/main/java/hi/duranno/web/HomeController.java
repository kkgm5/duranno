package hi.duranno.web;

import hi.duranno.domain.Board;
import hi.duranno.domain.RoleType;
import hi.duranno.domain.service.BoardService;
import hi.duranno.web.argumentresolver.Login;
import hi.duranno.web.session.SessionValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(@Login SessionValue sessionValue, Model model, @RequestParam(defaultValue = "1") int page) {

        int totalListCnt = boardService.allBoardCnt();

        BoardPagination pagination = new BoardPagination(totalListCnt, page);

        int startIndex = pagination.getStartIndex();
        int pageSize = pagination.getPageSize();

        List<Board> boardList = boardService.findBoardList(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("currentPage", page);

        int remainder = totalListCnt % 5;
        if (remainder == 0) {
            remainder = pagination.getPageSize();
        }
        if (remainder != 0) {
            if (page == pagination.getTotalPageCnt()) {
                remainder = pagination.getPageSize();
            }
        }

        model.addAttribute("remainder", remainder);

        if (sessionValue != null) {
            if (sessionValue.getRoleType() == RoleType.ADMIN) {
                String roleType = "admin";
                model.addAttribute("roleType", roleType);
            }
        }
        model.addAttribute("sessionValue", sessionValue);

        return "home";
    }

    //공지사항
    @GetMapping("/board")
    public String addForm(@ModelAttribute("board") Board board) {
        return "/board/add";
    }


    @PostMapping("/board")
    public String add(@Validated Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/board/add";
        }

        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        board.setLocalDate(formatDate);
        boardService.save(board);

        return "redirect:/#reviews";
    }

    @GetMapping("/boards/{boardId}")
    public String view(@PathVariable Long boardId, Model model, @Login SessionValue sessionValue, @RequestParam(required = false) int num) {
        String roleType = "admin";
        Board board = boardService.findById(boardId);
        Integer cnt = board.getCnt();
        
        boardService.viewUpdate(boardId, ++cnt); //조회수 증가

        model.addAttribute("board", board);
        model.addAttribute("sessionValue", sessionValue);
        model.addAttribute("num", num);

        if (sessionValue.getRoleType() == RoleType.ADMIN) {
            model.addAttribute("roleType", roleType);
        } else {
            roleType = "user";
            model.addAttribute("roleType", roleType);
        }

        return "board/view";
    }

    //공지사항 수정
    @GetMapping("/boards/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        log.info("call editForm");

        Board board = boardService.findById(boardId);
        model.addAttribute("board", board);
        return "board/edit";
    }

    @PostMapping("/boards/{boardId}/edit")
    public String edit(@PathVariable Long boardId, @ModelAttribute Board board) {

        boardService.update(boardId, board);

        return "redirect:/#reviews";
    }

    //공지사항 삭제
    @GetMapping("/boards/{boardId}/remove")
    public String remove(@PathVariable Long boardId) {
        Board findBoard = boardService.findById(boardId);
        boardService.remove(findBoard);

        return "redirect:/#reviews";
    }
}
