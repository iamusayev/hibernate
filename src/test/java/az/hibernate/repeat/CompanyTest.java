package az.hibernate.repeat;


import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.repeat.entity.Company;
import az.hibernate.repeat.util.HibernateUtil;
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

        Company company = session.get(Company.class, 3);
        session.remove(company);
        session.flush();
        Company deletedCompany = session.get(Company.class, 3);
        assertThat(deletedCompany).isNull();

        session.getTransaction().rollback();
    }
}