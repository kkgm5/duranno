package hi.duranno.domain.service;

import hi.duranno.domain.Board;
import hi.duranno.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public Long save(Board board) {
        boardRepository.save(board);
        return board.getId();
    }

    public void remove(Board board) {
        boardRepository.remove(board);
    }

    public List<Board> findBoardList(int startIndex, int pageSize) {
        return boardRepository.findBoardList(startIndex, pageSize);
    }

    public int allBoardCnt() {
        return boardRepository.allBoardCnt();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id);
    }

    public void update(Long boardId, Board updateParam) {
        boardRepository.update(boardId, updateParam);
    }

    public void viewUpdate(Long boardId, Integer view) {
        boardRepository.viewUpdate(boardId, view);
    }
}
