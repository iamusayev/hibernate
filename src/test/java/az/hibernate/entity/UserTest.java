package az.hibernate.entity;

import static az.hibernate.repeat.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.entity.User_;
import az.hibernate.repeat.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class UserTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();


    @Test
    void queryFetchHQL() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = session.createQuery("select u from User u join fetch  u.company", User.class)
                .list();
        assertThat(users).hasSize(5);

        session.getTransaction().commit();
    }

    @Test
    void queryFetchCriteriaAPI() {
        @Cleanup Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        user.fetch(User_.company);

        criteria.select(user);

        List<User> users = session.createQuery(criteria)
                .list();

        assertThat(users).hasSize(5);
    }

    @Test
    void queryFetchQueryDsl() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = new JPAQuery<>(session)
                .select(user)
                .from(user)
                .join(user.company).fetchJoin()
                .fetch();

        assertThat(users).hasSize(5);

        session.getTransaction().commit();
    }
}