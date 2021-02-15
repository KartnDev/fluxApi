package com.example.demo.repos;

import com.example.demo.dto.Employee;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Service
public interface EmployeeDataService {
    Flux<Employee> getAllEmployees();
}
