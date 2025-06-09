package ru.javabegin.backend.todo.todobackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.backend.todo.todobackend.entity.Stat;
import ru.javabegin.backend.todo.todobackend.service.StatService;




@RestController
// base URI is not needed since there is only one method
public class StatController {

    private final StatService statService; // service to access data (we don't call repositories directly)

    // constructor-based dependency injection
    // we don't use @Autowired on the field because "Field injection is not recommended"
    public StatController(StatService statService) {
        this.statService = statService;
    }


    // for statistics, we always get only one row with id=1 (according to the DB table)
    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email) {

        // you can skip ResponseEntity and just return the collection, status code will still be 200 OK
        return ResponseEntity.ok(statService.findStat(email));
    }


}
