package com.example.employees.repository;

import com.example.employees.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepository {
private Map<Long,List<Employee>> emplByProjectId = new HashMap<>();

    public void save(Employee employee) {
        long projectId = employee.getProjectId();
        emplByProjectId.putIfAbsent(projectId, new ArrayList<>());
        emplByProjectId.get(projectId).add(employee);
    }

    public void saveAll(List<Employee> employees) {
    employees.stream().forEach(empl -> this.save(empl));
        }

    public List<Employee> getEmployeesByProject(long projectId) {
        return emplByProjectId.get(projectId);
    }

    public List<Long> getAllProjects(){
        return emplByProjectId.keySet().stream().toList();
    }


}
