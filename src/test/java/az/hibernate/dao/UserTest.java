package az.hibernate.dao;

import az.hibernate.repeat.dao.UserDao;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.util.HibernateUtil;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class UserTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserDao userDao = UserDao.getInstance();

    @Test
    void batchSize() {
        List<User> users = userDao.findAllByCompanyName(sessionFactory.openSession(), "Google");
        users.forEach(user -> user.getPayments().forEach(System.out::println));
    }
}