package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

    private FilmController filmController;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        userController = new UserController();
    }

    @Test
    public void addNewFilmResultTrue() {
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169);
        assertEquals(0, filmController.getFilms().size());

        filmController.addFilm(film);

        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    public void addNewFilmNull() {
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(null));
    }

    @Test
    public void addNewFilmWithoutName() {
        Film film = new Film(1, "", "Про космос",
                LocalDate.of(2014, 11, 6), 169);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewFilmWithDescriptionMore200Symbols() {
        Film film = new Film(1, "Интерстеллар", "Когда засуха, пыльные бури и вымирание растений" +
                " приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется" +
                " сквозь червоточину (которая предположительно соединяет области пространства-времени через большое" +
                " расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека" +
                " и найти планету с подходящими для человечества условиями.\n" +
                "\n",
                LocalDate.of(2014, 11, 6), 169);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewFilmWithDateReleaseBeforeFirstDate() {
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(1800, 11, 6), 169);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewFilmWithNegativeDuration() {
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), -169);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addNewUser() {
        User user = new User(1, "evgeniy@yandex.ru", "evgen", "Evgeniy",
                LocalDate.of(1999, 12, 18));

        assertEquals(0, userController.getUsers().size());

        userController.addUser(user);
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    public void addNewUserNull() {
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(null));
    }

    @Test
    public void addNewUserWithoutEmail() {
        User user = new User(1, "", "evgen", "Evgeniy",
                LocalDate.of(1999, 12, 18));

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithoutInEmailSymbol() {
        User user = new User(1, "evgeniy.yandex.ru", "evgen", "Evgeniy",
                LocalDate.of(1999, 12, 18));

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithoutLogin() {
        User user = new User(1, "evgeniy@yandex.ru", "", "Evgeniy",
                LocalDate.of(1999, 12, 18));

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithBlankInLogin() {
        User user = new User(1, "evgeniy@yandex.ru", "ev gen", "Evgeniy",
                LocalDate.of(1999, 12, 18));

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void addNewUserWithoutName() {
        User user = new User(1, "evgeniy@yandex.ru", "evgen", "",
                LocalDate.of(1999, 12, 18));

        userController.addUser(user);
        assertEquals(1, userController.getUsers().size());
        assertEquals("evgen", user.getName());
    }

    @Test
    public void addNewUserWithBirthdayInFuture() {
        User user = new User(1, "evgeniy@yandex.ru", "evgen", "Evgeniy",
                LocalDate.of(9999, 12, 18));

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}
