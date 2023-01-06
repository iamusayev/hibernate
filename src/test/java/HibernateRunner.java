import az.hibernate.repeat.entity.Payment;
import az.hibernate.repeat.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession();
             Session session1 = sessionFactory.openSession()) {
            session.beginTransaction();
            session1.beginTransaction();

            Payment payment = session.find(Payment.class, 1);
            payment.setAmount(payment.getAmount() + 10);
            Payment anotherPayment = session1.find(Payment.class, 1);
            anotherPayment.setAmount(anotherPayment.getAmount() + 10);

            session.getTransaction().commit();
            session1.getTransaction().commit();
        }
    }
}