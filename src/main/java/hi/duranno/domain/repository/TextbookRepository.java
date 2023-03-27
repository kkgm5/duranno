package hi.duranno.domain.repository;

import hi.duranno.domain.Ordinal;
import hi.duranno.domain.Textbook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TextbookRepository {

    private final EntityManager em;

    public void save(Textbook textbook) {
        em.persist(textbook);
    }

    public Textbook findById(Long id) {
         return em.find(Textbook.class, id);
    }

    public void update(Long textbookId, Textbook updateParam) {
        Textbook findTextbook = findById(textbookId);
        findTextbook.setFileName(updateParam.getFileName());
        findTextbook.setLocalDate(updateParam.getLocalDate());
        findTextbook.setTitle(updateParam.getTitle());
    }

    public void remove(Textbook textbook) {
        em.remove(textbook);
    }

    public List<Textbook> search(int firstResult, int maxResult, Ordinal ordinal) {
        return em.createQuery("select t from Textbook t where t.ordinal = :ordinal order by t.id desc", Textbook.class)
                .setParameter("ordinal", ordinal)
                .setFirstResult(firstResult)
                .setMaxResults(maxResult)
                .getResultList();
    }


    public int allTextbookCnt(Ordinal ordinal) {
        return ((Number) em.createQuery("select count(*) from Textbook t where t.ordinal = :ordinal")
                .setParameter("ordinal", ordinal)
                .getSingleResult()).intValue();
    }
}
