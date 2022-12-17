package az.hibernate.repeat;

import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.model.Role;
import az.hibernate.repeat.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Test
    void removeLifeCycle() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .firstname("John")
                .lastname("Wick")
                .role(Role.USER)
                .build();

        session.save(user);

        session.getTransaction().commit();
    }
}