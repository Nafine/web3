package se.ifmo.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import se.ifmo.api.dots.Dot;

import java.util.LinkedList;
import java.util.List;

public class DotManager {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("se.ifmo.web3");
    private final EntityManager em = emf.createEntityManager();
    private List<Dot> dots = new LinkedList<>();

    public void add(Dot dot) {
        this.em.getTransaction().begin();
        em.persist(dot);
        em.getTransaction().commit();
    }

    public void clear() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM dot").executeUpdate();
        em.getTransaction().commit();
        dots.clear();
    }

    public List<Dot> get() {
        em.getTransaction().begin();
        dots = em.createQuery("SELECT d FROM dot d").getResultList();
        em.getTransaction().commit();
        return dots;
    }
}
