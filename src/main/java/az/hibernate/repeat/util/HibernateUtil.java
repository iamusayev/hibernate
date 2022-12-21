package az.hibernate.repeat.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory getSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }

}