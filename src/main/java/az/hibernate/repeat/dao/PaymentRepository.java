package az.hibernate.repeat.dao;

import az.hibernate.repeat.entity.Payment;
import javax.persistence.EntityManager;
import org.hibernate.SessionFactory;

public class PaymentRepository extends RepositoryBase<Integer, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}