CREATE TABLE IF NOT EXISTS mpas (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar
);

CREATE TABLE IF NOT EXISTS films (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name varchar NOT NULL,
        description varchar(200),
        release_date date,
        duration integer,
        rating integer REFERENCES mpas (rating_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar,
  	login varchar,
  	name varchar,
  	birthday date
);

CREATE TABLE IF NOT EXISTS friends (
  	user_id_from INTEGER REFERENCES users (id) ON DELETE CASCADE,
  	user_id_to INTEGER REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
	user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres_names (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar
);

CREATE TABLE IF NOT EXISTS genres (
    film_id INTEGER REFERENCES films (id) ON DELETE CASCADE,
    genre INTEGER REFERENCES genres_names (genre_id) ON DELETE CASCADE
);