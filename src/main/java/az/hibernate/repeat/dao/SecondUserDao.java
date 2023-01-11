package az.hibernate.repeat.dao;

import az.hibernate.dto.CompanyDto;
import az.hibernate.dto.UserDto;
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
public class SecondUserDao {

    private static final SecondUserDao INSTANCE = new SecondUserDao();

    /**
     * Возвращает всех сотрудников
     */

    public List<User> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */

    public List<User> findAllByFirstname(Session session, String name) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user)
                .where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), name));

        return session.createQuery(criteria)
                .list();

    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */

    public List<User> findLimitedUserOrderByBirthdate(Session session, int limit) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    //4

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */

    public List<User> findAllByCompanyName(Session session, String companyName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        Join<User, Company> company = user.join(User_.company);

        criteria.select(user)
                .where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria)
                .list();
    }

    //5

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанным именем, упорядоченные по имени сотрудника, а затем по размеру выплатыю
     */


    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);

        criteria.select(payment)
                .where(cb.equal(company.get(Company_.name), companyName))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount)));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией.
     */
    //6
    public Double findAveragePaymentAmountByFirstAndLastname(Session session, String firstName, String lastName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        criteria.select(cb.avg(payment.get(Payment_.amount)))
                .where(
                        cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName),
                        cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));

        return session.createQuery(criteria)
                .uniqueResult();
    }


    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */

    //7
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderByCompanyName(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = cb.createQuery(Object[].class);
        Root<User> user = criteria.from(User.class);
        Join<User, Company> company = user.join(User_.company);
        ListJoin<User, Payment> payment = user.join(User_.payments);

        criteria.multiselect(company.get(Company_.name), cb.avg(payment.get(Payment_.amount)))
                .groupBy(company.get(Company_.name));

        return session.createQuery(criteria)
                .list();
    }

    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderByCompanyNameWithCompanyDto(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CompanyDto> criteria = cb.createQuery(CompanyDto.class);
        Root<User> user = criteria.from(User.class);
        Join<User, Company> company = user.join(User_.company);
        ListJoin<User, Payment> payment = user.join(User_.payments);

        criteria.select(cb.construct(CompanyDto.class, company.get(Company_.name), cb.avg(payment.get(Payment_.amount))))
                .groupBy(company.get(Company_.name));

        return session.createQuery(criteria)
                .list();
    }

    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderByCompanyNameWithTuple(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<User> user = criteria.from(User.class);
        Join<User, Company> company = user.join(User_.company);
        ListJoin<User, Payment> payment = user.join(User_.payments);

        criteria.select(cb.tuple(company.get(Company_.name), cb.avg(payment.get(Payment_.amount))))
                .groupBy(company.get(Company_.name));

        return session.createQuery(criteria)
                .list();
    }


    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат больше среднего размера всех сотрудников.
     * Упорядочить по имени сотрудника
     */

    public List<Object[]> isItPossible(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = cb.createQuery(Object[].class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        Subquery<Double> subQuery = criteria.subquery(Double.class);
        Root<Payment> paymentSubQuery = subQuery.from(Payment.class);

        criteria.multiselect(user, cb.avg(payment.get(Payment_.amount)))
                .groupBy(user.get(User_.id))
                .having(cb.ge(cb.avg(payment.get(Payment_.amount)),
                        subQuery.select(cb.avg(paymentSubQuery.get(Payment_.amount)))))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }

    public List<Tuple> isItPossibleWithTuple(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        Subquery<Double> subQuery = criteria.subquery(Double.class);
        Root<Payment> paymentSubQuery = subQuery.from(Payment.class);

        criteria.select(cb.tuple(user, cb.avg(payment.get(Payment_.amount))))
                .groupBy(user.get(User_.id))
                .having(cb.ge(cb.avg(payment.get(Payment_.amount)),
                        subQuery.select(cb.avg(paymentSubQuery.get(Payment_.amount)))))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }

    public List<UserDto> isItPossibleWithDto(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserDto> criteria = cb.createQuery(UserDto.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        Subquery<Double> subQuery = criteria.subquery(Double.class);
        Root<Payment> paymentSubQuery = subQuery.from(Payment.class);

        criteria.select(cb.construct(UserDto.class, user, cb.avg(payment.get(Payment_.amount))))
                .groupBy(user.get(User_.id))
                .having(cb.ge(cb.avg(payment.get(Payment_.amount)),
                        subQuery.select(cb.avg(paymentSubQuery.get(Payment_.amount)))
                ))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }

    public static SecondUserDao getInstance() {
        return INSTANCE;
    }
}

