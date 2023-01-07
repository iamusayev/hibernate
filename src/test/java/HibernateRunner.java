import az.hibernate.repeat.entity.Payment;
import az.hibernate.repeat.util.HibernateUtil;
import az.hibernate.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession();) {
            session.beginTransaction();
            TestDataImporter.importData(sessionFactory);
            Payment payment = new Payment(null, 100, null, null, null, null);
            session.save(payment);
            session.getTransaction().commit();
        }
    }
}