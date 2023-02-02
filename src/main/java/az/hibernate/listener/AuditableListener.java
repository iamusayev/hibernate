package az.hibernate.listener;

import static java.time.Instant.now;

import az.hibernate.repeat.entity.Payment;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditableListener {

    @PrePersist
    public void prePersist(Object entity) {
        ((Payment) entity).setCreatedAt(now());
        ((Payment) entity).setCreatedBy("Security Context");
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        ((Payment) entity).setUpdatedAt(now());
        ((Payment) entity).setUpdatedBy("Security Context");
    }
}
