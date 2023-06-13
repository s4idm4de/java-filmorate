package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validation {
    public static void filmValidation(Film film) throws ValidationException {
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

    public static void userValidation(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("email должен содержать @");
        if (user.getLogin().isBlank() || user.getLogin().contains(" "))
            throw new ValidationException("логин не должен содержать пробелов");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("нельзя родиться в будущем");
    }
}
