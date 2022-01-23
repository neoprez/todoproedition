package com.rodnyperez.todolist.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemRequest {

    private String text;

    private Boolean completed;

}
