package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RestController
@Getter
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping(value = "/films")
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.info("POST-запрос на добавление фильма.");
        filmService.addFilm(film);
        log.info("Фильм с названием {} добавлен.", film.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(film);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.info("PUT-запрос на обновление фильма с id = {}.", film.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(filmService.updateFilm(film));
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("GET-Запрос на получение всех фильмов.");
        List<Film> films = filmService.getFilms();
        log.info("Количество фильмов: {}.", films.size());
        return films;
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilmsById(@PathVariable int id) {
        log.info("GET-Запрос на получение фильма по id = {}.", id);
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getFilmById(id));
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("PUT-Запрос за лайк фильму с filmId = {} от пользователя с userId = {}.", id, userId);
        filmService.addLike(id, userId);
        ResponseEntity.status(HttpStatus.OK);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("DELETE-Запрос на удаление лайка фильма с filmId = {} от пользователя с userId = {}.", id, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(filmService.deleteLike(id, userId));
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET-Запрос на получение топ-списка {} фильмов.", count);
        return filmService.getTopFilms(count);
    }
}
