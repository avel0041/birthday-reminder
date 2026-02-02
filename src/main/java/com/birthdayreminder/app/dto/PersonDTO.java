package com.birthdayreminder.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String photoUrl;
    private Integer age;
    private LocalDate nextBirthday;
    private Boolean birthdayToday;
    private Integer daysUntilBirthday;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}