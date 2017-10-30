import dao.ReferenceDao;
import entity.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceApplicationContext.class)
@Transactional
public class ReferenceDaoTest {
    @Inject
    private ReferenceDao referenceDao;

    @PersistenceContext
    private EntityManager em;

    @Test(expected = PersistenceException.class)
    public void testNewReferenceWithoutTitleError() {
        Reference reference = new Reference();
        referenceDao.create(reference);
    }

    @Test
    public void testReferenceValidationWithBadPagesPatternError() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Reference reference = new Reference();
        reference.setTitle("Test title");
        reference.setPages("not a range of pages");
        Set<ConstraintViolation<Reference>> violations = validator.validate(reference);
        assertEquals("There should be one constraint violation for the pages field", 1, violations.size());
    }

    @Test
    public void testNewReferenceSuccess() {
        Reference reference = getTestReference();
        referenceDao.create(reference);

        assertNotNull("Reference should have its id set after saving.", reference.getId());
        Reference foundReference = em.find(Reference.class, reference.getId());
        assertEquals("Saved and found references should be equal", reference, foundReference);
    }

    @Test
    public void testUpdateReferenceSuccess() {
        Reference reference = getTestReference();
        em.persist(reference);
        em.detach(reference);

        reference.setTitle("Modified test reference");
        reference.setAuthors(new ArrayList<>());
        reference.setPages("0-1");
        reference.setVenue("New test venue");

        referenceDao.update(reference);

        Reference foundReference = em.find(Reference.class, reference.getId());
        assertEquals("Original and found references should be equal", reference, foundReference);
    }

    @Test
    public void testRemoveReferenceSuccess() {
        Reference reference = getTestReference();
        em.persist(reference);
        em.detach(reference);

        referenceDao.remove(reference);

        assertTrue("There should be no persisted references after remove", getAllPersistedReferences().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNewReferenceFail() {
        Reference reference = getTestReference();
        referenceDao.remove(reference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAlreadyRemovedReferenceFail() {
        Reference reference = getTestReference();
        em.persist(reference);
        em.remove(reference);
        referenceDao.remove(reference);
    }

    @Test
    public void testFindAllReferences() {
        Reference reference1 = getTestReference();
        Reference reference2 = getTestReference();
        reference2.setTitle("Test reference 2");
        em.persist(reference1);
        em.persist(reference2);

        Set<Reference> references = new HashSet<>(referenceDao.findAll());
        Set<Reference> expectedReferences = new HashSet<>(getAllPersistedReferences());

        assertEquals("References returned by the ReferenceDao should match persisted references",
            expectedReferences, references);
    }

    @Test
    public void testFindById() {
        Reference reference = getTestReference();
        em.persist(reference);

        Reference foundReference = referenceDao.findById(reference.getId());
        assertEquals(reference, foundReference);
    }

    private List<Reference> getAllPersistedReferences() {
        return em.createQuery("select r from Reference r", Reference.class).getResultList();
    }

    private Reference getTestReference() {
        Reference reference = new Reference();
        reference.setTitle("Test reference");
        List<String> authors = new ArrayList<>();
        authors.add("Author One");
        authors.add("Author Two");
        reference.setAuthors(authors);
        reference.setPages("50-55");
        reference.setVenue("Test venue");

        return reference;
    }
}
