package hi.duranno.domain.repository;

import hi.duranno.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public void save(Board board) {
        em.persist(board);
    }

    public void remove(Board board) {
        em.remove(board);
    }

    public List<Board> findBoardList(int startIndex, int pageSize) {
        return em.createQuery("select b from Board b order by b.id desc", Board.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public int allBoardCnt() {
        return ((Number) em.createQuery("select count(*) from Board")
                .getSingleResult()).intValue();
    }

    public Board findById(Long id) {
        return em.find(Board.class, id);
    }

    public void update(Long boardId, Board updateParam) {
        Board findBoard = findById(boardId);
        findBoard.setTitle(updateParam.getTitle());
        findBoard.setContent(updateParam.getContent());
    }

    public void viewUpdate(Long boardId, Integer view) {
        Board findBoard = findById(boardId);
        findBoard.setCnt(view);
    }

}
