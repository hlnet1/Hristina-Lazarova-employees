package com.example.employees.service;

import com.example.employees.entity.Employee;
import com.example.employees.entity.Pair;
import com.example.employees.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void addAllEmployee(List<Employee> employees){
    this.employeeRepository.saveAll(employees);
    }

    public List<Employee> getAllEmployees(long projectId) {
        return this.employeeRepository.getEmployeesByProject(projectId);
    }

    public List<Long> getAllProjects() {
        List<Long> result = this.employeeRepository.getAllProjects();
        if(result.isEmpty()){
            throw new NoSuchElementException("No data");
        }
        return result;
    }

    public List<Pair> getOverlappingDurationPairs (long projectId){

        List<Employee> emplByProject = getAllEmployees(projectId);
        List<Pair> employeePairs = new ArrayList<>();
        if(emplByProject.size() > 1) {
            for (int i = 0; i < emplByProject.size() - 1; i++) {
                for (int j = i + 1; j < emplByProject.size(); j++) {
                    Employee firstEmployee = emplByProject.get(i);
                    Employee secondEmployee = emplByProject.get(j);
                    if (!Objects.equals(firstEmployee.getEmployeeId(), secondEmployee.getEmployeeId())
                            && hasOverlapping(firstEmployee, secondEmployee)) {
                        employeePairs.add(getPair(firstEmployee, secondEmployee));
                    }
                }
            }
            return employeePairs;
        }
        return List.of();
    }

    public List<Pair> maxOverlappingDurationPair (long projectId){
        List<Pair> employeePairsByProject = this.getOverlappingDurationPairs(projectId);
            return employeePairsByProject
                    .stream()
                    .sorted(Comparator.comparing(Pair::getTotalDuration).reversed())
                    .collect(Collectors.toList());

    }

    public List<Pair> maxOverlappingDurationPair (){
        List<Long> allProjectsById = this.getAllProjects();
        List<Pair> allPairs = new ArrayList<>();
        allProjectsById.forEach(projectId -> {
            List<Pair> pair = maxOverlappingDurationPair(projectId);
            if(!pair.isEmpty()) {
                allPairs.addAll(maxOverlappingDurationPair(projectId));
            }
        });
                allPairs
                .stream()
                .sorted(Comparator.comparing(Pair::getTotalDuration).reversed())
                        .collect(Collectors.toList());

                Integer maxDuration = !allPairs.isEmpty() ? allPairs.get(0).getTotalDuration() : 0;

                return this.getAllByEqualMaxOverlappingDuration(allPairs,maxDuration);
    }

    private boolean hasOverlapping(Employee firstEmployee, Employee secondEmployee) {
        return (firstEmployee.getDateFrom().isBefore(secondEmployee.getDateTo())
                || firstEmployee.getDateFrom().isEqual(secondEmployee.getDateTo()))
                && (firstEmployee.getDateTo().isAfter(secondEmployee.getDateFrom())
                || firstEmployee.getDateTo().isEqual(secondEmployee.getDateFrom()));
    }

    private Pair getPair(Employee firstEmployee, Employee secondEmployee) {
        return new Pair(firstEmployee.getEmployeeId(),
                secondEmployee.getEmployeeId(),
                firstEmployee.getProjectId(),
                calculateOverlappingDuration(firstEmployee,secondEmployee));
    }

    private List<Pair> getAllByEqualMaxOverlappingDuration(List<Pair> sortedPairs,Integer maxDuration) {
        if (sortedPairs.isEmpty()){
            throw new NoSuchElementException("No employees overlapping");
        }
        return sortedPairs
                .stream()
                .filter(pair -> Objects.equals(pair.getTotalDuration(), maxDuration))
                .collect(Collectors.toList());
    }

    private Integer calculateOverlappingDuration(Employee firstEmployee,Employee secondEmployee) {
        LocalDate periodStartDate =
                firstEmployee.getDateFrom().isBefore(secondEmployee.getDateFrom()) ?
                        secondEmployee.getDateFrom() : firstEmployee.getDateFrom();

        LocalDate periodEndDate =
                firstEmployee.getDateTo().isBefore(secondEmployee.getDateTo()) ?
                        firstEmployee.getDateTo() : secondEmployee.getDateTo();

        return Math.toIntExact(Math.abs(ChronoUnit.DAYS.between(periodStartDate, periodEndDate)));

    }

}
