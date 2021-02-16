package com.example.demo.repos;

import com.example.demo.dto.Employee;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.LinkedList;

@Service
public class EmployeeData implements EmployeeDataService {

    private Flux<Employee> employeeDataFlux = Flux.empty();

    @Override
    public Flux<Employee> getAllEmployees() {
        employeeDataFlux = Flux.concat(employeeDataFlux, Flux.fromIterable(emitBatch(0, 5)));

        employeeDataFlux = Flux.concat(employeeDataFlux, Flux.fromIterable(emitBatch(6, 15)));

        employeeDataFlux = Flux.concat(employeeDataFlux, Flux.fromIterable(emitBatch(55, 58)));

        return employeeDataFlux;
    }

    private Collection<Employee> emitBatch(int from, int to){
        Collection<Employee> employees = new LinkedList<>();

        for (int i = from; i < to; i++) {
            Employee created = new Employee("Name" + i,
                    "Surname" + i,
                    "Java Dev",
                    i*100);

            employees.add(created);
        }

        return employees;
    }

}
