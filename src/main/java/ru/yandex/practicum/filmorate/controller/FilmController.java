package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int filmId = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (validation(film)) {
            if (film.getId() == null) {
                film.setId(filmId);
                filmId++;
            }
            films.put(film.getId(), film);
            log.info("Добавлен фильм {}", film);
            return film;
        } else {
            throw new ValidationException();
        }
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (validation(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм {} обновлён", film);
            return film;
        } else {
            throw new ValidationException();
        }
    }

    private boolean validation(Film film) {
        if (film.getName().isBlank()) return false;
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28))) return false;
        if (film.getDuration() <= 0) return false;
        return true;
    }
}
