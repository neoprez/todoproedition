package com.rodnyperez.todolist;

import com.rodnyperez.todolist.model.ListItem;
import com.rodnyperez.todolist.model.TodoList;
import com.rodnyperez.todolist.request.*;
import com.rodnyperez.todolist.services.TodoListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
public class TodoListController {
    private TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping
    public List<TodoList> list() {
        //Returns current list. Will return specific list when needed
        return todoListService.getAll();
    }

    @PostMapping
    public ResponseEntity<AddListResponse> createList(@RequestBody AddListRequest request) {
        try {
            TodoList todoList = todoListService.createList(request.getTitle());
            return ResponseEntity.ok(new AddListResponse(todoList.getId()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{listId}")
    public ResponseEntity<Void> updateList(@PathVariable("listId") Integer listId, @RequestBody UpdateListRequest request) {
        try {
            todoListService.updateList(listId, request.getTitle());
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable("listId") Integer listId) {
        try {
            todoListService.deleteList(listId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{listId}")
    public TodoList getList(@PathVariable("listId") Integer listId) {
        return todoListService.getList(listId);
    }

    @PostMapping("/{listId}/listItem")
    public AddListItemResponse addListItem(@PathVariable("listId") Integer listId, @RequestBody AddListItemRequest request) {
        ListItem newItem = todoListService.addItemToList(listId, request.getText());
        return new AddListItemResponse(newItem.getId());
    }

    @PostMapping("/{listId}/listItem/{itemId}")
    public ResponseEntity<Void> updateListItem(@PathVariable("itemId") Integer itemId, @RequestBody UpdateItemRequest request) {
        try {
            todoListService.updateListItem(itemId, request.getText(), request.getCompleted());
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{listId}/listItem/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Integer itemId) {
        todoListService.deleteListItem(itemId);
        return ResponseEntity.ok().build();
    }
}
