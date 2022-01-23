package com.rodnyperez.todolist.services;

import com.rodnyperez.todolist.data.ListItemDto;
import com.rodnyperez.todolist.data.TodoListDto;
import com.rodnyperez.todolist.model.ListItem;
import com.rodnyperez.todolist.model.TodoList;
import com.rodnyperez.todolist.repo.ListItemRepository;
import com.rodnyperez.todolist.repo.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TodoListService {
    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private ListItemRepository listItemRepository;

    public TodoList getFirstList() {
        return todoListRepository.findFirstByOrderById()
                .map(this::mapTodoListDto)
                .orElseThrow();
    }

    public List<TodoList> getAll() {
        return StreamSupport.stream(todoListRepository.findAll().spliterator(), false)
                .map(this::mapTodoListDto)
                .toList();
    }

    public TodoList getList(Integer listId) {
        return todoListRepository.findById(listId).map(this::mapTodoListDto).orElseThrow();
    }

    public ListItem addItemToList(Integer listId, String text) {
        TodoListDto listDto = todoListRepository.findById(listId).orElseThrow();

        ListItemDto newItem = new ListItemDto();
        newItem.setText(text);
        newItem.setAddedOn(LocalDateTime.now());
        newItem.setUpdatedAt(LocalDateTime.now());
        newItem.setTodoList(listDto);

        listItemRepository.save(newItem);

        listDto.getListItems().add(newItem);
        listDto.setUpdatedAt(LocalDateTime.now());
        todoListRepository.save(listDto);

        return mapItemDto(newItem);
    }

    public void updateListItem(Integer itemId, String text, Boolean completed) {
        ListItemDto dto = listItemRepository.findById(itemId).orElseThrow();
        if (text != null) {
            dto.setText(text);
        }
        if (completed != null) {
            dto.setCompleted(completed);
        }

        dto.setUpdatedAt(LocalDateTime.now());

        listItemRepository.save(dto);
    }

    public void deleteListItem(Integer itemId) {
        ListItemDto dto = listItemRepository.findById(itemId).orElseThrow();
        TodoListDto listDto = dto.getTodoList();
        listDto.getListItems().remove(dto);
        listItemRepository.deleteById(itemId);
    }

    private ListItem mapItemDto(ListItemDto dto) {
        return new ListItem(dto.getId(), dto.getText(), dto.isCompleted());
    }

    private TodoList mapTodoListDto(TodoListDto dto) {
        return new TodoList(dto.getTitle(), dto.getId(), dto.getListItems().stream().map(this::mapItemDto).toList());
    }

    public TodoList createList(String title) {
        TodoListDto dto = new TodoListDto();
        dto.setTitle(title);
        dto.setCreatedDateTime(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setListItems(Collections.emptyList());
        dto = todoListRepository.save(dto);
        return mapTodoListDto(dto);
    }

    public void updateList(Integer listId, String title) {
        TodoListDto dto = todoListRepository.findById(listId).orElseThrow();
        dto.setTitle(title);
        todoListRepository.save(dto);
    }

    public void deleteList(Integer listId) {
        todoListRepository.deleteById(listId);
    }
}
