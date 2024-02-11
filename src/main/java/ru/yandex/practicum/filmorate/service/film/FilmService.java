package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }


    public Film addLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> usersWhoLikes = film.getLikes();
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectIsNull("Такого пользователя не сущестует");
        }
        usersWhoLikes.add(userId);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> usersWhoLikes = film.getLikes();
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectIsNull("Такого пользователя не сущестует");
        }
        usersWhoLikes.remove(userId);
        return film;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilms = new ArrayList<>();
        if (!filmStorage.getFilms().isEmpty()) {
            List<Film> allFilms = filmStorage.getFilms().stream().sorted((film1, film2) -> {
                if (film1.getLikes().size() > film2.getLikes().size()) {
                    return -1;
                } else if (film1.getLikes().size() < film2.getLikes().size()) {
                    return 1;
                }
                return 0;
            }).collect(Collectors.toList());

            if (count < 0) {
                throw new IllegalArgumentException("Передано отрицательное число");
            }

            if (!allFilms.isEmpty()) {
                for (int i = 0; i < count && i < allFilms.size(); i++) {
                    topFilms.add(allFilms.get(i));
                }
            }
        }
        return topFilms;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
}
