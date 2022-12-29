package az.hibernate.repeat;

import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.repeat.entity.Profile;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.model.Role;
import az.hibernate.repeat.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class ProfileEntityTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();


    @Test
    void persistProfileWithUser() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Profile profile = Profile.builder()
                .language("ENG")
                .street("58 Street")
                .build();

        profile.setUser(User.builder()
                .role(Role.ADMIN)
                .build());

        session.persist(profile);
        session.flush();
        session.clear();

        Profile savedProfile = session.find(Profile.class, profile.getId());

        assertThat(savedProfile.getId()).isNotNull();
        assertThat(Hibernate.unproxy(savedProfile.getUser().getId())).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void findProfile() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Profile profile = session.find(Profile.class, 5);
        Integer userId = profile.getUser().getId();
        assertThat(profile).isNotNull();
        assertThat(userId).isNotNull();
        assertThat(userId).isEqualTo(Integer.valueOf(56));

        session.getTransaction().commit();
    }
}