package az.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.util.HibernateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class SecondUserDaoTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();


    @Test
    void batchSize() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = session.createQuery("select u from User u join fetch u.payments join fetch u.company", User.class)
                .list();
        users.forEach(user -> System.out.println(user.getPayments().size()));
        users.forEach(user -> user.getCompany().getName());
        assertThat(users).isNotEmpty();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyFetchProfile() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.enableFetchProfile("withCompany");
        User user = session.find(User.class, 1);
        assertThat(user).isNotNull();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyAndPaymentsFetchProfile() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.enableFetchProfile("withCompanyAndPayments");
        User user = session.find(User.class, 1);
        assertThat(user).isNotNull();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyEntityGraphById() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        RootGraph<?> graph = session.getEntityGraph("WithCompany");
        Map<String, Object> properties = new HashMap<>();
        properties.put(GraphSemantic.FETCH.getJpaHintName(), graph);
        User user = session.find(User.class, 1, properties);
        assertThat(user).isNotNull();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyEntityGraphWithHQL() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        RootGraph<?> graph = session.getEntityGraph("WithCompany");

        List<User> users = session.createQuery("select u from User u join u.payments p", User.class)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .list();
        assertThat(users).isNotEmpty();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyAndPaymentsEntityGraphById() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        RootGraph<?> graph = session.getEntityGraph("WithCompanyAndPayments");
        Map<String, Object> properties = new HashMap<>();
        properties.put(GraphSemantic.FETCH.getJpaHintName(), graph);
        User user = session.find(User.class, 1, properties);
        assertThat(user).isNotNull();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyAndPaymentsEntityGraphWithHQL() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        RootGraph<?> graph = session.getEntityGraph("WithCompanyAndPayments");
        List<User> users = session.createQuery("select u from User u ", User.class)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .list();
        assertThat(users).isNotEmpty();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyAndCountriesById() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.find(User.class, 1);
        assertThat(user).isNotNull();

        session.getTransaction().commit();
    }

    @Test
    void userWithCompanyAndCountriesWithHQL() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Map<String, Object> properties = new HashMap<>();
        properties.put(GraphSemantic.FETCH.getJpaHintName(), session.getEntityGraph("WithCompanyAndCountries"));
        User user = session.find(User.class, 1, properties);
        assertThat(user).isNotNull();

        session.getTransaction().commit();
    }


    @Test
    void withCompanyAndPaymentsAndCountriesById() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = session.createQuery("select u from User u ", User.class)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), session.getEntityGraph("WithCompanyAndCountries"))
                .list();
        assertThat(users).isNotEmpty();

        session.getTransaction().commit();
    }


    @Test
    void withCompanyAndPaymentsAndCountriesWithHQL() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = session.createQuery("select u from User u", User.class)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), session.getEntityGraph("WithCompanyAndPaymentsAndCountries"))
                .list();
        assertThat(users).isNotEmpty();

        session.getTransaction().commit();

    }
}