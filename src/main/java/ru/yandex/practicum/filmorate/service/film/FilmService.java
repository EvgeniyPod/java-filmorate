package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    private final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);

    public void addLike(int id, int userId) {
        filmStorage.addLike(id, userId);
    }

    public Film deleteLike(int id, int userId) {
        if (!filmStorage.checkForAvailability(id)) {
            throw new ObjectIsNull("film с id = " + id + " не найден");
        }
        if (userId < 0) {
            throw new ObjectIsNull("Неверный id пользователя");
        }
        Film film = getFilmById(id);
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }

    public void addFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()) == null) {
            throw new ObjectIsNull("film с id = " + film.getId() + " не найден");
        }
        if (filmStorage.checkForAvailability(film.getId())) {
            return filmStorage.updateFilm(film);
        } else if (!filmStorage.checkForAvailability(film.getId()) && (film.getId() != 0)) {
            throw new RuntimeException();
        } else {
            return filmStorage.addFilm(film);
        }
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    private void validateFilm(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("The name is empty");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("The description length more than 200 symbols");
        }
        if (film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Release date earlier than 28-12-1895");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Film duration is negative");
        }
    }
}
