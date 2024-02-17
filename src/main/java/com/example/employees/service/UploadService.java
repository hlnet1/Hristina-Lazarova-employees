package com.example.employees.service;

import com.example.employees.entity.Employee;
import com.example.employees.entity.EmployeeCsvRepresentation;
import com.example.employees.repository.EmployeeRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvBadConverterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploadService {
    private final EmployeeRepository employeeRepository;

    public UploadService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Set<Employee> uploadEmployees(MultipartFile file){
        Set<Employee> employees = parseCsv(file);
        employeeRepository.saveAll(employees);
        return employees;
    }

    private Set<Employee> parseCsv(MultipartFile file)  {

        try {
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

            HeaderColumnNameMappingStrategy<EmployeeCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EmployeeCsvRepresentation.class);
            CsvToBean<EmployeeCsvRepresentation> csvToBean =
                    new CsvToBeanBuilder<EmployeeCsvRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> Employee.builder()
                            .employeeId(csvLine.getEmployeeId())
                            .projectId(csvLine.getProjectId())
                            .dateFrom(parseDate(csvLine.getDateFrom()))
                            .dateTo("NULL".equals(csvLine.getDateTo())
                                    ? LocalDate.now()
                                    : parseDate(csvLine.getDateTo()))
                            .build()
                    )
                    .collect(Collectors.toSet());
        } catch (IOException | CsvBadConverterException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDate parseDate(String date) {
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[MM/dd/yyyy]" + "[dd-MM-yyyy]" + "[yyyy-MM-dd]"));
        try {
            DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
            return LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            log.info(ex.getMessage());
        }
        return null;
    }


}
