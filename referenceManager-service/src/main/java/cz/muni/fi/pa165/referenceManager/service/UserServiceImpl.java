package cz.muni.fi.pa165.referenceManager.service;

import cz.muni.fi.pa165.referenceManager.dao.TagDao;
import cz.muni.fi.pa165.referenceManager.dao.UserDao;
import cz.muni.fi.pa165.referenceManager.entity.Reference;
import cz.muni.fi.pa165.referenceManager.entity.Tag;
import cz.muni.fi.pa165.referenceManager.entity.User;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author Jan Bílek
 */

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Inject
    private TagDao tagDao;

    @Inject
    private ConfigurablePasswordEncryptor passwordEncryptor;

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    public User findUserById(Long id) {
        return userDao.findById(id);
    }

    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    public void registerUser(User user, String plainPassword) {
        user.setPasswordHash(createPasswordHash(plainPassword));
        userDao.create(user);
    }

    public Collection<User> getAllUsers() {
        return userDao.findAll();
    }

    public boolean authenticate(User user, String password) {
        return verifyPassword(user.getPasswordHash(), password);
    }

    @Override
    public void addReference(User user, Reference reference) {
        user.addReference(reference);
    }

    @Override
    public void removeReference(User user, Reference reference) {
        user.removeReference(reference);
    }

    @Override
    public void addTag(Long userId, Long tagId) {
        User user = userDao.findById(userId);
        Tag tag = tagDao.findById(tagId);
        user.addTag(tag);
        userDao.update(user);    }

    @Override
    public void removeTag(Long userId, Long tagId) {
        User user = userDao.findById(userId);
        Tag tag = tagDao.findById(tagId);
        user.removeTag(tag);
        userDao.update(user);
    }

    @Override
    public void shareTag(Long userId, Long tagId) {
        User user = userDao.findById(userId);
        Tag tag = tagDao.findById(tagId);
        tag.addUser(user);
        tagDao.update(tag);
    }

    @Override
    public void unshareTag(Long userId, Long tagId) {
        User user = userDao.findById(userId);
        Tag tag = tagDao.findById(tagId);
        tag.removeUser(user);
        tagDao.update(tag);
    }


    private boolean verifyPassword(String password, String correctPasswordHash) {
        if (password == null) {
            return false;
        }
        if (correctPasswordHash == null) {
            throw new IllegalArgumentException("Password hash cannot be null.");
        }
        return passwordEncryptor.checkPassword(correctPasswordHash, password);


    }

    private String createPasswordHash(String password) {
        return passwordEncryptor.encryptPassword(password);
    }
}
