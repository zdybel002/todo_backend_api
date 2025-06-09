package ru.javabegin.backend.todo.todobackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.todo.todobackend.entity.Priority;


import java.util.List;

// OOP principle: abstraction-implementation — here we describe all available ways to access data
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    // find all values for the given user
    List<Priority> findByUserEmailOrderByIdAsc(String email);

    // find values by title for a specific user
    @Query("SELECT p FROM Priority p where " +
            "(:title is null or :title='' " + // if the title parameter is empty, all records will be selected (this condition applies)
            " or lower(p.title) like lower(concat('%', :title,'%'))) " + // if the title parameter is not empty, this condition applies
            " and p.user.email=:email " + // filtering for a specific user
            "order by p.title asc") // sorting by title
    List<Priority> findByTitle(@Param("title") String title, @Param("email") String email);

}

