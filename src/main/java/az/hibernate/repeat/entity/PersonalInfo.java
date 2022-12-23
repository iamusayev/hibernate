package az.hibernate.repeat.entity;

import com.sun.xml.internal.ws.developer.Serialization;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {

    @Serialization
    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;
    private LocalDate birthday;
}