package az.hibernate.repeat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import az.hibernate.repeat.entity.Company;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.model.Role;
import az.hibernate.repeat.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
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
        List<User> users = new ArrayList<>();
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

    @Test
    void persistUserWithCompany() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = new Company(null, "Facebook", null);
        User user = User.builder()
                .firstname("John")
                .lastname("Wick")
                .role(Role.USER)
                .build();
        user.addCompany(company);
        session.persist(user);

        session.clear();
        Company savedCompany = session.find(Company.class, company.getId());
        User savedUser = session.find(User.class, user.getId());
        assertAll(
                () -> assertThat(savedCompany).isNotNull(),
                () -> assertThat(savedUser).isNotNull(),
                () -> assertThat(savedUser.getFirstname()).isEqualTo(user.getFirstname()),
                () -> assertThat(savedUser.getLastname()).isEqualTo(user.getLastname()),
                () -> assertThat(savedCompany.getName()).isEqualTo(company.getName()));


        session.getTransaction().rollback();
    }

    @Test
    void removeUserWithCompany() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.find(User.class, 19);
        session.remove(user);
        session.flush();

        User removedUser = session.find(User.class, 19);
        Company removedCompany = session.find(Company.class, 24);

        assertAll(
                () -> assertThat(removedUser).isNull(),
                () -> assertThat(removedCompany).isNull()
        );

        session.getTransaction().rollback();
    }
}