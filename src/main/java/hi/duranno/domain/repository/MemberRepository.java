package hi.duranno.domain.repository;

import hi.duranno.domain.Member;
import hi.duranno.web.validation.MemberEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findByEmail(String email) {

        List<Member> member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return member.stream().findAny();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public void update(Long id, MemberEditForm updateParam) {
        Member findMember = findById(id);
        findMember.setPassword(updateParam.getPassword());
    }

    public void update(Long id, String password) {
        Member findMember = findById(id);
        findMember.setPassword(password);
    }
}
