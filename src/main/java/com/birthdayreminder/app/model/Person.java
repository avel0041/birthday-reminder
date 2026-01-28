package com.birthdayreminder.app.model;

import java.time.LocalDate;
import java.time.MonthDay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="People")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person{
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;
    
    @Column(nullable = false)
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;
    
    @Column(nullable = false)
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;
    
    // Метод для получения текущего возраста
    public Integer getAge() {
        LocalDate today = LocalDate.now();
        return (MonthDay.from(today).isBefore(MonthDay.from(birthDate)))?
        LocalDate.now().getYear() - birthDate.getYear() - 1
        :LocalDate.now().getYear() - birthDate.getYear();
    }
    
    // Метод для получения даты (LocalDate) следующего дня рождения
    public LocalDate getNextBirthday() {
        LocalDate today = LocalDate.now();
        LocalDate thisYearBirthday = birthDate.withYear(today.getYear());
        
        if (thisYearBirthday.isBefore(today) || thisYearBirthday.isEqual(today)) {
            return thisYearBirthday.plusYears(1);
        }
        return thisYearBirthday;
    }
    
    // Метод для проверки, сегодня ли день рождения
    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return birthDate.getMonth() == today.getMonth() 
            && birthDate.getDayOfMonth() == today.getDayOfMonth();
    }
}