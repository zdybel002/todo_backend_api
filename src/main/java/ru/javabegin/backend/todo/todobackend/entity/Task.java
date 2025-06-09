package ru.javabegin.backend.todo.todobackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import org.hibernate.type.NumericBooleanConverter;

import java.util.Date;
import java.util.Objects;

/*

User tasks

*/

@Entity
@Table(name = "task", schema = "todolist", catalog = "postgres")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task {

    // specify that the field is generated in the DB
    // necessary when adding a new object and returning it with a new id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    @Convert(converter = NumericBooleanConverter.class)
    private Boolean completed;
    // for automatic conversion of number to true/false

    @Column(name = "task_date") // in DB the field is named task_date because date is a reserved word
    private Date taskDate;

    // task can have only one priority (from the other side - the same priority can be used in many tasks)
    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id") // fields to join by (foreign key)
    private Priority priority;

    // task can have only one category (from the other side - the same category can be used in many tasks)
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id") // fields to join by (foreign key)
    private Category category;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // fields to join by (foreign key)
    private User user; // for which user the task belongs


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return title;
    }
}
