package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int filmId = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        validation(film);
        if (film.getId() == null) {
            film.setId(filmId);
            filmId++;

            films.put(film.getId(), film);
            log.info("Добавлен фильм {}", film);
            return film;
        } else {
            throw new ValidationException("you shell not pass с id");
        }
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        validation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм {} обновлён", film);
            return film;
        } else {
            throw new ValidationException("нельзя обновить то, у чего нет id");
        }
    }

    private void validation(Film film) throws ValidationException {
        if (film.getName().isBlank())
            throw new ValidationException("у фильма должно быть название");
        if (film.getDescription().length() > 200)
            throw new ValidationException("сочинения свыше 200 символов никто читать не будет");
        if (film.getReleaseDate() != null && film.getReleaseDate()
                .isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("курица или яйцо? сначала изобрели камеру, а потом начали снимать фильмы. " +
                    "Никаких фильмов до 28 декабря 1895 года!");
        if (film.getDuration() <= 0)
            throw new ValidationException("фильм должен длиться хоть сколько-то");
    }
}
