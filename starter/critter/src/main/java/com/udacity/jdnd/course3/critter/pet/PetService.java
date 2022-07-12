package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.Exceptions.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository ownerRepository;

    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

    // finding the pet with the given id, and if it's not already recorded in DB throw proper exception
    public Pet getPetById(Long petId){
        Optional<Pet> optionalPet = this.petRepository.findById(petId);
        if(optionalPet.isPresent()){
            return optionalPet.get();
        }
        return optionalPet.orElseThrow(() -> new ResourceNotFoundException("Pet with id: "+ petId + " not found"));
    }

    public List<Pet> getPetsOfAnOwner(Long ownerId){
        return this.petRepository.findPetsByCustomerId(ownerId);

    }

    public List<Pet> findAllPets(){
        return this.petRepository.findAll();
    }
}
