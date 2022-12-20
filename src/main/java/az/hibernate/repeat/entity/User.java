package az.hibernate.repeat.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import az.hibernate.repeat.model.Role;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = {"company", "profile"})
@ToString(exclude = {"company", "profile"})
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private Profile profile;

    public void deleteProfile(Profile profile) {
        this.profile = null;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}