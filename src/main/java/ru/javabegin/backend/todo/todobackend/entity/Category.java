package ru.javabegin.backend.todo.todobackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import java.util.Objects;


/*

Reference value - user category
Can be used for user's tasks
Contains statistics for each category

*/

@Entity
@Table(name = "category", schema = "todolist", catalog = "postgres")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category {

    // specify that the field is populated in the DB
    // needed when adding a new object and it is returned with a new id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    @Column(name = "completed_count", updatable = false) // since this field is automatically calculated in triggers - we don't update it manually (updatable = false)
    private Long completedCount;

    @Column(name = "uncompleted_count", updatable = false) // since this field is automatically calculated in triggers - we don't update it manually (updatable = false)
    private Long uncompletedCount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // which fields link these two objects (foreign key)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
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
