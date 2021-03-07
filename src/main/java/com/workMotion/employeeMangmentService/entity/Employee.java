package com.workMotion.employeeMangmentService.entity;

import com.workMotion.employeeMangmentService.utils.enums.EmployeeState;

import javax.persistence.*;

/**
 * Employee entity is a Pojo object to used to pereist employee information in database
 *  @author Mounier Shokry
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmployeeState employeeState;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public EmployeeState getEmployeeState() {
        return employeeState;
    }

    public void setEmployeeState(EmployeeState employeeState) {
        this.employeeState = employeeState;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", employeeState=" + employeeState +
                ", id=" + id +
                '}';
    }
}
