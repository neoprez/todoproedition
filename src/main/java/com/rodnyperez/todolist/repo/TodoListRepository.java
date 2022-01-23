package com.rodnyperez.todolist.repo;

import com.rodnyperez.todolist.data.TodoListDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TodoListRepository extends CrudRepository<TodoListDto, Integer> {
    Optional<TodoListDto> findFirstByOrderById();
}
