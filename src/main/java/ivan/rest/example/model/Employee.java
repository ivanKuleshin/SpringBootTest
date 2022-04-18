package ivan.rest.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Employee implements Comparable<Employee> {
    private Integer id;
    private String name;
    private String passportNumber;
    private String education;

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }
}
