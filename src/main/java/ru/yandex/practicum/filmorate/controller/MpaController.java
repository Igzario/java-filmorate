package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.services.MpaService;

@RequiredArgsConstructor
@Slf4j
@RestController
@Component
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/mpa/{id}")
    public ResponseEntity getMpa(@PathVariable int id) throws EntityNotFoundException {
        return mpaService.getMpa(id);
    }

    @GetMapping("/mpa")
    public ResponseEntity getAllMpa() throws EntityNotFoundException {
        return mpaService.getAllMpa();
    }
}