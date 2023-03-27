package hi.duranno.domain.repository;

import hi.duranno.domain.Ordinal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrdinalRepository {

    private final EntityManager em;

    public void save(Ordinal ordinal) {
        em.persist(ordinal);
    }

    public Ordinal findByOrdinal(String ordinal) {
        List<Ordinal> ordinalList = em.createQuery("select o from Ordinal o where o.ordinal = :ordinal", Ordinal.class)
                .setParameter("ordinal", ordinal)
                .getResultList();

        return ordinalList.stream().findAny().get();
    }
}
