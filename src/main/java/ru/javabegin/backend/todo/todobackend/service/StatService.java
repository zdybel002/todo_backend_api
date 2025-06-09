package ru.javabegin.backend.todo.todobackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.todo.todobackend.entity.Stat;
import ru.javabegin.backend.todo.todobackend.repo.StatRepository;



@Service


@Transactional
public class StatService {

    private final StatRepository repository;

    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public Stat findStat(String email) {
        return repository.findByUserEmail(email);
    }

}
