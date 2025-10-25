package se.ifmo.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import se.ifmo.api.Dot;

import java.util.List;

@ApplicationScoped
public class DotManager {

    @PersistenceContext(name = "se.ifmo.web3")
    private EntityManager em;

    @Transactional
    public void add(Dot dot) {
        em.persist(dot);
    }

    @Transactional
    public void clear() {
        em.createQuery("DELETE FROM Dot").executeUpdate();
    }

    public List<Dot> get() {
        return em.createQuery("SELECT d FROM Dot d ORDER BY d.id", Dot.class)
                .getResultList();
    }
}