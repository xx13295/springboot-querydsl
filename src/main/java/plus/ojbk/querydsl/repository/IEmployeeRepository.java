package plus.ojbk.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import plus.ojbk.querydsl.entity.Employee;
@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long>, QuerydslPredicateExecutor<Employee> {
}
