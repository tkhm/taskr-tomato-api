package com.soybs.taskrtomato.api.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TasksDto {

    public UUID id;

    public String category;

    public String title;

    public String description;

    public boolean isFinished;

    public Timestamp createdAt;

    public Timestamp updatedAt;

    public List<TomatoesDto> tomatoes;

    public String userId;

    public TasksDto() {
        this.tomatoes = new ArrayList<>();
    }
}
