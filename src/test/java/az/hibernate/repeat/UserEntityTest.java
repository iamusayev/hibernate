package az.hibernate.repeat;

import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.repeat.entity.Company;
import az.hibernate.repeat.entity.Profile;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.model.Role;
import az.hibernate.repeat.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void save() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .firstname("Test")
                .lastname("Test")
                .role(Role.ADMIN)
                .build();
        session.persist(user);
        session.flush();
        session.evict(user);

        assertThat(user.getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void saveUserWithProfile() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .firstname("Test")
                .lastname("Test")
                .profile(Profile.builder()
                        .street("Some street")
                        .language("ENG")
                        .build())
                .role(Role.ADMIN)
                .build();
        session.persist(user);
        session.flush();
        session.clear();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getProfile().getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void saveUserWithCompany() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .firstname("Test")
                .lastname("Test")
                .company(Company.builder()
                        .name("Amazon")
                        .build())
                .role(Role.ADMIN)
                .build();
        session.persist(user);
        session.flush();
        session.clear();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getCompany().getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void saveUserWithCompanyAndProfile() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .firstname("Test")
                .lastname("Test")
                .profile(Profile.builder()
                        .street("Some street")
                        .language("ENG")
                        .build())
                .company(Company.builder()
                        .name("Twitter")
                        .build())
                .role(Role.ADMIN)
                .build();
        session.persist(user);
        session.flush();
        session.clear();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getCompany().getId()).isNotNull();
        assertThat(user.getProfile().getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void findUser() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.find(User.class, 55);
        assertThat(user).isNotNull();


        session.getTransaction().commit();
    }
}