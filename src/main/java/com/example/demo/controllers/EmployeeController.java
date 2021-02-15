package com.example.demo.controllers;

import com.example.demo.dto.Employee;
import com.example.demo.repos.EmployeeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeDataService employeeDataService;

    @GetMapping("/getAll")
    private Flux<Employee> getAll() {
        return employeeDataService
                .getAllEmployees()
                .delayElements(Duration.ofMillis(500))
                .doOnEach(System.out::println)
                .concatWithValues();
    }

    @GetMapping("/getTotal")
    private Mono<Integer> totalSalary() {
        return WebClient.create()
                .get()
                .uri("http://localhost:8080/employees/getAll")
                .retrieve()
                .to(Employee.class)
                .map(Employee::getSalary)
                .reduce(Integer::sum);
    }

}
