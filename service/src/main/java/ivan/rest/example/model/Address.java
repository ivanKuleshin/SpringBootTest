package ivan.rest.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Address {

    private String city;
    private String country;
    private String zip;

    @JsonIgnore
    public boolean isEmpty() {
        return this.equals(new Address());
    }
}
