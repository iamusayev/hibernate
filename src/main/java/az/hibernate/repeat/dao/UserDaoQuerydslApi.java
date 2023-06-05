package az.hibernate.repeat.dao;

import static az.hibernate.repeat.entity.QCompany.company;
import static az.hibernate.repeat.entity.QPayment.payment;
import static az.hibernate.repeat.entity.QUser.user;

import az.hibernate.QPredicates;
import az.hibernate.repeat.entity.Payment;
import az.hibernate.repeat.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoQuerydslApi {

    private static final UserDaoQuerydslApi INSTANCE = new UserDaoQuerydslApi();

    public List<User> findAll(Session session) {
        return new JPAQuery<>(session)
                .select(user)
                .from(user)
                .fetch();
    }


    public List<User> findAllByFirstname(Session session, String firstname) {
        return new JPAQuery<>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstname.eq(firstname))
                .fetch();
    }

    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        return new JPAQuery<>(session)
                .select(user)
                .from(user)
                .orderBy(user.personalInfo.birthday.asc())
                .limit(limit)
                .fetch();
    }

    public List<User> findAllByCompanyName(Session session, String companyName) {
        return new JPAQuery<>(session)
                .select(user)
                .from(company)
                .join(company.users, user)
                .where(company.name.eq(companyName))
                .fetch();
    }

    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return new JPAQuery<>(session)
                .select(payment)
                .from(payment)
                .join(payment.receiver, user)
                .join(user.company, company)
                .where(company.name.eq(companyName))
                .orderBy(user.personalInfo.firstname.asc(), payment.amount.asc())
                .fetch();
    }

    public Double findAveragePaymentAmountByFirstNameAndLastName(Session session, String firstName, String lastName) {

        Predicate predicate = QPredicates.builder()
                .add(firstName, user.personalInfo.firstname::eq)
                .add(lastName, user.personalInfo.lastname::eq)
                .buildAnd();

        return new JPAQuery<Double>(session)
                .select(payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                .where(predicate).fetchOne();
    }

    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        return new JPAQuery<Tuple>(session)
                .select(company.name, payment.amount.avg())
                .from(company)
                .join(company.users, user)
                .join(user.payments, payment)
                .groupBy(company.name)
                .orderBy(company.name.asc())
                .fetch();
    }

    public List<Tuple> isItPossible(Session session) {
        return new JPAQuery<Tuple>(session)
                .select(user, payment.amount.avg())
                .from(user)
                .join(user.payments, payment)
                .groupBy(user.id)
                .having(payment.amount.avg().gt(new JPAQuery<Double>(session)
                        .select(payment.amount.avg())
                        .from(payment)))
                .orderBy(user.personalInfo.firstname.asc())
                .fetch();
    }


    public static UserDaoQuerydslApi getInstance() {
        return INSTANCE;
    }

}
