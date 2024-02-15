package com.example.employees.web;

import com.example.employees.entity.Pair;
import com.example.employees.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/employees")
public class Controller {

    private final EmployeeService employeeService;

    public Controller(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Pair>> employeesMaxOverlapping(){
        return ResponseEntity.ok(this.employeeService.maxOverlappingDurationPair());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ NoSuchElementException.class })
    public void handleUnabletoReallocate(Exception ex) {
        log.error("Exception is: ", ex);
        // just return empty 501
    }
}
