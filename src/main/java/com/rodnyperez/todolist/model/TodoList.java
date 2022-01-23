package com.rodnyperez.todolist.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoList {
    private String title;
    private Integer id;
    private List<ListItem> items;
}
