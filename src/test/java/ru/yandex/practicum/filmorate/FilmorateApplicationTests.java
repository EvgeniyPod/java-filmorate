package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

    private FilmController filmController;
    private UserController userController;
    public FilmStorage filmStorage;
    public UserStorage userStorage;
    public UserService userService;
    public FilmService filmService;

    @BeforeEach
    public void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(userStorage, filmStorage);
        userService = new UserService(userStorage);
        filmController = new FilmController(filmService);
        userController = new UserController(userService);
    }

    @Test
    public void addNewFilmResultTrue() {
        Set<Integer> usersWhoLikes = new HashSet<>();
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169, usersWhoLikes);
        assertEquals(0, filmController.getFilms().size());

        filmController.addFilm(film);

        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    public void addNewFilmNull() {
        Assertions.assertThrows(NullPointerException.class, () -> filmController.addFilm(null));
    }

    @Test
    public void addNewFilmWithoutName() {
        Set<Integer> usersWhoLikes = new HashSet<>();
        Film film = new Film(1, "", "Про космос",
                LocalDate.of(2014, 11, 6), 169, usersWhoLikes);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewFilmWithDescriptionMore200Symbols() {
        Set<Integer> usersWhoLikes = new HashSet<>();
        Film film = new Film(1, "Интерстеллар", "Когда засуха, пыльные бури и вымирание растений" +
                " приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется" +
                " сквозь червоточину (которая предположительно соединяет области пространства-времени через большое" +
                " расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека" +
                " и найти планету с подходящими для человечества условиями.\n" +
                "\n",
                LocalDate.of(2014, 11, 6), 169, usersWhoLikes);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewFilmWithDateReleaseBeforeFirstDate() {
        Set<Integer> usersWhoLikes = new HashSet<>();
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(1800, 11, 6), 169, usersWhoLikes);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewFilmWithNegativeDuration() {
        Set<Integer> usersWhoLikes = new HashSet<>();
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), -169, usersWhoLikes);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewUser() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "evgeniy@yandex.ru", "evgen", "Evgeniy",
                LocalDate.of(1999, 12, 18), friends);

        assertEquals(0, userController.getUsers().size());

        userController.addUser(user);
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    public void addNewUserNull() {
        Assertions.assertThrows(NullPointerException.class, () -> userController.addUser(null));
    }

    @Test
    public void addNewUserWithoutEmail() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "", "evgen", "Evgeniy",
                LocalDate.of(1999, 12, 18), friends);

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithoutInEmailSymbol() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "evgeniy.yandex.ru", "evgen", "Evgeniy",
                LocalDate.of(1999, 12, 18), friends);

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithoutLogin() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "evgeniy@yandex.ru", "", "Evgeniy",
                LocalDate.of(1999, 12, 18), friends);

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithBlankInLogin() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "evgeniy@yandex.ru", "ev gen", "Evgeniy",
                LocalDate.of(1999, 12, 18), friends);

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithoutName() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "evgeniy@yandex.ru", "evgen", "",
                LocalDate.of(1999, 12, 18), friends);

        userController.addUser(user);
        assertEquals(1, userController.getUsers().size());
        assertEquals("evgen", user.getName());
    }

    @Test
    public void addNewUserWithBirthdayInFuture() {
        Set<Integer> friends = new HashSet<>();
        User user = new User(1, "evgeniy@yandex.ru", "evgen", "Evgeniy",
                LocalDate.of(9999, 12, 18), friends);

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}
