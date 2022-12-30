package az.hibernate.repeat;


import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.repeat.entity.Company;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.model.Role;
import az.hibernate.repeat.util.HibernateUtil;
import java.util.List;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

class CompanyTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    @Test
    void persist() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Google")
                .build();

        session.persist(company);
        session.evict(company);
        Company savedCompany = session.find(Company.class, company.getId());

        assertThat(savedCompany).isNotNull();
        assertThat(savedCompany.getId()).isEqualTo(company.getId());
        assertThat(savedCompany.getName()).isEqualTo(company.getName());

        session.getTransaction().commit();
    }

    @Test
    void remove() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.find(Company.class, 3);
        session.remove(company);
        session.flush();
        Company deletedCompany = session.find(Company.class, 3);
        assertThat(deletedCompany).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void findCompanyWithAllUsers() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.find(Company.class, 3);

        session.getTransaction().commit();
    }

    @Test
    void saveCompanyWithUser() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Google")
                .build();
        company.addUser(new User(null, "John", "Wick", Role.USER, null));
        session.persist(company);

        session.getTransaction().commit();
    }

    @Test
    void removeCompanyWithAllUsers() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.find(Company.class, 3);
        session.remove(company);
        session.flush();

        Company removedCompany = session.find(Company.class, 3);
        List<User> usersByCompanyId = session.createQuery("select u from User u where u.company.id =:companyId", User.class)
                .setParameter("companyId", company.getId())
                .getResultList();

        assertThat(removedCompany).isNull();
        assertThat(usersByCompanyId).isEmpty();

        session.getTransaction().rollback();
    }
}