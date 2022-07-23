package evengom.lotto.repository;

import evengom.lotto.domain.Lotto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.service.LottoService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class LottoRepository {

    private final EntityManager em;

    public LottoRepository(EntityManager em) {
        this.em = em;
    }

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
}
