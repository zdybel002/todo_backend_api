package ru.javabegin.backend.todo.todobackend.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.todo.todobackend.entity.Category;
import ru.javabegin.backend.todo.todobackend.search.CategorySearchValues;
import ru.javabegin.backend.todo.todobackend.service.CategoryService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category") // base URI for all methods in this controller
public class CategoryController {

    // this is the service layer to work with the database
    private CategoryService categoryService;

    // constructor injection (we inject service using constructor)
    // we do not use @Autowired on the field because it is not recommended
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String email) {
        // return all categories for this email
        return categoryService.findAll(email);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {

        // check if ID is set (should be null when adding)
        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // check if title is empty or null
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        // save category and return it with generated ID
        return ResponseEntity.ok(categoryService.add(category));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {

        // ID must be provided for updating
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // title must not be empty
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // update the category
        categoryService.update(category);

        // return status 200 OK
        return new ResponseEntity(HttpStatus.OK);
    }

    // delete category by ID (using DELETE method and path variable)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        // try to delete the category
        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        // return status 200 OK if delete is successful
        return new ResponseEntity(HttpStatus.OK);
    }

    // search categories by title and email
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {

        // check if email is missing
        if (categorySearchValues.getEmail() == null || categorySearchValues.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        // find all categories for this user with a specific title
        List<Category> list = categoryService.findByTitle(categorySearchValues.getTitle(), categorySearchValues.getEmail());

        return ResponseEntity.ok(list);
    }

    // find category by ID (ID is in request body, not in URL)
    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {

        Category category = null;

        // try to find category by ID
        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        // return found category
        return ResponseEntity.ok(category);
    }
}
