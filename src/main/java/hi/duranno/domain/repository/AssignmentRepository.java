package hi.duranno.domain.repository;

import hi.duranno.domain.Assignment;
import hi.duranno.domain.Ordinal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AssignmentRepository {
    private final EntityManager em;

    public void save(Assignment assignment) {
        em.persist(assignment);
    }

    public void remove(Assignment assignment) {
        em.remove(assignment);
    }

    public void update(Long assignmentId, Assignment updateParam) {
        Assignment findAssignment = findById(assignmentId);
        findAssignment.setFileName(updateParam.getFileName());
        findAssignment.setLocalDate(updateParam.getLocalDate());
        findAssignment.setTitle(updateParam.getTitle());
    }

    public Assignment findById(Long id) {
        return em.find(Assignment.class, id);
    }

    public List<Assignment> search(int firstResult, int maxResult, Ordinal ordinal) {
        return em.createQuery("select a from Assignment a where a.ordinal = :ordinal order by a.id desc", Assignment.class)
                .setParameter("ordinal", ordinal)
                .setFirstResult(firstResult)
                .setMaxResults(maxResult)
                .getResultList();
    }

    public List<Assignment> searchByOrdinal(Ordinal ordinal) {
        return em.createQuery("select a from Assignment a where a.ordinal = :ordinal order by a.id desc", Assignment.class)
                .setParameter("ordinal", ordinal)
                .getResultList();
    }

    public int allAssignmentCnt(Ordinal ordinal) {
        return ((Number) em.createQuery("select count(*) from Assignment a where a.ordinal = :ordinal")
                .setParameter("ordinal", ordinal)
                .getSingleResult()).intValue();
    }
}
