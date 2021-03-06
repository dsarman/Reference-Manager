package cz.muni.fi.pa165.referenceManager.dao;

import cz.muni.fi.pa165.referenceManager.entity.Reference;
import cz.muni.fi.pa165.referenceManager.entity.Tag;
import cz.muni.fi.pa165.referenceManager.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Jan Bílek
 * @since 2017-10-28
 **/
@Repository
public class ReferenceDaoImpl implements ReferenceDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Reference r) {
        em.persist(r);
    }

    @Override
    public Reference update(Reference r) {
        return em.merge(r);
    }

    @Override
    public void remove(Reference r) {
        Reference reference = em.find(Reference.class,r.getId());
        if (reference == null) {
            throw new IllegalArgumentException("No reference with id " + r.getId() + " could be found");
        }
        for (Tag tag : reference.getTags()) {
            tag.removeReference(reference);
        }
        List<User> users = em.createQuery("select u from User u where :reference member of u.references", User.class)
            .setParameter("reference", reference).getResultList();
        for (User user : users) {
            user.removeReference(reference);
        }
        em.remove(reference);
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
