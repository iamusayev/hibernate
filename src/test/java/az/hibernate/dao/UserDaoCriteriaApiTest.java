package az.hibernate.dao;

import static org.assertj.core.api.Assertions.assertThat;

import az.hibernate.dto.CompanyDto;
import az.hibernate.repeat.dao.UserDaoCriteriaApi;
import az.hibernate.repeat.entity.Payment;
import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.util.HibernateUtil;
import java.util.List;
import javax.persistence.Tuple;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserDaoCriteriaApiTest {

    private static final UserDaoCriteriaApi userDaoCriteriaApi = UserDaoCriteriaApi.getInstance();
    private static final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();


    @Test
    void findAveragePaymentAmountByFirstAndLastname() {
        Double averagePaymentAmount =
                userDaoCriteriaApi.findAveragePaymentAmountByFirstNameAndLastName(sessionFactory.openSession(), "Bill", "Gates");
        assertThat(averagePaymentAmount).isEqualTo(300);
    }

    @Test
    void findCompanyNamesWithAvgUserPaymentsOrderByCompanyName() {
        List<Object[]> objects =
                userDaoCriteriaApi.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(sessionFactory.openSession());
        assertThat(objects).hasSize(3);
    }

    @Test
    void findCompanyNamesWithAvgUserPaymentsOrderedByCompanyNameWithDto() {
        List<CompanyDto> companyDtos =
                userDaoCriteriaApi.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyNameWithDto(sessionFactory.openSession());
        assertThat(companyDtos).hasSize(3);
        System.out.println(companyDtos);
    }

    @Test
    void isItPossible() {
        List<Tuple> usersAndAvgPayment = userDaoCriteriaApi.isItPossible(sessionFactory.openSession());
        assertThat(usersAndAvgPayment).hasSize(14);
    }

    @Nested
    static class EasyQueries {

        @Test
        void findAll() {
            List<User> users = userDaoCriteriaApi.findAll(sessionFactory.openSession());
            assertThat(users).hasSize(82);
        }

        @Test
        void findAllByFirstname() {
            List<User> users = userDaoCriteriaApi.findAllByFirstName(sessionFactory.openSession(), "Oxy");
            assertThat(users).hasSize(26);
        }

        @Test
        void findLimitedUserOrderByBirthdate() {
            List<User> users = userDaoCriteriaApi.findLimitedUsersOrderedByBirthday(sessionFactory.openSession(), 10);
            assertThat(users).hasSize(10);
        }

        @Test
        void findAllPaymentsByCompanyName() {
            List<Payment> users = userDaoCriteriaApi.findAllPaymentsByCompanyName(sessionFactory.openSession(), "Amazon");
            assertThat(users).isEmpty();
        }

        @Test
        void findAllByCompanyName() {
            List<User> users = userDaoCriteriaApi.findAllByCompanyName(sessionFactory.openSession(), "Amazon");
            assertThat(users).hasSize(1);
        }
    }
}