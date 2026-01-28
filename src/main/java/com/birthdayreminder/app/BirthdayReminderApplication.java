package com.birthdayreminder.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.birthdayreminder.app.repository.PersonRepository;
import com.birthdayreminder.app.util.DataGenerator;

@SpringBootApplication
public class BirthdayReminderApplication {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BirthdayReminderApplication.class, args);


		// Рандомное заполнение БД
		PersonRepository rep = context.getBean(PersonRepository.class);
		for (int i = 0; i < 200; i++){
			rep.save(DataGenerator.generateRandomPerson());
		}
	}
}
