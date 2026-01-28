package com.birthdayreminder.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private List<PersonDTO> todayBirthdays;
    private List<PersonDTO> upcomingBirthdays;
    private Long totalCount;
    private Integer todayCount;
    private Integer upcomingCount;
}
