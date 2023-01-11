package az.hibernate.repeat.dao;

import az.hibernate.repeat.entity.Company;
import javax.persistence.EntityManager;
import org.hibernate.SessionFactory;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}