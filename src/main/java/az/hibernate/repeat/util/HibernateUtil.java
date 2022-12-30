package az.hibernate.repeat.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    public SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }
}