package com.rodnyperez.todolist.repo;

import com.rodnyperez.todolist.data.ListItemDto;
import org.springframework.data.repository.CrudRepository;

public interface ListItemRepository extends CrudRepository<ListItemDto, Integer> {
}
