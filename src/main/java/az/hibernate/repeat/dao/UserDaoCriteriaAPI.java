package az.hibernate.repeat.dao;

import az.hibernate.repeat.entity.Payment;
import az.hibernate.repeat.entity.User;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoCriteriaAPI {

    private static final UserDaoCriteriaAPI INSTANCE = new UserDaoCriteriaAPI();

    //1) * Returns all employees
    public List<User> findAll(Session session) {
        return Collections.emptyList();
    }

    //2) * Returns all employees with the given name
    public List<User> findAllByFirstName(Session session, String firstName) {
        return Collections.emptyList();
    }

    //3) * Returns the first {limit} employees sorted by date of birth (in ascending order)
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        return Collections.emptyList();
    }

    //4) * Returns all employees of the company with the specified name
    public List<User> findAllByCompanyName(Session session, String companyName) {
        return Collections.emptyList();
    }

    //5) * Returns all payouts received by employees of the company with the given name,
    //     sorted by employee name and then by payout size
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return Collections.emptyList();
    }

    //6) * Returns the average salary of an employee with the specified first and last name.
    public Double findAveragePaymentAmountByFirstNameAndLastName(Session session, String firstName, String lastName) {
        return Double.MIN_NORMAL;
    }

    //7) * Returns for each company: name, average salary of all its employees. Companies are ordered by name.
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session, String companyName) {
        return Collections.emptyList();
    }

    //8) * Returns a list: employee (User object), average pay, but only for those employees whose average pay is greater than the average of all employees.
    //   * Sort by employee name
    public List<Object[]> isItPossible(Session session) {
        return Collections.emptyList();
    }

    public static UserDaoCriteriaAPI getInstance() {
        return INSTANCE;
    }
}
