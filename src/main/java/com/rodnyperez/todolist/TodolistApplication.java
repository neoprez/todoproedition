package com.rodnyperez.todolist;

import com.rodnyperez.todolist.data.TodoListDto;
import com.rodnyperez.todolist.repo.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Component
	public static class DataFeeder implements ApplicationRunner {

		@Autowired
		private TodoListRepository repository;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			TodoListDto dto = new TodoListDto();
			dto.setTitle("My First List");
			dto.setCreatedDateTime(LocalDateTime.now());
			dto.setUpdatedAt(LocalDateTime.now());
			repository.save(dto);
		}
	}

}
