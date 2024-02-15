package com.example.employees.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair {
    private Long firstEmployeeId;
    private Long secondEmployeeId;
    private Long projectId;
    private Integer totalDuration;
}
