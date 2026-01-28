package com.birthdayreminder.app.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.birthdayreminder.app.dto.DashboardDTO;
import com.birthdayreminder.app.dto.PersonDTO;
import com.birthdayreminder.app.model.Person;
import com.birthdayreminder.app.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BirthdayService {
    private final PersonRepository personRepository;
    
    private PersonDTO convertToDTO(Person person) {
        return PersonDTO.builder()
            .id(person.getId())
            .firstName(person.getFirstName())
            .lastName(person.getLastName())
            .birthDate(person.getBirthDate())
            .age(person.getAge())
            .nextBirthday(person.getNextBirthday())
            .birthdayToday(person.isBirthdayToday())
            .daysUntilBirthday((int) ChronoUnit.DAYS.between(
                LocalDate.now(), 
                person.getNextBirthday()))
            .build();
    }
    
    public DashboardDTO getDashboardData() {
        List<Person> todayBirthdays = personRepository.findTodayBirthdays();
        List<Person> upcomingBirthdays = personRepository.findUpcomingBirthdays();
        
        List<PersonDTO> todayDTOs = todayBirthdays.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
            
        List<PersonDTO> upcomingDTOs = upcomingBirthdays.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return new DashboardDTO(
            todayDTOs,
            upcomingDTOs,
            personRepository.count(),
            todayDTOs.size(),
            upcomingDTOs.size()
        );
    }
    
    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = Person.builder()
            .firstName(personDTO.getFirstName())
            .lastName(personDTO.getLastName())
            .birthDate(personDTO.getBirthDate())
            .build();
            
        Person saved = personRepository.save(person);
        return convertToDTO(saved);
    }
    
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        Person person = personRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Person not found"));
            
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setBirthDate(personDTO.getBirthDate());
        
        return convertToDTO(personRepository.save(person));
    }
    
    @Transactional
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
    
    public List<PersonDTO> getBirthdaysInNextDays(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        
        return personRepository.findBirthdaysBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}
