package com.example.async.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Task {

    /**
     * タスクID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 総タスク
     */
    private int total;
    /**
     * 完了タスク
     */
    private int done;

    @Version
    private int version;

    public Task(int total) {
        this.total = total;
        this.done = 0;
    }

    protected Task() {
    }

}
