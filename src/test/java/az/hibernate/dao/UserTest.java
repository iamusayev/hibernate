package az.hibernate.dao;

import az.hibernate.repeat.dao.UserDao;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.util.HibernateUtil;
import java.util.List;
import lombok.Cleanup;
import org.hibernate.Session;
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

    @Test
    void fetchAnnotation() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = session.createQuery("select u from User u ", User.class).list();
        users.forEach(user -> System.out.println(user.getPayments().size()));
        users.forEach(user -> System.out.println(user.getCompany().getName()));

        session.getTransaction().commit();
    }
}