package ivan.rest.example.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Employee implements Comparable<Employee> {

    @NotNull
    private Integer id;
    private String name;
    private String passportNumber;
    private String education;
    private Address address;
    private String employeeHash;

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }
}
