package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.Exceptions.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Schedule saveSchedule(Schedule schedule){
        return this.scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return this.scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPetId(Long petId){

        return this.scheduleRepository.getScheduleByPetsId(petId);
    }

    // finding the customer with the given id, and if it's not already recorded in DB throw proper exception
    public List<Schedule> getScheduleByCustomerId(Long customerId) {
        Optional<Customer> optionalCustomer = this.customerRepository.findById(customerId);

        if (!optionalCustomer.isPresent()) {
            throw new ResourceNotFoundException("No schedule found for the given owner id: "+ customerId);
        }
        else{
            Customer customer = optionalCustomer.get();
            List<Pet> pets = customer.getPets();
            List<Schedule> schedules = new ArrayList<>();

            for (Pet pet : pets) {
                schedules.addAll(scheduleRepository.getScheduleByPetsId(pet.getId()));
            }
            return schedules;
        }
    }

    public List<Schedule> getScheduleByEmployee(Long employeeId) {
        return scheduleRepository.getScheduleByEmployeesId(employeeId);
    }
}
