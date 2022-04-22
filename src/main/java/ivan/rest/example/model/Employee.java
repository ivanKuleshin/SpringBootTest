package ivan.rest.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee implements Comparable<Employee> {

//    @NotNull
    private Integer id;
    private String name;
    private String passportNumber;
    private String education;
    private Address address;

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }
}
