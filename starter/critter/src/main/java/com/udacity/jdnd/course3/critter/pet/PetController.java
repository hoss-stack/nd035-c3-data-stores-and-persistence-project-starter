package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        // in order for copyProperties to work, properties of the DTO and normal object must match in name
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getCustomer() != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO){
        ModelMapper modelMapper = new ModelMapper();
        Pet pet = modelMapper.map(petDTO, Pet.class);
        return pet;
    }
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = null;

        if (petDTO.getOwnerId() != 0) {
            customer = this.userService.findCustomerById(petDTO.getOwnerId());
        }

        Pet pet = convertPetDTOToPet(petDTO);
        pet.setCustomer(customer);
        Pet savedPet = petService.savePet(pet);

        if (customer != null) {
            customer.addPet(savedPet);
        }
        return convertPetToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(this.petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = this.petService.findAllPets();
        List<PetDTO> petsDTO = new ArrayList<>();
        for(Pet pet : pets){
            petsDTO.add(convertPetToPetDTO(pet));
        }
        return petsDTO;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = this.petService.getPetsOfAnOwner(ownerId);
        List<PetDTO> petsDTO = new ArrayList<>();
        for(Pet pet : pets){
            petsDTO.add(convertPetToPetDTO(pet));
        }
        return petsDTO;
    }
}
