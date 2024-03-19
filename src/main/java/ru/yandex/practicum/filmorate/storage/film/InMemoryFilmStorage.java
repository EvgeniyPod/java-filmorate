package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;

    @Override
    public Film addFilm(Film film) {
        if (film.getId() == 0 && !films.containsValue(film)) {
            film.setId(generatorId);
        }
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        generatorId++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(@PathVariable int id) {
        if (!films.containsKey(id)) {
            throw new ObjectIsNull("Фильма с таким номером не сущесвует");
        }
        return films.get(id);
    }

    @Override
    public boolean checkForAvailability(int id) {
        return films.containsKey(id);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return films.values().stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(int id, int userId) {
        if (!checkForAvailability(id)) {
            throw new ObjectIsNull("film с id = " + id + " не найден");
        }
        Film film = getFilmById(id);
        film.getLikes().add(userId);
        return film;
    }
}
