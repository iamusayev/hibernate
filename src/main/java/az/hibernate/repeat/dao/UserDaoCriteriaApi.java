package az.hibernate.repeat.dao;

import az.hibernate.dto.CompanyDto;
import az.hibernate.repeat.entity.Company;
import az.hibernate.repeat.entity.Company_;
import az.hibernate.repeat.entity.Payment;
import az.hibernate.repeat.entity.Payment_;
import az.hibernate.repeat.entity.PersonalInfo_;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.entity.User_;
import java.util.List;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoCriteriaApi {

    private static final UserDaoCriteriaApi INSTANCE = new UserDaoCriteriaApi();

    //1) * Returns all employees
    public List<User> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        return session.createQuery(criteria).list();
    }

    //2) * Returns all employees with the given name
    public List<User> findAllByFirstName(Session session, java.lang.String firstName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user)
                .where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));

        return session.createQuery(criteria).list();
    }

    //3) * Returns the first {limit} employees sorted by date of birth (in ascending order)
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    //4) * Returns all employees of the company with the specified name
    public List<User> findAllByCompanyName(Session session, String companyName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        Join<User, Company> company = user.join(User_.company);
        criteria.select(user).where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria)
                .list();
    }

    //5) * Returns all payouts received by employees of the company with the given name,
    //     sorted by employee name and then by payout size
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);

        criteria.select(payment)
                .where(cb.equal(company.get(Company_.name), company))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount)));

        return session.createQuery(criteria)
                .list();
    }


    //6) * Returns the average salary of an employee with the specified first and last name.
    public Double findAveragePaymentAmountByFirstNameAndLastName(Session session, String firstName, String lastName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        criteria.select(cb.avg(payment.get(Payment_.amount)))
                .where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName),
                        cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName)
                );

        return session.createQuery(criteria)
                .uniqueResult();
    }

    //7) * Returns for each company: name, average salary of all its employees. Companies are ordered by name.
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = cb.createQuery(Object[].class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);

        criteria.multiselect(company.get(Company_.name), cb.avg(payment.get(Payment_.amount)))
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria).list();
    }

    //7) * Returns for each company: name, average salary of all its employees. Companies are ordered by name with dto.
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyNameWithDto(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CompanyDto> criteria = cb.createQuery(CompanyDto.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);

        criteria.select(cb.construct(CompanyDto.class,
                        company.get(Company_.name),
                        cb.avg(payment.get(Payment_.amount))))
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));


        return session.createQuery(criteria).list();

    }

    //8) * Returns a list: employee (User object), average pay, but only for those employees whose average pay is greater than the average of all employees.
    //   * Sort by employee name
    public List<Tuple> isItPossible(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<User> user = criteria.from(User.class);
        ListJoin<User, Payment> payment = user.join(User_.payments);

        Subquery<Double> subquery = criteria.subquery(Double.class);
        Root<Payment> paymentSubquery = subquery.from(Payment.class);

        criteria.select(cb.tuple(user, cb.avg(payment.get(Payment_.amount))))
                .groupBy(user.get(User_.id))
                .having(cb.ge(cb.avg(payment.get(Payment_.amount)),
                        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }

    public static UserDaoCriteriaApi getInstance() {
        return INSTANCE;
    }
}