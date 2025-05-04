package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TodoDao;
import com.example.demo.entity.Todo;

@Service
public class TodoService{
    @Autowired
    TodoDao todoDao;

    public Iterable<Todo> getTodo(){
        return todoDao.findAll();
    }
}
