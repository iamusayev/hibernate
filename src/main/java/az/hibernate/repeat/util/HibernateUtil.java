package az.hibernate.repeat.util;

import az.hibernate.listener.GlobalInterceptor;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
        Configuration configuration = buildConfiguration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setInterceptor(new GlobalInterceptor());
        return configuration;
    }

}