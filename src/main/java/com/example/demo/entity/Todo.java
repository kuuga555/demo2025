package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table
public class Todo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column String task;
    @Column Integer status;

    public Integer getId(){ return id; }
    public void setId(Integer id){ this.id = id; }

    public String getTask(){ return task; }
    public void setTask(String task){ this.task = task; }

    public Integer getStatus(){ return status; }
    public void setStatus(Integer status){ this.status = status; }
    
    @Override
    public String toString(){
        return "Todo{" + "id=" + id + ", task='" + task + '\'' + ", status=" + status + '}';
    }
}