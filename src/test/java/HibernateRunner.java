import az.hibernate.repeat.entity.User;
import az.hibernate.repeat.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                User user = session.find(User.class, 1);
                user.setUsername(user.getUsername() + "Another one");
                session.getTransaction().commit();
            }
        }
    }
}
