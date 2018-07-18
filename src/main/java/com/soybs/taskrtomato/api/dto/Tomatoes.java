package com.soybs.taskrtomato.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tomatoes {

    /** tomatoes_id_seqはDB上のseq用のテーブル(serial指定時自動生成) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String summary;

    @Column(name = "finished_at")
    public Timestamp finishedAt;

    @Column(name = "task_id")
    public UUID taskId;

}
