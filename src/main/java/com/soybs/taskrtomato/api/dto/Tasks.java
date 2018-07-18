package com.soybs.taskrtomato.api.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Tasks {

    @Id
    public UUID id;

    public String category;

    public String title;

    public String description;

    @Column(name = "is_finished")
    public boolean isFinished;

    @Column(name = "created_at")
    public Timestamp createdAt;

    @Column(name = "updated_at")
    public Timestamp updatedAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    public List<Tomatoes> tomatoes;

    @Column(name = "user_id")
    public String userId;

    public Tasks() {
        this.tomatoes = new ArrayList<>();
    }
}
