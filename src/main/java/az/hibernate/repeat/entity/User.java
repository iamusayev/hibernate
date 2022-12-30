package az.hibernate.repeat.entity;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import az.hibernate.repeat.model.Role;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@EqualsAndHashCode(exclude = "company")
@ToString(exclude = "company")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    @Enumerated(STRING)
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {PERSIST, REMOVE})
    private Company company;

    public void addCompany(Company company) {
        this.company = company;
    }
}