package com.example.employees.factory;

import com.example.employees.entity.Employee;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
@Slf4j
public final class EmployeeFactory {

    public static Employee create(String line) {

        String[] recordArgs = line.split(", ");

        long emplID = Long.parseLong(recordArgs[0].trim());

        long projectID = Long.parseLong(recordArgs[1].trim());

            LocalDate dateFrom = parseDate(recordArgs[2]);
//        LocalDate dateFrom = DateUtils.parseDateStrictly(recordArgs[2],new String[]{"yyyy/MM/dd", "dd/MM/yyyy", "yyyy-MM-dd"});

        LocalDate dateTo;
        if (recordArgs[3] == null || "NULL".equals(recordArgs[3])) {
            dateTo = LocalDate.now();
        } else {
            dateTo = parseDate(recordArgs[3]);
        }

        return new Employee(
                emplID,
                projectID,
                dateFrom,
                dateTo
        );
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
