package com.birthdayreminder.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.birthdayreminder.app.dto.DashboardDTO;
import com.birthdayreminder.app.dto.PersonDTO;
import com.birthdayreminder.app.service.BirthdayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/birthdays")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BirthdayController {
    private final BirthdayService birthdayService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        try {
            return ResponseEntity.ok(birthdayService.getDashboardData());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка загрузки dashboard", e);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        try {
            return ResponseEntity.ok(birthdayService.getAllPersons());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка загрузки дней рождений", e);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getBirthdayById(@PathVariable Long id) {
        try {
            List<PersonDTO> all = birthdayService.getAllPersons();
            return all.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка загрузки дня рождения", e);
        }
    }

    @GetMapping("/today")
    public ResponseEntity<List<PersonDTO>> getTodayBirthdays() {
        try {
            DashboardDTO dashboard = birthdayService.getDashboardData();
            return ResponseEntity.ok(dashboard.getTodayBirthdays());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка загрузки сегодняшних дней рождений", e);
        }
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<PersonDTO>> getUpcomingBirthdays() {
        try {
            DashboardDTO dashboard = birthdayService.getDashboardData();
            return ResponseEntity.ok(dashboard.getUpcomingBirthdays());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка загрузки ближайших дней рождений", e);
        }
    }
    
    @GetMapping("/next/{days}")
    public ResponseEntity<List<PersonDTO>> getBirthdaysInNextDays(@PathVariable int days) {
        try {
            return ResponseEntity.ok(birthdayService.getBirthdaysInNextDays(days));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка загрузки дней рождений", e);
        }
        
    }
    
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        try {
            return ResponseEntity.ok(birthdayService.createPerson(personDTO));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Ошибка создания дня рождения: " + e.getMessage(), e);
        }
    }
    
    @PostMapping("/{id}/photo")
    public ResponseEntity<PersonDTO> uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка загрузки фото");
        }

        try {
            Path uploadDir = Paths.get("uploads", "people");
            Files.createDirectories(uploadDir);

            String extension = getExtension(photo.getOriginalFilename());
            String fileName = id + extension;
            Path targetPath = uploadDir.resolve(fileName).normalize();

            Files.copy(photo.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String photoUrl = "/uploads/people/" + fileName;
            return ResponseEntity.ok(birthdayService.updatePhoto(id, photoUrl));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Ошибка чтения файла", e);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фото не найдено", e);
            }
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable Long id,
            @RequestBody PersonDTO personDTO) {
        try {
            return ResponseEntity.ok(birthdayService.updatePerson(id, personDTO));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "День рождения не найден", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Ошибка обновления дня рождения: " + e.getMessage(), e);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        try {
            birthdayService.deletePerson(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "День рождения не найден", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Ошибка удаления дня рождения: " + e.getMessage(), e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return ".jpg";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return ".jpg";
        }
        String ext = filename.substring(dotIndex).toLowerCase();
        if (!ext.matches("\\.[a-z0-9]{1,10}")) {
            return ".jpg";
        }
        return ext;
    }
}
