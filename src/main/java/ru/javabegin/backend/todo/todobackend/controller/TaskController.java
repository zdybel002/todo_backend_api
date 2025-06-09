package ru.javabegin.backend.todo.todobackend.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.todo.todobackend.entity.Category;
import ru.javabegin.backend.todo.todobackend.entity.Task;
import ru.javabegin.backend.todo.todobackend.search.TaskSearchValues;
import ru.javabegin.backend.todo.todobackend.service.TaskService;

import java.text.ParseException;
import java.util.*;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/task") // base URI
public class TaskController {

    public static final String ID_COLUMN = "id"; // name of the ID column
    private final TaskService taskService; // service to access data (we don't call repositories directly)


    // constructor-based dependency injection
    // we don't use @Autowired on the class field because "Field injection is not recommended"
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    // get all tasks
    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody String email) {
        return ResponseEntity.ok(taskService.findAll(email)); // find all tasks for the specific user
    }

//    @PostMapping("/category")
//    public ResponseEntity<List<Task>> findByCategoryId(@RequestBody Long id) {
//        return ResponseEntity.ok(taskService.findByCategoryId(id));
//
//    }

    @PostMapping("/category")
    public ResponseEntity<List<Task>> getTasksByCategory(@RequestBody Map<String, Long> body) {
        Long categoryId = body.get("categoryId");
        List<Task> tasks = taskService.findByCategoryId(categoryId);
        return ResponseEntity.ok(tasks);
    }

    // add new task
    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {

        // check required fields
        if (task.getId() != null && task.getId() != 0) {
            // id is auto-generated in DB (autoincrement), so it shouldn't be passed to avoid uniqueness conflicts
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // if title is empty or null
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(taskService.add(task)); // return created object with generated id

    }


    // update existing task
    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {

        // check required fields
        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // if title is empty or null
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }


        // save works for both adding and updating
        taskService.update(task);

        return new ResponseEntity(HttpStatus.OK); // just return status 200 (operation succeeded)

    }


    // for deletion, we use DELETE with id in URL, not PUT,
    // because DELETE is more suitable and RESTful for removing resources
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        // try-catch is optional, without it stacktrace will be returned on error
        // here is an example of handling exceptions and sending custom message/status
        try {
            taskService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK); // just return status 200 (operation succeeded)
    }


    // get task by id
    @PostMapping("/id")
    public ResponseEntity<Task> findById(@RequestBody Long id) {

        Task task = null;

        // try-catch is optional, without it stacktrace will be returned on error
        // here is an example of handling exceptions and sending custom message/status
        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) { // if object not found
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(task);
    }


    // search by any parameters in TaskSearchValues
    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) throws ParseException {

        // avoid NullPointerException
        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        // convert Boolean to Integer
        Boolean completed = taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 1 ? true : false;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;

        String email = taskSearchValues.getEmail() != null ? taskSearchValues.getEmail() : null; // to show tasks for this user only

        // check required params
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }


        // to capture all tasks within date range regardless of time - set times from 00:00 to 23:59

        Date dateFrom = null;
        Date dateTo = null;


        // set time to 00:01 for start date (if specified)
        if (taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 1);
            calendarFrom.set(Calendar.SECOND, 1);
            calendarFrom.set(Calendar.MILLISECOND, 1);

            dateFrom = calendarFrom.getTime(); // set start date time to 00:01

        }


        // set time to 23:59 for end date (if specified)
        if (taskSearchValues.getDateTo() != null) {

            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);

            dateTo = calendarTo.getTime(); // set end date time to 23:59

        }


        // sort direction
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /* We add ID as the second sort field to ensure a consistent order.
           For example, if two tasks have the same priority and we sort by that field,
           the order of those two records could vary on each query since no secondary sort is specified.
           Using ID as the secondary sort field guarantees consistent ordering of tasks with identical priority.
         */

        // create sort object with column and direction
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        // pagination object
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // query result with pagination
        Page<Task> result = taskService.findByParams(title, completed, priorityId, categoryId, email, dateFrom, dateTo, pageRequest);

        // return query result
        return ResponseEntity.ok(result);

    }





}
