package ru.javabegin.backend.todo.todobackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import ru.javabegin.backend.todo.todobackend.entity.Category;
import ru.javabegin.backend.todo.todobackend.repo.CategoryRepository;


import java.util.List;



@Service


@Transactional
public class CategoryService {


    private final CategoryRepository repository; // service has the right to access the repository (database)

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll(String email) {
        return repository.findByUserEmailOrderByIdAsc(email);
    }

    public Category add(Category category) {
        return repository.save(category); // save method updates or creates a new object if it didn't exist
    }

    public Category update(Category category) {
        return repository.save(category); // save method updates or creates a new object if it didn't exist
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // find user categories by title
    public List<Category> findByTitle(String text, String email) {
        return repository.findByTitle(text, email);
    }

    // find category by ID
    public Category findById(Long id) {
        return repository.findById(id).get(); // since an Optional is returned, we get the object using get()
    }

}

