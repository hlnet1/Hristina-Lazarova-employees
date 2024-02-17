package com.example.employees.repository;

import com.example.employees.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EmployeeRepository {
private Map<Long,List<Employee>> emplByProjectId = new HashMap<>();

    public void save(Employee employee) {
        long projectId = employee.getProjectId();
        emplByProjectId.putIfAbsent(projectId, new ArrayList<>());
        emplByProjectId.get(projectId).add(employee);
    }

    public void saveAll(Set<Employee> employees) {
    employees.forEach(this::save);
        }

    public List<Employee> getEmployeesByProject(long projectId) {
        return emplByProjectId.get(projectId);
    }

    public List<Long> getAllProjects(){
        return emplByProjectId.keySet().stream().toList();
    }

    public void deleteAll(){
        emplByProjectId.clear();
    }


}
