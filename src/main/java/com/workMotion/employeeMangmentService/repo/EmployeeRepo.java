package com.workMotion.employeeMangmentService.repo;

import com.workMotion.employeeMangmentService.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Employee repo that handle all database Transatactions for employee data
 *  @author Mounier Shokry
 */
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findOneByEmailIgnoreCase(String email);


}
