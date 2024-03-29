package az.hibernate.repeat;


import az.hibernate.repeat.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class CompanyTest {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    void save() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("THE BEST COMPANY IN THE WORLD!")
                .build();
        session.save(company);

        assertThat(company.getId()).isNotNull();

        session.getTransaction().commit();
    }

    @Test
    void findById() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.find(Company.class, 8);

        assertThat(company).isNotNull();
    void test() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.get(User.class, 1);
        session.getTransaction().commit();
    }

}