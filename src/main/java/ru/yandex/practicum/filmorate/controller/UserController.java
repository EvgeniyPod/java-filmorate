package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST-Запрос на добавление пользователя.");
        userService.addUser(user);
        return user;
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("PUT-Запрос на обновление пользователя.");
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(user));
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("GET-запрос на получение списка всех пользователей.");
        List<User> users = userService.getUsers();
        log.info("Текущее количество пользователей: {}.", users.size());
        return users;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        log.info("GET-Запрос на получение пользователя по id = {}.", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("PUT-Запрос на добавление в друзья пользователя friendId = {} от пользователя id = {}.", friendId, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("DELETE-Удаление пользователя friendId = {} из друзей пользователя id = {}.", friendId, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.deleteFriend(id, friendId));
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("GET-Запрос на получение друзей пользователя с id = {}.", id);
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("GET-Запрос на получение общих друзей пользователей с id = {} и otherId = {}.", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}
