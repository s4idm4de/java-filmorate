package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends Exception {
    ValidationException() {
        log.error("Ой-ой");
    }
}
