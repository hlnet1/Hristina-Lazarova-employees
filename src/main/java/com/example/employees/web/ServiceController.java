package com.example.employees.web;

import com.example.employees.entity.Pair;
import com.example.employees.service.EmployeeService;
import com.example.employees.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ServiceController {

    private final UploadService uploadService;
    private final EmployeeService employeeService;

    public ServiceController(UploadService uploadService, EmployeeService employeeService) {
        this.uploadService = uploadService;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {


        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        }
        try {
            uploadService.uploadEmployees(file);
//            Set<Employee> employees = uploadService.uploadEmployees(file);
//            employees.stream().forEach(empl ->{
//                model.addAttribute("employees",employees);
                model.addAttribute("status", true);
//            });

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }

//        return "file-upload-status";
        return "redirect:/employee-pairs";
    }

    @GetMapping("/employee-pairs")
    public String employeesMaxOverlapping(Model model) {
        try {
            List<Pair> pairs = this.employeeService.maxOverlappingDurationPair();
            pairs.forEach(pair -> {
                model.addAttribute("pairs", pairs);
                model.addAttribute("status", true);
            });
        }
        catch (Exception ex){
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("status", false);
        }


        return "employee-pairs";
    }

    @DeleteMapping()
    public String reset() {
    employeeService.deleteData();
        return "redirect:/";
    }
}

