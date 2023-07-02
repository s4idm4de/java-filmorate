package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@Qualifier("FilmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select id from films where id = ?", film.getId());
        if (sqlRows.next()) {
            try {
                Validation.filmValidation(film);
                String sqlQuery = "update films set " +
                        "name = ?, description = ?, duration = ? , release_date = ?, rating = ?" +
                        "where id = ?";
                jdbcTemplate.update(sqlQuery,
                        film.getName(),
                        film.getDescription(),
                        film.getDuration(),
                        film.getReleaseDate(),
                        film.getMpa().getId(),
                        film.getId()
                );
                String sqlDel = "delete from genres where film_id = ?";
                jdbcTemplate.update(sqlDel,
                        film.getId());
                film.getGenres().forEach(genre -> {
                    String sqlGenres = "insert into genres (film_id, genre) " + "values(?, ?)";
                    jdbcTemplate.update(sqlGenres, film.getId(), genre.getId());
                });
                return film;
            } catch (ValidationException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        } else {
            log.info("НЕТ ФИЛЬМА {} ", film.getId());
            throw new NotFoundException("нет такого filma");
        }
    }

    @Override
    public Film addFilm(Film film) {
        try {
            Validation.filmValidation(film);
            String sqlQuery = "insert into films(name, description, duration, release_date, rating) " +
                    "values (?, ?, ?, ?, ?)";

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getDuration(),
                    film.getReleaseDate(),
                    film.getMpa().getId()
            );
            SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select count(*) as last_id from films");
            sqlRows.next();
            film.setId(sqlRows.getInt("last_id"));
            film.getGenres().forEach(genre -> {
                String sqlGenres = "insert into genres(film_id, genre) " + "values (?, ?)";
                jdbcTemplate.update(sqlGenres, film.getId(), genre.getId());
            });

            return film;
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlFilms = "select * from films";
        List<Film> films = jdbcTemplate.query(sqlFilms, (filmRows, rowNum) -> Film.builder().id(filmRows.getInt("id"))
                .name(filmRows.getString("name")).description(filmRows.getString("description"))
                .duration(filmRows.getInt("duration"))
                .releaseDate(filmRows.getDate("release_date").toLocalDate())
                .build());
        films.forEach(film -> {
            getGenres(film);
            getLikes(film);
            getMpa(film);
        });
        return films;
    }

    @Override
    public Film getFilmById(Integer filmId) throws NotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", filmId);
        if (filmRows.next()) {
            Film film = Film.builder().id(filmRows.getInt("id"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .description(filmRows.getString("description"))
                    .name(filmRows.getString("name"))
                    .build();
            getGenres(film);
            getLikes(film);
            getMpa(film);
            return film;
        } else {
            throw new NotFoundException("нет такого фильма");
        }
    }

    @Override
    public void addLikeToFilm(Integer filmId, Integer userId) {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select user_id, film_id from" +
                " likes where user_id = ? and film_id = ?", userId, filmId);
        if (!sqlRows.next()) {
            String sqlQuery = "insert into likes(user_id, film_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    userId,
                    filmId);
        }
    }

    @Override
    public void deleteLikeFromFilm(Integer filmId, Integer userId) throws NotFoundException {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select id from users where id = ?", userId);
        if (sqlRows.next()) {
            String sqlQuery = "delete from likes where user_id = ? and film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    userId,
                    filmId);
        } else {
            throw new NotFoundException("нет такого пользователя");
        }
    }

    private void getGenres(Film film) {
        String sqlGenres = "select genre from genres where film_id = ? group by genre";
        List<Integer> genres = jdbcTemplate.query(sqlGenres, (rs, rowNum) -> rs.getInt("genre"), film.getId());
        genres.forEach(genreId -> {
            SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select genre_name from genres_names" +
                    " where genre_id = ?", genreId);
            if (sqlRows.next()) {
                film.setGenre(Genre.builder().id(genreId).name(sqlRows.getString("genre_name")).build());
            }
        });

    }

    private void getLikes(Film film) {
        String sqlLikes = "select user_id from likes where film_id = ? group by user_id";
        List<Integer> likes = jdbcTemplate.query(sqlLikes, (rs, rowNum) -> rs.getInt("user_id"), film.getId());
        likes.forEach(like -> film.setLike(like));
    }

    private void getMpa(Film film) {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select m.rating_id as rating_id," +
                " m.rating_name as rating_name from " +
                "mpas as m join films as f on m.rating_id = f.rating where f.id = ?", film.getId());
        if (sqlRows.next()) {
            film.setMpa(Mpa.builder().id(sqlRows.getInt("rating_id")).name(sqlRows.getString("rating_name")).build());
        }
    }

}
