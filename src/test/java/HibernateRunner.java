import az.hibernate.repeat.dao.UserRepository;
import az.hibernate.repeat.mapper.CompanyReadMapper;
import az.hibernate.repeat.mapper.UserReadMapper;
import az.hibernate.repeat.service.UserService;
import az.hibernate.repeat.util.HibernateUtil;
import java.lang.reflect.Proxy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[] {Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));


            //Уже напрашивается использовать какой либо framework, что то вроде dependency injection у Spring'a, либо joose, hibernate не представляет такой функциональности
            session.beginTransaction();
            UserRepository userRepository = new UserRepository(session);
            UserReadMapper userReadMapper = new UserReadMapper(new CompanyReadMapper());
            UserService userService = new UserService(userRepository, userReadMapper);

            //Теперь мы безопасно можем вывести её на консольо потому что это DTO, только нужны данные вернуться нашему пользователю
            userService.findById(1).ifPresent(System.out::println);


            session.getTransaction().commit();
        }
    }
}