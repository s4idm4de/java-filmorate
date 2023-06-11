package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder(toBuilder = true)
public class Genre {
    private Integer id;
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()){ return false;}
        Genre otherTask = (Genre) obj;
        return this.id == otherTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
