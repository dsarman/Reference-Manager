package dao;

import entity.Reference;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Jan Bílek
 * @since 2017-10-28
 **/

public class ReferenceDaoImpl implements ReferenceDao {
    @PersistenceContext
    EntityManager em;

    @Override
    public void create(Reference r) {
        em.persist(r);
    }

    @Override
    public void update(Reference r) {
        em.merge(r);
    }

    @Override
    public void remove(Reference r) {
        Reference managed = em.merge(r);
        em.remove(managed);
    }

    @Override
    public Reference findById(Long id) {
        return em.find(Reference.class, id);
    }

    @Override
    public List<Reference> findAll() {
        return em.createQuery("select r from Reference r", Reference.class).getResultList();
    }
}