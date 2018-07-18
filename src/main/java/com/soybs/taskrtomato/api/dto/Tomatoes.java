package com.soybs.taskrtomato.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tomatoes {

    @Id
    public int id;

    public String summary;

    @Column(name = "finished_at")
    public Timestamp finishedAt;

    @Column(name = "task_id")
    public UUID taskId;

}
