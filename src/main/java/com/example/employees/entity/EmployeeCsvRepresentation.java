package com.example.employees.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCsvRepresentation {
    @CsvBindByName(column = "EmpID")
    private Long employeeId;
    @CsvBindByName(column = "ProjectID")
    private Long projectId;
    @CsvBindByName(column = "DateFrom")
    private String dateFrom;
    @CsvBindByName(column = "DateTo")
    private String dateTo;
}
