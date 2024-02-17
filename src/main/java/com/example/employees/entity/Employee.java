package com.example.employees.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Employee {

    private Long employeeId;
    private Long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;


}
