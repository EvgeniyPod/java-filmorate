package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private int generatorId = 1;

    @Override
    public User addUser(User user) {
        if (user.getId() == 0 && !users.containsValue(user)) {
            user.setId(generatorId);
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        generatorId++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(@PathVariable int id) {
        if (!users.containsKey(id)) {
            throw new ObjectIsNull("Внимание пользователя с таким номером не существует!");
        }
        return users.get(id);
    }

    @Override
    public boolean checkForAvailability(int id) {
        return users.containsKey(id);
    }

    @Override
    public User addFriend(int id, int friendId) {
        if (!checkForAvailability(id)) {
            throw new ObjectIsNull("Пользователя с id = " + id + " нет.");
        }
        if (!checkForAvailability(friendId)) {
            throw new ObjectIsNull("Пользователя с id = " + friendId + " нет.");
        }
        User user = getUserById(id);
        user.getFriends().add(friendId);
        User friendUser = getUserById(friendId);
        friendUser.getFriends().add(id);
        return user;
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        User user = getUserById(id);
        if (checkForAvailability(id) && checkForAvailability(friendId)) {
            user.getFriends().remove(friendId);
        } else if (!checkForAvailability(id)) {
            throw new ObjectIsNull("Пользователя с id = " + id + " нет.");
        } else {
            throw new ObjectIsNull("Пользователя с friendId = " + friendId + " нет.");
        }
        user.getFriends().remove(friendId);
        return user;
    }

    @Override
    public List<User> getUsersFriends(int id) {
        User user = getUserById(id);
        if (user == null) {
            throw new ObjectIsNull("Пользователя с id = " + id + " нет.");
        }
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        User firstUser = getUserById(id);
        if (firstUser == null) {
            throw new ObjectIsNull("Пользователя с id = " + id + " нет.");
        }
        User secondUser = getUserById(otherId);
        if (secondUser == null) {
            throw new ObjectIsNull("Пользователя с id = " + otherId + " нет.");
        }
        return firstUser.getFriends()
                .stream()
                .filter(f -> secondUser.getFriends().contains(f))
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
