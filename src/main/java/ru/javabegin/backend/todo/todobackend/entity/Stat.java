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

общая статистика по задачам (незвисимо от категорий задач)

 */

@Entity
@Table(name = "stat", schema = "todolist", catalog = "postgres")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stat { // в этой таблице всего 1 запись, которая обновляется (но никогда не удаляется)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "completed_total", updatable = false)
    private Long completedTotal; // значение задается в триггере в БД

    @Column(name = "uncompleted_total", updatable = false)
    private Long uncompletedTotal; // значение задается в триггере в БД

    @OneToOne(fetch = FetchType.EAGER)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связывать (foreign key)
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat stat = (Stat) o;
        return id.equals(stat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
