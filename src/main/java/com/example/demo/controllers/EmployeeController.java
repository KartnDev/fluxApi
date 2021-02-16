package com.example.demo.controllers;

import com.example.demo.dto.Employee;
import com.example.demo.repos.EmployeeDataService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeDataService employeeDataService;

    public EmployeeController(EmployeeDataService employeeDataService) {
        this.employeeDataService = employeeDataService;
    }

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
                .bodyToMono(new ParameterizedTypeReference<List<Employee>>() {
                })
                .map(t -> t.stream()
                        .map(Employee::getSalary)
                        .reduce(Integer::sum)
                ).map(e -> e.orElse(null));
    }

    @GetMapping("/findByName/{name}")
    private Flux<Employee> findByName(@PathVariable String name) {
        return WebClient.create()
                .get()
                .uri("http://localhost:8080/employees/getAll")
                .retrieve()
                .bodyToFlux(Employee.class)
                .filter(t -> t.getName().equals(name));
    }

}
