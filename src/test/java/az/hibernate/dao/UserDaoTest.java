package az.hibernate.dao;

import az.hibernate.repeat.dao.UserDao;
import az.hibernate.repeat.util.HibernateUtil;
import az.hibernate.util.TestDataImporter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class UserDaoTest {

    private final SessionFactory session = HibernateUtil.buildSessionFactory();
    private final UserDao userDao = UserDao.getInstance();

    @BeforeAll
    public void initDb() {
        TestDataImporter.importData(session);
    }

    @Test
    void something() {
        List<Object[]> company =
                userDao.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(session.openSession());
        List<Object> collect = company.stream().flatMap(Arrays::stream).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    void isItPossible() {
        List<Object[]> objects = userDao.isItPossible(session.openSession());
        List<Object> collect = objects.stream().flatMap(Arrays::stream).collect(Collectors.toList());
        System.out.println(collect);
    }


    @AfterAll
    public void finish() {
        session.close();
    }

}