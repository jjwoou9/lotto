package evengom.lotto.repository;

import evengom.lotto.domain.Lotto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LottoRepository {

    private final EntityManager em;

    public List<Lotto> findAll() {
        return em.createQuery(
                "select  l from Lotto l", Lotto.class
        ).getResultList();
    }

    public List<Lotto> findLatest(int count) {
        return em.createQuery(
                "select  l from Lotto l " +
                        "order by l.round desc", Lotto.class
        ).setMaxResults(count).getResultList();
    }

    public Lotto finOne(int round) {
        return em.find(Lotto.class, round);
    }

    public void insert(Lotto lotto) {
        em.persist(lotto);
        em.flush();
    }
}
