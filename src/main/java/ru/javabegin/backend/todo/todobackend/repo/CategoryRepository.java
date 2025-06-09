package ru.javabegin.backend.todo.todobackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.todo.todobackend.entity.Category;

import java.util.List;


// You can immediately use all CRUD methods (Create, Read, Update, Delete)
// OOP principle: abstraction-implementation — here we describe all available ways to access data
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // search user's categories (by title)
    List<Category> findByUserEmailOrderByIdAsc(String email);

    // search values by title for a specific user
    @Query("SELECT c FROM Category c where " +
            "(:title is null or :title='' " + // if the title parameter is empty, then all records will be selected (this condition will apply)
            " or lower(c.title) like lower(concat('%', :title,'%'))) " + // if the title parameter is not empty, then this condition will apply
            " and c.user.email=:email  " + // filtering for a specific user
            " order by c.title asc") // sorting by title
    List<Category> findByTitle(@Param("title") String title, @Param("email") String email);
}
