package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addFriend(int id, int friendId) {
        userStorage.checkForAvailability(id);
        userStorage.checkForAvailability(friendId);
        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriend(int id, int friendId) {
        return userStorage.deleteFriend(id, friendId);
    }

    public List<User> getCommonFriends(int id, int otherUserId) {
        userStorage.checkForAvailability(id);
        userStorage.checkForAvailability(otherUserId);
        return userStorage.getCommonFriends(id, otherUserId);
    }

    public List<User> getFriendsOfUser(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new ObjectIsNull("Пользователя с id = " + id + " нет.");
        }
        return userStorage.getUsersFriends(id);
    }

    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new ObjectIsNull("Пользователя с id " + id + "нет");
        }
        return user;
    }

    public void addUser(User user) {
        validateUser(user);
        userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (userStorage.checkForAvailability(user.getId())) {
            return userStorage.updateUser(user);
        } else if (!userStorage.checkForAvailability(user.getId()) && (user.getId() != 0)) {
            throw new ObjectIsNull("Пользователя с id = " + user.getId() + " нет.");
        } else {
            return userStorage.addUser(user);
        }
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    private void validateUser(User user) {
        if ((user.getEmail().isEmpty()) || (!user.getEmail().contains("@"))) {
            throw new ValidationException("E-mail is empty or not contains symbol \"@\"");
        }
        if ((user.getLogin().isEmpty()) || user.getLogin().contains(" ")) {
            throw new ValidationException("Login is empty or contains a space");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }
    }
}
