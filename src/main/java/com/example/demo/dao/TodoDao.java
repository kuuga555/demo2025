package com.example.demo.dao;

import com.example.demo.entity.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoDao extends CrudRepository<Todo, Integer>{

}
