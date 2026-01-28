package com.birthdayreminder.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.birthdayreminder.app.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
    @Query("SELECT p FROM Person p WHERE (MONTH(p.birthDate) = MONTH(CURRENT_DATE) AND DAY(p.birthDate) = DAY(CURRENT_DATE))")
    List<Person> findTodayBirthdays();

    @Query("SELECT p FROM Person p " +
           "WHERE (MONTH(p.birthDate) > MONTH(CURRENT_DATE) " +
           "OR (MONTH(p.birthDate) = MONTH(CURRENT_DATE) " +
           "AND DAY(p.birthDate) >= DAY(CURRENT_DATE))) " +
           "AND (p.birthDate NOT IN (SELECT p2.birthDate FROM Person p2 " +
           "WHERE MONTH(p2.birthDate) = MONTH(CURRENT_DATE) " +
           "AND DAY(p2.birthDate) = DAY(CURRENT_DATE))) " +
           "ORDER BY MONTH(p.birthDate), DAY(p.birthDate)")
    List<Person> findUpcomingBirthdays();
    
    // Поиск дней рождения в диапазоне дат
    @Query("SELECT p FROM Person p " +
           "WHERE (MONTH(p.birthDate), DAY(p.birthDate)) " +
           "BETWEEN (MONTH(:startDate), DAY(:startDate)) " +
           "AND (MONTH(:endDate), DAY(:endDate))")
    List<Person> findBirthdaysBetween(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    // Поиск по имени или фамилии
    List<Person> findByFirstNameContainingOrLastNameContaining(
        String firstName, String lastName);
}
