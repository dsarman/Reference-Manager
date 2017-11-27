package cz.muni.fi.pa165.referenceManager.service;

import cz.muni.fi.pa165.referenceManager.dao.TagDao;
import cz.muni.fi.pa165.referenceManager.dao.UserDao;
import cz.muni.fi.pa165.referenceManager.entity.Tag;
import cz.muni.fi.pa165.referenceManager.entity.User;
import cz.muni.fi.pa165.referenceManager.service.config.ServiceConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    private User user1;
    private User user2;
    private Tag tag1;
    private Tag tag2;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);

        user1 = Mockito.mock(User.class);
        user2 = Mockito.mock(User.class);

        Mockito.when(userDao.findById(1L)).thenReturn(user1);
        Mockito.when(userDao.findById(2L)).thenReturn(user2);

        List<User> users = Arrays.asList(user1, user2);
        Mockito.when(userDao.findAll()).thenReturn(users);

        tag1 = Mockito.mock(Tag.class);
        tag2 = Mockito.mock(Tag.class);

        Mockito.when(tagDao.findById(1L)).thenReturn(tag1);
        Mockito.when(tagDao.findById(2L)).thenReturn(tag2);

        List<Tag> tags = Arrays.asList(tag1, tag2);
        Mockito.when(tagDao.findAll()).thenReturn(tags);
    }

    @Test
    public void testAddTag() {
        userService.addTag(1L, 1L);
        Mockito.verify(user1, Mockito.times(1)).addTag(tag1);
        Mockito.verify(userDao, Mockito.times(1)).update(user1);
    }
}
