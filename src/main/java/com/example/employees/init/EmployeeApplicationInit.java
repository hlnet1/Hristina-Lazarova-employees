package com.example.employees.init;

import com.example.employees.entity.Employee;
import com.example.employees.factory.EmployeeFactory;
import com.example.employees.service.EmployeeService;
import com.example.employees.util.FileReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component()
public class EmployeeApplicationInit implements CommandLineRunner {

    public static final String FILE_PATH = "src/main/resources/input.txt";
    private final FileReader reader;
    private final EmployeeService service;

    public EmployeeApplicationInit(FileReader reader, EmployeeService service) {
        this.reader = reader;
        this.service = service;
    }

    @Override
    public void run(String... args)  {
        List<Employee> employees = this.reader.read(FILE_PATH)
                .stream()
                .map(EmployeeFactory::create)
                .collect(Collectors.toList());

        this.service.addAllEmployee(employees);
    }
}
