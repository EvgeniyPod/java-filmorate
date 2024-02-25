package ru.yandex.practicum.filmorate.dao.user;

import lombok.Getter;

@Getter
public class UserDbFriends {
    private final int userId;
    private final int friendId;

    public UserDbFriends(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
