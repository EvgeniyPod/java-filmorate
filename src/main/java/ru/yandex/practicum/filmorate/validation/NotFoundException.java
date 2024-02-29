package ru.yandex.practicum.filmorate.validation;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
