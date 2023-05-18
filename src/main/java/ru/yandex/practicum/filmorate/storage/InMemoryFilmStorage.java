package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) throws ValidationException, NotFoundException {
        validation(film);
        if (film.getId() == null) {
            film.setId(filmId);
            filmId++;
            films.put(film.getId(), film);
            log.info("Добавлен фильм {}", film);
            return film;
        } else {
            throw new NotFoundException("you shell not pass с id");
        }
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        validation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм {} обновлён", film);
            return film;
        } else {
            throw new NotFoundException("нельзя обновить то, у чего нет id");
        }
    }

    @Override
    public void deleteFilm(Integer filmId) throws NotFoundException {
        if (films.containsKey(filmId)) {
            films.remove(filmId);
        } else {
            throw new NotFoundException("Нет фильма с таким id");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> list = new ArrayList<Film>(films.values());
        return list;
    }

    @Override
    public Film getFilmById(Integer filmId) throws NotFoundException {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else {
            throw new NotFoundException("нет фильма с таким id");
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
