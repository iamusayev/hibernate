package az.hibernate.repeat.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import az.hibernate.repeat.model.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfile.FetchOverride;

@Data
@EqualsAndHashCode(exclude = {"company", "profile", "payments"})
@ToString(exclude = {"company", "profile", "payments"})
@NamedEntityGraph(name = "WithCompanyAndCountries", attributeNodes = {
        @NamedAttributeNode(value = "company", subgraph = "country")
}, subgraphs = @NamedSubgraph(name = "country", attributeNodes = @NamedAttributeNode("countries")))
@NamedEntityGraph(name = "WithCompanyAndPaymentsAndCountries", attributeNodes = {
        @NamedAttributeNode(value = "company", subgraph = "country"),
        @NamedAttributeNode(value = "payments")
}, subgraphs = @NamedSubgraph(name = "country", attributeNodes = @NamedAttributeNode("countries")))
@NamedEntityGraph(name = "WithCompany", attributeNodes = {
        @NamedAttributeNode(value = "company")
})
@NamedEntityGraph(name = "WithCompanyAndPayments", attributeNodes = {
        @NamedAttributeNode(value = "company"),
        @NamedAttributeNode(value = "payments")
})
@FetchProfile(name = "withCompany", fetchOverrides = {
        @FetchOverride(entity = User.class, association = "company", mode = FetchMode.JOIN)
})
@FetchProfile(name = "withCompany", fetchOverrides = {
        @FetchOverride(entity = User.class, association = "company", mode = FetchMode.JOIN),
        @FetchOverride(entity = User.class, association = "payments", mode = FetchMode.JOIN)
})
@Data
@EqualsAndHashCode(exclude = {"company", "payments"})
@ToString(exclude = {"company", "payments"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String username;
    private String info;
    private PersonalInfo personalInfo;
    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToOne(mappedBy = "user", orphanRemoval = true, fetch = LAZY, cascade = CascadeType.PERSIST)
    private Profile profile;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder.Default
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();
}
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();
}

