package com.workMotion.employeeMangmentService.utils.dto;

import com.workMotion.employeeMangmentService.utils.enums.EmployeeState;

/**
 * Employee DTO so that prevent using database entity
 * and to limit usage of insertion because user is not allowed to define state for added employee
 */
public class EmployeeDTO {
    private String name;
    private String email;
    private  Integer age;

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

}
