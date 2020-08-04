package plus.ojbk.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import plus.ojbk.querydsl.entity.Department;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, Long>, QuerydslPredicateExecutor<Department> {
}
