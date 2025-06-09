package ru.javabegin.backend.todo.todobackend.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.todo.todobackend.entity.Task;


import java.util.Date;
import java.util.List;

// OOP principle: abstraction-implementation — here we describe all available ways to access data
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t where " +
            "(:title is null or :title='' or lower(t.title) like lower(concat('%', :title,'%'))) and" +
            "(:completed is null or t.completed=:completed) and " +  // consider that parameter can be null or empty
            "(:priorityId is null or t.priority.id=:priorityId) and " +
            "(:categoryId is null or t.category.id=:categoryId) and " +
            "(:categoryId is null or t.category.id=:categoryId) and " +
            "(" +
            "(cast(:dateFrom as timestamp) is null or t.taskDate>=:dateFrom) and " +
            "(cast(:dateTo as timestamp) is null or t.taskDate<=:dateTo)" +
            ") and " +
            "(t.user.email=:email)" // show tasks only for the specified user, not all
    )
        // search by all provided parameters (empty parameters will be ignored)
    Page<Task> findByParams(@Param("title") String title,
                            @Param("completed") Boolean completed,
                            @Param("priorityId") Long priorityId,
                            @Param("categoryId") Long categoryId,
                            @Param("email") String email,
                            @Param("dateFrom") Date dateFrom,
                            @Param("dateTo") Date dateTo,
                            Pageable pageable
    );

    // find all tasks of a specific user
    List<Task> findByUserEmailOrderByTaskDateDesc(String email);

    List<Task> findByCategory_IdOrderByTaskDateAsc(Long categoryId);

}

