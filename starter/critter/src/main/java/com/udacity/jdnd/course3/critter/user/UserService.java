package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Customer saveCustomer(Customer owner){
        return this.customerRepository.save(owner);
    }

    public Employee saveEmployee(Employee employee){
        return this.employeeRepository.save(employee);
    }

    public Employee findEmployeeById(Long id){
        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            return employee;
        }
        return optionalEmployee.orElseThrow(() -> new ResourceNotFoundException("Employee with id: "+ id + " not found"));
    }

    public Customer findCustomerById(Long id){
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            Customer owner = optionalCustomer.get();
            return owner;
        }
        return optionalCustomer.orElseThrow(() -> new ResourceNotFoundException("Customer with id: "+ id + " not found"));
    }

    public List<Customer> findAllCustomers(){
        return this.customerRepository.findAll();
    }


    public void updateDaysAvailable(Set<DayOfWeek> daysAvailable, Long id){
        Employee employee = this.findEmployeeById(id);

        employee.setDaysAvailable(daysAvailable);
        this.employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, LocalDate date){
        List<Employee> availableEmployees = new ArrayList<>();
        List<Employee> employees = this.employeeRepository.findEmployeesByDaysAvailable(date.getDayOfWeek());

        for (Employee employee : employees) {
            if (employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        }
        return availableEmployees;
    }
}
