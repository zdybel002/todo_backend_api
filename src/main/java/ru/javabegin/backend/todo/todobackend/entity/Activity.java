package ru.javabegin.backend.todo.todobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.type.NumericBooleanConverter;


import java.util.Objects;


/*

All user activity (account activation, other actions as needed)

*/

@Entity
@Table(name = "activity", schema = "todolist", catalog = "postgres")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity { // table name will be automatically taken from the class name with lowercase: activity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = NumericBooleanConverter.class) // for automatic conversion from number to true/false
    private Boolean activated; // becomes true only after user confirms activation (logically cannot become false again)

    @Column(updatable = false)
    private String uuid; // created only once using a trigger in the database

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // equals and hashCode overridden for entity identity based on id

}
