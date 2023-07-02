package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();

    public void setGenre(Genre genre) {
        genres.add(genre);
    }

    public void setLike(Integer userId) {
        likes.add(userId);
    }
}
