package hr.betaSoft.repository;

import hr.betaSoft.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findById(long id);
}
