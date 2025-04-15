package ru.javabegin.backend.todo.todobackend.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.todo.todobackend.entity.Task;
import ru.javabegin.backend.todo.todobackend.repo.TaskRepository;

import java.util.Date;
import java.util.List;

// всегда нужно создавать отдельный класс Service для доступа к данным, даже если кажется,
// что мало методов или это все можно реализовать сразу в контроллере
// Такой подход полезен для будущих доработок и правильной архитектуры (особенно, если работаете с транзакциями)
@Service

// все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе возникнет исключение - все выполненные операции откатятся (Rollback)
@Transactional
public class TaskService {

    private final TaskRepository repository; // сервис имеет право обращаться к репозиторию (БД)

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }


    public List<Task> findAll(String email) {
        return repository.findByUserEmailOrderByTaskDateDesc(email);
    }

    public Task add(Task task) {
        return repository.save(task); // метод save обновляет или создает новый объект, если его не было
    }

    public Task update(Task task) {
        return repository.save(task); // метод save обновляет или создает новый объект, если его не было
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }


    public Page<Task> findByParams(String text, Boolean completed, Long priorityId, Long categoryId, String email, Date dateFrom, Date dateTo, PageRequest paging) {
        return repository.findByParams(text, completed, priorityId, categoryId, email, dateFrom, dateTo, paging);
    }

    public Task findById(Long id) {
        return repository.findById(id).get(); // т.к. возвращается Optional - можно получить объект методом get()
    }

    public List<Task> findByCategoryId(Long categoryId){
        return  repository.findByCategory_IdOrderByTaskDateAsc(categoryId);
    }
}
