package ru.javabegin.backend.todo.todobackend.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.todo.todobackend.entity.Stat;


// OOP principle: abstraction-implementation — here we describe all available ways to access data
@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserEmail(String email); // always get only 1 object, since 1 user has only 1 statistics record (one-to-one relationship)
}
