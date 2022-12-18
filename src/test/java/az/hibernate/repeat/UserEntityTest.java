package az.hibernate.repeat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import az.hibernate.repeat.entity.Company;
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
    void persist() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .firstname("John")
                .lastname("Wick")
                .role(Role.ADMIN)
                .build();
        session.persist(user);
        session.evict(user);
        User savedUser = session.find(User.class, user.getId());

        assertAll(
                () -> assertThat(savedUser.getId()).isEqualTo(user.getId()),
                () -> assertThat(savedUser.getFirstname()).isEqualTo(user.getFirstname()),
                () -> assertThat(savedUser.getLastname()).isEqualTo(user.getLastname()),
                () -> assertThat(savedUser.getRole()).isEqualTo(user.getRole())
        );

        session.getTransaction().commit();
    }

    @Test
    void remove() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.find(User.class, 2);
        session.remove(user);
        session.flush();
        User deletedUser = session.find(User.class, 2);
        assertThat(deletedUser).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void findAllUsersWithCompany() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.find(User.class, 2);
        Company userCompany = user.getCompany();

        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getId()).isEqualTo(2),
                () -> assertThat(user.getFirstname()).isEqualTo("John"),
                () -> assertThat(user.getLastname()).isEqualTo("Wick"),
                () -> assertThat(user.getRole()).isEqualTo(Role.USER),
                () -> assertThat(userCompany).isNotNull(),
                () -> assertThat(userCompany.getId()).isEqualTo(3),
                () -> assertThat(userCompany.getName()).isEqualTo("Google")
        );

        session.getTransaction().commit();
    }
}