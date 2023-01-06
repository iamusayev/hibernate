package az.hibernate.repeat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Data
@EqualsAndHashCode(exclude = "receiver")
@ToString(exclude = "receiver")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer amount;

    @Version
    private Long version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}