package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(int id);

    boolean checkForAvailability(int id);

    List<Film> getTopFilms(int count);

    Film addLike(int id, int userId);

}
