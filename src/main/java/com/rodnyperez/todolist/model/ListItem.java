package com.rodnyperez.todolist.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListItem {
    private Integer id;
    private String text;
    private boolean completed;
}
