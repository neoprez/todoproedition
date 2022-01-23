package com.rodnyperez.todolist.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ListItemDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    private boolean completed;

    private LocalDateTime addedOn;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private TodoListDto todoList;
}
